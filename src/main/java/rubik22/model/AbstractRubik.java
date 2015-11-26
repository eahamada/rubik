package rubik22.model;

public interface AbstractRubik {

	public abstract String toString();

	public abstract int hashCode();

	public abstract AbstractRubik rotate(Rotation r);

}