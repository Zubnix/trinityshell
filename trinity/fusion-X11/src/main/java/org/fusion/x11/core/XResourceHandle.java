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

import java.util.HashMap;
import java.util.Map;

import org.hydrogen.displayinterface.ResourceHandle;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class XResourceHandle implements ResourceHandle {

	private final Long handle;

	private static final Map<Long, XResourceHandle> VALUES = new HashMap<Long, XResourceHandle>();

	/**
	 * 
	 * @param handle
	 */
	private XResourceHandle(final Long handle) {
		this.handle = handle;
		final Long handleValue = Long.valueOf(handle);
		XResourceHandle.VALUES.put(handleValue, this);
	}

	/**
	 * The native id of the X resource.
	 * 
	 * @return
	 */
	public Long getNativeHandle() {
		return this.handle;
	}

	/**
	 * Create a new cached <code>XResourceHandle</code> or get a reference to an
	 * already created <code>XResourceHandle</code>.
	 * 
	 * @param xResourceId
	 * @return
	 */
	public static XResourceHandle valueOf(final Long xResourceId) {
		if (XResourceHandle.VALUES.containsKey(xResourceId)) {
			return XResourceHandle.VALUES.get(xResourceId);
		} else {
			return new XResourceHandle(xResourceId);
		}
	}

	@Override
	public String toString() {
		return String.format("%s: %d", XResourceHandle.class.getSimpleName(),
				getNativeHandle());
	}
}