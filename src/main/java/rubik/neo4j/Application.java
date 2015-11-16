package rubik.neo4j;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import rubik.Functions;
import rubik.Rotation;
import rubik.Rubik;

public class Application {
	final int numberOfCores = Runtime.getRuntime().availableProcessors();
	final double blockingCoefficient = 0.999;
	final int poolSize = (int) (numberOfCores / (1 - blockingCoefficient));

	final List<Callable<Void>> partitions = new ArrayList<Callable<Void>>();
	@SuppressWarnings("unchecked")
	public void concurrentMergeRotations(Session session)
			throws InterruptedException {
		partitions.clear();
		String query = "MATCH a WHERE NOT (a)-[:LCW|:LCCW|:RCW|:RCCW|:DCW|:DCCW|:UCW|:UCCW|:FCW|:FCCW|:BCW|:BCCW]->() RETURN a.configuration";
		Iterable<String> configs = session.query(String.class, query,
				Collections.EMPTY_MAP);
		for (final String config : configs) {
			final Rubik rubik = Functions.TO_RUBIK.apply(config);
			partitions.add(new Callable<Void>() {
				public Void call() throws Exception {
					for (Rotation rotation : Rotation.values()) {
						mergeRotations(session, config, rotation.name(), rubik
								.applyRotation(rotation).toString());
					}
					return null;
				}
			});
		}
		final ExecutorService executorPool = Executors
				.newFixedThreadPool(poolSize);
		executorPool.invokeAll(partitions, 10000, TimeUnit.SECONDS);
		executorPool.shutdown();
	}

	@SuppressWarnings("unchecked")
	public void run(String... args) throws Exception {
		SessionFactory sessionFactory = new SessionFactory();
		Session session = sessionFactory.openSession(
				"http://192.168.1.67:7474", "neo4j", "j4oen");
		createUniqueConstraint(session);
		String initial = new Rubik.Builder().build().toString();
		while(true){
//			Transaction tx = session.beginTransaction();
			try {
				mergeConfiguration(session, initial);
				concurrentMergeRotations(session);
//				mergeRotations(session);
//				tx.commit();
			} catch (Exception e) {
				System.out.println(e);
//				tx.rollback();
			}
//			tx.close();
			String query = "MATCH a WHERE NOT (a)-[:LCW|:LCCW|:RCW|:RCCW|:DCW|:DCCW|:UCW|:UCCW|:FCW|:FCCW|:BCW|:BCCW]->() RETURN a.configuration";
			boolean hasNext = session
					.query(String.class, query, Collections.EMPTY_MAP)
					.iterator().hasNext();
			if (!hasNext)
				break;
		}
	}

	@SuppressWarnings("unchecked")
	private void mergeRotations(Session session, String configuration,
			String rotation, String image) {
		Map<String, String> params = new HashMap<>();
		params.put("configuration", configuration);
		params.put("rotation", rotation);
		params.put("image", image);
		String query = MessageFormat
				.format("MATCH (a:RubikConfiguration '{'configuration:''{0}'''}') MERGE (b:RubikConfiguration '{'configuration: ''{2}'''}') CREATE UNIQUE (a)-[r:{1}]->(b)",
						configuration, rotation, image);
		session.query(query, params);
	}

	@SuppressWarnings("unchecked")
	private void mergeRotations(Session session) {

		String query = "MATCH a WHERE NOT (a)-[:LCW|:LCCW|:RCW|:RCCW|:DCW|:DCCW|:UCW|:UCCW|:FCW|:FCCW|:BCW|:BCCW]->() RETURN a.configuration";
		Iterable<String> configs = session.query(String.class, query,
				Collections.EMPTY_MAP);
		if (configs.iterator().hasNext()) {
			for (String config : configs) {
				Rubik rubik = Functions.TO_RUBIK.apply(config);
				for (Rotation rotation : Rotation.values()) {
					mergeRotations(session, config, rotation.name(), rubik
							.applyRotation(rotation).toString());
				}
			}
			mergeRotations(session);
		}
	}

	private void mergeConfiguration(Session session, String conf) {
		Map<String, Object> params = new HashMap<>();
		params.put("conf", conf);
		String query = "MERGE (:RubikConfiguration { configuration: {conf} })";
		session.query(query, params);
	}

	private void createUniqueConstraint(Session session) {
		String uniqueConstraint = "CREATE CONSTRAINT ON (n:RubikConfiguration) ASSERT n.configuration IS UNIQUE";

		session.execute(uniqueConstraint);
	}

	public static void main(String[] args) throws Exception {
		new Application().run(args);
	}
}
