package rubik22.generator;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import rubik22.model.AbstractRubik;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class JedisWriter extends UntypedActor {

	@Override
	public void onReceive(Object msg) throws IOException {
		ActorRef sender = getSender();
		Jedis jedis = RubikGeneratorRouter.pool.getResource();
		if (msg instanceof List<?>) {
			@SuppressWarnings("unchecked")
			List<RubikRotationImage> rubikRotationImages = (List<RubikRotationImage>) msg;
			List<String> responses = new ArrayList<>();
			List<AbstractRubik> images = new ArrayList<>();
			
			for (RubikRotationImage rri : rubikRotationImages) {
				String key = MessageFormat.format("{0},{1}", rri.rubik.toString(),rri.rotation.name());
				String value = rri.image.toString() ;
				jedis.set(key, value); 
				jedis.set(String.valueOf(rri.rubik.hashCode()), rri.rubik.toString());
				responses.add(jedis.get(String.valueOf(rri.image.hashCode())));
				images.add(rri.image);
			}
			for (int i = 0; i < responses.size(); i++) {
				if (responses.get(i) == null) {
					sender.tell(images.get(i), ActorRef.noSender());
				}
			}
			 jedis.close();
		} else
			unhandled(msg);
	}
}
