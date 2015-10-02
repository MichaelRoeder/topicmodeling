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
	 * Removes the given {@link TaskObserver} from the list of observers if it
	 * exists.
	 * 
	 * @param observer
	 *            the {@link TaskObserver} that should be removed
	 */
	public void removeObserver(TaskObserver observer);

	/**
	 * Returns the {@link Worker} instance that is assigned to the given task.
	 * 
	 * @param task
	 *            the task that is performed by the worker
	 * @return the worker or null if the given task is not known
	 */
	public Worker getWorker(Task task);
}
