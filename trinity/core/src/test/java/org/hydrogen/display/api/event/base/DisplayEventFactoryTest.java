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
package org.hydrogen.display.api.event.base;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.trinity.core.display.api.event.ButtonNotifyEvent;
import org.trinity.core.display.api.event.ConfigureRequestEvent;
import org.trinity.core.display.api.event.DisplayEventFactory;
import org.trinity.core.display.api.event.DisplayEventSource;
import org.trinity.core.display.impl.event.BaseDisplayEventModule;
import org.trinity.core.input.api.MouseInput;

import com.google.inject.Guice;
import com.google.inject.Injector;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class DisplayEventFactoryTest {

	@Test
	public void testConfiguration() {
		final Injector injector = Guice
				.createInjector(new BaseDisplayEventModule());
		final DisplayEventFactory displayEventFactory = injector
				.getInstance(DisplayEventFactory.class);

		final DisplayEventSource eventSource = Mockito
				.mock(DisplayEventSource.class);
		final MouseInput mouseInput = Mockito.mock(MouseInput.class);

		// test a few events to see if everything is set up correctly

		// button pressed
		final ButtonNotifyEvent buttonPressed = displayEventFactory
				.createButtonPressed(eventSource, mouseInput);
		Assert.assertNotNull(buttonPressed);
		Assert.assertEquals(eventSource, buttonPressed.getEventSource());
		Assert.assertEquals(mouseInput, buttonPressed.getInput());

		// configure request
		final ConfigureRequestEvent configureRequest = displayEventFactory
				.createConfigureRequest(eventSource,
										true,
										true,
										true,
										true,
										1,
										2,
										3,
										4);
		Assert.assertNotNull(configureRequest);
		Assert.assertEquals(1, configureRequest.getX());
		Assert.assertEquals(2, configureRequest.getY());
		Assert.assertEquals(3, configureRequest.getWidth());
		Assert.assertEquals(4, configureRequest.getHeight());
	}
}
