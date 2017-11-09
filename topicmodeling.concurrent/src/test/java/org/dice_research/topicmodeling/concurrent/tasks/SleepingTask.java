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
package org.dice_research.topicmodeling.concurrent.tasks;

import org.dice_research.topicmodeling.concurrent.tasks.waiting.AbstractWaitingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SleepingTask extends AbstractWaitingTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(SleepingTask.class);

    private String id;
    private long duration;
    private long startTime;

    public SleepingTask(String id, long duration) {
        this.id = id;
        this.duration = duration;
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        startWaiting();
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            LOGGER.warn("Interrupted while sleeping.");
        }
        stopWaiting();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getProgress() {
        return Double.toString(((System.currentTimeMillis() - startTime) / (double) duration) * 100.0) + "%";
    }
}
