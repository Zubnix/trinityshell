package org.trinity.shell.core.impl;

import org.junit.Test;
import org.mockito.Mockito;
import org.trinity.foundation.display.api.DisplayServer;
import org.trinity.foundation.display.api.event.DisplayEventSource;

import com.google.common.eventbus.EventBus;

public class ShellEventDispatcherImplTest {

	/***************************************
	 * Test if registering multiple {@link DisplayEventSource}s will each
	 * receive the same event if one arrives & if no {@link DisplayEventSource}
	 * is registered that a new {@link ShellClientSurface} will be created.
	 *************************************** 
	 */
	@Test
	public void test() {
		final EventBus shellEventBus = new EventBus();

		final ShellClientSurfaceFactory shellClientSurfaceFactory = Mockito.mock(ShellClientSurfaceFactory.class);
		final DisplayServer displayServer = Mockito.mock(DisplayServer.class);

		final ShellEventDispatcherImpl shellEventDispatcherImpl = new ShellEventDispatcherImpl(	shellEventBus,
																								shellClientSurfaceFactory,
																								displayServer);

		throw new RuntimeException("Not yet implemented.");
	}
}
