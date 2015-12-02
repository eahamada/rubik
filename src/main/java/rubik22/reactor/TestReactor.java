package rubik22.reactor;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import rubik22.generator.RubikRotationImage;
import rubik22.model.AbstractRubik;
import rubik22.model.Rotation;
import rubik3.model.Cubie;
import rubik3.model.Rubik;

import com.google.common.collect.ImmutableList;

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
		Processor<String, String> p = RingBufferWorkProcessor.create();
		Stream<String> s = Streams.wrap(p);

		Processor<AbstractRubik, AbstractRubik> q = RingBufferWorkProcessor
				.create();
		Stream<AbstractRubik> t = Streams.wrap(q);
		s.map(Rubik::valueOf).consume(c -> {
			 List<RubikRotationImage> images = Collections.emptyList();
		        ImmutableList.Builder<RubikRotationImage> builder = ImmutableList.builder();
		        for (Rotation rotation : Rotation.values()) {
		          builder.add(new RubikRotationImage.Builder().withRubik(c).withRotation(rotation)
		              .withImage(c.rotate(rotation)).build());
		        }
		        images = builder.build();
		        String hashcode = String.valueOf(c.hashCode());
		        ImmutableList.Builder<Response<Boolean>> exists = ImmutableList.builder();
		        Pipeline pipelined = j.pipelined();
		        pipelined.set(hashcode, c.toString());
		        for (String string : c.aliases()) {
		        	pipelined.set(String.valueOf(string.hashCode()), string);
		        }
		        for (RubikRotationImage rri : images) {
		          String key = MessageFormat.format("{0},{1}", rri.rubik.toString(), rri.rotation.name());
		          String value = rri.image.toString();
		          String imageHash = String.valueOf(rri.image.hashCode());
		          pipelined.set(key, value);
		          exists.add(pipelined.exists(imageHash));
		        }
		        pipelined.sync();
		        List<Response<Boolean>> responses = exists.build();
		        for (int i = 0; i < responses.size(); i++) {
		          if(!responses.get(i).get()){
		            j.sadd("TODO", images.get(i).image.toString());
		          }
		        }
		});
		k.sadd("TODO", new Rubik.Builder().withCubies(Cubie.values()).build().toString());
		Long scard = k.scard("TODO");
		while (scard > 0) {
			String spop = k.spop("TODO");
			p.onNext(spop);
			scard = k.scard("TODO");
		}
		k.close();
		j.close();
	}

}
