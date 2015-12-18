package dfa;

public class State {
	public final String id;

	private State(Builder builder) {
	  this.id = builder.id;
	}

	public static class Builder{

		private String id;

		public Builder withId(String id) {
		  this.id = id;
		  return this;
		}

		public State build() {
		  return new State(this);
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

	
}