/*
 * This file is part of Fusion-X11.
 * 
 * Fusion-X11 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.x11.core;

import org.hydrogen.displayinterface.DisplayResource;

/**
 * An <code>XResource</code> is a resource (window, pixmap, ...) that lives on
 * an X server. An <code>XResource</code> is identified by an <code>XID</code>.
 * This <code>XID</code> holds a reference to the native resource id and the
 * display that issued the id.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public abstract class XResource implements DisplayResource {
	private final XID xid;

	/**
	 * 
	 * @param xid
	 * 
	 */
	protected XResource(final XID xid) {
		this.xid = xid;
		xid.getDisplay().getDisplayPlatform().getResourcesRegistry()
				.register(this);
	}

	@Override
	public XID getDisplayResourceHandle() {
		return this.xid;
	}

	@Override
	public String toString() {
		final String toString = String.format(
				"Resource class: <%s> - id: <%s>", getClass().getSimpleName(),
				getDisplayResourceHandle().getResourceHandle());
		return toString;
	}
}
