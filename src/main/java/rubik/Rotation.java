package rubik;

import org.neo4j.graphdb.Relationship;

public enum Rotation {
  LCW("L", 90, new int[] { 1, 0, 0 }),
  LCCW("L'", -90, new int[] { 1, 0, 0 }), 
  RCW("R", 90, new int[] { 1, 0, 0 }), 
  RCCW("R'", -90, new int[] { 1, 0, 0 }), 
  DCW("D", 90, new int[]{0,1,0}), 
  DCCW("D'", -90, new int[]{0,1,0}), 
  UCW("U", 90, new int[]{0,1,0}), 
  UCCW("U'", -90, new int[]{0,1,0}), 
  BCW("B", 90, new int[]{0,0,1}), 
  BCCW("B'", -90, new int[]{0,0,1}), 
  FCW("F", 90, new int[]{0,0,1}), 
  FCCW("F'", -90, new int[]{0,0,1});
  
  public final String text;
  public final int angle;
  public final int[] axe;

  Rotation(String text, int angle, int[] axe) {
    this.text = text;
    this.angle = angle;
    this.axe = axe;
  }
}
