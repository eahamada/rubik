package rubik22.model;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class Rubik {
  private final Cubie[] cubies;
  private final static Cache<String, Rubik> RUBIKS = CacheBuilder.newBuilder().maximumSize(1000)
      .build();

  @Override
  public String toString() {
    byte[] bytes = new byte[8];
    for (int i = 0; i < cubies.length; i++) {
      bytes[i] = cubies[i].value;
    }
    return new String(bytes);
  }

  public List<String> aliases() {
   return Collections.emptyList();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(cubies);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Rubik other = (Rubik) obj;
    if (!Arrays.equals(cubies, other.cubies))
      return false;
    return true;
  }

  public final Rubik rotate(Rotation r) {
    Builder builder = new Builder();
    final Cubie a = cubies[0];
    final Cubie b = cubies[1];
    final Cubie c = cubies[2];
    final Cubie d = cubies[3];
    final Cubie e = cubies[4];
    final Cubie f = cubies[5];
    final Cubie g = cubies[6];
    final Cubie h = cubies[7];

    switch (r) {
    case LCW:
      builder = builder.withCubies(e, a, c, d, f, b, g, h);
      break;
    case LCCW:
      builder = builder.withCubies(b, f, c, d, a, e, g, h);
      break;
    case RCW:
      builder = builder.withCubies(a, b, g, c, e, f, h, d);
      break;
    case RCCW:
      builder = builder.withCubies(a, b, d, h, e, f, c, g);
      break;
    case DCW:
      builder = builder.withCubies(d, b, c, h, a, f, g, e);
      break;
    case DCCW:
      builder = builder.withCubies(e, b, c, a, h, f, g, d);
      break;
    case UCW:
      builder = builder.withCubies(a, f, b, d, e, g, c, h);
      break;
    case UCCW:
      builder = builder.withCubies(a, c, g, d, e, b, f, h);
      break;
    case BCW:
      builder = builder.withCubies(d, a, b, c, e, f, g, h);
      break;
    case BCCW:
      builder = builder.withCubies(b, c, d, a, e, f, g, h);
      break;
    case FCW:
      builder = builder.withCubies(a, b, c, d, h, e, f, g);
      break;
    case FCCW:
      builder = builder.withCubies(a, b, c, d, f, g, h, e);
      break;
    default:
      throw new IllegalArgumentException(MessageFormat.format("Invalid rotation: {0}", r));
    }
    return builder.build();
  }

  private Rubik(Builder builder) {
    this.cubies = builder.cubies;
  }

  public static class Builder {

    private Cubie[] cubies;

    public Builder withCubies(Cubie... cubies) {
      this.cubies = cubies;
      return this;
    }

    public Rubik build() {
      return new Rubik(this);
    }
  }

  public static Rubik valueOf(String s) {
    Rubik r = RUBIKS.getIfPresent(s);
    if (r == null) {
      Cubie[] c = new Cubie[8];
      String[] split = s.split("");
      for (int i = 0; i < split.length; i++) {
        c[i] = Cubie.fromString(split[i]);
      }
      r = new Builder().withCubies(c).build();
      RUBIKS.put(s, r);
    }
    return r;
  }

}
