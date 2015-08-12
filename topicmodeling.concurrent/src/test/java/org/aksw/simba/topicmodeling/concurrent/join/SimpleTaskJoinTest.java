package org.aksw.simba.topicmodeling.concurrent.join;

import java.util.Random;

import junit.framework.Assert;

import org.aksw.simba.topicmodeling.concurrent.overseers.pool.DefeatableOverseer;
import org.aksw.simba.topicmodeling.concurrent.overseers.pool.ExecutorBasedOverseer;
import org.aksw.simba.topicmodeling.concurrent.tasks.SleepingTask;
import org.aksw.simba.topicmodeling.concurrent.tasks.TaskState;
import org.junit.Test;

public class SimpleTaskJoinTest {

	private static final int NUMBER_OF_TEST_RUNS = 3;

	@Test
	public void test() throws InterruptedException {
		Random rand = new Random();
		int numberOfTasks;
		DefeatableOverseer overseer = new ExecutorBasedOverseer(2);
		SimpleTaskJoin join = new SimpleTaskJoin();
		TaskState states[];
		for (int i = 0; i < NUMBER_OF_TEST_RUNS; ++i) {
			numberOfTasks = rand.nextInt(10);
			for (int j = 0; j < numberOfTasks; ++j) {
				overseer.startTask(new SleepingTask(Integer.toString((i * 10) + j), (long) rand.nextInt(2000)));
			}

			join.joinWithTasks(overseer);

			states = overseer.getTaskStates();
			for (int j = 0; j < states.length; ++j) {
				Assert.assertEquals(Thread.State.TERMINATED, states[j].state);
			}
		}
		overseer.shutdown();
	}
}
