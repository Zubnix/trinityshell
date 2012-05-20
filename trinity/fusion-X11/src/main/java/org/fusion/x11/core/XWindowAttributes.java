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

import org.hydrogen.display.api.PlatformRenderAreaAttributes;

/**
 * An <code>XWindowAttributes</code> groups a number of native X window related
 * attributes from an <code>XWindow</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XWindowAttributes implements PlatformRenderAreaAttributes {

	private final boolean overrideRedirect;
	private final int viewableMapState;
	private final long visualPeer;

	/**
	 * Create a new <code>XWindowAttributes</code> with the given map state and
	 * the given override redirect indication.
	 * 
	 * @param mapState
	 *            An X native map state code.
	 * @param overrideRedirect
	 *            true if override redirect is enabled, false if not.
	 */
	public XWindowAttributes(final int mapState,
			final boolean overrideRedirect, final long visualPeer) {
		this.overrideRedirect = overrideRedirect;
		this.viewableMapState = mapState;
		this.visualPeer = visualPeer;
	}

	public long getVisualPeer() {
		return this.visualPeer;
	}

	@Override
	public boolean isOverrideRedirect() {
		return this.overrideRedirect;
	}

	@Override
	public boolean isUnmapped() {
		final boolean returnboolean = (this.viewableMapState == XProtocolConstants.IS_UNMAPPED);
		return returnboolean;
	}

	@Override
	public boolean isUnviewable() {
		final boolean returnboolean = (this.viewableMapState == XProtocolConstants.IS_UNVIEWABLE);
		return returnboolean;
	}

	@Override
	public boolean isViewable() {
		final boolean returnboolean = (this.viewableMapState == XProtocolConstants.IS_VIEWABLE);
		return returnboolean;
	}

}
