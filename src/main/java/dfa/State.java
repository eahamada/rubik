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

	
}
