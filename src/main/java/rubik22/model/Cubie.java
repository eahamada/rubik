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
}
