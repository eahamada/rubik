package rubik22.model;

public enum Cubie {
	WGO((byte) 'A'), 
	WBO((byte) 'B'),
	YBO((byte) 'C'), 
	YGO((byte) 'D'),
	WGR((byte) 'E'), 
	WBR((byte) 'F'), 
	YBR((byte) 'G'),
	YGR((byte) 'H');
	public final byte value;

	private Cubie(byte value) {
		this.value = value;
	}
	public static Cubie fromString(String s){
		switch (s) {
		case "A":
			return WGO;
		case "B":
			return WBO;
		case "C":
			return YBO;
		case "D":
			return YGO;
		case "E":
			return WGR;
		case "F":
			return WBR;
		case "G":
			return YBR;
		case "H":
			return YGR;
		default:
			throw new IllegalArgumentException("Invalid String: "+s);
		}
	}
}
