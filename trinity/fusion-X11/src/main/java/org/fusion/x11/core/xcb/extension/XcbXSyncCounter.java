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
package org.fusion.x11.core.xcb.extension;

import org.fusion.x11.core.XID;
import org.fusion.x11.core.XResource;
import org.fusion.x11.core.extension.XSyncSystemCounter;
 

// TODO documentation
// currently unused
/**
 * @author Erik De Rijcke
 * @since 1.1
 */
public class XcbXSyncCounter extends XResource implements XSyncSystemCounter {

	private final String name;

	protected XcbXSyncCounter(final XID xid, final String name)
			   {
		super(xid);
		this.name = name;
	}

	@Override
	public long getCounter() {
		// TODO XSyncQueryCounter
		return 0;
	}

	@Override
	public void setCounter(final long counter) {
		// TODO XSyncChangeCounter

	}

	@Override
	protected void finalize() throws Throwable {
		// TODO XSyncDestroyCounter
		super.finalize();
	}

	@Override
	public String getName() {
		return this.name;
	}
}
