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
package org.aksw.simba.topicmodeling.concurrent.overseers.decorator;

import java.util.concurrent.Semaphore;

import org.aksw.simba.topicmodeling.concurrent.overseers.Overseer;
import org.aksw.simba.topicmodeling.concurrent.tasks.Task;
import org.aksw.simba.topicmodeling.concurrent.workers.Worker;
import org.aksw.simba.topicmodeling.concurrent.workers.WorkerObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @deprecated because it can't work as a decorator if it tries to use semaphores since it won't be used as
 *             {@link WorkerObserver} by the {@link Worker}s instantiated by the decorated {@link Overseer}.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 * 
 */
@Deprecated
public class BlockingOverseer extends AbstractOverseerDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockingOverseer.class);

    private Semaphore semaphore;

    public BlockingOverseer(Overseer overseer, int numberOfConcurrentTasks) {
        super(overseer);
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
