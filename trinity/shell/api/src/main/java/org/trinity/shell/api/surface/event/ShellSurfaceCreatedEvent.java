/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.api.surface.event;

import org.trinity.foundation.api.display.DisplayArea;
import org.trinity.shell.api.surface.ShellSurface;

import com.google.common.eventbus.EventBus;
import com.google.inject.name.Named;

/**
 * 
 * Emitted by an {@link EventBus} which is {@link Named} "shellEventBus" to
 * indicate that a new client {@link ShellSurface} was created. A client
 * {@code ShellSurface} is a surface that maps to a native {@link DisplayArea}
 * that was created by an external program.
 * 
 */
public class ShellSurfaceCreatedEvent {

	private final ShellSurface client;

	/**
	 * Construct a new event to indicate that a new {@code ShellSurface} has
	 * been created.
	 * 
	 * @param client
	 *            The client {@link ShellSurface}.
	 */
	public ShellSurfaceCreatedEvent(final ShellSurface client) {
		this.client = client;
	}

	public ShellSurface getClient() {
		return this.client;
	}
}
