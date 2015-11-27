package rubik22.reactor;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import reactor.Environment;
import reactor.core.Dispatcher;
import reactor.fn.Consumer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import rubik22.generator.RubikRotationImage;
import rubik22.model.AbstractRubik;
import rubik22.model.Rotation;
import rubik3.model.Cubie;
import rubik3.model.Rubik;

import com.google.common.collect.ImmutableList;

public class TestReactor {
	public final static int numberOfCores = Runtime.getRuntime()
			.availableProcessors();
	public final static double blockingCoefficient = 0.99;
	public final static int poolSize = (int) (numberOfCores / (1 - blockingCoefficient));
	static JedisPool pool;
	static {
		// Only done once, statically, and shared across this classloader
		Environment.initialize();
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(10);
		pool = new JedisPool(poolConfig, "192.168.1.67", 6379, poolSize + 5);
	}

	public static void main(String[] args) throws InterruptedException {
		Dispatcher dispatcher = Environment.sharedDispatcher();
		Consumer<Throwable> errorHandler = t -> {t.printStackTrace();};
		Consumer<AbstractRubik> c = new Consumer<AbstractRubik>() {
			Jedis j = new Jedis("192.168.1.67", 6379);

			@Override
			public void accept(AbstractRubik rubik) {
				List<RubikRotationImage> images = Collections.EMPTY_LIST;
				ImmutableList.Builder<RubikRotationImage> builder = ImmutableList
						.builder();
				for (Rotation rotation : Rotation.values()) {
					builder.add(new RubikRotationImage.Builder()
							.withRubik(rubik).withRotation(rotation)
							.withImage(rubik.rotate(rotation)).build());
				}
				images = builder.build();
				Pipeline p = j.pipelined();
				p.sadd("DONE", images.get(0).rubik.toString());
				for (RubikRotationImage rri : images) {
					String key = MessageFormat.format("{0},{1}",
							rri.rubik.toString(), rri.rotation.name());
					String value = rri.image.toString();
					p.set(key, value);
					p.set(String.valueOf(rri.rubik.hashCode()),
							rri.rubik.toString());
					p.sadd("TODO", value);
				}
				p.sync();
			}

		};

		Jedis jedis = new Jedis("192.168.1.67", 6379);
		jedis.sadd("TODO", new Rubik.Builder().withCubies(Cubie.values())
				.build().toString());
		Set<String> sdiff = jedis.sdiff("TODO", "DONE");
		long start = System.nanoTime();
		while (!sdiff.isEmpty()) {
			for (String s : sdiff) {
				dispatcher.dispatch(Rubik.valueOf(s), c, errorHandler);
			}
			sdiff = jedis.sdiff("TODO", "DONE");
		}
		System.out.println((System.nanoTime() - start) / 1000 + " µs");
		jedis.close();
		Environment.terminate();
	}
}
