package rubik;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RubikTest {
  @Test
  public void shouldBeAbleToBuildRubikFromConfiguration () {
   String initialConfiguration = new Rubik.Builder().build().toString();
   String _initialConfiguration = Functions.TO_RUBIK.apply(initialConfiguration).toString();
   assertEquals(initialConfiguration, _initialConfiguration);
  }
  
  @Test
  public void shouldBeAbleToBuildRubikFromConfigurationAfterRotation () {
    for (Rotation r : Rotation.values()) {
      String conf = new Rubik.Builder().build().applyRotation(r).toString();
      String _conf = Functions.TO_RUBIK.apply(conf).toString();
      assertEquals(conf,_conf);
    }
  }
}
