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
package org.trinity.display.x11.impl;

import org.trinity.display.x11.api.core.XResource;

/**
 * An <code>XResource</code> is a resource (window, pixmap, ...) that lives on
 * an X server. An <code>XResource</code> is identified by an <code>XID</code>.
 * This <code>XID</code> holds a reference to the native resource id and the
 * display that issued the id.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XResourceImpl implements XResource {
	private final XResourceHandleImpl xResourceHandle;

	/**
	 * @param xid
	 */
	protected XResourceImpl(final XResourceHandleImpl xResourceHandle) {
		this.xResourceHandle = xResourceHandle;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.XResource#getResourceHandle()
	 */
	@Override
	public XResourceHandleImpl getResourceHandle() {
		return this.xResourceHandle;
	}

	@Override
	public String toString() {
		final String toString = String
				.format("Resource class: <%s> - id: <%s>", getClass()
						.getSimpleName(), getResourceHandle());
		return toString;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof XResourceImpl) {
			final XResourceImpl otherObj = (XResourceImpl) obj;
			return otherObj.getResourceHandle().equals(getResourceHandle());
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getResourceHandle().hashCode();
	}
}
