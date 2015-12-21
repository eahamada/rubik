package rubik22.reactor;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import org.reactivestreams.Processor;

import reactor.Environment;
import reactor.core.processor.RingBufferWorkProcessor;
import reactor.rx.Stream;
import reactor.rx.Streams;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import rubik22.model.AbstractRubik;
import rubik22.model.Rotation;
import rubik22.model.RubikRotationImage;
import rubik3.model.Cubie;
import rubik3.model.Rubik;

import com.google.common.collect.ImmutableList;

import dfa.DFA;
import dfa.State;

public class TestReactor {
	static {
		// Only done once, statically, and shared across this classloader
		Environment.initialize();
	}

	private final static String IP_REDIS = "192.168.1.67";

	public static void main(String[] args) throws InterruptedException {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(5);
		JedisPool jedisPool = new JedisPool(poolConfig, IP_REDIS);
		Jedis j = jedisPool.getResource();
		Jedis k = jedisPool.getResource();
	
		State q0 = State.valueOf("q0");
		DFA dfa = new DFA.Builder()
		.withQ(q0)
		.withΣ()
		.withδ((state, character) -> null)
		.withQ0(q0)
		.withF()
		.build();
		k.sadd("TODO", new Rubik.Builder().withCubies(Cubie.values()).build().toString());
		Long scard = k.scard("TODO");
		while (scard > 0) {
			String spop = k.spop("TODO");
			Rubik c = Rubik.valueOf(spop);
			 List<RubikRotationImage> images = Collections.emptyList();
		        ImmutableList.Builder<RubikRotationImage> builder = ImmutableList.builder();
		        for (Rotation rotation : Rotation.values()) {
		          builder.add(new RubikRotationImage.Builder().withRubik(c).withRotation(rotation)
		              .withImage(c.rotate(rotation)).build());
		        }
		        images = builder.build();
		        ImmutableList.Builder<Boolean> exists = ImmutableList.builder();
		        dfa = dfa.forceAccept(c.toString());
		        for (String string : c.aliases()) {
		        	dfa = dfa.forceAccept(string);
		        }
		        for (RubikRotationImage rri : images) {
		          String value = rri.image.toString();
		          exists.add(dfa.accept(value));
		        }
		        List<Boolean> responses = exists.build();
		        for (int i = 0; i < responses.size(); i++) {
		          if(!responses.get(i)){
		            j.sadd("TODO", images.get(i).image.toString());
		          }
		        }
			scard = k.scard("TODO");
		}
		k.close();
		j.close();
		jedisPool.close();
	}

}
