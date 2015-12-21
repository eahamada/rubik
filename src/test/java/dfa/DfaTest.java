package dfa;

import static dfa.DFA.PATTERN;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.text.MessageFormat;
import java.util.Map;
import java.util.function.BiFunction;

import org.junit.Ignore;
import org.junit.Test;




import com.google.common.collect.ImmutableMap;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;

public class DfaTest {

  @Test
	public void testShouldAcceptDivisibleByThree() {
	  State q0 = State.valueOf("q0");
		State q1 = State.valueOf("q1");
		State q2 = State.valueOf("q2");

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
		DFA dfa = new DFA.Builder()
						.withQ(q0, q1, q2)
						.withΣ('0', '1', '2','3','4','5','6','7','8','9')
						.withδ(δ)
						.withQ0(q0)
						.withF(q0)
						.build()
						.minimize();
		assertTrue(dfa.accept("012345678912345678912345678912345679801234567891234567891234567891234567980123456789123456789123456789123456798012345678912345678912345678912345679801234567891234567891234567891234567980123456789123456789123456789123456798012345678912345678912345678912345679801234567891234567891234567891234567980123456789123456789123456789123456798"));
		assertFalse(dfa.accept("012345678912345678912345678912345679012345678912345678912345678912345679801234567891234567891234567891234567980123456789123456789123456789123456798012345678912345678912345678912345679801234567891234567891234567891234567980123456789123456789123456789123456798012345678912345678912345678912345679801234567891234567891234567891234567980123456789123456789123456789123456798"));
	}
  @Test
  public void testMinimize() {
    State a = State.valueOf("a");
    State b = State.valueOf("b");
    State c = State.valueOf("c");
    State d = State.valueOf("d");
    State e = State.valueOf("e");
    State f = State.valueOf("f");
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
    assertTrue(dfa.Q.size() == 3);
    assertTrue(dfa.Σ.size() == 2);
    assertTrue(dfa.F.size() == 1);
  }
  @Test
  public void testRemoveUnreachableStates() {
    State s0 = State.valueOf("0");
    State s1 = State.valueOf("1");
    State s2 = State.valueOf("2");
    

    BiFunction<State, Character, State> δ =
        (state, character) -> {
        ImmutableMap.Builder<String, State> builder = ImmutableMap.builder();
        Map<String, State> map = builder
        .put(PATTERN.apply(s0,'0'), s1)
        .put(PATTERN.apply(s0,'1'), s0)
        .put(PATTERN.apply(s1,'0'), s1)
        .put(PATTERN.apply(s1,'1'), s0)
        .put(PATTERN.apply(s2,'0'), s1)
        .put(PATTERN.apply(s2,'1'), s0)
        .build();
    return map.get(PATTERN.apply(state,character));
        };
    DFA dfa = new DFA.Builder()
            .withQ(s0,s1,s2)
            .withΣ('0', '1')
            .withδ(δ)
            .withQ0(s0)
            .withF(s1)
            .build()
            .removeUnreachableStates()
            ;
    assertTrue(dfa.Q.size() == 2);
  }
  @Test
  public void testForceAcceptChar() {
    State s0 = State.valueOf("0");
    BiFunction<State, Character, State> δ =
        (state, character) -> null;
    DFA dfa = new DFA.Builder()
            .withQ(s0)
            .withΣ()
            .withδ(δ)
            .withQ0(s0)
            .withF()
            .build()
            .removeUnreachableStates()
            .normalize()
            ;
    assertFalse(dfa.accept("A"));
    assertFalse(dfa.accept("B"));
    DFA forceAccept = dfa.forceAccept(dfa.q0,'A');
    assertTrue(forceAccept.accept("A"));
    assertFalse(forceAccept.accept("B"));
  }
  @Test
  public void testForceAcceptString() {
    State s0 = State.valueOf("q0");
    BiFunction<State, Character, State> δ =
        (state, character) -> null;
    DFA dfa = new DFA.Builder()
            .withQ(s0)
            .withΣ()
            .withδ(δ)
            .withQ0(s0)
            .withF()
            .build()
            .removeUnreachableStates()
            .normalize()
            ;
    DFA dfaA = dfa.forceAccept("ABCD").forceAccept("DABCD").minimize().normalize();
    assertTrue(dfaA.accept("ABCD"));
  }
  @Test
  public void testAutomaton() {
    Automaton makeString = BasicAutomata.makeString("ABCD");
    Automaton min = makeString.union(BasicAutomata.makeString("DABCD"));
    assertTrue(min.run("DABCD"));
    assertTrue(min.run("ABCD"));
    min.minimize();
    System.out.println(min.getNumberOfStates());
  }
}
