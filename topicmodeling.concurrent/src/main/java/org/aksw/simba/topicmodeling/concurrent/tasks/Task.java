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
package org.aksw.simba.topicmodeling.concurrent.tasks;

import org.aksw.simba.topicmodeling.concurrent.workers.Worker;

/**
 * Interface of a task that is processable by a {@link Worker}.
 * 
 * @author Michael Röder <roeder@informatik.uni-leipzig.de>
 * 
 */
public interface Task extends Runnable {

    /**
     * Returns an identifying description for this task.
     * 
     * @return an identifying description for this task
     */
    public String getId();

    /**
     * Returns a description of the tasks progress or null, if such a
     * description is not supported.
     * 
     * @return a description of the tasks progress or null
     */
    public String getProgress();
}
