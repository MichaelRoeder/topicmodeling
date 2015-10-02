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
package org.aksw.simba.topicmodeling.concurrent.reporter;

import org.aksw.simba.topicmodeling.concurrent.overseers.Overseer;
import org.aksw.simba.topicmodeling.concurrent.tasks.Task;
import org.aksw.simba.topicmodeling.concurrent.tasks.TaskState;
import org.aksw.simba.topicmodeling.concurrent.tasks.waiting.WaitingTask;
import org.aksw.simba.topicmodeling.concurrent.workers.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitingThreadInterrupter extends AbstractReporter implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(WaitingThreadInterrupter.class);

	public static final long DEFAULT_TIME_INTERVAL = 1000;
	
	private static final String THREAD_NAME = "Interrupter"; 

	private long timeInterval;
	private long maximumWaitingTime;

	public WaitingThreadInterrupter(Overseer overseer, long maximumWaitingTime, long timeInterval) {
		super(overseer);
		this.maximumWaitingTime = maximumWaitingTime;
		this.timeInterval = timeInterval;

		Thread t = new Thread(this);
		t.setDaemon(true);
		t.setName(THREAD_NAME);
		t.start();
	}

	public WaitingThreadInterrupter(Overseer overseer, long maximumWaitingTime) {
		this(overseer, maximumWaitingTime, DEFAULT_TIME_INTERVAL);
	}

	@Override
	public void reportTaskState(TaskState state) {
		Task task = state.task;
		if (task instanceof WaitingTask) {
			long time = ((WaitingTask) task).getTimeWaiting();
			if (time > maximumWaitingTime) {
				Worker worker = super.getOverseer().getWorker(task);
				if (worker != null) {
					LOGGER.warn("Task is waiting for " + time + "ms. Interrupting it.");
					worker.interrupt();
				}
			}
		}
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(timeInterval);
			} catch (InterruptedException e) {
				LOGGER.warn("Interrupted while sleeping.", e);
			}
			reportCurrentState();
		}
	}

	/**
	 * @return the maximumWaitingTime
	 */
	public long getMaximumWaitingTime() {
		return maximumWaitingTime;
	}

	/**
	 * @param maximumWaitingTime
	 *            the maximumWaitingTime to set
	 */
	public void setMaximumWaitingTime(long maximumWaitingTime) {
		this.maximumWaitingTime = maximumWaitingTime;
	}

}
