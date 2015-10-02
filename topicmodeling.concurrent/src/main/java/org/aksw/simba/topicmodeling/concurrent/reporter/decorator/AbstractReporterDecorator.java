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
package org.aksw.simba.topicmodeling.concurrent.reporter.decorator;

import org.aksw.simba.topicmodeling.concurrent.overseers.Overseer;
import org.aksw.simba.topicmodeling.concurrent.reporter.Reporter;
import org.aksw.simba.topicmodeling.concurrent.tasks.Task;
import org.aksw.simba.topicmodeling.concurrent.tasks.TaskState;

public class AbstractReporterDecorator implements ReporterDecorator {

	private Reporter reporter;

	public AbstractReporterDecorator(Reporter reporter) {
		super();
		this.reporter = reporter;
	}

	public void reportCurrentState() {
		reporter.reportCurrentState();
	}

	public void setOverseer(Overseer overseer) {
		reporter.setOverseer(overseer);
	}

	public Overseer getOverseer() {
		return reporter.getOverseer();
	}

	public void reportTaskFinished(Task task) {
		reporter.reportTaskFinished(task);
	}

	public void reportTaskThrowedException(Task task, Throwable t) {
		reporter.reportTaskThrowedException(task, t);
	}

	public void reportTaskState(TaskState state) {
		reporter.reportTaskState(state);
	}

	public Reporter getDecorated() {
		return reporter;
	}

}
