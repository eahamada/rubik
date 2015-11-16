package rubik;

import static rubik.Color.BLUE;
import static rubik.Color.GREEN;
import static rubik.Color.ORANGE;
import static rubik.Color.RED;
import static rubik.Color.WHITE;
import static rubik.Color.YELLOW;
import static rubik.Coord.ONE;
import static rubik.Coord.MINUS_ONE;
import static rubik.Face.BACK;
import static rubik.Face.DOWN;
import static rubik.Face.FRONT;
import static rubik.Face.LEFT;
import static rubik.Face.RIGHT;
import static rubik.Face.UP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.base.Splitter;

import rubik.Rubik.Builder;

public enum Functions {
  INSTANCE;
  public static final Function<Character, Color> TO_COLOR = c -> {
    final Color color;
    switch (c) {
    case 'w':
      color = WHITE;
      break;
    case 'y':
      color = YELLOW;
      break;
    case 'g':
      color = GREEN;
      break;
    case 'b':
      color = BLUE;
      break;
    case 'o':
      color = ORANGE;
      break;
    case 'r':
      color = RED;
      break;
    default:
      throw new IllegalArgumentException("Invalid color: " + c);
    }
    return color;
  };
  public static final Function<Integer, Coord> TO_COORD = i -> {
    if (i == -1) {
      return MINUS_ONE;
    }
    if (i == 1) {
      return ONE;
    }
    throw new IllegalArgumentException("Invalid coord: " + i);
  };
  public static final Function<Character, Face> TO_FACE = c -> {
    final Face result;
    switch (c) {
    case 'L':
      result = LEFT;
      break;
    case 'R':
      result = RIGHT;
      break;
    case 'D':
      result = DOWN;
      break;
    case 'U':
      result = UP;
      break;
    case 'B':
      result = BACK;
      break;
    case 'F':
      result = FRONT;
      break;
    default:
      throw new IllegalArgumentException("Invalid Face: " + c);
    }
    return result;
  };
  public final static Function<String, Rubik> TO_RUBIK = s -> {

    List<Cube.Builder> builders = new ArrayList<>();
    for (Coord x : Coord.values()) {
      for (Coord y : Coord.values()) {
        for (Coord z : Coord.values()) {
          builders.add(new Cube.Builder().withCoordX(x).withCoordY(y).withCoordZ(z));
        }
      }
    }
    Iterator<String> it = Splitter.fixedLength(1).split(s).iterator();
    Iterator<Face> faceIt = Arrays.asList(Face.values()).iterator();
    builders.stream().filter(faceIt.next().predicate).forEach(b -> b.colorX = TO_COLOR.apply(it.next().charAt(0)));
    builders.stream().filter(faceIt.next().predicate).forEach(b -> b.colorX = TO_COLOR.apply(it.next().charAt(0)));
    builders.stream().filter(faceIt.next().predicate).forEach(b -> b.colorY = TO_COLOR.apply(it.next().charAt(0)));
    builders.stream().filter(faceIt.next().predicate).forEach(b -> b.colorY = TO_COLOR.apply(it.next().charAt(0)));
    builders.stream().filter(faceIt.next().predicate).forEach(b -> b.colorZ = TO_COLOR.apply(it.next().charAt(0)));
    builders.stream().filter(faceIt.next().predicate).forEach(b -> b.colorZ = TO_COLOR.apply(it.next().charAt(0)));
    
    final Builder builder = new Builder();
    builder.withCubes(builders.stream().map(b -> b.build()).collect(Collectors.toList()));
    return builder.build();
  };

}
