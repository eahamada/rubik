package rubik3.model;

public enum Cubie {
	WGO((byte) 'A'), 
	WO((byte) 'B'),
	WBO((byte) 'C'),
	BO((byte) 'D'),
	YBO((byte) 'E'),
	YO((byte) 'F'),
	YGO((byte) 'G'),
	GO((byte) 'H'),
	WG((byte) 'I'),
	WB((byte) 'J'),
	YB((byte) 'K'),
	YG((byte) 'L'),
	WGR((byte) 'M'),
	WR((byte) 'N'),
	WBR((byte) 'O'),
	BR((byte) 'P'),
	YBR((byte) 'Q'),
	YR((byte) 'R'),
	YGR((byte) 'S'),
	GR((byte) 'T');
	public final byte value;

	private Cubie(byte value) {
		this.value = value;
	}
	
	public static Cubie fromString(String s){
		switch (s) {
		case "A":
			return WGO;
		case "B":
			return WO;
		case "C":
			return WBO;
		case "D":
			return BO;
		case "E":
			return YBO;
		case "F":
			return YO;
		case "G":
			return YGO;
		case "H":
			return GO;
		case "I":
			return WG;
		case "J":
			return WB;
		case "K":
			return YB;
		case "L":
			return YG;
		case "M":
			return WGR;
		case "N":
			return WR;
		case "O":
			return WBR;
		case "P":
			return BR;
		case "Q":
			return YBR;
		case "R":
			return YR;
		case "S":
			return YGR;
		case "T":
			return GR;
		default:
			throw new IllegalArgumentException("Invalid String: "+s);
		}
	}
}
