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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.trinity.core.event.api.EventBus;

/**
 * An <code>XResourcesRegistry</code> keeps track of registered
 * <code>XResource</code>s by their <code>XID</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class XResourcesRegistry extends EventBus {
	private final Map<XID, XResource> registeredResources = Collections
			.synchronizedMap(new HashMap<XID, XResource>());

	/**
	 * Check if an <code>XResource</code> is registered with the given
	 * <code>XID</code> on this <code>XResourcesRegistry</code>.
	 * 
	 * @param xid
	 *            An {@link XID}.
	 * @return True if an <code>XResource</code> is registered with the given
	 *         <code>XID</code>, false if not.
	 */
	public boolean containsKey(final XID xid) {
		synchronized (this.registeredResources) {
			final boolean returnboolean = this.registeredResources
					.containsKey(xid);
			return returnboolean;
		}
	}

	/**
	 * Query an <code>XResource</code> with the given <code>XID</code>. If no
	 * <code>XResource</code> is found an <code>AssertionError</code> is thrown.
	 * 
	 * @param xid
	 *            An {@link XID}.
	 * @return An {@link XResource}.
	 */
	public XResource get(final XID xid) {
		synchronized (this.registeredResources) {
			if (!this.registeredResources.containsKey(xid)) {
				throw new AssertionError(String.format(
						"Trying to use not registered resource: %d", xid
								.getResourceHandle().getNativeHandle()));
			}

			XResource returnXResource = null;

			returnXResource = this.registeredResources.get(xid);

			if (returnXResource == null) {
				throw new AssertionError(
						"Trying to use not registered resource");
			}

			return returnXResource;
		}
	}

	/**
	 * Register an <code>XResource</code> with this
	 * <code>XResourceRegistry</code>. If the given <code>XResource</code> is
	 * already registered, an <code>AssertionError</code> is thrown.
	 * 
	 * @param xobject
	 *            An {@link XResource}.
	 */
	public void register(final XResource xobject) {
		synchronized (this.registeredResources) {
			final XID xid = xobject.getDisplayResourceHandle();

			if (this.registeredResources.containsKey(xid)) {
				throw new AssertionError(
						"Trying to register an already registered resource.");
			}
			this.registeredResources.put(xid, xobject);
		}

		// TODO is this ok?
		// fireEvent(new XResourceEvent(XResourceEvent.NEW_RESOURCE, xobject));

	}

	@Override
	public String toString() {
		return this.registeredResources.keySet().toString();
	}

	/**
	 * Remove an <code>XResource</code> registration from this
	 * <code>XResourceRegistry</code> by the given <code>XID</code>.
	 * 
	 * @param xid
	 *            An <code>XID</code>.
	 */
	public void unregister(final XID xid) {
		synchronized (this.registeredResources) {
			if (this.registeredResources.containsKey(xid) == false) {
				throw new AssertionError(
						"Trying to use not registered resource");
			}
			this.registeredResources.remove(xid);
		}
	}

	/**
	 * A client XWindow. If no client XWindow is found with the given XID a new
	 * one will be created.
	 * 
	 * @param xid
	 * @return
	 */
	public XWindow getClientXWindow(final XID xid) {
		XWindow xWindow;
		if (containsKey(xid)) {
			final XResource xResource = get(xid);
			if (xResource instanceof XWindow) {
				xWindow = (XWindow) xResource;
			} else {
				throw new IllegalArgumentException(String.format(
						"Given XID : %s is not an X window.", xid));
			}
		} else {
			xWindow = new XWindow(xid);
		}
		return xWindow;
	}
}
