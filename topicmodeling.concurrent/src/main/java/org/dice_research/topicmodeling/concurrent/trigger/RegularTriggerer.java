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
package org.dice_research.topicmodeling.concurrent.trigger;

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
