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
