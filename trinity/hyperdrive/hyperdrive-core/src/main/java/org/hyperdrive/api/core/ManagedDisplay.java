package org.hyperdrive.api.core;

import org.hydrogen.api.display.Display;
import org.hydrogen.api.display.event.DisplayEvent;
import org.hydrogen.api.display.event.DisplayEventSource;
import org.hydrogen.api.event.EventManager;
import org.hydrogen.api.paint.PainterFactory;
import org.hyperdrive.api.core.event.ClientCreatedHandler;
import org.hyperdrive.api.core.event.DisplayEventHandler;
import org.hyperdrive.api.input.ManagedKeyboard;
import org.hyperdrive.api.widget.Widget;
import org.hyperdrive.input.BaseManagedMouse;

public interface ManagedDisplay {

	Display getDisplay();

	ManagedKeyboard getManagedKeyboard();

	BaseManagedMouse getManagedMouse();

	PainterFactory getPainterFactory();

	void shutDown();

	void start();

	Widget getRoot();

	void addDisplayEventHandler(
			DisplayEventHandler<? extends DisplayEvent> displayEventHandler);

	void removeDisplayEventHandler(
			DisplayEventHandler<? extends DisplayEvent> displayEventHandler);

	void addDisplayEventManager(EventManager manager,
			DisplayEventSource forDisplayEventSource);

	// TODO void removeDisplayEventManager(..)?

	void deliverNextDisplayEvent(boolean block);

	void addClientCreatedHandler(ClientCreatedHandler clientCreatedHandler);

	void removeClientCreatedHandler(ClientCreatedHandler clientCreatedHandler);

	// TODO be able to listen for client created events
}
