package org.hyperdrive.api.core;

import org.hydrogen.api.display.Display;
import org.hydrogen.api.display.PlatformRenderArea;
import org.hydrogen.api.display.event.DisplayEventSource;
import org.hydrogen.api.event.EventManager;
import org.hydrogen.api.paint.PainterFactory;
import org.hyperdrive.api.widget.Widget;
import org.hyperdrive.input.ManagedKeyboard;
import org.hyperdrive.input.ManagedMouse;

public interface ManagedDisplay {

	Display getDisplay();

	ManagedKeyboard getManagedKeyboard();

	ManagedMouse getManagedMouse();

	PainterFactory getPainterFactory();

	void shutDown();

	void start();

	Widget getRoot();

	RenderArea getClientWindow(PlatformRenderArea platformRenderArea);

	void addEventManagerForDisplayEventSource(EventManager manager,
			DisplayEventSource forSource);

	// this method is not strictly needed if we implement the backing
	// eventmanager
	// collection as a weak collection.
	// void removeDisplayEventManager(EventManager manager);

	// Collection<EventManager> getEventManagersForDisplayEventSource(
	// DisplayEventSource forSource);
}
