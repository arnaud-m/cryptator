package cryptator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cryptator.cmd.WordArray;

public class WordArrayTest {

	private final String[] words = {"a", "bb", "ccc"};
	
	@Test
	public void testWordArray1() {
		final WordArray w = new WordArray(Arrays.asList(words), null);
		assertArrayEquals(words, w.getWords());
		assertFalse(w.hasRightMember());
		assertTrue(w.getLB() < 0);
		assertTrue(w.getUB() < 0);
	}
	
	@Test
	public void testWordArray2() {
		final WordArray w = new WordArray(Arrays.asList(words), "dddd");
		assertEquals(4, w.getWords().length);
		assertTrue(w.hasRightMember());
		assertTrue(w.getLB() < 0);
		assertTrue(w.getUB() < 0);
	}
	
	@Test
	public void testWordArray3() {
		final WordArray w = new WordArray("FR", "fr", 1, 3);
		assertArrayEquals(new String[] {"zero", "un", "deux", "trois"}, w.getWords());
		assertFalse(w.hasRightMember());
		assertEquals(1, w.getLB());
		assertEquals(3, w.getUB());
	}
}
