package rubik22.generator;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import rubik22.model.Cubie;
import rubik22.model.Rubik;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;

public class RubikGeneratorRouter extends UntypedActor {

	public final static int numberOfCores = Runtime.getRuntime()
			.availableProcessors();
	public final static double blockingCoefficient = 0.99;
	public final static int poolSize = (int) (numberOfCores / (1 - blockingCoefficient));
	private Router router;
	private int i = 1;
	static JedisPool pool;
	private Jedis j;
	static {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(RubikGeneratorRouter.poolSize);
		pool = new JedisPool(poolConfig, "192.168.1.67", 6379, 10000);
	}
	{
		List<Routee> routees = new ArrayList<Routee>();
		for (int i = 0; i < poolSize; i++) {
			ActorRef r = getContext().actorOf(
					Props.create(RubikConfigurationGenerator.class));
			getContext().watch(r);
			routees.add(new ActorRefRoutee(r));
		}
		router = new Router(new RoundRobinRoutingLogic(), routees);
		j = pool.getResource();
	}

	@Override
	public void preStart() throws InterruptedException {
		router.route(new Rubik.Builder().withCubies(Cubie.values()).build(),
				getSelf());
	}

	public void onReceive(Object msg) {
		if (msg instanceof Terminated) {
			ActorRef actor = ((Terminated) msg).actor();
			router = router.removeRoutee(actor);
			ActorRef r = getContext().actorOf(
					Props.create(RubikConfigurationGenerator.class));
			getContext().watch(r);
			router = router.addRoutee(new ActorRefRoutee(r));
		} else if (msg instanceof Rubik) {
			Rubik r = (Rubik) msg;

			if (j.get(String.valueOf(r.hashCode())) == null) {
				System.out.println(i++);
				router.route(msg, getSelf());
			}
		} else {
			unhandled(msg);
		}
	}
}
