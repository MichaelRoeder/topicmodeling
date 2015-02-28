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

import org.aksw.simba.topicmodeling.concurrent.overseers.Overseer;
import org.aksw.simba.topicmodeling.concurrent.tasks.Task;
import org.aksw.simba.topicmodeling.concurrent.tasks.TaskObserver;
import org.aksw.simba.topicmodeling.concurrent.tasks.TaskState;
import org.aksw.simba.topicmodeling.concurrent.workers.Worker;

public class AbstractOverseerDecorator implements OverseerDecorator {

    private Overseer overseer;

    public AbstractOverseerDecorator(Overseer overseer) {
        this.overseer = overseer;
    }

    public void startTask(Task task) {
        overseer.startTask(task);
    }

    public void addObserver(TaskObserver observer) {
        overseer.addObserver(observer);
    }

    public void reportTaskFinished(Worker worker) {
        overseer.reportTaskFinished(worker);
    }

    public void reportTaskThrowedException(Worker worker, Throwable t) {
        overseer.reportTaskThrowedException(worker, t);
    }

    public Overseer getDecorated() {
        return overseer;
    }

    public TaskState getTaskState(Task task) {
        return overseer.getTaskState(task);
    }

    public TaskState[] getTaskStates() {
        return overseer.getTaskStates();
    }

}
