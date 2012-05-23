package org.hyperdrive.foundation.impl;

import junit.framework.Assert;

import org.hyperdrive.MockedEnv;
import org.hyperdrive.core.api.ManagedDisplay;
import org.hyperdrive.core.api.PropertyManipulator;
import org.hyperdrive.core.api.RenderArea;
import org.hyperdrive.core.api.event.ClientCreatedHandler;
import org.hyperdrive.core.api.event.PropertyChangedEvent;
import org.hyperdrive.core.api.event.PropertyChangedHandler;
import org.hyperdrive.foundation.impl.ManagedDisplayImpl;
import org.hyperdrive.foundation.impl.BasePropertyManipulator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.trinity.core.display.api.Property;
import org.trinity.core.display.api.PropertyInstance;
import org.trinity.core.display.api.event.DisplayEventType;
import org.trinity.core.display.api.event.MapRequestEvent;
import org.trinity.core.display.api.event.PropertyChangedNotifyEvent;

public class PropertyInteractionTest {

	private MockedEnv mockedEnv;

	@Before
	public void setup() {
		this.mockedEnv = new MockedEnv();
		this.mockedEnv.setUp();
	}

	@Test
	public void testPropertyRead() {
		final PropertyManipulator propertyManipulator = new BasePropertyManipulator(
				this.mockedEnv.widgetMock);
		final PropertyInstance propInst = propertyManipulator
				.getPropertyValue(this.mockedEnv.propertyMockName);

		Assert.assertEquals(this.mockedEnv.propertyInstanceMock, propInst);
	}

	@Test
	public void testPropertyWrite() {
		final PropertyManipulator propertyManipulator = new BasePropertyManipulator(
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

		Mockito.when(this.mockedEnv.displayMock.getEventFromMasterQueue())
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

		managedDisplay.deliverNextDisplayEvent(false);
		managedDisplay.deliverNextDisplayEvent(false);

		Mockito.verify(handler, Mockito.times(1))
				.handleEvent(
						(PropertyChangedEvent<Property<? extends PropertyInstance>>) Matchers
								.any());
	}
}
