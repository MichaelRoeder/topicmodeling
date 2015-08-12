package org.aksw.simba.topicmodeling.concurrent.tasks.waiting;

/**
 * This abstract class offers simple methods to implement the needed
 * functionality of a {@link WaitingTask}. Inside the implementation the methods
 * {@link #startWaiting()} and {@link #stopWaiting()} should be used before and
 * after the task entered the section in which it has to wait.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 *
 */
public abstract class AbstractWaitingTask implements WaitingTask {

	protected static final long NOT_WAITING_SENTINEL = -1;

	/**
	 * The time stamp at which the current task started waiting.
	 */
	protected long waitingStarted = NOT_WAITING_SENTINEL;

	@Override
	public long getTimeWaiting() {
		if (waitingStarted != NOT_WAITING_SENTINEL) {
			return System.currentTimeMillis() - waitingStarted;
		} else {
			return 0;
		}
	}

	/**
	 * This method should be called before the task enters the section in which
	 * it might wait a long time.
	 */
	protected void startWaiting() {
		waitingStarted = System.currentTimeMillis();
	}

	/**
	 * This method should be called after the task has finished the section in
	 * which it might wait a long time.
	 */
	protected void stopWaiting() {
		waitingStarted = NOT_WAITING_SENTINEL;
	}
}
