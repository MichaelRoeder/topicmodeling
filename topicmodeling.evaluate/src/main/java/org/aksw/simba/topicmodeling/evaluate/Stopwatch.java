package org.aksw.simba.topicmodeling.evaluate;

public class Stopwatch {

	protected long time = 0;
	protected long startedTime = 0;

	public Stopwatch() {
	};

	/**
	 */
	public void start() {
		setStartedTime(System.currentTimeMillis());
	}

	/**
	 */
	public void stop() {
		time += System.currentTimeMillis() - startedTime;
	}

	/**
	 */
	public void reset() {
		stop();
		time = 0;
	}

	/**
	 * Set the value of time
	 * 
	 * @param newVar
	 *            the new value of time
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * Get the value of time
	 * 
	 * @return the value of time
	 */
	public long getTime() {
		return time;
	}

	public long getStartedTime() {
		return startedTime;
	}

	public void setStartedTime(long startedTime) {
		this.startedTime = startedTime;
	}
}
