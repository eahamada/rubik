package rubik22.model;

import java.text.MessageFormat;
import java.util.Arrays;

public class Rubik implements AbstractRubik {
	private final Cubie[] cubies;

	/* (non-Javadoc)
	 * @see rubik22.model.AbstractRubik#toString()
	 */
	@Override
	public String toString() {
		byte[] bytes = new byte[8];
		for (int i = 0; i < cubies.length; i++) {
			bytes[i] = cubies[i].value;
		}
		return new String(bytes);
	}

	/* (non-Javadoc)
	 * @see rubik22.model.AbstractRubik#hashCode()
	 */
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

	/* (non-Javadoc)
	 * @see rubik22.model.AbstractRubik#rotate(rubik22.model.Rotation)
	 */
	@Override
	public final AbstractRubik rotate(Rotation r) {
		AbstractRubik result = null;
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
			result = new Builder().withCubies(e, a, c, d, f, b, g, h).build();
			break;
		case LCCW:
			result = new Builder().withCubies(b, f, c, d, a, e, g, h).build();
			break;
		case RCW:
			result = new Builder().withCubies(a, b, g, c, e, f, h, d).build();
			break;
		case RCCW:
			result = new Builder().withCubies(a, b, d, h, e, f, c, g).build();
			break;
		case DCW:
			result = new Builder().withCubies(e, b, c, a, h, f, g, d).build();
			break;
		case DCCW:
			result = new Builder().withCubies(d, b, c, h, a, f, g, e).build();
			break;
		case UCW:
			result = new Builder().withCubies(a, f, b, d, e, g, c, h).build();
			break;
		case UCCW:
			result = new Builder().withCubies(a, c, g, d, e, b, f, h).build();
			break;
		case BCW:
			result = new Builder().withCubies(d, a, b, c, e, f, g, h).build();
			break;
		case BCCW:
			result = new Builder().withCubies(b, c, d, a, e, f, g, h).build();
			break;
		case FCW:
			result = new Builder().withCubies(a, b, c, d, h, e, f, g).build();
			break;
		case FCCW:
			result = new Builder().withCubies(a, b, c, d, f, g, h, e).build();
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

		public AbstractRubik build() {

			return new Rubik(this);
		}
	}

}
