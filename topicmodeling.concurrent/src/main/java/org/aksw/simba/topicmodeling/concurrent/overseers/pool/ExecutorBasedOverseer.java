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
package org.aksw.simba.topicmodeling.concurrent.overseers.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.aksw.simba.topicmodeling.concurrent.overseers.AbstractOverseer;
import org.aksw.simba.topicmodeling.concurrent.tasks.Task;
import org.aksw.simba.topicmodeling.concurrent.workers.Worker;
import org.aksw.simba.topicmodeling.concurrent.workers.WorkerImpl;

public class ExecutorBasedOverseer extends AbstractOverseer implements DefeatableOverseer {

    private ExecutorService executor;

    public ExecutorBasedOverseer(int poolSize) {
        executor = Executors.newFixedThreadPool(poolSize);
    }

    public ExecutorBasedOverseer(int poolSize, ThreadFactory factory) {
        executor = Executors.newFixedThreadPool(poolSize, factory);
    }

    @Override
    protected Worker createWorker(Task task) {
        return new WorkerImpl(task, this);
    }

    @Override
    protected void startWorker(Worker worker) {
        executor.execute(worker);
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }
}
