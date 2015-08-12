/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.aksw.simba.topicmodeling.concurrent.reporter;

import java.util.concurrent.Semaphore;

import org.aksw.simba.topicmodeling.concurrent.trigger.RegularTriggerer;
import org.aksw.simba.topicmodeling.concurrent.trigger.Triggerable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple thread that regularly triggers the reporting of the current state of
 * the given {@link Reporter} instances after the given time interval. After
 * creation the thread has to be started. It might be stopped and restarted.
 * Note, that the used thread is a deamon thread.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 *
 */
public class RegularReportTriggerer implements Triggerable {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegularReportTriggerer.class);

	/**
	 * Time interval at which the next reporting is triggered.
	 */
	private long timeInterval;

	/**
	 * Mutex to manage the access to the {@link #triggerer} instance.
	 */
	private Semaphore triggererMutex = new Semaphore(1);

	/**
	 * {@link RegularTriggerer} that is used to trigger the reporting in
	 * parallel to the running program.
	 */
	private RegularTriggerer triggerer = null;

	/**
	 * Array of reporters that are triggered.
	 */
	private Reporter reporters[];

	/**
	 * Constructor.
	 * 
	 * @param timeInterval
	 *            Time interval at which the next reporting is triggered.
	 * @param reporters
	 *            Array of reporters that are triggered.
	 */
	public RegularReportTriggerer(long timeInterval, Reporter... reporters) {
		this.timeInterval = timeInterval;
		this.reporters = reporters;
	}

	/**
	 * Starts the internal deamon thread if it is not already existing.
	 */
	public void start() {
		try {
			triggererMutex.acquire();
		} catch (InterruptedException e) {
			LOGGER.error("Interrupted while waiting for mutex. Returning.", e);
			return;
		}
		if (triggerer == null) {
			triggerer = new RegularTriggerer(this, timeInterval);
			Thread thread = new Thread(triggerer);
			thread.setDaemon(true);
			thread.setName("ReportTriggerThread");
			thread.start();
		} else {
			LOGGER.warn("RegularReporter is already running.");
		}
		triggererMutex.release();
	}

	/**
	 * Stops the triggering thread if it is existing.
	 */
	public void stop() {
		try {
			triggererMutex.acquire();
		} catch (InterruptedException e) {
			LOGGER.error("Interrupted while waiting for mutex. Returning.", e);
			return;
		}
		if (triggerer != null) {
			triggerer.stop();
			triggerer = null;
		}
		triggererMutex.release();
	}

	@Override
	public void trigger() {
		for (int i = 0; i < reporters.length; ++i) {
			reporters[i].reportCurrentState();
		}
	}
}
