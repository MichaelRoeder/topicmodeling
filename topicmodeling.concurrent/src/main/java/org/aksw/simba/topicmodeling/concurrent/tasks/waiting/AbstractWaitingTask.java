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
