/**
 * This file is part of topicmodeling.concurrent.
 *
 * topicmodeling.concurrent is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.concurrent is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.concurrent.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.concurrent.reporter;

import java.util.concurrent.Semaphore;

import org.dice_research.topicmodeling.concurrent.trigger.RegularTriggerer;
import org.dice_research.topicmodeling.concurrent.trigger.Triggerable;
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
