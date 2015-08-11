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
package org.aksw.simba.topicmodeling.concurrent.reporter;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.aksw.simba.topicmodeling.concurrent.overseers.Overseer;
import org.aksw.simba.topicmodeling.concurrent.tasks.TaskHelper;
import org.aksw.simba.topicmodeling.concurrent.tasks.TaskState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogReporter extends AbstractReporter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogReporter.class);

	public LogReporter(Overseer overseer) {
		super(overseer);
	}

	public void reportTaskState(TaskState state) {
		if (LOGGER.isInfoEnabled() || (state.throwable != null)) {
			StringBuilder builder = new StringBuilder();
			builder.append("Task=");
			builder.append(TaskHelper.taskToString(state.task));
			builder.append("\tstate=");
			builder.append(state.state);

			String taskProgress = state.task.getProgress();
			if (taskProgress != null) {
				builder.append("\tprogress=");
				builder.append(taskProgress);
			}

			if ((state.stackTrace != null) && (state.stackTrace.length > 0)) {
				builder.append("\tposition=");
				builder.append(state.stackTrace[0].toString());
			}
			if (state.throwable != null) {
				StringWriter stringWriter = new StringWriter();
				PrintWriter writer = new PrintWriter(stringWriter);
				state.throwable.printStackTrace(writer);
				writer.close();
				builder.append("\twith exception: ");
				builder.append(stringWriter.getBuffer().toString());
				LOGGER.error(builder.toString());
			} else {
				LOGGER.info(builder.toString());
			}
		}
	}
}
