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
package org.dice_research.topicmodeling.concurrent.workers;

import org.dice_research.topicmodeling.concurrent.overseers.Overseer;
import org.dice_research.topicmodeling.concurrent.tasks.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleWorker extends Thread implements Worker {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleWorker.class);

    private Task task;
    private Overseer overseer;

    public SimpleWorker(Task task, Overseer overseer) {
        super();
        this.task = task;
        this.overseer = overseer;
    }

    @Override
    public void run() {
        try {
            task.run();
            overseer.reportTaskFinished(this);
        } catch (Exception e) {
            LOGGER.error("Got an exception from task {}.", task.getId());
            overseer.reportTaskThrowedException(this, e);
        } catch (Error e) {
            LOGGER.error("Got an error from task {}.", task.getId());
            overseer.reportTaskThrowedException(this, e);
        }
    }

    public Task getTask() {
        return task;
    }
}
