package cryptator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cryptator.solver.CryptaSolutionException;
import cryptator.solver.CryptaSolutionMap;
import cryptator.specs.ICryptaSolution;

public class EvaluationTest {

	public EvaluationTest() {}
	
	@Test
	public void testSolutionParser1() throws CryptaSolutionException {
		final String solution = "A=  1 B    2 C   =3 D=4 E  =  5";
		final ICryptaSolution s = CryptaSolutionMap.parseSolution(solution);
		assertEquals(5, s.size());
		assertEquals(1, s.getDigit('A'));
		assertEquals(2, s.getDigit('B'));
		assertEquals(3, s.getDigit('C'));
		assertEquals(4, s.getDigit('D'));
		assertEquals(5, s.getDigit('E'));		
	}
	
	@Test(expected = CryptaSolutionException.class)
	public void testInvalidSolutionParser1() throws CryptaSolutionException {
		CryptaSolutionMap.parseSolution("AB 1");		
	}
	
	@Test(expected = CryptaSolutionException.class)
	public void testInvalidSolutionParser2() throws CryptaSolutionException {
		CryptaSolutionMap.parseSolution("A B");		
	}
	
	@Test(expected = CryptaSolutionException.class)
	public void testInvalidSolutionParser3() throws CryptaSolutionException {
		CryptaSolutionMap.parseSolution("A 1 B");		
	}

}
