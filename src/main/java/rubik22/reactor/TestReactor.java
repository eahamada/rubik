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
import redis.clients.jedis.Response;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
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
    Dispatcher dispatcher = Environment.sharedDispatcher();
    Consumer<Throwable> errorHandler = t -> {
      t.printStackTrace();
    };
    Consumer<AbstractRubik> c = new Consumer<AbstractRubik>() {
      Jedis j = new Jedis(IP_REDIS, 6379);

      @Override
      public void accept(AbstractRubik rubik) {
        List<RubikRotationImage> images = Collections.emptyList();
        ImmutableList.Builder<RubikRotationImage> builder = ImmutableList.builder();
        for (Rotation rotation : Rotation.values()) {
          builder.add(new RubikRotationImage.Builder().withRubik(rubik).withRotation(rotation)
              .withImage(rubik.rotate(rotation)).build());
        }
        images = builder.build();
        String hashcode = String.valueOf(rubik.hashCode());
        ImmutableList.Builder<Response<Boolean>> exists = ImmutableList.builder();
        Pipeline p = j.pipelined();
        p.set(hashcode, rubik.toString());
        for (String string : rubik.aliases()) {
        	p.set(String.valueOf(string.hashCode()), string);
        }
        for (RubikRotationImage rri : images) {
          String key = MessageFormat.format("{0},{1}", rri.rubik.toString(), rri.rotation.name());
          String value = rri.image.toString();
          String imageHash = String.valueOf(rri.image.hashCode());
          p.set(key, value);
          exists.add(p.exists(imageHash));
        }
        p.sync();
        List<Response<Boolean>> responses = exists.build();
        for (int i = 0; i < responses.size(); i++) {
          if(!responses.get(i).get()){
            j.sadd("TODO", images.get(i).image.toString());
          }
        }
      }
    };

    Jedis jedis = new Jedis(IP_REDIS, 6379, 1000 * 1000, 1000 * 1000);
    jedis.configSet("timeout", "10");
    jedis.sadd("TODO", new Rubik.Builder().withCubies(Cubie.values()).build().toString());
    ScanResult<String> scanResult = jedis.sscan("TODO", "0", new ScanParams().count(1000));
    long start = System.nanoTime();
    while (!scanResult.getResult().isEmpty()) {
      for (String s : scanResult.getResult()) {
        dispatcher.dispatch(Rubik.valueOf(s), c, errorHandler);
      }
      scanResult = jedis.sscan("TODO", scanResult.getStringCursor(),
          new ScanParams().count(100000));
    }
    System.out.println((System.nanoTime() - start) / 1000 + " µs");
    jedis.close();
    Environment.terminate();
  }
}
