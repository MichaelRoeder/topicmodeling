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
package org.dice_research.topicmodeling.concurrent.reporter;

import java.lang.Thread.State;

import org.dice_research.topicmodeling.concurrent.overseers.Overseer;
import org.dice_research.topicmodeling.concurrent.tasks.Task;
import org.dice_research.topicmodeling.concurrent.tasks.TaskState;

public abstract class AbstractReporter implements Reporter {

	private Overseer overseer;

	public AbstractReporter(Overseer overseer) {
		setOverseer(overseer);
	}

	public void reportTaskFinished(Task task) {
		reportTaskState(new TaskState(task, State.TERMINATED));
	}

	public void reportTaskThrowedException(Task task, Throwable t) {
		reportTaskState(new TaskState(task, State.TERMINATED, t));
	}

	public void reportCurrentState() {
		TaskState states[] = overseer.getTaskStates();
		for (int i = 0; i < states.length; ++i) {
			reportTaskState(states[i]);
		}
	}

	public void setOverseer(Overseer overseer) {
		this.overseer = overseer;
		overseer.addObserver(this);
	}

	public Overseer getOverseer() {
		return overseer;
	}
}
