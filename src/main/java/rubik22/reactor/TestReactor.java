package rubik22.reactor;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.reactivestreams.Processor;

import reactor.Environment;
import reactor.core.processor.RingBufferWorkProcessor;
import reactor.rx.Stream;
import reactor.rx.Streams;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import rubik22.model.Cubie;
import rubik22.model.Rotation;
import rubik22.model.Rubik;
import rubik22.model.RubikRotationImage;

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
		Processor<String, String> p = RingBufferWorkProcessor.create();
		Stream<String> s = Streams.wrap(p);
		Stream<Rubik> map = s.map(Rubik::valueOf);
    map.consume(c -> {
		        j.sadd("DONE", c.toString());
		      		        
	  List<RubikRotationImage> images = Arrays.asList(Rotation.values()).stream()
        .map(rotation -> 
        new RubikRotationImage.Builder().withRubik(c).withRotation(rotation).withImage(c.rotate(rotation)).build()).collect(Collectors.toList());
//    images.forEach(rri -> j.set(MessageFormat.format("{0},{1}", rri.rubik.toString(), rri.rotation.name()), String.valueOf(rri.image.hashCode())));
    images.stream().filter(rri-> !j.sismember("DONE",rri.image.toString())).forEach(rri ->  j.sadd("TODO", rri.image.toString()));
    });
		j.sadd("TODO", new Rubik.Builder().withCubies(Cubie.values()).build().toString());
		int count = 100;
		while (j.scard("TODO") > 0) {
			String spop = j.spop("TODO");
			  p.onNext(spop);
			  TimeUnit.MILLISECONDS.sleep(count--);
		}
		p.onComplete();
		System.out.println("DONE = "+j.scard("DONE"));
		j.close();
		jedisPool.close();
	}

}
