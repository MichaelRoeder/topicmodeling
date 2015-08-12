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
public class RegularReportTriggerer implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegularReportTriggerer.class);

	/**
	 * Time interval at which the next reporting is triggered.
	 */
	private long timeInterval;

	/**
	 * Simple flag used to stop the thread after the {@link #stop()} method is
	 * called.
	 */
	private boolean isAllowedToRun = false;

	/**
	 * Mutex to manage the access to the {@link #isAllowedToRun} flag.
	 */
	private Semaphore flagMutex = new Semaphore(1);

	/**
	 * Thread that is used to trigger the reporting in parallel to the running
	 * program.
	 */
	private Thread thread = null;

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
		if (thread == null) {
			try {
				flagMutex.acquire();
			} catch (InterruptedException e) {
				LOGGER.error("Interrupted while waiting for mutex. Returning.", e);
				return;
			}
			isAllowedToRun = true;
			thread = new Thread(this);
			thread.setDaemon(true);
			thread.setName("ReportTriggerThread");
			thread.start();
			flagMutex.release();
		} else {
			LOGGER.warn("RegularReporter is already running.");
		}
	}

	/**
	 * Run method that implements the regular triggering.
	 */
	public void run() {
		try {
			flagMutex.acquire();
		} catch (InterruptedException e) {
			LOGGER.error("Interrupted while waiting for mutex. Returning.", e);
			return;
		}
		while (isAllowedToRun) {
			flagMutex.release();

			try {
				Thread.sleep(timeInterval);
			} catch (InterruptedException e) {
				LOGGER.warn("Interrupted while sleeping.", e);
			}

			reportCurrentState();

			try {
				flagMutex.acquire();
			} catch (InterruptedException e) {
				LOGGER.error("Interrupted while waiting for mutex. Returning.", e);
				return;
			}
		}
		flagMutex.release();
	}

	private void reportCurrentState() {
		for (int i = 0; i < reporters.length; ++i) {
			reporters[i].reportCurrentState();
		}
	}

	/**
	 * Stops the triggering thread if it is existing.
	 */
	public void stop() {
		if (thread != null) {
			try {
				flagMutex.acquire();
			} catch (InterruptedException e) {
				LOGGER.error("Interrupted while waiting for mutex. Returning.", e);
				return;
			}
			isAllowedToRun = false;
			thread = null;
			flagMutex.release();
		}
	}
}
