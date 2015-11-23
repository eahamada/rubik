package rubik3.model;

import java.text.MessageFormat;
import java.util.Arrays;

import rubik22.model.Rotation;

public class Rubik {
	private final Cubie[] cubies;

	@Override
	public String toString() {
		byte[] bytes = new byte[20];
		for (int i = 0; i < cubies.length; i++) {
			bytes[i] = cubies[i].value;
		}
		return new String(bytes);
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

	public final Rubik rotate(Rotation rotation) {
		Rubik result = null;
		final Cubie a = cubies[0];
		final Cubie b = cubies[1];
		final Cubie c = cubies[2];
		final Cubie d = cubies[3];
		final Cubie e = cubies[4];
		final Cubie f = cubies[5];
		final Cubie g = cubies[6];
		final Cubie h = cubies[7];
		final Cubie i = cubies[8];
		final Cubie j = cubies[9];
		final Cubie k = cubies[10];
		final Cubie l = cubies[11];
		final Cubie m = cubies[12];
		final Cubie n = cubies[13];
		final Cubie o = cubies[14];
		final Cubie p = cubies[15];
		final Cubie q = cubies[16];
		final Cubie r = cubies[17];
		final Cubie s = cubies[18];
		final Cubie t = cubies[19];

		switch (rotation) {
		case LCW:
			result = new Builder().withCubies().build();
			break;
		case LCCW:
			result = new Builder().withCubies().build();
			break;
		case RCW:
			result = new Builder().withCubies().build();
			break;
		case RCCW:
			result = new Builder().withCubies().build();
			break;
		case DCW:
			result = new Builder().withCubies().build();
			break;
		case DCCW:
			result = new Builder().withCubies().build();
			break;
		case UCW:
			result = new Builder().withCubies().build();
			break;
		case UCCW:
			result = new Builder().withCubies().build();
			break;
		case BCW:
			result = new Builder().withCubies().build();
			break;
		case BCCW:
			result = new Builder().withCubies().build();
			break;
		case FCW:
			result = new Builder().withCubies().build();
			break;
		case FCCW:
			result = new Builder().withCubies().build();
			break;
		default:
			throw new IllegalArgumentException(MessageFormat.format(
					"Invalid rotation: {0}", r));
		}
		return result;
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

}
