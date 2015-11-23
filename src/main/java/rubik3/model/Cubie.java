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
}
