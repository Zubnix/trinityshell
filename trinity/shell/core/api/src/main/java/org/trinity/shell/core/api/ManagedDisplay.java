/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.core.api;

import org.trinity.foundation.display.api.DisplayServer;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.shell.core.api.event.ClientCreatedHandler;
import org.trinity.shell.core.api.event.DisplayEventHandler;

public interface ManagedDisplay {

	DisplayServer getDisplay();

	void shutDown();

	void start();

	void addDisplayEventHandler(DisplayEventHandler<? extends DisplayEvent> displayEventHandler);

	void removeDisplayEventHandler(DisplayEventHandler<? extends DisplayEvent> displayEventHandler);

	void addDisplayEventManager(EventManager manager,
								DisplayEventSource forDisplayEventSource);

	// TODO void removeDisplayEventManager(..)?

	void deliverNextDisplayEvent(boolean block);

	void addClientCreatedHandler(ClientCreatedHandler clientCreatedHandler);

	void removeClientCreatedHandler(ClientCreatedHandler clientCreatedHandler);

	// TODO be able to listen for client created events
}
