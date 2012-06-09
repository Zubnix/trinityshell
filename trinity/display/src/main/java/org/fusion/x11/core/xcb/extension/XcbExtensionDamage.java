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

import org.trinity.display.x11.api.extension.damage.XDamage;
import org.trinity.display.x11.api.extension.damage.XDamageToReport;
import org.trinity.display.x11.api.extension.damage.XExtensionDamage;
import org.trinity.display.x11.api.extension.fixes.XFixesXServerRegion;
import org.trinity.display.x11.impl.XCallerImpl;
import org.trinity.display.x11.impl.XServerImpl;
import org.trinity.display.x11.impl.XIDImpl;
import org.trinity.display.x11.impl.XResourceHandleImpl;
import org.trinity.display.x11.impl.XWindowImpl;

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

	public XcbExtensionDamage(final XCallerImpl nativeCaller,
			final XExtensionNativeCalls nativeCalls, final XServerImpl display) {
		super(nativeCaller, nativeCalls, nativeCalls.getCallXDamageInit(),
				display, XcbExtensionDamage.EXTENSION_NAME,
				XcbExtensionDamage.MAJOR_VERSION,
				XcbExtensionDamage.MINOR_VERSION);
	}

	@Override
	public XcbXDamage create(final XWindowImpl window,
			final XDamageToReport damageReport) {
		final Long displayPeer = window.getDisplayResourceHandle().getDisplay()
				.getNativePeer();
		final Long windowId = window.getDisplayResourceHandle()
				.getResourceHandle().getNativeHandle();
		final Integer damageToReportValue = damageReport.ordinal();

		XcbXDamage damage;

		final Long xDamageId = this.nativeCaller.doCall(
				this.nativeCalls.getCallXDamageCreate(), displayPeer, windowId,
				damageToReportValue);
		// TODO use resource registry
		damage = new XcbXDamage(new XIDImpl(window.getDisplayResourceHandle()
				.getDisplay(), XResourceHandleImpl.valueOf(xDamageId)));

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

		this.nativeCaller.doCall(
				this.nativeCalls.getCallXDamageSubtract(), displayPeer,
				damageId, repairRegionId, partsRegionId);
	}
}
