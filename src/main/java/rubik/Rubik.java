package rubik;

import static rubik.Color.BLUE;
import static rubik.Color.GREEN;
import static rubik.Color.ORANGE;
import static rubik.Color.RED;
import static rubik.Color.WHITE;
import static rubik.Color.YELLOW;
import static rubik.Coord.ONE;
import static rubik.Coord.MINUS_ONE;
import static rubik.MatrixOperations.COLUMN_TO_MATRIX;
import static rubik.MatrixOperations.MATRIX_TO_COLUMN;
import static rubik.MatrixOperations.add;
import static rubik.MatrixOperations.multiply;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Rubik {
  public final List<Cube> cubes;

  private Rubik(Builder builder) {
    this.cubes = builder.cubes;
  }
  @Override
  public String toString() {
    StringBuilder configuration = new StringBuilder();
    for (Face face : Face.values()) {
      configuration.append(printFace(face));
    }
    return configuration.toString();
  }

  public String printFace(Face face) {
    StringBuilder configuration = new StringBuilder();
    cubes.stream().filter(face.predicate).forEach(c -> configuration.append(c.print(face)));
    return configuration.toString();
  }

  public void printCube() {
    StringBuilder configuration = new StringBuilder();
    cubes.forEach(c -> configuration.append(c.toString()));
    System.out.println(configuration.toString());
  }

  public Rubik applyRotation(Rotation ro) {
    Function<Rotation, Function<Cube, Cube>> rotate = r -> c -> {
      Face f = Functions.TO_FACE.apply(ro.text.charAt(0));
      Cube result = c;
      if (f.predicate.test(c)) {
        Cube.Builder b = new Cube.Builder();
        withNewCoords(r, c, b);
        withNewColors(r, c, b);
        result = b.build();
      }
      return result;
    };

    return new Builder()
        .withCubes(this.cubes.stream().map(rotate.apply(ro)).collect(Collectors.toList())).build();
  }

  // https://fr.wikipedia.org/wiki/Rotation_vectorielle
  private void withNewCoords(Rotation r, Cube c, Cube.Builder b) {
    int[][] u = COLUMN_TO_MATRIX.apply(c.getCoords());
    int[] n = r.axe;
    int[][] m0 = new int[3][3];
    m0[0] = new int[] { n[0] * n[0], n[0] * n[1], n[0] * n[2] };
    m0[1] = new int[] { n[1] * n[0], n[1] * n[1], n[1] * n[2] };
    m0[2] = new int[] { n[2] * n[0], n[2] * n[1], n[2] * n[2] };

    int[][] m1 = new int[3][3];
    m1[0] = new int[] { 0, -n[2], n[1] };
    m1[1] = new int[] { n[2], 0, -n[0] };
    m1[2] = new int[] { -n[1], n[0], 0 };

    int[][] m = new int[3][3];
    m = add(m0, multiply(m1, (int) Math.sin(Math.toRadians(r.angle))));
    int[] newCoords = MATRIX_TO_COLUMN.apply(multiply(m, u));
    b.withCoords(newCoords);
  }

  private void withNewColors(Rotation r, Cube c, Cube.Builder b) {
    switch (r) {
    case LCW:
    case LCCW:
    case RCW:
    case RCCW:
      b.withColorX(c.colorX).withColorY(c.colorZ).withColorZ(c.colorY);
      break;
    case DCW:
    case DCCW:
    case UCW:
    case UCCW:
      b.withColorX(c.colorZ).withColorY(c.colorY).withColorZ(c.colorX);
      break;
    case FCW:
    case FCCW:
    case BCW:
    case BCCW:
      b.withColorX(c.colorY).withColorY(c.colorX).withColorZ(c.colorZ);
      break;

    default:
      throw new IllegalArgumentException();
    }
  }

  public static class Builder {
    private final static List<Cube> INITIAL;
    static {
      List<Cube.Builder> builders = new ArrayList<>();
      for (Coord x : Coord.values()) {
        for (Coord y : Coord.values()) {
          for (Coord z : Coord.values()) {
            builders.add(new Cube.Builder().withCoordX(x).withCoordY(y).withCoordZ(z));
          }
        }
      }
      builders.stream().filter(b -> b.coordZ == ONE).forEach(b -> b.colorZ = WHITE);
      builders.stream().filter(b -> b.coordZ == MINUS_ONE).forEach(b -> b.colorZ = YELLOW);
      builders.stream().filter(b -> b.coordY == MINUS_ONE).forEach(b -> b.colorY = GREEN);
      builders.stream().filter(b -> b.coordY == ONE).forEach(b -> b.colorY = BLUE);
      builders.stream().filter(b -> b.coordX == MINUS_ONE).forEach(b -> b.colorX = ORANGE);
      builders.stream().filter(b -> b.coordX == ONE).forEach(b -> b.colorX = RED);
      INITIAL = builders.stream().map(b -> b.build()).collect(Collectors.toList());
    }

    private List<Cube> cubes = INITIAL;

    public Builder withCubes(List<Cube> cubes) {
      this.cubes = cubes;
      return this;
    }

    public Rubik build() {
      return new Rubik(this);
    }
  }
}
