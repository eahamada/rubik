package dfa;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
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
      (state, character) -> MessageFormat.format("{0}:{1}", state.id, character);
      
	@SuppressWarnings("unused")
	private Set<Character> Σ(State q) {
		return Σ.stream()
		.filter(c -> δ.apply(q, c) != null)
		.collect(Collectors.toSet());
	}

	private DFA(Builder builder) {
		this.Q = builder.Q;
		this.Σ = builder.Σ;
		this.δ = builder.δ;
		this.q0 = builder.q0;
		this.F = builder.F;
	}
	public Builder toBuilder(){
		Builder builder = new Builder()
			.withF(ImmutableSet.copyOf(F))
			.withQ(ImmutableSet.copyOf(Q))
			.withΣ(ImmutableSet.copyOf(Σ))
			.withQ0(q0)
			.withδ(δ)
			;
		return builder;
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
		return accept(q0, word);
	}
	public boolean acceptStream(State s, String word) {
		boolean result = false;
		State r = word.chars().mapToObj(i -> (char) i).reduce(s, δ, (p,q) ->p);
		result = F.contains(r);
		return result;
	}
	public boolean accept(State s, String word) {
		boolean result = false;
		State r = s;
		for (int i = 0; i < word.length() && r!=null; i++) {
			r = δ.apply(r,word.charAt(i));
		}
		result = F.contains(r);
		return result;
	}
	public boolean accept(State s, Character c) {
		return F.contains(δ.apply(s, c));
	}
	public DFA forceAccept(String word) {
		if(accept(word)){
			return this;
		}else{
			Builder dfaBuilder = toBuilder();
			State temp = q0;
			int i  = 0;
			for (; i < word.length(); i++) {
				char c = word.charAt(i);
				State t = δ.apply(temp, c);
				if(t==null){
					break;
				}else{
					temp = t;
				}
			}
			String substring = word.substring(i);
			ImmutableSet.Builder<State> newQ = new ImmutableSet.Builder<State>().addAll(Q);
			ImmutableMap.Builder<String, State> newMap = toMapBuilder(δ);
			ImmutableSet.Builder<Character> newΣ = new ImmutableSet.Builder<Character>().addAll(Σ);
			for (int j = 0; j < substring.length(); j++) {
				State qj = State.valueOf("q"+(Q.size()+j));
				char c = substring.charAt(j);
				newΣ.add(c);
				newQ.add(qj);
				newMap.put(PATTERN.apply(temp, c), qj);
				temp = qj;
			}
			ImmutableMap<String, State> map = newMap.build();
			Set<State> _F = new ImmutableSet.Builder<State>().addAll(F).add(temp).build();
			BiFunction<State, Character, State> _δ = (state,character) -> map .get(PATTERN.apply(state, character));
			Set<Character> _Σ = newΣ.build();
			Set<State> _Q = newQ.build();
			return dfaBuilder.withQ(_Q).withF(_F).withδ(_δ ).withΣ(_Σ).build() ; 
		}
	}
	public DFA forceAccept(Character c) {
		return forceAccept(q0,c);
	}
	public DFA forceAccept(State s, Character c) {
		State t = δ.apply(s, c);
		if(F.contains(t)){
			return this;
		}else{
			Builder builder = toBuilder();
			if(t == null){
				t = State.valueOf("q"+Q.size());
			}
			final Map<String, State> map = toMapBuilder(δ).put(PATTERN.apply(s, c), t).build();
			BiFunction<State, Character, State> _δ = (state,character) -> map.get(PATTERN.apply(state, character));
			Set<State> _F = new ImmutableSet.Builder<State>().add(t).addAll(F).build();
			Set<Character> _Σ = new ImmutableSet.Builder<Character>().add(c).addAll(Σ).build();
			Set<State> _Q = new ImmutableSet.Builder<State>().add(t).addAll(Q).build();
			return builder.withQ(_Q).withF(_F).withδ(_δ ).withΣ(_Σ).build() ;
		}
	}
	public DFA normalize(){
		ImmutableMap.Builder<String, State> deltaMap = ImmutableMap.builder();
		ImmutableMap.Builder<State, State> fromQtoNewQ = ImmutableMap.builder();
		State newQ0 = State.valueOf("q0");
		fromQtoNewQ.put(q0, newQ0);
		AtomicInteger i = new AtomicInteger(1);
		Set<State> newQ = new ImmutableSet.Builder<State>()
				.add(newQ0)
				.addAll(Q.stream()
						.filter(q->q!=q0)
						.map(q->{
							State _q = State.valueOf("q"+String.valueOf(i.getAndIncrement()));
							fromQtoNewQ.put(q,_q);
							return _q;
						})
						.collect(Collectors.toSet()))
				.build();
		ImmutableMap<State, State> QtoNewQ = fromQtoNewQ.build();
		Set<State> newF = F.stream().map(q -> QtoNewQ.get(q)).collect(Collectors.toSet());
		for (State state : Q) {
			for (Character character : Σ) {
				State apply = δ.apply(state, character);
				if(apply != null){
					deltaMap.put(PATTERN.apply(QtoNewQ.get(state), character), QtoNewQ.get(apply));
				}
			}
		}
		ImmutableMap<String, State> map = deltaMap.build();
		BiFunction<State, Character, State> newδ = (s,c)->map.get(PATTERN.apply(s, c));
		return new Builder().withF(newF).withQ(newQ).withQ0(newQ0).withδ(newδ).withΣ(Σ).build();
	}
	private ImmutableMap.Builder<String, State> toMapBuilder(BiFunction<State, Character, State> delta){
		ImmutableMap.Builder<String, State> builder = ImmutableMap.builder();
		for (State state : Q) {
			for (Character character : Σ) {
				State apply = delta.apply(state, character);
				if(apply !=null){
				builder.put(PATTERN.apply(state, character), apply);
				}
			}
		}
		return builder;
	}
	
   @SuppressWarnings("unchecked")
  public DFA removeUnreachableStates(){
      Set<State> reachable_states = ImmutableSet.of(q0);
      Set<State> new_states = ImmutableSet.of(q0);
      do{
        Set<State> temp = Collections.EMPTY_SET;
        for (State q : new_states) {
          for (Character c : Σ) {
        	  
            State apply = δ.apply(q,c);
            if(apply != null){
            	temp = Sets.union(temp, ImmutableSet.of(apply));
            }
          }
        }
        new_states = Sets.difference(temp, reachable_states);
        reachable_states = Sets.union(reachable_states, new_states);
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
	 DFA minimized = ImmutableList.of(this).stream()
	 	.map(dfa -> dfa.removeUnreachableStates())
	 	.map(dfa -> {
	 		Set<Set<State>> P = Sets.newHashSet(dfa.F, Sets.difference(dfa.Q, dfa.F));
	 	     Set<Set<State>> W = new HashSet<Set<State>>();
	 	     W.add(dfa.F);
	 		 while(!W.isEmpty()){
	 		       Set<State> A = W.iterator().next();
	 		       W.remove(A);
	 		       dfa.Σ.stream().forEach(c -> {
	 		    	   Set<State> X = dfa.Q.stream().filter(s -> A.contains(dfa.δ.apply(s,c))).collect(Collectors.toSet());
	 		           
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
	 		       });
	 		     }
	 		 return buildMinimizedDFA(dfa.Σ, dfa.F, dfa.q0, dfa.δ, P);
	 	})
	 	.collect(Collectors.toList())
	 	.get(0);	   
       
     return minimized;
    
   }
private DFA buildMinimizedDFA(Set<Character> _Σ, Set<State> _F, State _q0,
		BiFunction<State, Character, State> _δ, Set<Set<State>> P) {
	Set<State> minQ = P.stream().map(s -> State.valueOf(String.valueOf(s.hashCode()))).collect(Collectors.toSet());
    State minQ0 = P.stream().filter(s -> s.contains(_q0)).map(s -> State.valueOf(String.valueOf(s.hashCode()))).collect(Collectors.toList()).get(0);
    final ImmutableMap.Builder<String, State> builder = ImmutableMap.builder();
    P.stream().forEach(set -> {
    	State s = set.iterator().next();
    	for (Character a : _Σ) {
			State t = _δ.apply(s, a);
			if(t != null){
				State state = P.stream().filter(p -> p.contains(t)).map(p->State.valueOf(String.valueOf(p.hashCode()))).collect(Collectors.toList()).get(0);
				builder.put(PATTERN.apply(State.valueOf(String.valueOf(set.hashCode())), a), state);
			}
		}
    });
    BiFunction<State, Character, State> minδ = (s,c) -> {
    	 Map<String, State> map = builder.build();
    	 return map.get(PATTERN.apply(s,c));
    };
    Set<State> minF = P.stream().filter(s -> !Sets.intersection(s, _F).isEmpty()).map(s -> State.valueOf(String.valueOf(s.hashCode()))).collect(Collectors.toSet());
    return new DFA.Builder()
         .withQ(minQ)
         .withQ0(minQ0)
         .withδ(minδ)
         .withF(minF)
         .withΣ(Σ)
         .build()
         ;
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
        if(q.equals(q0)){
          arrays[i][0] += ">";
        }
        if(F.contains(q)){
          arrays[i][0] += "*";
        }
          arrays[i][1] = q.id;
          State s = δ.apply(q, c);
		arrays[i++][j] = s!=null?s.id:"";
      } 
      j++;
    }
    printer.print(arrays);
   }
}
