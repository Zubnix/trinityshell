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

import org.trinity.display.x11.api.core.XResourceHandle;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

// TODO documentation
/**
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XResourceHandleImpl implements XResourceHandle {

	private final Integer handle;

	/**
	 * @param handle
	 */
	@Inject
	public XResourceHandleImpl(@Assisted final Object handle) {
		this.handle = (Integer) handle;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof XResourceHandleImpl) {
			final XResourceHandleImpl otherObj = (XResourceHandleImpl) obj;
			return otherObj.getNativeHandle().equals(getNativeHandle());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getNativeHandle().intValue();
	}

	/**
	 * The native id of the X resource.
	 * 
	 * @return
	 */
	@Override
	public Integer getNativeHandle() {
		return this.handle;
	}

	@Override
	public String toString() {
		return String.format(	"%s: %d",
								XResourceHandleImpl.class.getSimpleName(),
								getNativeHandle());
	}
}