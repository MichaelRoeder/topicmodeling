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
package org.dice_research.topicmodeling.concurrent.join;

import java.util.concurrent.Semaphore;

import org.dice_research.topicmodeling.concurrent.overseers.Overseer;
import org.dice_research.topicmodeling.concurrent.tasks.Task;
import org.dice_research.topicmodeling.concurrent.tasks.TaskObserver;
import org.dice_research.topicmodeling.concurrent.tasks.TaskState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleTaskJoin {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTaskJoin.class);

	public void joinWithTasks(Overseer overseer) throws InterruptedException {
		// Add the observer
		JoiningObserver observer = new JoiningObserver(overseer);
		overseer.addObserver(observer);
		try {
			// let the observer wait for the tasks to finish
			observer.waitForTasksToFinish();
		} finally {
			// remove the observer
			overseer.removeObserver(observer);
		}
	}

	protected static class JoiningObserver implements TaskObserver {

		private Overseer overseer;
		private Semaphore taskCountMutex = new Semaphore(1);
		private Semaphore finishedTasksCount = null;

		public JoiningObserver(Overseer overseer) {
			this.overseer = overseer;
		}

		@Override
		public void reportTaskFinished(Task task) {
			try {
				taskCountMutex.acquire();
			} catch (InterruptedException e) {
				LOGGER.warn("Interrupted while waiting for taskCountMutex. Returning.");
				return;
			}
			if (finishedTasksCount != null) {
				finishedTasksCount.release();
			}
			taskCountMutex.release();
		}

		@Override
		public void reportTaskThrowedException(Task task, Throwable t) {
			reportTaskFinished(task);
		}

		public void waitForTasksToFinish() throws InterruptedException {
			// Count active tasks and create a semaphore to count the finished
			// tasks
			taskCountMutex.acquire();
			int activeTasks = countActiveTasks();
			finishedTasksCount = new Semaphore(0);
			while (activeTasks > 0) {
				// release the mutex for enabling the releasing to the new
				// created semaphore inside the two finish methods
				taskCountMutex.release();

				// wait for all tasks to finish
				finishedTasksCount.acquire(activeTasks);

				taskCountMutex.acquire();
				// count again (maybe new tasks have been started)
				activeTasks = countActiveTasks();
			}
			// destroy the semaphore since we do not need it anymore
			finishedTasksCount = null;
			taskCountMutex.release();
		}

		protected int countActiveTasks() {
			TaskState states[] = overseer.getTaskStates();
			int count = 0;
			for (int i = 0; i < states.length; ++i) {
				switch (states[i].state) {
				case NEW: // falls through
				case RUNNABLE:
				case BLOCKED:
				case WAITING:
				case TIMED_WAITING: {
					++count;
					break;
				}
				case TERMINATED: {
					// nothing to do
				}
				}
			}
			return count;
		}
	}
}
