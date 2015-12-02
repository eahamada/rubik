package rubik22.model;

import java.util.List;

public interface AbstractRubik {

	public abstract String toString();
	
	public abstract List<String> aliases();

	public abstract int hashCode();

	public abstract AbstractRubik rotate(Rotation r);

}