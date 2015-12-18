package dfa;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class DFA {
	public final Set<State> Q;
	public final Set<Character> SIGMA;
	public final Map<StateCharacter, State> delta;
	public final State q0;
	public final Set<State> F;

	private DFA(Builder builder) {
		this.Q = builder.Q;
		this.SIGMA = builder.SIGMA;
		this.delta = builder.delta;
		this.q0 = builder.q0;
		this.F = builder.F;
	}

	public static class Builder {

		private Set<State> Q;
		private Set<Character> SIGMA;
		private Map<StateCharacter, State> delta;
		private State q0;
		private Set<State> F;

		public Builder withQ(Set<State> Q) {
			this.Q = Q;
			return this;
		}

		public Builder withQ(State... Q) {
			ImmutableSet.Builder<State> builder = ImmutableSet.builder();
			for (State q : Q) {
				builder.add(q);
			}
			return withQ(builder.build());
		}

		public Builder withSIGMA(Set<Character> SIGMA) {
			this.SIGMA = SIGMA;
			return this;
		}

		public Builder withSIGMA(char... SIGMA) {
			ImmutableSet.Builder<Character> builder = ImmutableSet.builder();
			for (char c : SIGMA) {
				builder.add(c);
			}
			return withSIGMA(builder.build());
		}

		public Builder withDelta(
				Map<StateCharacter, State> delta) {
			this.delta = delta;
			return this;
		}

		public Builder withQ0(State q0) {
			this.q0 = q0;
			return this;
		}

		public Builder withF(Set<State> F) {
			this.F = F;
			return this;
		}

		public Builder withF(State... F) {
			ImmutableSet.Builder<State> builder = ImmutableSet.builder();
			for (State f : F) {
				builder.add(f);
			}
			return withF(builder.build());
		}

		public DFA build() {
			return new DFA(this);
		}
	}

	public boolean accept(String word) {
		boolean result = false;
		State r = q0;
		for (int i = 0; i < word.length() && r!=null; i++) {
			r = delta.get(new StateCharacter(r,word.charAt(i)));
		}
		result = F.contains(r);
		return result;
	}
	public DFA forceAccept(String word) {
		State r = q0;
		State t = q0;
		int i = 0;
		while (i < word.length() && t!=null) {
			t = delta.get(new StateCharacter(r,word.charAt(i)));
			if(t != null){
				r = t;
				i++;
			}
		}
		String substring = word.substring(i);
		System.out.println(substring);
		return null;
	}

}
