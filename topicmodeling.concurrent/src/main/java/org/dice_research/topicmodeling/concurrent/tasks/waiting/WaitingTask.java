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
package org.dice_research.topicmodeling.concurrent.tasks.waiting;

import org.dice_research.topicmodeling.concurrent.tasks.Task;

/**
 * This interface has been defined for tasks that might wait for a long time,
 * e.g., for an external resource to load. Those tasks offer a method that
 * returns the time they are already waiting. Thus, instances of this interface
 * can be used for environments, in which a task should be interrupted if it
 * takes to much time.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 *
 */
public interface WaitingTask extends Task {

	/**
	 * Returns the time the task is already waiting.
	 * 
	 * @return the time in milliseconds
	 */
	public long getTimeWaiting();
}
