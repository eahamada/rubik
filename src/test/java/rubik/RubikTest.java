package rubik;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import rubik22.model.AbstractRubik;
import rubik22.model.Cubie;
import rubik22.model.Rubik;
import static rubik22.model.Rotation.*
;public class RubikTest {
  @Test
  public void initialRubikShouldBeABCDEFGH () {
	  AbstractRubik r = new Rubik.Builder().withCubies(Cubie.values()).build();
	  assertEquals("ABCDEFGH", r.toString());
	}
  @Test
  public void testRotateRCW () {
	  AbstractRubik r = new Rubik.Builder().withCubies(Cubie.values()).build().rotate(RCW);
	  assertEquals("ABGCEFHD", r.toString());
	}
  
  
}
