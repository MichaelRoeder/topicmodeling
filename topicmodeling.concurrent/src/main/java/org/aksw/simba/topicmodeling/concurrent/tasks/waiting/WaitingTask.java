package org.aksw.simba.topicmodeling.concurrent.tasks.waiting;

import org.aksw.simba.topicmodeling.concurrent.tasks.Task;

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
