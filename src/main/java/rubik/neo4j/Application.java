package rubik.neo4j;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.session.transaction.Transaction;

import rubik.Rubik;
import rubik.neo4j.domain.RubikConfiguration;

public class Application {

  @SuppressWarnings("unchecked")
  public void run(String... args) throws Exception {
    SessionFactory sessionFactory = new SessionFactory("rubik.neo4j.domain");
    Session session = sessionFactory.openSession("http://10.69.19.77:7474", "neo4j", "j4oen");
    String initial = new Rubik.Builder().build().toString();
    Map<String, Object> params = new HashMap<>();
    Map<String, Object> props = new HashMap<>();
    props.put("configuration", initial);
    params.put("props", props);
    String query = "CREATE (:RubikConfiguration { props })";
    String uniqueConstraint = "CREATE CONSTRAINT ON (n:RubikConfiguration) ASSERT n.id IS UNIQUE";
    session.execute(uniqueConstraint);
    Transaction tx = session.beginTransaction();
    try {
//      session.save(new RubikConfiguration.Builder().withConfiguration(initial).build());
      session.execute(query, params);
      tx.commit();
    } catch (Exception e) {
      System.out.println(e);
      tx.rollback();
    }
    tx.close();

    // String query = "MATCH a WHERE NOT
    // (a)-[:LCW|:LCCW|:RCW|:RCCW|:DCW|:DCCW|:UCW|:UCCW|:FCW|:FCCW|:BCW|:BCCW]->() RETURN a";
    
    // for (RubikConfiguration rubikConfiguration : rubikConfigurations) {
    // Rubik r = Functions.TO_RUBIK.apply(rubikConfiguration.getConfiguration());
    // rubikConfiguration.lcw = new
    // RubikConfiguration.Builder().withConfiguration(r.applyRotation(LCW).toString()).build();
    // rubikConfiguration.lccw = new
    // RubikConfiguration.Builder().withConfiguration(r.applyRotation(LCCW).toString()).build();
    // rubikConfiguration.rcw = new
    // RubikConfiguration.Builder().withConfiguration(r.applyRotation(RCW).toString()).build();
    // rubikConfiguration.rccw = new
    // RubikConfiguration.Builder().withConfiguration(r.applyRotation(RCCW).toString()).build();
    // rubikConfiguration.dcw = new
    // RubikConfiguration.Builder().withConfiguration(r.applyRotation(DCW).toString()).build();
    // rubikConfiguration.dccw = new
    // RubikConfiguration.Builder().withConfiguration(r.applyRotation(DCCW).toString()).build();
    // rubikConfiguration.ucw = new
    // RubikConfiguration.Builder().withConfiguration(r.applyRotation(UCW).toString()).build();
    // rubikConfiguration.uccw = new
    // RubikConfiguration.Builder().withConfiguration(r.applyRotation(UCCW).toString()).build();
    // rubikConfiguration.fcw = new
    // RubikConfiguration.Builder().withConfiguration(r.applyRotation(FCW).toString()).build();
    // rubikConfiguration.fccw = new
    // RubikConfiguration.Builder().withConfiguration(r.applyRotation(FCCW).toString()).build();
    // rubikConfiguration.bcw = new
    // RubikConfiguration.Builder().withConfiguration(r.applyRotation(BCW).toString()).build();
    // rubikConfiguration.bccw = new
    // RubikConfiguration.Builder().withConfiguration(r.applyRotation(BCCW).toString()).build();
    // }
  }

  public static void main(String[] args) throws Exception {
    new Application().run(args);
  }
}
