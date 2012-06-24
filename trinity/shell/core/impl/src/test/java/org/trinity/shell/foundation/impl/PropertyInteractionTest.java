package org.trinity.shell.foundation.impl;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.trinity.foundation.display.api.Property;
import org.trinity.foundation.display.api.PropertyInstance;
import org.trinity.foundation.display.api.event.DisplayEventType;
import org.trinity.foundation.display.api.event.MapRequestEvent;
import org.trinity.foundation.display.api.event.PropertyChangedNotifyEvent;
import org.trinity.shell.MockedEnv;
import org.trinity.shell.core.api.ManagedDisplay;
import org.trinity.shell.core.api.PropertyManipulator;
import org.trinity.shell.core.api.RenderArea;
import org.trinity.shell.core.api.event.ClientCreatedHandler;
import org.trinity.shell.core.api.event.PropertyChangedEvent;
import org.trinity.shell.core.api.event.PropertyChangedHandler;
import org.trinity.shell.foundation.impl.PropertyManipulatorImpl;
import org.trinity.shell.foundation.impl.ManagedDisplayImpl;

public class PropertyInteractionTest {

	private MockedEnv mockedEnv;

	@Before
	public void setup() {
		this.mockedEnv = new MockedEnv();
		this.mockedEnv.setUp();
	}

	@Test
	public void testPropertyRead() {
		final PropertyManipulator propertyManipulator = new PropertyManipulatorImpl(
				this.mockedEnv.widgetMock);
		final PropertyInstance propInst = propertyManipulator
				.getPropertyValue(this.mockedEnv.propertyMockName);

		Assert.assertEquals(this.mockedEnv.propertyInstanceMock, propInst);
	}

	@Test
	public void testPropertyWrite() {
		final PropertyManipulator propertyManipulator = new PropertyManipulatorImpl(
				this.mockedEnv.widgetMock);

		propertyManipulator.setPropertyValue(this.mockedEnv.propertyMockName,
				this.mockedEnv.propertyInstanceMock);

		Mockito.verify(this.mockedEnv.widgetPlatformRenderAreaMock,
				Mockito.times(1)).setPropertyInstance(
				this.mockedEnv.propertyMock, this.mockedEnv.propertyInstanceMock);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testHandleChangedProperty() {
		final ManagedDisplay managedDisplay = new ManagedDisplayImpl(
				this.mockedEnv.displayMock);

		final MapRequestEvent c0Map = Mockito.mock(MapRequestEvent.class);
		Mockito.when(c0Map.getType()).thenReturn(DisplayEventType.MAP_REQUEST);
		Mockito.when(c0Map.getEventSource()).thenReturn(this.mockedEnv.c0);

		final String propertyName = "dummyProperty";
		final Property prop = Mockito.mock(Property.class);
		Mockito.when(prop.getAtomName()).thenReturn(propertyName);

		final PropertyChangedNotifyEvent c0Prop = Mockito
				.mock(PropertyChangedNotifyEvent.class);
		Mockito.when(c0Prop.getEventSource()).thenReturn(this.mockedEnv.c0);
		Mockito.when(c0Prop.getType()).thenReturn(
				DisplayEventType.PROPERTY_CHANGED);
		Mockito.when(c0Prop.getChangedProperty()).thenReturn(prop);

		Mockito.when(this.mockedEnv.displayMock.getNextDisplayEvent())
				.thenReturn(c0Map, c0Prop);

		final PropertyChangedHandler<Property<? extends PropertyInstance>> handler = Mockito
				.mock(PropertyChangedHandler.class);

		final ClientCreatedHandler clientCreatedHandler = new ClientCreatedHandler() {
			@Override
			public void handleCreatedClient(final RenderArea client) {
				client.addPropertyChangedHandler(handler, propertyName);
			}
		};

		managedDisplay.addClientCreatedHandler(clientCreatedHandler);

		managedDisplay.postNextDisplayEvent(false);
		managedDisplay.postNextDisplayEvent(false);

		Mockito.verify(handler, Mockito.times(1))
				.handleEvent(
						(PropertyChangedEvent<Property<? extends PropertyInstance>>) Matchers
								.any());
	}
}