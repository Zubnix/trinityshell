package org.trinity.shell.impl.surface;

import org.junit.Test;
import org.mockito.Mockito;
import org.trinity.foundation.display.api.DisplayServer;
import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.DisplayEventSource;

import com.google.common.eventbus.EventBus;

public class ShellEventDispatcherImplTest {

	/***************************************
	 * Test if registering multiple {@link DisplayEventSource}s, each will
	 * receive the same event if one arrives
	 * <p>
	 * Test if no {@link DisplayEventSource} is registered, a new
	 * {@link ShellClientSurface} will be created.
	 * <p>
	 * TODO test unregistering
	 *************************************** 
	 */
	@Test
	public void test() {

		final EventBus nodeEventBus = Mockito.mock(EventBus.class);

		final DisplayEventSource displayEventSource = Mockito.mock(DisplayEventSource.class);

		final ShellClientSurfaceFactory shellClientSurfaceFactory = Mockito.mock(ShellClientSurfaceFactory.class);

		final DisplaySurface displaySurface = Mockito.mock(DisplaySurface.class);

		final DisplayServer displayServer = Mockito.mock(DisplayServer.class);
		final DisplayEvent displayEvent0 = Mockito.mock(DisplayEvent.class);
		final DisplayEvent displayEvent1 = Mockito.mock(DisplayEvent.class);
		final DisplayEvent displayEvent2 = Mockito.mock(DisplayEvent.class);

		Mockito.when(displayEvent0.getDisplayEventSource()).thenReturn(displayEventSource);
		Mockito.when(displayEvent1.getDisplayEventSource()).thenReturn(displayEventSource);
		Mockito.when(displayEvent2.getDisplayEventSource()).thenReturn(displaySurface);

		Mockito.when(displayServer.getNextDisplayEvent()).thenReturn(	displayEvent0,
																		displayEvent1,
																		displayEvent2);

		final ShellDisplayEventDispatcherImpl shellEventDispatcherImpl = new ShellDisplayEventDispatcherImpl(	new EventBus(),
																								shellClientSurfaceFactory,
																								displayServer);

		shellEventDispatcherImpl.registerDisplayEventSourceListener(nodeEventBus,
															displayEventSource);
		shellEventDispatcherImpl.dispatchDisplayEvent(false);
		shellEventDispatcherImpl.dispatchDisplayEvent(false);
		shellEventDispatcherImpl.dispatchDisplayEvent(false);

		/*
		 * verify if registering multiple {@link DisplayEventSource}s, each will
		 * receive the same event if one arrives
		 */
		Mockito.verify(	nodeEventBus,
						Mockito.times(1)).post(displayEvent0);
		Mockito.verify(	nodeEventBus,
						Mockito.times(1)).post(displayEvent1);

		/*
		 * verify if no {@link DisplayEventSource} is registered, a new {@link
		 * ShellClientSurface} will be created.
		 */
		Mockito.verify(	shellClientSurfaceFactory,
						Mockito.times(1)).createShellClientSurface(displaySurface);
	}
}
