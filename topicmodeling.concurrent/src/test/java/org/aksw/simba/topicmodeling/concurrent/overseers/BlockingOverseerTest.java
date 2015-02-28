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
