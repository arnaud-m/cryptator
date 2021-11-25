package cryptator.json;

import java.math.BigInteger;
import java.util.List;
import java.util.function.BiConsumer;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cryptator.specs.ICryptaEvaluation;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.tree.CryptaEvaluation;
import cryptator.tree.CryptaEvaluationException;

public final class SolveOutput implements BiConsumer<ICryptaNode, ICryptaSolution> {

	private String cryptarithm;

	private int base;

	private char[] letters;
	
	private List<int[]> solutions;

	private int invalidSolution;

	// TODO Remove the annotation and give test scope to jakcson.
	@JsonIgnore
	private final ICryptaEvaluation eval = new CryptaEvaluation();

	public SolveOutput() {
		super();
	}

	public SolveOutput(SolveInput input) {
		this(input.getCryptarithm(), input.getConfig().getArithmeticBase());
	}
	
	public SolveOutput(String cryptarithm, int base) {
		super();
		this.cryptarithm = cryptarithm;
		this.base = base;
	}
	
	
	public String getCryptarithm() {
		return cryptarithm;
	}

	public void setCryptarithm(String cryptarithm) {
		this.cryptarithm = cryptarithm;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public char[] getLetters() {
		return letters;
	}

	public void setLetters(char[] letters) {
		this.letters = letters;
	}

	public List<int[]> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<int[]> solutions) {
		this.solutions = solutions;
	}

	public int getInvalidSolution() {
		return invalidSolution;
	}

	public void setInvalidSolution(int invalidSolution) {
		this.invalidSolution = invalidSolution;
	}

	public ICryptaEvaluation getEval() {
		return eval;
	}

	@Override
	public void accept(ICryptaNode n, ICryptaSolution s) {
		try {
			if (eval.evaluate(n, s, base).compareTo(BigInteger.ZERO) == 0) invalidSolution++;
			else {
				// TODO Record solution
			}
		} catch (CryptaEvaluationException e) {
			invalidSolution++;
		}
	}

}