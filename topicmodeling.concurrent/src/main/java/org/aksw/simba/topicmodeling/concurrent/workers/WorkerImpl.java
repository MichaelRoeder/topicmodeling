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
package org.aksw.simba.topicmodeling.concurrent.workers;

import java.lang.Thread.State;

import org.aksw.simba.topicmodeling.concurrent.overseers.Overseer;
import org.aksw.simba.topicmodeling.concurrent.tasks.Task;
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
