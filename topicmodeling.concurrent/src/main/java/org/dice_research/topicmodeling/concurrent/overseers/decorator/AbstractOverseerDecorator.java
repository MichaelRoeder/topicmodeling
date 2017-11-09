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
package org.dice_research.topicmodeling.concurrent.overseers.decorator;

import org.dice_research.topicmodeling.concurrent.overseers.Overseer;
import org.dice_research.topicmodeling.concurrent.tasks.Task;
import org.dice_research.topicmodeling.concurrent.tasks.TaskObserver;
import org.dice_research.topicmodeling.concurrent.tasks.TaskState;
import org.dice_research.topicmodeling.concurrent.workers.Worker;

public class AbstractOverseerDecorator implements OverseerDecorator {

	private Overseer overseer;

	public AbstractOverseerDecorator(Overseer overseer) {
		this.overseer = overseer;
	}

	public void startTask(Task task) {
		overseer.startTask(task);
	}

	public void addObserver(TaskObserver observer) {
		overseer.addObserver(observer);
	}

	@Override
	public void removeObserver(TaskObserver observer) {
		overseer.removeObserver(observer);
	}

	public void reportTaskFinished(Worker worker) {
		overseer.reportTaskFinished(worker);
	}

	public void reportTaskThrowedException(Worker worker, Throwable t) {
		overseer.reportTaskThrowedException(worker, t);
	}

	public Overseer getDecorated() {
		return overseer;
	}

	public TaskState getTaskState(Task task) {
		return overseer.getTaskState(task);
	}

	public TaskState[] getTaskStates() {
		return overseer.getTaskStates();
	}

	public Worker getWorker(Task task) {
		return overseer.getWorker(task);
	}

}
