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
import org.fusion.x11.core.XID;
import org.fusion.x11.core.XResourceHandle;
import org.fusion.x11.core.XWindow;
import org.fusion.x11.core.extension.XExtensionXFixes;
import org.fusion.x11.core.extension.XFixesXServerRegion;
import org.fusion.x11.core.extension.XRenderPicture;
import org.fusion.x11.nativeHelpers.XNativeCaller;

// TODO documentation
// currently unused
/**
 * @author Erik De Rijcke
 * @since 1.1
 */
public class XcbExtensionXFixes extends XcbExtensionBase implements
		XExtensionXFixes {

	public static final String EXTENSION_NAME = "XFIXES";
	public static final int MAJOR_VERSION = 2;
	public static final int MINOR_VERSION = 0;

	public XcbExtensionXFixes(final XNativeCaller nativeCaller,
			final XExtensionNativeCalls nativeCalls, final XDisplay display) {
		super(nativeCaller, nativeCalls, nativeCalls.getCallXFixesInit(),
				display, XcbExtensionXFixes.EXTENSION_NAME,
				XcbExtensionXFixes.MAJOR_VERSION,
				XcbExtensionXFixes.MINOR_VERSION);
	}

	@Override
	public XcbXFixesXServerRegion createRegionFromWindow(final XWindow window,
			final int regionBounding) {
		final Long displayPeer = window.getDisplayResourceHandle().getDisplay()
				.getNativePeer();
		final Long windowId = window.getDisplayResourceHandle()
				.getResourceHandle().getNativeHandle();
		final Integer regionBoundingValue = Integer.valueOf(regionBounding);

		Long regionId;

		regionId = this.nativeCaller.doNativeCall(
				this.nativeCalls.getCallXFixesCreateRegionFromWindow(),
				displayPeer, windowId, regionBoundingValue);

		// TODO use resources registry
		final XcbXFixesXServerRegion region = new XcbXFixesXServerRegion(
				new XID(window.getDisplayResourceHandle().getDisplay(),
						XResourceHandle.valueOf(regionId)));
		return region;
	}

	@Override
	public void translateRegion(final XFixesXServerRegion region, final int x,
			final int y) {

		final Long displayPeer = region.getDisplayResourceHandle().getDisplay()
				.getNativePeer();
		final Long regionId = region.getDisplayResourceHandle()
				.getResourceHandle().getNativeHandle();
		final Integer xValue = Integer.valueOf(x);
		final Integer yValue = Integer.valueOf(y);

		this.nativeCaller.doNativeCall(
				this.nativeCalls.getCallXFixesTranslateRegion(), displayPeer,
				regionId, xValue, yValue);
	}

	@Override
	public void setPictureClipRegion(final XRenderPicture picture,
			final int clipXOrigin, final int clipYOrigin,
			final XFixesXServerRegion region) {

		final Long displayPeer = picture.getDisplayResourceHandle()
				.getDisplay().getNativePeer();
		final Long pictureId = region.getDisplayResourceHandle()
				.getResourceHandle().getNativeHandle();
		final Integer clipXOriginVal = Integer.valueOf(clipXOrigin);
		final Integer clipYOriginVal = Integer.valueOf(clipYOrigin);
		final Long regionId = region.getDisplayResourceHandle().getDisplay()
				.getNativePeer();

		this.nativeCaller.doNativeCall(
				this.nativeCalls.getCallXFixesSetPictureClipRegion(),
				displayPeer, pictureId, clipXOriginVal, clipYOriginVal,
				regionId);

	}

	@Override
	public void destroyRegion(final XFixesXServerRegion region) {
		final Long displayPeer = region.getDisplayResourceHandle().getDisplay()
				.getNativePeer();
		final Long regionId = region.getDisplayResourceHandle()
				.getResourceHandle().getNativeHandle();

		this.nativeCaller.doNativeCall(
				this.nativeCalls.getCallXFixesDestroyRegion(), displayPeer,
				regionId);

	}
}
