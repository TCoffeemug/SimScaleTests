package uiTestingAssignment.util;

import java.util.concurrent.TimeUnit;

/**
 * Class to help looping and waiting for certain events. User creates a
 * LoopHelper instance and implements the getCurrentState() method of the ICheck
 * interface.
 *
 * @param <T>
 */
public class LoopHelper<T> {

	/**
	 * Interface to be implemented by user of the loop
	 *
	 * @param <T>
	 */
	public interface ICheck<T> {

		/**
		 * Method to be implemented, which will be called periodically by
		 * LoopHelper
		 *
		 * @return true, if check successful, false otherwise. Must return true
		 *         to stop the loop.
		 */
		public T getCurrentState();
	}

	/**
	 * Exception thrown in case of timeout
	 *
	 */
	public static class LoopTimeoutException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public LoopTimeoutException(String message) {
			super(message);
		}

	}

	private static final int ITERATION_DELAY_IN_SECONDS = 1;

	private ICheck<?> mCheck;
	private int mTimeoutInSeconds;
	private T mExpectedState;

	public LoopHelper(int timeout, T expectedState, ICheck<?> check) {
		mCheck = check;
		mTimeoutInSeconds = timeout;
		mExpectedState = expectedState;
	}

	public void run() {
		final long timeStarted = System.currentTimeMillis();
		T currentState = null;
		while ((System.currentTimeMillis() - timeStarted) < (TimeUnit.SECONDS.toMillis(mTimeoutInSeconds))) {
			try {
				TimeUnit.SECONDS.sleep(ITERATION_DELAY_IN_SECONDS);
			} catch (InterruptedException e) {
				System.err.println("LoopHelper delay sleep was interrupted");
			}
			currentState = (T) mCheck.getCurrentState();
			if (mExpectedState.equals(currentState)) {
				return;
			}
		}
		String errorMessage = "Was waiting for " + mExpectedState + " for "
				+ ((System.currentTimeMillis() - timeStarted) / 1000) + " seconds. Result was " + currentState;
		throw new LoopTimeoutException(errorMessage);
	}

}
