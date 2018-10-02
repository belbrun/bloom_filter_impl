package testresults;

/**
 * Holds speed test results.
 * @author Bruno
 *
 */
public class SpeedTestResult extends TestResult{
	
	private int messagesAdded;
	private long timeElapsed;
	
	/**
	 * Default constructor. Takes number of messages added and the time needed to do the adding.
	 * 
	 * @param messagesAdded number of messages added
	 * @param timeElapsed time used for processing messages
	 */
	public SpeedTestResult(int messagesAdded, long timeElapsed) {
		super();
		this.messagesAdded = messagesAdded;
		this.timeElapsed = timeElapsed;
	}

	public int getMessagesAdded() {
		return messagesAdded;
	}

	public long getTimeElapsed() {
		return timeElapsed;
	}
	
	
	
}
