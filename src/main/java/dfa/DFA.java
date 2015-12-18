package dfa;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class DFA {
	public final Set<State> Q;
	public final Set<Character> Σ;
	public final BiFunction<State, Character, State> δ;
	public final State q0;
	public final Set<State> F;
	
	public final static BiFunction<State, Character, String> PATTERN =
      (state, character) -> MessageFormat.format("{0}:{1}", state, character);

	private DFA(Builder builder) {
		this.Q = builder.Q;
		this.Σ = builder.Σ;
		this.δ = builder.δ;
		this.q0 = builder.q0;
		this.F = builder.F;
	}

	public static class Builder {

		private Set<State> Q;
		private Set<Character> Σ;
		private BiFunction<State, Character, State> δ;
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

		public Builder withΣ(Set<Character> Σ) {
			this.Σ = Σ;
			return this;
		}

		public Builder withΣ(char... SIGMA) {
			ImmutableSet.Builder<Character> builder = ImmutableSet.builder();
			for (char c : SIGMA) {
				builder.add(c);
			}
			return withΣ(builder.build());
		}

		public Builder withδ(
		    BiFunction<State, Character, State> δ) {
			this.δ = δ;
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
			r = δ.apply(r,word.charAt(i));
		}
		result = F.contains(r);
		return result;
	}
	public DFA forceAccept(String word) {
		State r = q0;
		State t = q0;
		int i = 0;
		while (i < word.length() && t!=null) {
			t = δ.apply(r, word.charAt(i));
			if(t != null){
				r = t;
				i++;
			}
		}
		String substring = word.substring(i);
		System.out.println(substring);
		return null;
	}
	
	
   @SuppressWarnings("unchecked")
  public DFA removeUnreachableStates(){
      Set<State> reachable_states = ImmutableSet.of(q0);
      Set<State> new_states = Collections.EMPTY_SET;
      do{
        Set<State> temp = Collections.EMPTY_SET;
        for (State q : new_states) {
          for (Character c : Σ) {
            temp = Sets.union(temp, ImmutableSet.of(δ.apply(q,c))).immutableCopy();
          }
        }
        new_states = Sets.difference(temp, reachable_states).immutableCopy();
        reachable_states = Sets.union(reachable_states, new_states).immutableCopy();
      }while(!new_states.isEmpty());
      return new DFA.Builder()
        .withQ(reachable_states)
        .withΣ(Σ)
        .withδ(δ)
        .withQ0(q0)
        .withF(F)
        .build();
   }
   @SuppressWarnings("unchecked")
  public DFA minimize(){
     Set<Set<State>> P = Sets.newHashSet(F, Sets.difference(Q, F).immutableCopy());
     Set<Set<State>> W = Sets.newHashSet(F);
     while(!W.isEmpty()){
       Set<State> A = W.iterator().next();
       W.remove(A);
       for (Character c : Σ) {
        Set<State> X = Q.stream().filter(s -> A.contains(δ.apply(s,c))).collect(Collectors.toSet());
        
        Set<Set<State>> Ys = P.stream().filter(Y -> !Sets.intersection(X, Y).isEmpty() && !Sets.difference(Y, X).isEmpty()).collect(Collectors.toSet());
        for (Set<State> Y : Ys) {
          Set<State> XInterY = Sets.intersection(X, Y);
          SetView<State> YDiffX = Sets.difference(Y, X);
          P.remove(Y);
          P.add(XInterY);
          P.add(YDiffX);
          if(W.contains(Y)){
            W.remove(Y);
            W.add(XInterY);
            W.add(YDiffX);
          }else{
            BiFunction<Set<State>,Set<State>,Set<State>> min = (s0, s1) -> s0.size() <= s1.size() ? s0:s1;
            W.add(min.apply(XInterY,YDiffX));
          }
          
        }
      }
     }
     final AtomicInteger i = new AtomicInteger(0);
    Set<State> minQ = P.stream().map(s -> new State.Builder().withId("q"+i.getAndIncrement()).build()).collect(Collectors.toSet());
    State minQ0 = P.stream().filter(s -> s.contains(q0)).map(s -> new State.Builder().withId(String.valueOf(s.hashCode())).build()).collect(Collectors.toList()).get(0);
    BiFunction<State, Character, State> minδ = (s,c) -> {
      return new State.Builder().withId("").build();
    };
    Set<State> minF = P.stream().filter(s -> !Sets.intersection(s, F).isEmpty()).map(s -> new State.Builder().withId(String.valueOf(s.hashCode())).build()).collect(Collectors.toSet());
    return new DFA.Builder()
         .withQ(minQ)
         .withQ0(minQ0)
         .withδ(minδ)
         .withF(minF)
         .withΣ(Σ)
         .build();
   }
   
   public void prettyPrint(){
     final PrettyPrinter printer = new PrettyPrinter(System.out);
     String[][] arrays = new String[Q.size()+1][Σ.size()+2] ;
     arrays[0][0] ="";
     arrays[0][1] ="";
     int j = 2;
     for (Character c : Σ) {
      arrays[0][j] = String.valueOf(c);
      int i = 1;
      for (State q : Q) {
        arrays[i][0] ="";
        if(q == q0){
          arrays[i][0] += ">";
        }
        if(F.contains(q)){
          arrays[i][0] += "*";
        }
          arrays[i][1] = q.id;
          arrays[i++][j] = δ.apply(q, c).id;
      } 
      j++;
    }
    printer.print(arrays);
   }

}
