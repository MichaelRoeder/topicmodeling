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

import java.lang.Thread.State;

import org.dice_research.topicmodeling.concurrent.tasks.Task;

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
