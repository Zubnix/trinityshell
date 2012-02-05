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

import org.hydrogen.displayinterface.DisplayResourceHandle;

/**
 * An <code>XID</code> identifies an X resource by combining the
 * <code>XDisplay</code> it belongs to and the given
 * <code>XResourceHandle</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class XID implements DisplayResourceHandle {
	private final XDisplay display;
	private final int hashCode;
	private final XResourceHandle xResourceId;

	/**
	 * Create a new <code>XID</code> with the given <code>XDisplay</code> and
	 * the given x resource id.
	 * <p>
	 * The created <code>XID</code> will be equal to, and return the same hash
	 * code as another <code>XID</code> that was created with an equal
	 * <code>XCoreDisply</code> and an equal x resource id.
	 * 
	 * @param display
	 * @param xResourceId
	 */
	public XID(final XDisplay display, final XResourceHandle xResourceId) {
		this.display = display;
		this.xResourceId = xResourceId;
		{
			long hash = 23;
			hash = hash * 31l + this.display.getNativePeer().hashCode();
			hash = hash * 31l + this.xResourceId.hashCode();
			this.hashCode = (int) hash;
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof XID) {
			final XID otherXID = (XID) obj;
			return (otherXID.getDisplay().getNativePeer()
					.equals(getDisplay().getNativePeer()) && otherXID
					.getResourceHandle().equals(getResourceHandle()));
		} else {
			return super.equals(obj);
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getDisplay() + " : " + getResourceHandle();
	}

	@Override
	public XDisplay getDisplay() {
		return this.display;
	}

	@Override
	public XResourceHandle getResourceHandle() {
		return this.xResourceId;
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}
}
