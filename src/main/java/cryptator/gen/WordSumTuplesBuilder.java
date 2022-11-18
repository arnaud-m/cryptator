package cryptator.gen;

import java.math.BigInteger;

import org.chocosolver.solver.constraints.extension.Tuples;

/**
 * The Class WordSumTuplesBuilder is a factory that builds tuples.
 * 
 * Let S be a sum of numbers written in base b.
 * 
 * The tuples have the following semantics :
 * <ul>
 * <li>k:  </li>
 * <li> k: the number of digits of the largest number</li>
 * <li> x: the cardinality of numbers with k digits</li>
 * <li> y: the cardinality of numbers with k-1 digits</li>
 * <li> z: the cardinality of numbers with strictly less than k - 1</li>
 * <li> p: the number of digits of the sum S</li>
 * </ul>
 * 
 */
public final class WordSumTuplesBuilder {

	/** The base. */
	public BigInteger base = BigInteger.TEN;

	/**
	 * Instantiates a new word sum tuples builder in base 10.
	 *
	 */
	public WordSumTuplesBuilder() {
		super();
	}
	
	/**
	 * Instantiates a new word sum tuples builder.
	 *
	 * @param base the base 
	 */
	public WordSumTuplesBuilder(int base) {
		super();
		setBase(base);
	}
	
	public int getBase() {
		return base.intValue();
	}
	
	public void setBase(int base) {
		if(base > 1) this.base = BigInteger.valueOf(base);
		else throw new IllegalArgumentException("The base must be greater than 2.");
	}
	/**
	 * Computes the floor of the logarithm of x in base b.
	 *
	 * @param x the value for the logarithm
	 * @param b the base for the logarithm
	 * @return the floor of the logarithm in base b.
	 */
	public static final int logFloor(int x, int b) {
		return logFloor(BigInteger.valueOf(x), BigInteger.valueOf(b));
	}

	/**
	 * Computes the floor of the logarithm of x in base b.
	 *
	 * @param x the value for the logarithm
	 * @param b the base for the logarithm
	 * @return the floor of the logarithm in base b.
	 */
	public static final int logFloor(BigInteger x, int b) {
		return logFloor(x, BigInteger.valueOf(b));
	}

	/**
	 * Computes the floor of the logarithm of x in base b.
	 *
	 * @param x the value for the logarithm
	 * @param b the base for the logarithm
	 * @return the floor of the logarithm in base b.
	 */
	public static final int logFloor(BigInteger x, BigInteger b) {
		BigInteger m = b;
		int v = 0;
		while( x.compareTo(m) >= 0) {
			m = m.multiply(b);
			v++;
		}
		return v;
	}

	/**
	 * Computes the exponentiation of x in base b : x * b^k.
	 *
	 * @param x the coefficient 
	 * @param b the base
	 * @param k the positive exponent
	 * @return the exponentiation of x.
	 */
	public static final BigInteger exp(BigInteger x, BigInteger b, int k) {
		return b.pow(k).multiply(x);
	}

	/**
	 * Computes the exponentiation of x in base b : x * b^k.
	 * It fails if the exponent is negative unless x is equal to 0.
	 *
	 * @param x the coefficient 
	 * @param k the exponent
	 * @return the exponentiation of x.
	 */
	private BigInteger exp(int x, int k) {
		return x == 0 ? BigInteger.ZERO : exp(BigInteger.valueOf(x), base, k);
	}

	/**
	 * Computes the minimal number of digits the sum. 
	 *
	 * @param k the number of digits of the largest number
	 * @param x the cardinality of numbers with k digits
	 * @param y the cardinality of numbers with k-1 digits
	 * @param z the cardinality of numbers with strictly less than k - 1
	 * @return the minimal number of digits of the sum
	 */
	public int getMinLen(int k, int x, int y, int z) {
		final BigInteger v = exp(x, k-1).add(exp(y, k-2)).add(BigInteger.valueOf(z));
		// System.out.println(v + " " +  Integer.toBinaryString(v.intValue()));
		return logFloor(v, base) + 1;	
	}

	/**
	 * Computes the minimal number of digits the sum. 
	 *
	 * @param k the number of digits of the largest number
	 * @param x the cardinality of numbers with k digits
	 * @param y the cardinality of numbers with k-1 digits
	 * @param z the cardinality of numbers with strictly less than k - 1
	 * @return the minimal number of digits of the sum
	 */
	public int getMaxLen(int k, int x, int y, int z) {
		final BigInteger xyz = BigInteger.valueOf(x + y + z);
		final BigInteger v = exp(x, k).add(exp(y, k-1)).add(exp(z, k-2)).subtract(xyz);
		// System.out.println(v + " " +  Integer.toBinaryString(v.intValue()));
		return logFloor(v, base) + 1;	
	}


	/**
	 * Adds new tuples.
	 *
	 * @param tuples the tuples to extend
	 * @param k the number of digits of the largest number
	 * @param x the cardinality of numbers with k digits
	 * @param y the cardinality of numbers with k-1 digits
	 * @param z the cardinality of numbers with strictly less than k - 1
	 */
	private void addTuples(Tuples tuples, int k, int x, int y, int z) {
		final int min = getMinLen(k, x, y, z);
		final int max = getMaxLen(k, x, y, z);
		System.out.println(k+ " " +  x+ " " +  y+ " " + z + " " +  min+ "-" +  max);
		for (int p = min; p <= max; p++) {
			tuples.add(k, x, y, z, p);
		}
	}

	/**
	 * Computes the maximum cardinality of numbers with strictly less than k-1 digits.
	 *
	 * @param values the cardinalities of numbers, i.e. the i-th value is the cardinality of numbers with i digits.
	 * @param k the number of digits of the largest number
	 * @return the maximum cardinality
	 */
	public static int getMaxZ(int[] values, int k) {
		int maxz = 0;
		for (int i = 0; i < k - 1; i++) {
			maxz += values[i];
		}
		//System.out.println(maxz);
		return maxz;
	}

	/**
	 * Add new tuples.
	 *
	 * @param tuples the tuples to extends
	 * @param values the cardinalities of numbers, i.e. the i-th value is the cardinality of numbers with i digits.
	 * @param k the number of digits of the largest number
	 */
	private void addTuples(Tuples tuples, int[] values, int k) {
		final int maxz = getMaxZ(values, k);
		for (int x = 1; x <= values[k]; x++) {
			for (int y = 0; y <= values[k-1]; y++) {
				for (int z = 0; z <= maxz; z++) {
					addTuples(tuples, k, x, y, z);
				}
			}
		}
	}

	/**
	 * Builds the tuples.
	 *
	 * @param values the cardinalities of numbers, i.e. the i-th value is the cardinality of numbers with i digits.
	 * @return the tuples <k, x, y, z, l>
	 */
	public Tuples buildTuples(int[] values) {
		final Tuples tuples = new Tuples();
		for (int k = 1; k < values.length; k++) {
			addTuples(tuples, values, k);
		}
		return tuples;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		int[] v = {0, 9, 3, 5, 1, 4, 2};

		WordSumTuplesBuilder builder = new WordSumTuplesBuilder(2);
		Tuples tuples = builder.buildTuples(v);
		System.out.println(tuples);
	}

}
