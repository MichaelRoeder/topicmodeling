package org.aksw.simba.topicmodeling.concurrent.trigger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegularTriggerer implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegularTriggerer.class);

	private Triggerable triggerable;
	private long timeInterval;
	private boolean run = false;

	public RegularTriggerer(Triggerable triggerable, long timeInterval) {
		this.timeInterval = timeInterval;
		this.triggerable = triggerable;
	}

	/**
	 * Run method that implements the regular triggering.
	 */
	public void run() {
		run = true;
		while (run) {
			try {
				Thread.sleep(timeInterval);
			} catch (InterruptedException e) {
				LOGGER.warn("Interrupted while sleeping.", e);
			}

			triggerable.trigger();
		}
	}

	public void stop() {
		run = false;
	}

	public boolean isStopped() {
		return !run;
	}
}
