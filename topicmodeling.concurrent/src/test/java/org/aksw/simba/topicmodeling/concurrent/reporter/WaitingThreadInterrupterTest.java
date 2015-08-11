package org.aksw.simba.topicmodeling.concurrent.reporter;

import java.util.concurrent.Semaphore;

import org.aksw.simba.topicmodeling.concurrent.overseers.Overseer;
import org.aksw.simba.topicmodeling.concurrent.overseers.simple.SimpleOverseer;
import org.aksw.simba.topicmodeling.concurrent.tasks.SleepingTask;
import org.aksw.simba.topicmodeling.concurrent.tasks.Task;
import org.aksw.simba.topicmodeling.concurrent.tasks.TaskObserver;
import org.aksw.simba.topicmodeling.concurrent.utils.MaxDurationCheckingThread;
import org.junit.Assert;
import org.junit.Test;

public class WaitingThreadInterrupterTest implements TaskObserver {

	private Semaphore stoppedTasks = new Semaphore(0);

	@Test
	public void testTaskShouldBeInterrupted() {
		long MAX_WAITING_TIME = 1000;
		long MAX_TIME_FOR_TEST = 2 * MAX_WAITING_TIME;
		long TASK_SLEEP_DURATION = 5 * MAX_WAITING_TIME;

		Overseer overseer = new SimpleOverseer();
		overseer.addObserver(this);
		new WaitingThreadInterrupter(overseer, MAX_WAITING_TIME, (long) MAX_WAITING_TIME / 10);

		MaxDurationCheckingThread durationCheck = new MaxDurationCheckingThread(Thread.currentThread(),
				MAX_TIME_FOR_TEST);
		(new Thread(durationCheck)).start();

		overseer.startTask(new SleepingTask("Sleep", TASK_SLEEP_DURATION));
		try {
			// wait for the task to be interrupted
			stoppedTasks.acquire();
		} catch (InterruptedException e) {
		}
		// let the other thread know, that we have finished
		durationCheck.reportFinished();
	}

	@Test
	public void testTaskShouldNotBeInterrupted() {
		long MAX_WAITING_TIME = 1000;
		long MAX_TIME_FOR_TEST = 2 * MAX_WAITING_TIME;
		long TASK_SLEEP_DURATION = 100;

		Overseer overseer = new SimpleOverseer();
		overseer.addObserver(this);
		new WaitingThreadInterrupter(overseer, MAX_WAITING_TIME, (long) MAX_WAITING_TIME / 10);

		MaxDurationCheckingThread durationCheck = new MaxDurationCheckingThread(Thread.currentThread(),
				MAX_TIME_FOR_TEST);
		(new Thread(durationCheck)).start();

		long timeNeeded = System.currentTimeMillis();
		overseer.startTask(new SleepingTask("Sleep", TASK_SLEEP_DURATION));
		try {
			// wait for the task to finish
			stoppedTasks.acquire();
		} catch (InterruptedException e) {
		}
		timeNeeded = System.currentTimeMillis() - timeNeeded;
		Assert.assertTrue(timeNeeded > TASK_SLEEP_DURATION);
		Assert.assertTrue(timeNeeded < MAX_WAITING_TIME);
		// let the other thread know, that we have finished
		durationCheck.reportFinished();
	}

	@Override
	public void reportTaskFinished(Task task) {
		stoppedTasks.release();
	}

	@Override
	public void reportTaskThrowedException(Task task, Throwable t) {
		stoppedTasks.release();
	}
}
