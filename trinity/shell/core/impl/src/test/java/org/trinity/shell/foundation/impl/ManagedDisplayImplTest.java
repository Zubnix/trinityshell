package org.trinity.shell.foundation.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.trinity.core.event.api.EventManager;
import org.trinity.foundation.display.api.event.ButtonNotifyEvent;
import org.trinity.foundation.display.api.event.ConfigureRequestEvent;
import org.trinity.foundation.display.api.event.DisplayEventType;
import org.trinity.foundation.display.api.event.MapRequestEvent;
import org.trinity.shell.MockedEnv;
import org.trinity.shell.core.api.ManagedDisplay;
import org.trinity.shell.core.api.RenderArea;
import org.trinity.shell.core.api.event.ClientCreatedHandler;
import org.trinity.shell.core.api.event.ConfigureRequestHandler;
import org.trinity.shell.core.api.event.MapRequestHandler;
import org.trinity.shell.core.api.event.MouseButtonPressedHandler;
import org.trinity.shell.foundation.impl.ManagedDisplayImpl;

public class ManagedDisplayImplTest {

	private MockedEnv mockedEnv;

	@Before
	public void setup() {
		this.mockedEnv = new MockedEnv();
		this.mockedEnv.setUp();
	}

	@Test
	public void testStart() {
		// TODO
		throw new RuntimeException("Not yet implemented");
	}

	@Test
	public void testShutDown() {
		// TODO
		throw new RuntimeException("Not yet implemented");
	}

	@Test
	public void testAddDisplayEventHandler() {
		final ManagedDisplay baseManagedDisplay = new ManagedDisplayImpl(
				this.mockedEnv.displayMock);

		final MapRequestEvent c0Map = Mockito.mock(MapRequestEvent.class);
		Mockito.when(c0Map.getType()).thenReturn(DisplayEventType.MAP_REQUEST);
		Mockito.when(c0Map.getEventSource()).thenReturn(this.mockedEnv.c0);

		final ConfigureRequestEvent c0Req = Mockito
				.mock(ConfigureRequestEvent.class);
		Mockito.when(c0Req.getType()).thenReturn(
				DisplayEventType.CONFIGURE_REQUEST);
		Mockito.when(c0Req.getEventSource()).thenReturn(this.mockedEnv.c0);

		final MouseButtonPressedHandler buttonPressedHandler = Mockito
				.mock(MouseButtonPressedHandler.class);
		Mockito.when(buttonPressedHandler.getType()).thenReturn(
				DisplayEventType.BUTTON_PRESSED);

		final MapRequestHandler mapRequestHandler = Mockito
				.mock(MapRequestHandler.class);
		Mockito.when(mapRequestHandler.getType()).thenReturn(
				DisplayEventType.MAP_REQUEST);

		final ConfigureRequestHandler configureRequestHandler = Mockito
				.mock(ConfigureRequestHandler.class);
		Mockito.when(configureRequestHandler.getType()).thenReturn(
				DisplayEventType.CONFIGURE_REQUEST);

		Mockito.when(this.mockedEnv.displayMock.getNextDisplayEvent())
				.thenReturn(c0Req, c0Map, null);

		baseManagedDisplay.addDisplayEventHandler(mapRequestHandler);
		baseManagedDisplay.addDisplayEventHandler(configureRequestHandler);
		baseManagedDisplay.addDisplayEventHandler(buttonPressedHandler);

		baseManagedDisplay.postNextDisplayEvent(false);
		baseManagedDisplay.postNextDisplayEvent(false);
		baseManagedDisplay.postNextDisplayEvent(false);

		final InOrder eventHandlersOrder = Mockito.inOrder(mapRequestHandler,
				configureRequestHandler);

		eventHandlersOrder.verify(configureRequestHandler, Mockito.times(1))
				.handleEvent(c0Req);
		eventHandlersOrder.verify(mapRequestHandler, Mockito.times(1))
				.handleEvent(c0Map);
		Mockito.verify(buttonPressedHandler, Mockito.never()).handleEvent(
				(ButtonNotifyEvent) Matchers.any());
	}

	@Test
	public void testRemoveDisplayEventHandler() {
		final ManagedDisplayImpl baseManagedDisplay = new ManagedDisplayImpl(
				this.mockedEnv.displayMock);

		final MapRequestEvent c0Map = Mockito.mock(MapRequestEvent.class);
		Mockito.when(c0Map.getType()).thenReturn(DisplayEventType.MAP_REQUEST);
		Mockito.when(c0Map.getEventSource()).thenReturn(this.mockedEnv.c0);

		final ConfigureRequestEvent c0Req = Mockito
				.mock(ConfigureRequestEvent.class);
		Mockito.when(c0Req.getEventSource()).thenReturn(this.mockedEnv.c0);
		Mockito.when(c0Req.getType()).thenReturn(
				DisplayEventType.CONFIGURE_REQUEST);

		final MapRequestHandler mapRequestHandler = Mockito
				.mock(MapRequestHandler.class);
		Mockito.when(mapRequestHandler.getType()).thenReturn(
				DisplayEventType.MAP_REQUEST);

		final ConfigureRequestHandler configureRequestHandler = Mockito
				.mock(ConfigureRequestHandler.class);
		Mockito.when(configureRequestHandler.getType()).thenReturn(
				DisplayEventType.CONFIGURE_REQUEST);

		Mockito.when(this.mockedEnv.displayMock.getNextDisplayEvent())
				.thenReturn(c0Req, c0Map, null);

		baseManagedDisplay.addDisplayEventHandler(mapRequestHandler);
		baseManagedDisplay.addDisplayEventHandler(configureRequestHandler);

		baseManagedDisplay.removeDisplayEventHandler(configureRequestHandler);

		baseManagedDisplay.postNextDisplayEvent(false);
		baseManagedDisplay.postNextDisplayEvent(false);
		baseManagedDisplay.postNextDisplayEvent(false);

		Mockito.verify(mapRequestHandler, Mockito.times(1)).handleEvent(c0Map);

		Mockito.verify(configureRequestHandler, Mockito.never()).handleEvent(
				c0Req);
	}

	@Test
	public void testAddDisplayEventManager() {

		final ManagedDisplay managedDisplay = new ManagedDisplayImpl(
				this.mockedEnv.displayMock);

		// stub dummy display events
		final MapRequestEvent c0Map = Mockito.mock(MapRequestEvent.class);
		final ConfigureRequestEvent c1Req = Mockito
				.mock(ConfigureRequestEvent.class);
		final ConfigureRequestEvent c0Req = Mockito
				.mock(ConfigureRequestEvent.class);
		final MapRequestEvent c1Map = Mockito.mock(MapRequestEvent.class);

		Mockito.when(c0Map.getEventSource()).thenReturn(this.mockedEnv.c0);
		Mockito.when(c0Map.getType()).thenReturn(DisplayEventType.MAP_REQUEST);

		Mockito.when(c1Req.getEventSource()).thenReturn(this.mockedEnv.c1);
		Mockito.when(c1Req.getType()).thenReturn(
				DisplayEventType.CONFIGURE_REQUEST);

		Mockito.when(c0Req.getEventSource()).thenReturn(this.mockedEnv.c0);
		Mockito.when(c0Req.getType()).thenReturn(
				DisplayEventType.CONFIGURE_REQUEST);

		Mockito.when(c1Map.getEventSource()).thenReturn(this.mockedEnv.c1);
		Mockito.when(c1Map.getType()).thenReturn(DisplayEventType.MAP_REQUEST);

		Mockito.when(this.mockedEnv.displayMock.getNextDisplayEvent())
				.thenReturn(c0Map, c1Req, c0Req, c1Map, null);

		// add stubbed eventmanagers that should receive the stubbed display
		// events.
		final EventManager c0EventManagerMock = Mockito
				.mock(EventManager.class);
		final EventManager c1EventManagerMock = Mockito
				.mock(EventManager.class);
		managedDisplay.registerEventBusForSource(c0EventManagerMock,
				this.mockedEnv.c0);
		managedDisplay.registerEventBusForSource(c1EventManagerMock,
				this.mockedEnv.c1);

		managedDisplay.postNextDisplayEvent(false);
		managedDisplay.postNextDisplayEvent(false);
		managedDisplay.postNextDisplayEvent(false);
		managedDisplay.postNextDisplayEvent(false);

		final InOrder eventManagersOrder = Mockito.inOrder(c0EventManagerMock,
				c1EventManagerMock);

		eventManagersOrder.verify(c0EventManagerMock, Mockito.times(1))
				.fireEvent(c0Map);

		eventManagersOrder.verify(c1EventManagerMock, Mockito.times(1))
				.fireEvent(c1Req);

		eventManagersOrder.verify(c0EventManagerMock, Mockito.times(1))
				.fireEvent(c0Req);

		eventManagersOrder.verify(c1EventManagerMock, Mockito.times(1))
				.fireEvent(c1Map);
	}

	// TODO void removeDisplayEventManager(..)?
	// @Test
	// public void removeDisplayEventManager() {
	// final ManagedDisplay managedDisplay = new BaseManagedDisplay(
	// this.dummyEnv.displayMock);
	//
	// // stub dummy display events
	// final MapRequestEvent c0Map = Mockito.mock(MapRequestEvent.class);
	// final ConfigureRequestEvent c0Req = Mockito
	// .mock(ConfigureRequestEvent.class);
	//
	// Mockito.when(c0Map.getEventSource()).thenReturn(this.dummyEnv.c0);
	// Mockito.when(c0Map.getType()).thenReturn(DisplayEventType.MAP_REQUEST);
	//
	// Mockito.when(c0Req.getEventSource()).thenReturn(this.dummyEnv.c0);
	// Mockito.when(c0Req.getType()).thenReturn(
	// DisplayEventType.CONFIGURE_REQUEST);
	//
	// Mockito.when(this.dummyEnv.displayMock.getEventFromMasterQueue())
	// .thenReturn(c0Map, c0Req);
	//
	// final EventManager c0EventManagerMock = Mockito
	// .mock(EventManager.class);
	// managedDisplay.addDisplayEventManager(c0EventManagerMock,
	// this.dummyEnv.c0);
	//
	// managedDisplay.deliverNextDisplayEvent(false);
	//
	// }

	public void testDeliverNextDisplayEvent() {
		// trivial. not testing this. other tests will fail anyway if this
		// doesn't work.
	}

	@Test
	public void testAddClientCreatedListener() {
		final ManagedDisplay managedDisplay = new ManagedDisplayImpl(
				this.mockedEnv.displayMock);

		final MapRequestEvent c0Map = Mockito.mock(MapRequestEvent.class);
		Mockito.when(c0Map.getEventSource()).thenReturn(this.mockedEnv.c0);
		Mockito.when(c0Map.getType()).thenReturn(DisplayEventType.MAP_REQUEST);

		Mockito.when(this.mockedEnv.displayMock.getNextDisplayEvent())
				.thenReturn(c0Map);

		final ClientCreatedHandler clientCreatedHandler = Mockito
				.mock(ClientCreatedHandler.class);
		managedDisplay.addClientCreatedHandler(clientCreatedHandler);
		managedDisplay.postNextDisplayEvent(true);

		Mockito.verify(clientCreatedHandler, Mockito.times(1))
				.handleCreatedClient((RenderArea) Matchers.any());
	}

	@Test
	public void testRemoveClientCreatedListener() {
		final ManagedDisplay managedDisplay = new ManagedDisplayImpl(
				this.mockedEnv.displayMock);

		final MapRequestEvent c0Map = Mockito.mock(MapRequestEvent.class);
		Mockito.when(c0Map.getEventSource()).thenReturn(this.mockedEnv.c0);
		Mockito.when(c0Map.getType()).thenReturn(DisplayEventType.MAP_REQUEST);

		final MapRequestEvent c1Map = Mockito.mock(MapRequestEvent.class);
		Mockito.when(c1Map.getEventSource()).thenReturn(this.mockedEnv.c1);
		Mockito.when(c1Map.getType()).thenReturn(DisplayEventType.MAP_REQUEST);

		Mockito.when(this.mockedEnv.displayMock.getNextDisplayEvent())
				.thenReturn(c0Map, c1Map);

		final ClientCreatedHandler clientCreatedHandler = Mockito
				.mock(ClientCreatedHandler.class);
		managedDisplay.addClientCreatedHandler(clientCreatedHandler);
		managedDisplay.postNextDisplayEvent(true);
		managedDisplay.removeClientCreatedHandler(clientCreatedHandler);
		managedDisplay.postNextDisplayEvent(true);

		Mockito.verify(clientCreatedHandler, Mockito.times(1))
				.handleCreatedClient((RenderArea) Matchers.any());
	}
}
