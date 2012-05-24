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
import org.fusion.x11.core.extension.XDamage;
import org.fusion.x11.core.extension.XDamageToReport;
import org.fusion.x11.core.extension.XExtensionDamage;
import org.fusion.x11.core.extension.XFixesXServerRegion;
import org.fusion.x11.nativeHelpers.XNativeCaller;

// TODO documentation
// currently unused
/**
 * @author Erik De Rijcke
 * @since 1.1
 */
public class XcbExtensionDamage extends XcbExtensionBase implements
		XExtensionDamage {

	public static final String EXTENSION_NAME = "DAMAGE";

	public static final int MAJOR_VERSION = 1;
	public static final int MINOR_VERSION = 1;

	public XcbExtensionDamage(final XNativeCaller nativeCaller,
			final XExtensionNativeCalls nativeCalls, final XDisplay display) {
		super(nativeCaller, nativeCalls, nativeCalls.getCallXDamageInit(),
				display, XcbExtensionDamage.EXTENSION_NAME,
				XcbExtensionDamage.MAJOR_VERSION,
				XcbExtensionDamage.MINOR_VERSION);
	}

	@Override
	public XcbXDamage create(final XWindow window,
			final XDamageToReport damageReport) {
		final Long displayPeer = window.getDisplayResourceHandle().getDisplay()
				.getNativePeer();
		final Long windowId = window.getDisplayResourceHandle()
				.getResourceHandle().getNativeHandle();
		final Integer damageToReportValue = damageReport.ordinal();

		XcbXDamage damage;

		final Long xDamageId = this.nativeCaller.doNativeCall(
				this.nativeCalls.getCallXDamageCreate(), displayPeer, windowId,
				damageToReportValue);
		// TODO use resource registry
		damage = new XcbXDamage(new XID(window.getDisplayResourceHandle()
				.getDisplay(), XResourceHandle.valueOf(xDamageId)));

		return damage;
	}

	@Override
	public void subtract(final XDamage damage,
			final XFixesXServerRegion repair, final XFixesXServerRegion parts) {
		final Long displayPeer = damage.getDisplayResourceHandle().getDisplay()
				.getNativePeer();
		final Long damageId = damage.getDisplayResourceHandle()
				.getResourceHandle().getNativeHandle();
		final Long repairRegionId = repair.getDisplayResourceHandle()
				.getResourceHandle().getNativeHandle();
		final Long partsRegionId = parts.getDisplayResourceHandle()
				.getResourceHandle().getNativeHandle();

		this.nativeCaller.doNativeCall(
				this.nativeCalls.getCallXDamageSubtract(), displayPeer,
				damageId, repairRegionId, partsRegionId);
	}
}
