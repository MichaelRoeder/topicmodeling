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
package org.aksw.simba.topicmodeling.concurrent.overseers.simple;

import java.util.concurrent.Semaphore;

import org.aksw.simba.topicmodeling.concurrent.tasks.Task;
import org.aksw.simba.topicmodeling.concurrent.workers.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockingOverseer extends SimpleOverseer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockingOverseer.class);

    private Semaphore semaphore;

    public BlockingOverseer(int numberOfConcurrentTasks) {
        semaphore = new Semaphore(numberOfConcurrentTasks);
    }

    @Override
    public void startTask(Task task) {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted while waiting for a free slot to start a task. Aborting.", e);
            return;
        }
        super.startTask(task);
    }

    @Override
    public void reportTaskFinished(Worker worker) {
        try {
            super.reportTaskFinished(worker);
        } finally {
            semaphore.release();
        }
    }

    @Override
    public void reportTaskThrowedException(Worker worker, Throwable t) {
        try {
            super.reportTaskFinished(worker);
        } finally {
            semaphore.release();
        }
    }
}
