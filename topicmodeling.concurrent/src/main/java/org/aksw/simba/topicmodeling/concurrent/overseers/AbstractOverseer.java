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
package org.aksw.simba.topicmodeling.concurrent.overseers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.aksw.simba.topicmodeling.concurrent.tasks.Task;
import org.aksw.simba.topicmodeling.concurrent.tasks.TaskHelper;
import org.aksw.simba.topicmodeling.concurrent.tasks.TaskObserver;
import org.aksw.simba.topicmodeling.concurrent.tasks.TaskState;
import org.aksw.simba.topicmodeling.concurrent.workers.Worker;
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
