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

import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.extension.XExtensionXSync;
import org.fusion.x11.core.extension.XSyncWaitCondition;
import org.fusion.x11.nativeHelpers.XNativeCaller;

// TODO documentation
// currently unused
/**
 * @author Erik De Rijcke
 * @since 1.1
 */
public class XcbExtensionXSync extends XcbExtensionBase implements
		XExtensionXSync {

	public static final String EXTENSION_NAME = "XSYNC";
	public static final int MAJOR_VERSION = 3;
	public static final int MINOR_VERSION = 0;

	public XcbExtensionXSync(final XNativeCaller nativeCaller,
			final XExtensionNativeCalls nativeCalls, final XDisplay display) {
		super(nativeCaller, nativeCalls, nativeCalls.getCallXSyncInit(),
				display, XcbExtensionXSync.EXTENSION_NAME,
				XcbExtensionXSync.MAJOR_VERSION,
				XcbExtensionXSync.MINOR_VERSION);
	}

	@Override
	public void await(final XSyncWaitCondition... conditions) {
		// TODO Auto-generated method stub

	}
}
