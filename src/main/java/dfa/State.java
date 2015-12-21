package dfa;

import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class State {
	public final String id;
	private final static LoadingCache<String, State> STATES = CacheBuilder.newBuilder()
		       .maximumSize(1000)
		       .build(
		           new CacheLoader<String, State>() {
		             public State load(String key) {
		               return new State(key);
		             }
		           });
	private State(String id) {
	  this.id = id;
	}
	public static State valueOf(String id){
		try {
			return STATES.get(id);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    State other = (State) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
@Override
public String toString() {
	return "State [id=" + id + "]";
}
public static State valueOf(int i) {
	return valueOf(String.valueOf(i));
}

	
}
