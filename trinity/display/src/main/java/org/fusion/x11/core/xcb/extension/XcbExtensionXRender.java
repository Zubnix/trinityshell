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
import org.fusion.x11.core.XWindow;
import org.fusion.x11.core.extension.XExtensionXRender;
import org.fusion.x11.core.extension.XRenderPictFormat;
import org.fusion.x11.core.extension.XRenderPictOp;
import org.fusion.x11.core.extension.XRenderPicture;
import org.fusion.x11.nativeHelpers.XNativeCaller;

// TODO documentation
// currently unused
/**
 * @author Erik De Rijcke
 * @since 1.1
 */
public class XcbExtensionXRender extends XcbExtensionBase implements
		XExtensionXRender {

	public static final String EXTENSION_NAME = "XRENDER";
	// TODO lookup latest supported version
	public static final int MAJOR_VERSION = 0;
	public static final int MINOR_VERSION = 11;

	public XcbExtensionXRender(final XNativeCaller nativeCaller,
			final XExtensionNativeCalls nativeCalls, final XDisplay display) {
		super(nativeCaller, nativeCalls, nativeCalls.getCallXRenderInit(),
				display, XcbExtensionXRender.EXTENSION_NAME,
				XcbExtensionXRender.MAJOR_VERSION,
				XcbExtensionXRender.MINOR_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public XRenderPicture createPicture(final XWindow window) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XRenderPictFormat findVisualFormat(final XDisplay display,
			final long visualPeer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void XRenderComposite(final XDisplay display,
			final XRenderPictOp pictOp, final XRenderPicture src,
			final XRenderPicture mask, final XRenderPicture dst,
			final int srcX, final int srcY, final int maskX, final int maskY,
			final int destinationX, final int destinationY, final int width,
			final int height) {
		// TODO Auto-generated method stub

	}

}
