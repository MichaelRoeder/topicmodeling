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
package org.dice_research.topicmodeling.concurrent.overseers.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.dice_research.topicmodeling.concurrent.overseers.AbstractOverseer;
import org.dice_research.topicmodeling.concurrent.tasks.Task;
import org.dice_research.topicmodeling.concurrent.workers.Worker;
import org.dice_research.topicmodeling.concurrent.workers.WorkerImpl;

public class ExecutorBasedOverseer extends AbstractOverseer implements DefeatableOverseer {

	private ExecutorService executor;

	public ExecutorBasedOverseer(int poolSize) {
		executor = Executors.newFixedThreadPool(poolSize);
	}

	public ExecutorBasedOverseer(int poolSize, ThreadFactory factory) {
		executor = Executors.newFixedThreadPool(poolSize, factory);
	}

	/**
	 * Constructor. Note that calling the {@link #shutdown()} method closes the
	 * given executor service.
	 * 
	 * @param executor
	 */
	public ExecutorBasedOverseer(ExecutorService executor) {
		executor = this.executor;
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
