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
