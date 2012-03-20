package org.hyperdrive.core;

import org.hydrogen.api.display.event.ButtonNotifyEvent;
import org.hydrogen.api.display.event.ConfigureRequestEvent;
import org.hydrogen.api.display.event.DisplayEventType;
import org.hydrogen.api.display.event.MapRequestEvent;
import org.hydrogen.api.event.EventManager;
import org.hyperdrive.DummyEnv;
import org.hyperdrive.api.core.ManagedDisplay;
import org.hyperdrive.api.core.RenderArea;
import org.hyperdrive.api.core.event.ClientCreatedHandler;
import org.hyperdrive.api.core.event.ConfigureRequestHandler;
import org.hyperdrive.api.core.event.MapRequestHandler;
import org.hyperdrive.api.core.event.MouseButtonPressedHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class TestBaseManagedDisplay {

	private DummyEnv dummyEnv;

	@Before
	public void setup() {
		this.dummyEnv = new DummyEnv();
		this.dummyEnv.setup();
	}

	@Test
	public void testShutDown() {
		// TODO
		throw new RuntimeException("Not yet implemented");
	}

	@Test
	public void testAddDisplayEventHandler() throws InterruptedException {
		final ManagedDisplay baseManagedDisplay = new BaseManagedDisplay(
				this.dummyEnv.displayMock);

		final MapRequestEvent c0Map = Mockito.mock(MapRequestEvent.class);
		Mockito.when(c0Map.getType()).thenReturn(DisplayEventType.MAP_REQUEST);
		final ConfigureRequestEvent c0Req = Mockito
				.mock(ConfigureRequestEvent.class);
		Mockito.when(c0Req.getType()).thenReturn(
				DisplayEventType.CONFIGURE_REQUEST);
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

		Mockito.when(this.dummyEnv.displayMock.getEventFromMasterQueue())
				.thenReturn(c0Req, c0Map, null);

		baseManagedDisplay.addDisplayEventHandler(mapRequestHandler);
		baseManagedDisplay.addDisplayEventHandler(configureRequestHandler);
		baseManagedDisplay.addDisplayEventHandler(buttonPressedHandler);

		baseManagedDisplay.deliverNextDisplayEvent(false);
		baseManagedDisplay.deliverNextDisplayEvent(false);
		baseManagedDisplay.deliverNextDisplayEvent(false);

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
	public void testRemoveDisplayEventHandler() throws InterruptedException {
		final ManagedDisplay baseManagedDisplay = new BaseManagedDisplay(
				this.dummyEnv.displayMock);

		final MapRequestEvent c0Map = Mockito.mock(MapRequestEvent.class);
		Mockito.when(c0Map.getType()).thenReturn(DisplayEventType.MAP_REQUEST);
		Mockito.when(c0Map.getEventSource()).thenReturn(this.dummyEnv.c0);

		final ConfigureRequestEvent c0Req = Mockito
				.mock(ConfigureRequestEvent.class);
		Mockito.when(c0Req.getEventSource()).thenReturn(this.dummyEnv.c0);
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

		Mockito.when(this.dummyEnv.displayMock.getEventFromMasterQueue())
				.thenReturn(c0Req, c0Map, null);

		baseManagedDisplay.addDisplayEventHandler(mapRequestHandler);
		baseManagedDisplay.addDisplayEventHandler(configureRequestHandler);

		baseManagedDisplay.removeDisplayEventHandler(configureRequestHandler);

		baseManagedDisplay.deliverNextDisplayEvent(false);
		baseManagedDisplay.deliverNextDisplayEvent(false);
		baseManagedDisplay.deliverNextDisplayEvent(false);

		Mockito.verify(mapRequestHandler, Mockito.times(1)).handleEvent(c0Map);

		Mockito.verify(configureRequestHandler, Mockito.never()).handleEvent(
				c0Req);
	}

	@Test
	public void testAddDisplayEventManager() throws InterruptedException {

		final ManagedDisplay baseManagedDisplay = new BaseManagedDisplay(
				this.dummyEnv.displayMock);

		// stub dummy display events
		final MapRequestEvent c0Map = Mockito.mock(MapRequestEvent.class);
		final ConfigureRequestEvent c1Req = Mockito
				.mock(ConfigureRequestEvent.class);
		final ConfigureRequestEvent c0Req = Mockito
				.mock(ConfigureRequestEvent.class);
		final MapRequestEvent c1Map = Mockito.mock(MapRequestEvent.class);

		Mockito.when(c0Map.getEventSource()).thenReturn(this.dummyEnv.c0);
		Mockito.when(c0Map.getType()).thenReturn(DisplayEventType.MAP_REQUEST);

		Mockito.when(c1Req.getEventSource()).thenReturn(this.dummyEnv.c1);
		Mockito.when(c1Req.getType()).thenReturn(
				DisplayEventType.CONFIGURE_REQUEST);

		Mockito.when(c0Req.getEventSource()).thenReturn(this.dummyEnv.c0);
		Mockito.when(c0Req.getType()).thenReturn(
				DisplayEventType.CONFIGURE_REQUEST);

		Mockito.when(c1Map.getEventSource()).thenReturn(this.dummyEnv.c1);
		Mockito.when(c1Map.getType()).thenReturn(DisplayEventType.MAP_REQUEST);

		Mockito.when(this.dummyEnv.displayMock.getEventFromMasterQueue())
				.thenReturn(c0Map, c1Req, c0Req, c1Map, null);

		// add stubbed eventmanagers that should receive the stubbed display
		// events.
		final EventManager c0EventManagerMock = Mockito
				.mock(EventManager.class);
		final EventManager c1EventManagerMock = Mockito
				.mock(EventManager.class);
		baseManagedDisplay.addDisplayEventManager(c0EventManagerMock,
				this.dummyEnv.c0);
		baseManagedDisplay.addDisplayEventManager(c1EventManagerMock,
				this.dummyEnv.c1);

		baseManagedDisplay.deliverNextDisplayEvent(false);
		baseManagedDisplay.deliverNextDisplayEvent(false);
		baseManagedDisplay.deliverNextDisplayEvent(false);
		baseManagedDisplay.deliverNextDisplayEvent(false);

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

	public void testDeliverNextDisplayEvent() {
		// trivial. not testing this. other tests will fail anyway if this
		// doesn't work.
	}

	@Test
	public void testAddClientCreatedListener() {
		final ManagedDisplay managedDisplay = new BaseManagedDisplay(
				this.dummyEnv.displayMock);

		final MapRequestEvent c0Map = Mockito.mock(MapRequestEvent.class);
		Mockito.when(c0Map.getEventSource()).thenReturn(this.dummyEnv.c0);
		Mockito.when(c0Map.getType()).thenReturn(DisplayEventType.MAP_REQUEST);

		Mockito.when(this.dummyEnv.displayMock.getEventFromMasterQueue())
				.thenReturn(c0Map);

		final ClientCreatedHandler clientCreatedHandler = Mockito
				.mock(ClientCreatedHandler.class);
		managedDisplay.addClientCreatedHandler(clientCreatedHandler);
		managedDisplay.deliverNextDisplayEvent(true);

		Mockito.verify(clientCreatedHandler, Mockito.times(1))
				.handleCreatedClient((RenderArea) Matchers.any());
	}

	@Test
	public void testRemoveClientCreatedListener() {
		final ManagedDisplay managedDisplay = new BaseManagedDisplay(
				this.dummyEnv.displayMock);

		final MapRequestEvent c0Map = Mockito.mock(MapRequestEvent.class);
		Mockito.when(c0Map.getEventSource()).thenReturn(this.dummyEnv.c0);
		Mockito.when(c0Map.getType()).thenReturn(DisplayEventType.MAP_REQUEST);

		final MapRequestEvent c1Map = Mockito.mock(MapRequestEvent.class);
		Mockito.when(c1Map.getEventSource()).thenReturn(this.dummyEnv.c1);
		Mockito.when(c1Map.getType()).thenReturn(DisplayEventType.MAP_REQUEST);

		Mockito.when(this.dummyEnv.displayMock.getEventFromMasterQueue())
				.thenReturn(c0Map, c1Map);

		final ClientCreatedHandler clientCreatedHandler = Mockito
				.mock(ClientCreatedHandler.class);
		managedDisplay.addClientCreatedHandler(clientCreatedHandler);
		managedDisplay.deliverNextDisplayEvent(true);
		managedDisplay.removeClientCreatedHandler(clientCreatedHandler);
		managedDisplay.deliverNextDisplayEvent(true);

		Mockito.verify(clientCreatedHandler, Mockito.times(1))
				.handleCreatedClient((RenderArea) Matchers.any());
	}
}
