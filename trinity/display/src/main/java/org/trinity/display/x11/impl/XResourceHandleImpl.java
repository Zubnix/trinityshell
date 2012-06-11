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

import java.util.HashMap;
import java.util.Map;

import org.trinity.display.x11.api.XResourceHandle;

// TODO documentation
/**
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XResourceHandleImpl implements XResourceHandle {

	private final Integer handle;

	private static final Map<Integer, XResourceHandle> VALUES = new HashMap<Integer, XResourceHandle>();

	/**
	 * @param handle
	 */
	public XResourceHandleImpl(final int handle) {
		this.handle = Integer.valueOf(handle);
		XResourceHandleImpl.VALUES.put(this.handle, this);
	}

	/**
	 * The native id of the X resource.
	 * 
	 * @return
	 */
	public Integer getNativeHandle() {
		return this.handle;
	}

	/**
	 * Create a new cached <code>XResourceHandle</code> or get a reference to an
	 * already created <code>XResourceHandle</code>.
	 * 
	 * @param xResourceId
	 * @return
	 */
	public static XResourceHandle valueOf(final int xResourceId) {
		if (XResourceHandleImpl.VALUES.containsKey(xResourceId)) {
			return XResourceHandleImpl.VALUES.get(xResourceId);
		} else {
			return new XResourceHandleImpl(xResourceId);
		}
	}

	@Override
	public String toString() {
		return String.format(	"%s: %d",
								XResourceHandleImpl.class.getSimpleName(),
								getNativeHandle());
	}
}