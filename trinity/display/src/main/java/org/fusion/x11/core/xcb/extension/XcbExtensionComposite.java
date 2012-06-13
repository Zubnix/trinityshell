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

import org.trinity.dispay.x11.api.extension.composite.XCompositeUpdateMode;
import org.trinity.dispay.x11.api.extension.composite.XExtensionComposite;
import org.trinity.display.x11.impl.XCallerImpl;
import org.trinity.display.x11.impl.XServerImpl;
import org.trinity.display.x11.impl.XWindowImpl;

// TODO documentation
// currently unused
/**
 * @author Erik De Rijcke
 * @since 1.1
 */
public class XcbExtensionComposite extends XcbExtensionBase implements
		XExtensionComposite {

	public static final String EXTENSION_NAME = "XCOMPOSITE";
	public static final int MAJOR_VERSION = 0;
	public static final int MINOR_VERSION = 4;

	public XcbExtensionComposite(final XCallerImpl nativeCaller,
			final XExtensionNativeCalls nativeCalls, final XServerImpl display) {
		super(nativeCaller, nativeCalls, nativeCalls.getCallXCompositeInit(),
				display, XcbExtensionComposite.EXTENSION_NAME,
				XcbExtensionComposite.MAJOR_VERSION,
				XcbExtensionComposite.MINOR_VERSION);
	}

	@Override
	public void redirectSubWindow(final XWindowImpl window,
			final XCompositeUpdateMode updateMode) {
		final Long displayPeer = window.getDisplayResourceHandle().getDisplay()
				.getNativePeer();
		final Long windowId = window.getDisplayResourceHandle()
				.getResourceHandle().getNativeHandle();
		final Integer updateModeValue = Integer.valueOf(updateMode.ordinal());

		this.nativeCaller.doCall(
				this.nativeCalls.getCallXCompositeRedirectSubwindow(),
				displayPeer, windowId, updateModeValue);
	}
}
