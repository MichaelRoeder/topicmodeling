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
package org.aksw.simba.topicmodeling.concurrent.overseers;

import org.aksw.simba.topicmodeling.concurrent.overseers.Overseer;
import org.aksw.simba.topicmodeling.concurrent.overseers.simple.BlockingOverseer;
import org.aksw.simba.topicmodeling.concurrent.tasks.SimpleSleepingTask;
import org.aksw.simba.topicmodeling.concurrent.tasks.Task;
import org.aksw.simba.topicmodeling.concurrent.tasks.TaskObserver;
import org.aksw.simba.topicmodeling.concurrent.utils.MaxDurationCheckingThread;
import org.junit.Assert;
import org.junit.Test;

public class BlockingOverseerTest implements TaskObserver {

    private static final int NUMBER_OF_CONCURRENT_TASKS = 4;
    private static final int NUMBER_OF_TEST_TASKS = 20;
    private static final int TIME_TASK_IS_SLEEPING = 100;

    private int stoppedTasks = 0;

    @Test
    public void test() {
        MaxDurationCheckingThread durationCheck = new MaxDurationCheckingThread(Thread.currentThread(),
                (NUMBER_OF_TEST_TASKS + 2) * TIME_TASK_IS_SLEEPING);
        (new Thread(durationCheck)).start();

        Overseer overseer = new BlockingOverseer(NUMBER_OF_CONCURRENT_TASKS);
        overseer.addObserver(this);
        // lets start some tasks and test whether the number of concurrent tasks is equal to the expected number
        int expectedNumberOfTasks;
        for (int i = 0; i < NUMBER_OF_TEST_TASKS; ++i) {
            overseer.startTask(new SimpleSleepingTask(Integer.toString(i), TIME_TASK_IS_SLEEPING));
            expectedNumberOfTasks = Math.min(NUMBER_OF_CONCURRENT_TASKS, (i + 1) - stoppedTasks);
            Assert.assertEquals("In iteration " + i + "/" + NUMBER_OF_TEST_TASKS, expectedNumberOfTasks,
                    overseer.getTaskStates().length);
            try {
                // let's sleep a short time to make sure that the tasks are more even distributed.
                Thread.sleep(TIME_TASK_IS_SLEEPING / NUMBER_OF_CONCURRENT_TASKS);
            } catch (InterruptedException e) {
            }
        }
        try {
            // let's wait for the last task to finish
            Thread.sleep(TIME_TASK_IS_SLEEPING);
        } catch (InterruptedException e) {
        }
        Assert.assertEquals(NUMBER_OF_TEST_TASKS, stoppedTasks);
        // let the other thread know, that we have finished
        durationCheck.reportFinished();
    }

    @Override
    public void reportTaskFinished(Task task) {
        ++stoppedTasks;
    }

    @Override
    public void reportTaskThrowedException(Task task, Throwable t) {
        reportTaskFinished(task);
    }
}
