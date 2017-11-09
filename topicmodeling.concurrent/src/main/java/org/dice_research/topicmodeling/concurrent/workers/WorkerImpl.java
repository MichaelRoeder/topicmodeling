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
package org.dice_research.topicmodeling.concurrent.workers;

import java.lang.Thread.State;

import org.dice_research.topicmodeling.concurrent.overseers.Overseer;
import org.dice_research.topicmodeling.concurrent.tasks.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerImpl implements Worker {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkerImpl.class);

	protected Task task;
	protected Overseer overseer;
	protected Thread thread = null;
	protected boolean interrupted = false;

	public WorkerImpl(Task task, Overseer overseer) {
		this.task = task;
		this.overseer = overseer;
	}

	public void run() {
		try {
			thread = Thread.currentThread();
			if (!interrupted) {
				task.run();
			}
			overseer.reportTaskFinished(this);
		} catch (Throwable e) {
			LOGGER.error("Got a throwable from task {}.", task.getId());
			overseer.reportTaskThrowedException(this, e);
		}
	}

	public Task getTask() {
		return task;
	}

	public State getState() {
		if (thread == null) {
			return State.NEW;
		} else {
			return thread.getState();
		}
	}

	public StackTraceElement[] getStackTrace() {
		if (thread == null) {
			return null;
		} else {
			return thread.getStackTrace();
		}
	}

	@Override
	public void interrupt() {
		interrupted = true;
		if (thread != null) {
			thread.interrupt();
		}
	}

}
