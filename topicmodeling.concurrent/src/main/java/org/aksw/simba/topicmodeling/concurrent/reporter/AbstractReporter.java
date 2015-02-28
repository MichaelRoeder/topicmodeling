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

import java.lang.Thread.State;

import org.aksw.simba.topicmodeling.concurrent.overseers.Overseer;
import org.aksw.simba.topicmodeling.concurrent.tasks.Task;
import org.aksw.simba.topicmodeling.concurrent.tasks.TaskState;

public abstract class AbstractReporter implements Reporter {

    private Overseer overseer;

    public AbstractReporter(Overseer overseer) {
        setOverseer(overseer);
    }

    public void reportTaskFinished(Task task) {
        reportTaskState(new TaskState(task, State.TERMINATED));
    }

    public void reportTaskThrowedException(Task task, Throwable t) {
        reportTaskState(new TaskState(task, State.TERMINATED, t));
    }

    public void reportCurrentState() {
        TaskState states[] = overseer.getTaskStates();
        for (int i = 0; i < states.length; ++i) {
            reportTaskState(states[i]);
        }
    }

    public void setOverseer(Overseer overseer) {
        this.overseer = overseer;
        overseer.addObserver(this);
    }

    public Overseer getOverseer() {
        return overseer;
    }
}
