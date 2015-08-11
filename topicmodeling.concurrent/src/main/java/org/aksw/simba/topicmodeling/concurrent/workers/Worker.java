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
package org.aksw.simba.topicmodeling.concurrent.workers;

import java.lang.Thread.State;

import org.aksw.simba.topicmodeling.concurrent.tasks.Task;

/**
 * A Worker is a {@link Runnable} that is able to execute a {@link Task}.
 * 
 * @author Michael Röder <roeder@informatik.uni-leipzig.de>
 * 
 */
public interface Worker extends Runnable {

    /**
     * Returns the {@link Task} executed by this {@link Worker}.
     * 
     * @return the {@link Task} executed by this {@link Worker}.
     */
    public Task getTask();

    /**
     * Returns the {@link State} of the thread in which this {@link Worker} is
     * running. Returns {@link State}.NEW if this worker hasn't been started
     * until now.
     * 
     * @return the {@link State} of the thread in which this {@link Worker} is
     *         running.
     */
    public State getState();

    /**
     * Returns the stack trace of the thread in which this {@link Worker} is
     * running.
     * 
     * @return the stack trace of the thread in which this {@link Worker} is
     *         running.
     */
    public StackTraceElement[] getStackTrace();
    
    public void interrupt();
}
