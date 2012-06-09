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

import org.trinity.display.x11.api.extension.shape.XExtensionXShape;
import org.trinity.display.x11.impl.XCallerImpl;
import org.trinity.display.x11.impl.XServerImpl;
import org.trinity.display.x11.impl.XWindowImpl;

// TODO documentation
// currently unused
/**
 * @author Erik De Rijcke
 * @since 1.1
 */
public class XcbExtensionXShape extends XcbExtensionBase implements
		XExtensionXShape {

	public static final String EXTENSION_NAME = "XSHAPE";
	// TODO lookup latest supported version
	public static final int MAJOR_VERSION = 1;
	public static final int MINOR_VERSION = 0;

	public XcbExtensionXShape(final XCallerImpl nativeCaller,
			final XExtensionNativeCalls nativeCalls, final XServerImpl display) {
		super(nativeCaller, nativeCalls, nativeCalls.getCallXShapeInit(),
				display, XcbExtensionXShape.EXTENSION_NAME,
				XcbExtensionXShape.MAJOR_VERSION,
				XcbExtensionXShape.MINOR_VERSION);
	}

	@Override
	public void selectInput(final XWindowImpl window, final long shapeNotifyMask) {
		// TODO Auto-generated method stub

	}

}
