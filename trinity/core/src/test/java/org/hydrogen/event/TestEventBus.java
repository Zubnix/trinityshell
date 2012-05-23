package org.hydrogen.event;

import org.junit.Test;
import org.mockito.Mockito;
import org.trinity.core.event.api.Event;
import org.trinity.core.event.api.EventBus;
import org.trinity.core.event.api.EventHandler;
import org.trinity.core.event.api.Type;
import org.trinity.core.event.api.TypeBoundEventHandler;

public class TestEventBus extends EventBus {

	@SuppressWarnings("unchecked")
	@Test
	public void testEventPropagation() {
		final Type type = Mockito.mock(Type.class);
		final Event<Type> event = Mockito.mock(Event.class);
		Mockito.when(event.getType()).thenReturn(type);

		final EventHandler<Event<Type>> eventHandler = Mockito
				.mock(EventHandler.class);
		final TypeBoundEventHandler<Type, Event<Type>> typedEventHandler = Mockito
				.mock(TypeBoundEventHandler.class);
		Mockito.when(typedEventHandler.getType()).thenReturn(type);

		final EventBus eventBus = new EventBus();
		eventBus.addEventHandler(eventHandler, type);
		eventBus.addTypedEventHandler(typedEventHandler);

		eventBus.fireEvent(event);

		eventBus.removeEventHandler(typedEventHandler, type);

		eventBus.fireEvent(event);

		Mockito.verify(eventHandler, Mockito.times(2)).handleEvent(event);
		Mockito.verify(typedEventHandler, Mockito.times(1)).handleEvent(event);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testNestedEventHandlerAdd() {
		final Type type = Mockito.mock(Type.class);
		final Event<Type> event = Mockito.mock(Event.class);
		Mockito.when(event.getType()).thenReturn(type);

		final EventHandler<Event<Type>> nestedEventHandler = Mockito
				.mock(EventHandler.class);
		final TypeBoundEventHandler<Type, Event<Type>> typedEventHandler = Mockito
				.mock(TypeBoundEventHandler.class);
		Mockito.when(typedEventHandler.getType()).thenReturn(type);

		final EventBus eventBus = new EventBus();

		final EventHandler<Event<Type>> eventHandler = new EventHandler<Event<Type>>() {
			@Override
			public void handleEvent(final Event<Type> event) {
				eventBus.addEventHandler(nestedEventHandler, type);
			}
		};

		eventBus.addEventHandler(eventHandler, type);

		// should add nest
		eventBus.fireEvent(event);

		// should call nest
		eventBus.fireEvent(event);

		Mockito.verify(nestedEventHandler, Mockito.times(1)).handleEvent(event);
	}
}
