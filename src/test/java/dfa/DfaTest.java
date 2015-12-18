package dfa;

import static dfa.DFA.PATTERN;

import java.util.Map;
import java.util.function.BiFunction;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class DfaTest {

  @Test
  @Ignore
	public void test() {
	  State q0 = new State.Builder().withId("q0").build();
		State q1 = new State.Builder().withId("q1").build();
		State q2 = new State.Builder().withId("q2").build();

		BiFunction<State, Character, State> δ =
		    (s, c) -> {
		    ImmutableMap.Builder<String, State> builder = ImmutableMap.builder();
		    Map<String, State> map = builder
        .put(PATTERN.apply(q0,'0'), q0)
        .put(PATTERN.apply(q0,'3'), q0)
        .put(PATTERN.apply(q0,'6'), q0)
        .put(PATTERN.apply(q0,'9'), q0)

        .put(PATTERN.apply(q0,'1'), q1)
        .put(PATTERN.apply(q0,'4'), q1)
        .put(PATTERN.apply(q0,'7'), q1)

        .put(PATTERN.apply(q0,'2'), q2)
        .put(PATTERN.apply(q0,'5'), q2)
        .put(PATTERN.apply(q0,'8'), q2)

        .put(PATTERN.apply(q1,'0'), q1)
        .put(PATTERN.apply(q1,'3'), q1)
        .put(PATTERN.apply(q1,'6'), q1)
        .put(PATTERN.apply(q1,'9'), q1)

        .put(PATTERN.apply(q1,'1'), q2)
        .put(PATTERN.apply(q1,'4'), q2)
        .put(PATTERN.apply(q1,'7'), q2)

        .put(PATTERN.apply(q1,'2'), q0)
        .put(PATTERN.apply(q1,'5'), q0)
        .put(PATTERN.apply(q1,'8'), q0)

        .put(PATTERN.apply(q2,'0'), q2)
        .put(PATTERN.apply(q2,'3'), q2)
        .put(PATTERN.apply(q2,'6'), q2)
        .put(PATTERN.apply(q2,'9'), q2)

        .put(PATTERN.apply(q2,'1'), q0)
        .put(PATTERN.apply(q2,'4'), q0)
        .put(PATTERN.apply(q2,'7'), q0)

        .put(PATTERN.apply(q2,'2'), q1)
        .put(PATTERN.apply(q2,'5'), q1)
        .put(PATTERN.apply(q2,'8'), q1)
        .build();
		    return map.get(PATTERN.apply(s,c));
		};
		DFA d = new DFA.Builder()
						.withQ(q0, q1, q2)
						.withΣ('0', '1', '2','3','4','5','6','7','8','9')
						.withδ(δ)
						.withQ0(q0)
						.withF(q0)
						.build();
		d.prettyPrint();
	}
  @Test
  public void testMin() {
    State a = new State.Builder().withId("a").build();
    State b = new State.Builder().withId("b").build();
    State c = new State.Builder().withId("c").build();
    State d = new State.Builder().withId("d").build();
    State e = new State.Builder().withId("e").build();
    State f = new State.Builder().withId("f").build();
    

    BiFunction<State, Character, State> δ =
        (state, character) -> {
        ImmutableMap.Builder<String, State> builder = ImmutableMap.builder();
        Map<String, State> map = builder
        .put(PATTERN.apply(a,'0'), b)
        .put(PATTERN.apply(a,'1'), c)
        .put(PATTERN.apply(b,'0'), a)
        .put(PATTERN.apply(b,'1'), d)
        .put(PATTERN.apply(c,'0'), e)
        .put(PATTERN.apply(c,'1'), f)
        .put(PATTERN.apply(d,'0'), e)
        .put(PATTERN.apply(d,'1'), f)
        .put(PATTERN.apply(e,'0'), e)
        .put(PATTERN.apply(e,'1'), f)
        .put(PATTERN.apply(f,'0'), f)
        .put(PATTERN.apply(f,'1'), f)
        .build();
    return map.get(PATTERN.apply(state,character));
        };
    DFA dfa = new DFA.Builder()
            .withQ(a,b,c,d,e,f)
            .withΣ('0', '1')
            .withδ(δ)
            .withQ0(a)
            .withF(c,d,e)
            .build()
            .minimize()
            ;
    dfa.prettyPrint();
  }

}
