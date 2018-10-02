package testresults;

/**
 * Holds results of a Bloom filter size test.
 * 
 * @author Bruno
 *
 */
public class SizeTestResult extends TestResult{
	
	private int size;
	private int hashesPerMessage;
	private double theroeticalFPPercentage;
	private double testedFPPercentage;
	
	/**
	 * Default constructor. Takes size, number of hashes per message, theoretical and tested false positive percentage. 
	 * 
	 * @param size size of the Bloom filter
	 * @param hashesPerMessage number of hashes a the Bloom filter will make for saving/searching of a single message
	 * @param theroeticalFPPercentage theoretical false positive percentage
	 * @param testedFPPercentage tested false positive percentage
	 */
	public SizeTestResult(int size, int hashesPerMessage, double theroeticalFPPercentage, double testedFPPercentage) {
		super();
		this.size = size;
		this.hashesPerMessage = hashesPerMessage;
		this.theroeticalFPPercentage = theroeticalFPPercentage;
		this.testedFPPercentage = testedFPPercentage;
	}

	public int getSize() {
		return size;
	}

	public int getHashesPerMessage() {
		return hashesPerMessage;
	}

	public double getTheroeticalFPPercentage() {
		return theroeticalFPPercentage;
	}

	public double getTestedFPPercentage() {
		return testedFPPercentage;
	}
	
	
	
}
