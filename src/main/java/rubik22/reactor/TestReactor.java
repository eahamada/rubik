package rubik22.reactor;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;
import reactor.Environment;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import rubik22.model.Rotation;
import rubik22.model.RubikRotationImage;
import rubik3.model.Cubie;
import rubik3.model.Rubik;

public class TestReactor {
	static {
		// Only done once, statically, and shared across this classloader
		Environment.initialize();
	}

	private final static String IP_REDIS = "10.69.19.77";

	public static void main(String[] args) throws InterruptedException {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(5);
		JedisPool jedisPool = new JedisPool(poolConfig, IP_REDIS);
		Jedis j = jedisPool.getResource();
		Jedis k = jedisPool.getResource();
		String INIT = new Rubik.Builder().withCubies(Cubie.values()).build().toString();
    k.sadd("TODO", INIT);
		Long scard = k.scard("TODO");
		Automaton dfa = BasicAutomata.makeString(INIT);
		while (scard > 0) {
			String spop = k.spop("TODO");
			dfa = m(j, dfa, spop);
		        int numberOfTransitions = dfa.getNumberOfTransitions();
            int numberOfStates = dfa.getNumberOfStates();
            double density =  100 * numberOfTransitions / numberOfStates;
            System.out.println(MessageFormat.format("{0}:{1} => {2}", numberOfStates, numberOfTransitions,density));
			scard = k.scard("TODO");
		}
		k.close();
		j.close();
		jedisPool.close();
	}

  private static Automaton m(Jedis j, final Automaton dfa, String spop) {
    Rubik c = Rubik.valueOf(spop);
    Pipeline p = j.pipelined();
     List<Automaton> collect = Arrays.asList(Rotation.values()).stream()
       // Créer un cube image pour chaque rotation
       .map(rotation -> 
              new RubikRotationImage.Builder().withRubik(c).withRotation(rotation).withImage(c.rotate(rotation)).build())
       //Filtrer ceux qui n'ont pas déjà été traités
       .filter(rri -> 
                 !dfa.run(rri.image.toString()))
       //Les ajouter à TODO
       .map(rri -> { 
         String image = rri.image.toString();
                  p.sadd("TODO", image);
                  return BasicAutomata.makeString(image);
                  
       }).collect(Collectors.toList());
          p.sync();
          Builder<String> builder = new ImmutableList.Builder<String>().add(c.toString()).addAll(c.aliases());
          String[] strings = builder.build().toArray(new String[c.aliases().size()+1]);
          return Automaton.minimize(dfa.union(BasicAutomata.makeStringUnion(strings)).union(Automaton.union(collect)));
  }

}
