/*
 * This file is part of Fusion-X11. Fusion-X11 is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Fusion-X11 is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Fusion-X11. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.display.x11.core.impl;

import org.trinity.display.x11.core.api.XCall;
import org.trinity.display.x11.core.api.XCaller;
import org.trinity.display.x11.core.api.XConnection;
import org.trinity.foundation.input.api.Mouse;
import org.trinity.foundation.shared.geometry.api.Coordinates;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

/**
 * An <code>XMouse</code> represents an X mouse pointer on an X display server.
 * It holds the location of an X mouse pointer relative to the root window.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
@Singleton
public final class MouseImpl implements Mouse {

	private final XConnection<Long> xConnection;
	private final XCaller xCaller;
	private final XCall<Coordinates, Long, Void> mouseRootCoordinates;

	@Inject
	public MouseImpl(	final XConnection<Long> xConnection,
						final XCaller xCaller,
						@Named("mouseRootCoordinates") final XCall<Coordinates, Long, Void> mouseRootCoordinates) {
		this.xConnection = xConnection;
		this.xCaller = xCaller;
		this.mouseRootCoordinates = mouseRootCoordinates;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.core.input.api.Mouse#getRootCoordinates()
	 */
	@Override
	public Coordinates getRootCoordinates() {

		return this.xCaller.doCall(	this.mouseRootCoordinates,
									this.xConnection.getConnectionReference());
	}
}
