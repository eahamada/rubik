package rubik22.generator;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import rubik3.model.Rubik;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class Neo4JWriter extends UntypedActor {
	static SessionFactory sessionFactory = new SessionFactory();
	static Session session = sessionFactory.openSession(
			"http://192.168.1.67:7474", "neo4j", "j4oen");

	@Override
	public void onReceive(Object msg) throws IOException {
		ActorRef sender = getSender();
		if (msg instanceof List<?>) {
			@SuppressWarnings("unchecked")
			List<RubikRotationImage> rubikRotationImages = (List<RubikRotationImage>) msg;

			for (RubikRotationImage rri : rubikRotationImages) {

				String configuration = rri.rubik.toString();
				String rotation = rri.rotation.name();
				String image = rri.image.toString();

				Map<String, Object> params = new HashMap<>();
				String query = MessageFormat
						.format("MERGE (a:Configuration '{'configuration:''{0}'''}') "
								+ "MERGE (b:Configuration '{'configuration: ''{2}'''}') "
								+ "CREATE UNIQUE (a)-[r:{1}]->(b)",
								configuration, rotation, image);
				session.query(query, params);
			}
			String query = "MATCH a "
					+ "WHERE NOT (a)-[:LCW|:LCCW|:RCW|:RCCW|:DCW|:DCCW|:UCW|:UCCW|:FCW|:FCCW|:BCW|:BCCW]->() "
					+ "RETURN a.configuration";
			@SuppressWarnings("unchecked")
			Iterable<String> result = session.query(String.class, query,
					Collections.EMPTY_MAP);
			for (String string : result) {
				sender.tell(Rubik.valueOf(string), ActorRef.noSender());
			}
		} else
			unhandled(msg);
	}
}
