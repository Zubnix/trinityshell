package org.trinity.foundation.api.shared;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.eventbus.Subscribe;

public class AsyncListenableEventBusTest {

	@Test
	public void test() throws InterruptedException {
		final CountDownLatch countDownLatchAll = new CountDownLatch(6);

		final AsyncListenableEventBus asyncListenableEventBus = new AsyncListenableEventBus(Executors.newSingleThreadExecutor());

		asyncListenableEventBus.register(	new Object() {
												@Subscribe
												public void handle(final Object event) {
													countDownLatchAll.countDown();
													countDownLatchAll.countDown();
												}
											},
											Executors.newSingleThreadExecutor());

		asyncListenableEventBus.register(	new Object() {
												@Subscribe
												public void handle(final Object event) {
													countDownLatchAll.countDown();
												}
											},
											Executors.newSingleThreadExecutor());

		asyncListenableEventBus.post(new Object());
		asyncListenableEventBus.post(new Object());

		final boolean notimeout = countDownLatchAll.await(	1,
															TimeUnit.SECONDS);

		Assert.assertTrue(notimeout);
	}
}
