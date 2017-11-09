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
package org.dice_research.topicmodeling.concurrent.overseers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.dice_research.topicmodeling.concurrent.tasks.Task;
import org.dice_research.topicmodeling.concurrent.tasks.TaskHelper;
import org.dice_research.topicmodeling.concurrent.tasks.TaskObserver;
import org.dice_research.topicmodeling.concurrent.tasks.TaskState;
import org.dice_research.topicmodeling.concurrent.workers.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractOverseer implements Overseer {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOverseer.class);

	protected Set<Worker> currentWorkers = new HashSet<Worker>();
	protected Map<Task, Worker> taskWorkerMapping = new HashMap<Task, Worker>();
	protected Semaphore workersSetMutex = new Semaphore(1);
	protected List<TaskObserver> observers = new ArrayList<TaskObserver>();

	public void startTask(Task task) {
		Worker worker = createWorker(task);
		try {
			workersSetMutex.acquire();
		} catch (InterruptedException e) {
			LOGGER.error("Interrupted while wating for the worker set mutex. Aborting.", e);
			return;
		}
		currentWorkers.add(worker);
		taskWorkerMapping.put(task, worker);
		workersSetMutex.release();
		startWorker(worker);
	}

	protected abstract Worker createWorker(Task task);

	protected abstract void startWorker(Worker worker);

	public void reportTaskFinished(Worker worker) {
		try {
			workersSetMutex.acquire();
		} catch (InterruptedException e) {
			LOGGER.error("Interrupted while wating for the worker set mutex", e);
		}
		currentWorkers.remove(worker);
		taskWorkerMapping.remove(worker.getTask());
		workersSetMutex.release();

		for (TaskObserver observer : observers) {
			observer.reportTaskFinished(worker.getTask());
		}
	}

	public void reportTaskThrowedException(Worker worker, Throwable t) {
		try {
			workersSetMutex.acquire();
		} catch (InterruptedException ie) {
			LOGGER.error("Interrupted while wating for the worker set mutex", ie);
		}
		currentWorkers.remove(worker);
		taskWorkerMapping.remove(worker.getTask());
		workersSetMutex.release();

		for (TaskObserver observer : observers) {
			observer.reportTaskThrowedException(worker.getTask(), t);
		}
	}

	public void addObserver(TaskObserver observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(TaskObserver observer) {
		observers.remove(observer);
	}

	public TaskState getTaskState(Task task) {
		try {
			workersSetMutex.acquire();
		} catch (InterruptedException e) {
			LOGGER.error("Interrupted while wating for the worker set mutex. Returning null.", e);
			return null;
		}
		try {
			if (taskWorkerMapping.containsKey(task)) {
				LOGGER.warn("The given task {} is not known. Returning null.", TaskHelper.taskToString(task));
				return null;
			}
			Worker worker = taskWorkerMapping.get(task);
			return new TaskState(task, worker.getState(), worker.getStackTrace());
		} catch (Exception e) {
			LOGGER.error("Got an exception while requesting state of task " + TaskHelper.taskToString(task)
					+ ". Returning null.", e);
			return null;
		} finally {
			workersSetMutex.release();
		}
	}

	public TaskState[] getTaskStates() {
		try {
			workersSetMutex.acquire();
		} catch (InterruptedException e) {
			LOGGER.error("Interrupted while wating for the worker set mutex. Returning null.", e);
			return null;
		}
		try {
			TaskState states[] = new TaskState[currentWorkers.size()];
			int pos = 0;
			for (Worker worker : currentWorkers) {
				states[pos] = new TaskState(worker.getTask(), worker.getState(), worker.getStackTrace());
				++pos;
			}
			return states;
		} catch (Exception e) {
			LOGGER.error("Got an exception while requesting state of tasks. Returning null.", e);
			return null;
		} finally {
			workersSetMutex.release();
		}
	}

	@Override
	public Worker getWorker(Task task) {
		Worker worker = null;
		try {
			workersSetMutex.acquire();
		} catch (InterruptedException e) {
			LOGGER.error("Interrupted while wating for the worker set mutex", e);
		}
		if (taskWorkerMapping.containsKey(task)) {
			worker = taskWorkerMapping.get(task);
		}
		workersSetMutex.release();
		return worker;
	}
}
