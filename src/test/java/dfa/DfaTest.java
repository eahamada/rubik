package dfa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
public class DfaTest {

	@Test
	public void test() {
		State s1 = new State.Builder().withId("s1").build();
		State s2 = new State.Builder().withId("s2").build();
		
		Map<StateCharacter, State> delta = ImmutableMap.of(
											new StateCharacter(s1, '0'), s2,
//											new StateCharacter(s1, '1'), s1,
											new StateCharacter(s2, '0'), s1,
											new StateCharacter(s2, '1'), s2											
											);
		DFA d = new DFA.Builder()
						.withQ(s1,s2)
						.withSIGMA('0', '1')
						.withDelta(delta)
						.withQ0(s1)
						.withF(s1)
						.build();
		assertTrue(d.accept("010"));
		assertFalse(d.accept("0101"));
		d.forceAccept("0101");
	}

}
