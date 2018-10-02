package testresults;

import implementation.HashTable;

/**
 * Holds results of a Bloom filters hash function uniformity test.
 * @author Bruno
 *
 */
public class UniformityTestResult extends TestResult{
	int m;
	long numOfEmpty;
	long numOfFull;
	double percentageOfEmpty;
	double expectedZeroes;
	
	/**
	 * Default constructor. Takes number of messages added, number of empty (set to 0) and full (set to 1) fields in the bloom filters bit vector, 
	 * percentage of empty fields and expected percentage of empty fields.
	 * 
	 * @param m number of messages added
	 * @param numOfEmpty number of empty fields in the bloom filters bit vector
	 * @param numOfFull number of full fields in the bloom filters bit vector
	 * @param percentageOfEmpty percentage of empty fields 
	 * @param expecetedZeroes theoretical percentage of empty fields
	 */
	public UniformityTestResult(int m, long numOfEmpty, long numOfFull, double percentageOfEmpty, double expecetedZeroes) {
		super();
		this.m = m;
		this.numOfEmpty = numOfEmpty;
		this.numOfFull = numOfFull;
		this.percentageOfEmpty = percentageOfEmpty;
		this.expectedZeroes = expecetedZeroes;
	}
	
	
	/**
	 * Constructor that takes number of messages stored and a HashTable entity to delegate the parameters needed for a default
	 * constructor.
	 * @param m messages added
	 * @param bloomFilter HashTable entity to take and delegate the parameters from
	 */
	public UniformityTestResult(int m, HashTable bloomFilter){
		this(m, bloomFilter.getNumberOfEmpty(), bloomFilter.getNumberOfFull(), bloomFilter.getNumberOfEmpty()/(double)bloomFilter.getSize(), bloomFilter.expectedZeroes());
	}

	public int getM() {
		return m;
	}

	public long getNumOfEmpty() {
		return numOfEmpty;
	}

	public long getNumOfFull() {
		return numOfFull;
	}

	public double getPercentageOfEmpty() {
		return percentageOfEmpty;
	}
	
	public double getExpectedZeroes(){
		return expectedZeroes;
	}
	
	
	
}
