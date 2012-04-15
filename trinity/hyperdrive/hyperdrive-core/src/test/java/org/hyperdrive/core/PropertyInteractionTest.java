package org.hyperdrive.core;

import junit.framework.Assert;

import org.hydrogen.api.display.Property;
import org.hydrogen.api.display.PropertyInstance;
import org.hydrogen.api.display.event.DisplayEventType;
import org.hydrogen.api.display.event.MapRequestEvent;
import org.hydrogen.api.display.event.PropertyChangedNotifyEvent;
import org.hyperdrive.MockedEnv;
import org.hyperdrive.api.core.ManagedDisplay;
import org.hyperdrive.api.core.PropertyManipulator;
import org.hyperdrive.api.core.RenderArea;
import org.hyperdrive.api.core.event.ClientCreatedHandler;
import org.hyperdrive.api.core.event.PropertyChangedEvent;
import org.hyperdrive.api.core.event.PropertyChangedHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

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
		final ManagedDisplay managedDisplay = new BaseManagedDisplay(
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
