package rubik3.model;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import rubik22.model.AbstractRubik;
import rubik22.model.Rotation;

import com.google.common.collect.ImmutableList;

public class Rubik implements AbstractRubik {
	public final Cubie[] cubies;
	@Override
	public String toString() {
		byte[] bytes = new byte[20];
		for (int i = 0; i < cubies.length; i++) {
			bytes[i] = cubies[i].value;
		}
		return new String(bytes);
	}

	public List<String> aliases(){
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
		ImmutableList.Builder<String> builder = new ImmutableList.Builder<>();
		builder.add(toString(ImmutableList.of(c,d,e,f,g,h,a,b,j,k,l,i,o,p,q,r,s,t,m,n)));
		builder.add(toString(ImmutableList.of(e,f,g,h,a,b,c,d,k,l,i,j,q,r,s,t,m,n,o,p)));
		builder.add(toString(ImmutableList.of(g,h,a,b,c,d,e,f,l,i,j,k,s,t,m,n,o,p,q,r)));
		
		builder.add(toString(ImmutableList.of(g,f,e,k,q,r,s,l,h,d,p,t,a,b,c,j,o,n,m,i)));
		builder.add(toString(ImmutableList.of(e,k,q,r,s,l,g,f,d,p,t,h,c,j,o,n,m,i,a,b)));
		builder.add(toString(ImmutableList.of(q,r,s,l,g,f,e,k,p,t,h,d,o,n,m,i,a,b,c,j)));
		builder.add(toString(ImmutableList.of(s,l,g,f,e,k,q,r,t,h,d,p,m,i,a,b,c,j,o,n)));
		
		builder.add(toString(ImmutableList.of(s,r,q,p,o,n,m,t,l,k,j,i,g,f,e,d,c,b,a,h)));
		builder.add(toString(ImmutableList.of(q,p,o,n,m,t,s,r,k,j,i,l,e,d,c,b,a,h,g,f)));
		builder.add(toString(ImmutableList.of(o,n,m,t,s,r,q,p,j,i,l,k,c,b,a,h,g,f,e,d)));
		builder.add(toString(ImmutableList.of(m,t,s,r,q,p,o,n,i,l,k,j,a,h,g,f,e,d,c,b)));
		
		builder.add(toString(ImmutableList.of(m,n,o,j,c,b,a,i,t,p,d,h,s,r,q,k,e,f,g,l)));
		builder.add(toString(ImmutableList.of(o,j,c,b,a,i,m,n,p,d,h,t,q,k,e,f,g,l,s,r)));
		builder.add(toString(ImmutableList.of(c,b,a,i,m,n,o,j,d,h,t,p,e,f,g,l,s,r,q,k)));
		builder.add(toString(ImmutableList.of(a,i,m,n,o,j,c,b,h,t,p,d,g,l,s,r,q,k,e,f)));
		
		builder.add(toString(ImmutableList.of(q,k,e,d,c,j,o,p,r,f,b,n,s,l,g,h,a,i,m,t)));
		builder.add(toString(ImmutableList.of(e,d,c,j,o,p,q,k,f,b,n,r,g,h,a,i,m,t,s,l)));
		builder.add(toString(ImmutableList.of(c,j,o,p,q,k,e,d,b,n,r,f,a,i,m,t,s,l,g,h)));
		builder.add(toString(ImmutableList.of(o,p,q,k,e,d,c,j,n,r,f,b,m,t,s,l,g,h,a,i)));
		
		builder.add(toString(ImmutableList.of(g,l,s,t,m,i,a,h,f,r,n,b,e,k,q,p,o,j,c,d)));
		builder.add(toString(ImmutableList.of(s,t,m,i,a,h,g,l,r,n,b,f,q,p,o,j,c,d,e,k)));
		builder.add(toString(ImmutableList.of(m,i,a,h,g,l,s,t,n,b,f,r,o,j,c,d,e,k,q,p)));
		builder.add(toString(ImmutableList.of(a,h,g,l,s,t,m,i,b,f,r,n,c,d,e,k,q,p,o,j)));
		return builder.build();
	}
	public String toString(List<Cubie> list) {
		byte[] bytes = new byte[20];
		for (int i = 0; i < list.size(); i++) {
			bytes[i] = list.get(i).value;
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
			result = new Builder().withCubies(c,j,o,d,e,f,g,h,b,n,k,l,a,i,m,p,q,r,s,t).build();
			break;
		case LCCW:
			result = new Builder().withCubies(m,i,a,d,e,f,g,h,n,b,k,l,o,j,c,p,q,r,s,t).build();
			break;
		case RCW:
			result = new Builder().withCubies(a,b,c,d,q,k,e,h,i,j,r,f,m,n,o,p,s,l,g,t).build();
			break;
		case RCCW:
			result = new Builder().withCubies(a,b,c,d,g,l,s,h,i,j,f,r,m,n,o,p,e,k,q,t).build();
			break;
		case DCW:
			result = new Builder().withCubies(m,b,c,d,e,f,a,i,t,j,k,h,s,n,o,p,q,r,g,l).build();
			break;
		case DCCW:
			result = new Builder().withCubies(g,b,c,d,e,f,s,l,h,j,k,t,a,n,o,p,q,r,m,i).build();
			break;
		case UCW:
			result = new Builder().withCubies(a,b,o,j,c,f,g,h,i,p,d,l,m,n,q,k,e,r,s,t).build();
			break;
		case UCCW:
			result = new Builder().withCubies(a,b,e,k,q,f,g,h,i,d,p,l,m,n,c,j,o,r,s,t).build();
			break;
		case BCW:
			result = new Builder().withCubies(c,d,e,f,g,h,a,b,i,j,k,l,m,n,o,p,q,r,s,t).build();
			break;
		case BCCW:
			result = new Builder().withCubies(g,h,a,b,c,d,e,f,i,j,k,l,m,n,o,p,q,r,s,t).build();
			break;
		case FCW:
			result = new Builder().withCubies(a,b,c,d,e,f,g,h,i,j,k,l,s,t,m,n,o,p,q,r).build();
			break;
		case FCCW:
			result = new Builder().withCubies(a,b,c,d,e,f,g,h,i,j,k,l,o,p,q,r,s,t,m,n).build();
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

	public static Rubik valueOf(String string) {
		Cubie[] c = new Cubie[20];
		String[] split = string.split("");
		for (int i = 0; i < split.length; i++) {
			c[i] = Cubie.fromString(split[i]);
		}
		return new Builder().withCubies(c).build();
	}

}
