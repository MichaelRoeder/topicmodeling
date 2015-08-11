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

import org.aksw.simba.topicmodeling.concurrent.tasks.Task;
import org.aksw.simba.topicmodeling.concurrent.tasks.TaskObserver;
import org.aksw.simba.topicmodeling.concurrent.tasks.TaskState;
import org.aksw.simba.topicmodeling.concurrent.workers.Worker;
import org.aksw.simba.topicmodeling.concurrent.workers.WorkerObserver;

/**
 * The {@link Overseer} manages the assignment of {@link Task}s to
 * {@link Worker}s.
 * 
 * @author Michael Röder (roeder@informatik.uni-leipzig.de)
 * 
 */
public interface Overseer extends WorkerObserver {

	/**
	 * Starts the given {@link Task}.
	 * 
	 * @param task
	 *            the {@link Task} that should be executed
	 */
	public void startTask(Task task);

	/**
	 * Returns the state of the given {@link Task}.
	 * 
	 * @param task
	 *            the {@link Task} of which the state should be returned
	 * @return the state of the given {@link Task}
	 */
	public TaskState getTaskState(Task task);

	/**
	 * Returns the state of all {@link Task}s managed by this Overseer.
	 * 
	 * @return the state of all {@link Task}s managed by this Overseer
	 */
	public TaskState[] getTaskStates();

	/**
	 * Adds the given {@link TaskObserver} to the list of observers of this
	 * {@link Overseer} and is notified if the state of a task changes.
	 * 
	 * @param observer
	 *            the {@link TaskObserver} that should be a able to observe the
	 *            {@link Task}s managed by this overseer
	 */
	public void addObserver(TaskObserver observer);

	/**
	 * Returns the {@link Worker} instance that is assigned to the given task.
	 * 
	 * @param task
	 *            the task that is performed by the worker
	 * @return the worker or null if the given task is not known
	 */
	public Worker getWorker(Task task);
}
