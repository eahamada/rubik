package rubik;

import java.util.Arrays;

public final class Cube implements HasCoords {
  public final Coord coordX;
  public final Coord coordY;
  public final Coord coordZ;
  public final Color colorX;
  public final Color colorY;
  public final Color colorZ;

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((colorX == null) ? 0 : colorX.hashCode());
    result = prime * result + ((colorY == null) ? 0 : colorY.hashCode());
    result = prime * result + ((colorZ == null) ? 0 : colorZ.hashCode());
    result = prime * result + ((coordX == null) ? 0 : coordX.hashCode());
    result = prime * result + ((coordY == null) ? 0 : coordY.hashCode());
    result = prime * result + ((coordZ == null) ? 0 : coordZ.hashCode());
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
    Cube other = (Cube) obj;
    if (colorX != other.colorX)
      return false;
    if (colorY != other.colorY)
      return false;
    if (colorZ != other.colorZ)
      return false;
    if (coordX != other.coordX)
      return false;
    if (coordY != other.coordY)
      return false;
    if (coordZ != other.coordZ)
      return false;
    return true;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    Arrays.asList(colorX, colorY, colorZ).stream().filter(c -> c != null)
        .forEach(c -> sb.append(c.value));
    return sb.toString();
  }

  public char print(Face f) {
    switch (f) {
    case LEFT:
    case RIGHT:
      return (colorX.value);
    case UP:
    case DOWN:
      return (colorY.value);
    case FRONT:
    case BACK:
      return (colorZ.value);

    default:
      throw new IllegalArgumentException();
    }
  }

  private Cube(Builder builder) {
    this.coordX = builder.coordX;
    this.coordY = builder.coordY;
    this.coordZ = builder.coordZ;
    this.colorX = builder.colorX;
    this.colorY = builder.colorY;
    this.colorZ = builder.colorZ;
  }

  public static class Builder implements HasCoords{

    public Coord coordX;
    public Coord coordY;
    public Coord coordZ;
    public Color colorX;
    public Color colorY;
    public Color colorZ;

    public Builder withCoordX(Coord coordX) {
      this.coordX = coordX;
      return this;
    }

    public Builder withCoordY(Coord coordY) {
      this.coordY = coordY;
      return this;
    }

    public Builder withCoordZ(Coord coordZ) {
      this.coordZ = coordZ;
      return this;
    }

    public Builder withCoords(int[] coords) {
      if (coords == null || coords.length != 3) {
        throw new IllegalArgumentException();
      }
      this.coordX = Functions.TO_COORD.apply(coords[0]);
      this.coordY = Functions.TO_COORD.apply(coords[1]);
      this.coordZ = Functions.TO_COORD.apply(coords[2]);
      return this;
    }

    public Builder withColorX(Color colorX) {
      this.colorX = colorX;
      return this;
    }

    public Builder withColorY(Color colorY) {
      this.colorY = colorY;
      return this;
    }

    public Builder withColorZ(Color colorZ) {
      this.colorZ = colorZ;
      return this;
    }

    public Cube build() {
      return new Cube(this);
    }

    @Override
    public Coord getX() {
      return coordX;
    }

    @Override
    public Coord getY() {
      return coordY;
    }

    @Override
    public Coord getZ() {
      return coordZ;
    }
  }

  public int[] getCoords() {
    int[] result = { coordX.value, coordY.value, coordZ.value };
    return result;
  }

  @Override
  public Coord getX() {
    return coordX;
  }

  @Override
  public Coord getY() {
    return coordY;
  }

  @Override
  public Coord getZ() {
    return coordZ;
  }

}
