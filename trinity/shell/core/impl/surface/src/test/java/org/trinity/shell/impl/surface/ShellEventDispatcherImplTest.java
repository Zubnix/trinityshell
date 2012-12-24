/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.impl.surface;

import org.junit.Test;
import org.mockito.Mockito;
import org.trinity.foundation.display.api.DisplayServer;
import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.DisplayEventTarget;

import com.google.common.eventbus.EventBus;

public class ShellEventDispatcherImplTest {

	/***************************************
	 * Test if registering multiple {@link DisplayEventTarget}s, each will
	 * receive the same event if one arrives
	 * <p>
	 * Test if no {@link DisplayEventTarget} is registered, a new
	 * {@link ShellClientSurface} will be created.
	 * <p>
	 * TODO test unregistering
	 *************************************** 
	 */
	@Test
	public void test() {

		final EventBus nodeEventBus = Mockito.mock(EventBus.class);

		final DisplayEventTarget displayEventTarget = Mockito.mock(DisplayEventTarget.class);

		final ShellClientSurfaceFactory shellClientSurfaceFactory = Mockito.mock(ShellClientSurfaceFactory.class);

		final DisplaySurface displaySurface = Mockito.mock(DisplaySurface.class);

		final DisplayServer displayServer = Mockito.mock(DisplayServer.class);
		final DisplayEvent displayEvent0 = Mockito.mock(DisplayEvent.class);
		final DisplayEvent displayEvent1 = Mockito.mock(DisplayEvent.class);
		final DisplayEvent displayEvent2 = Mockito.mock(DisplayEvent.class);

		Mockito.when(displayEvent0.getDisplayEventTarget()).thenReturn(displayEventTarget);
		Mockito.when(displayEvent1.getDisplayEventTarget()).thenReturn(displayEventTarget);
		Mockito.when(displayEvent2.getDisplayEventTarget()).thenReturn(displaySurface);

		Mockito.when(displayServer.getNextDisplayEvent()).thenReturn(	displayEvent0,
																		displayEvent1,
																		displayEvent2);

		final ShellDisplayEventDispatcherImpl shellEventDispatcherImpl = new ShellDisplayEventDispatcherImpl(	new EventBus(),
																												shellClientSurfaceFactory,
																												displayServer);

		shellEventDispatcherImpl.registerDisplayEventSourceListener(nodeEventBus,
																	displayEventTarget);
		shellEventDispatcherImpl.dispatchDisplayEvent(false);
		shellEventDispatcherImpl.dispatchDisplayEvent(false);
		shellEventDispatcherImpl.dispatchDisplayEvent(false);

		/*
		 * verify if registering multiple {@link DisplayEventTarget}s, each will
		 * receive the same event if one arrives
		 */
		Mockito.verify(	nodeEventBus,
						Mockito.times(1)).post(displayEvent0);
		Mockito.verify(	nodeEventBus,
						Mockito.times(1)).post(displayEvent1);

		/*
		 * verify if no {@link DisplayEventTarget} is registered, a new {@link
		 * ShellClientSurface} will be created.
		 */
		Mockito.verify(	shellClientSurfaceFactory,
						Mockito.times(1)).createShellClientSurface(displaySurface);
	}
}
