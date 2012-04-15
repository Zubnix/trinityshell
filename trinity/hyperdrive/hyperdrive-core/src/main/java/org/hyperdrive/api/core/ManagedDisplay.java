/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
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
