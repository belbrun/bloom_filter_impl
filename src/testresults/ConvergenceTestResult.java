package testresults;

/**
 * Holds results of a convergence test, number of tests done and average false positive percentage.
 * 
 * @author Bruno
 *
 */
public class ConvergenceTestResult extends TestResult{
	
	private int numOfTestsDone;
	private double avarageFPPercentage;
	
	/**
	 * Default constructor. Takes number of tests done and average false positive percentage.
	 * @param numOfTestsDone index of this test in the testing sequence
	 * @param avarageFPPercentage average false positive percentage for this testing sequence so far
	 */
	public ConvergenceTestResult(int numOfTestsDone, double avarageFPPercentage) {
		super();
		this.numOfTestsDone = numOfTestsDone;
		this.avarageFPPercentage = avarageFPPercentage;
	}
	
	


	public int getNumOfTestsDone() {
		return numOfTestsDone;
	}


	public double getAvarageFPPercentage() {
		return avarageFPPercentage;
	}
	
	
	
}
