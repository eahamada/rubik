package rubik;

import static rubik.Coord.ONE;
import static rubik.Coord.MINUS_ONE;

import java.util.function.Predicate;

public enum Face {
  LEFT( b -> b.getX() == MINUS_ONE),
  RIGHT( b -> b.getX() == ONE),
  DOWN( b -> b.getY() == MINUS_ONE),
  UP( b -> b.getY() == ONE),
  BACK( b -> b.getZ() == MINUS_ONE),
  FRONT( b -> b.getZ() == ONE)
  ;
  public final Predicate<HasCoords> predicate;
  Face(Predicate<HasCoords> predicate){
    this.predicate = predicate;
  }
}
