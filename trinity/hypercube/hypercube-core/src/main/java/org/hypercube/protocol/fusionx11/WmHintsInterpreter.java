/*
 * This file is part of Hypercube.
 * 
 * Hypercube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hypercube is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hypercube. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hypercube.protocol.fusionx11;

import org.fusion.x11.core.XID;
import org.fusion.x11.core.XWindow;
import org.fusion.x11.icccm.WmHintsInstance;
import org.fusion.x11.icccm.WmStateEnum;
import org.hyperdrive.core.ClientWindow;
import org.hyperdrive.protocol.GeometryPreferences;
import org.hyperdrive.protocol.IconPreferences;
import org.hyperdrive.protocol.ProtocolEvent;
import org.hyperdrive.protocol.UrgentNotify;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
final class WmHintsInterpreter {

	private final XDesktopProtocol xDesktopProtocol;

	WmHintsInterpreter(final XDesktopProtocol xDesktopProtocol) {
		this.xDesktopProtocol = xDesktopProtocol;
	}

	void handleWmHint(final ClientWindow client, final WmHintsInstance instance) {

		final ProtocolEvent<IconPreferences> iconEvent = this.xDesktopProtocol
				.query(client, ProtocolEvent.ICON_PREFERENCES);
		final ProtocolEvent<GeometryPreferences> geoEvent = this.xDesktopProtocol
				.query(client, ProtocolEvent.GEO_PREFERENCES);

		final long input = instance.getInput();
		final WmStateEnum initialState = instance.getInitialState();
		final XID pixmapid = instance.getIconPixmap();
		final XWindow iconWindow = instance.getIconWindow();
		final int iconX = instance.getIconX();
		final int iconY = instance.getIconY();
		final XID iconMask = instance.getIconMask();
		final XWindow windowGroupLeader = instance.getWindowGroup();

		final int hintFlags = instance.getFlags();
		if ((hintFlags & 1) != 0) {
			// InputHint 1 input

		}
		if ((hintFlags & 2) != 0) {
			// StateHint 2 initial_state

		}
		if ((hintFlags & 4) != 0) {
			// IconPixmapHint 4 icon_pixmap

		}
		if ((hintFlags & 8) != 0) {
			// IconWindowHint 8 icon_window

		}
		if ((hintFlags & 16) != 0) {
			// IconPositionHint 16 icon_x & icon_y

		}
		if ((hintFlags & 32) != 0) {
			// IconMaskHint 32 icon_mask

		}
		if ((hintFlags & 64) != 0) {
			// WindowGroupHint 64 window_group

		}
		if ((hintFlags & 128) != 0) {
			// MessageHint 128 (this bit is obsolete)

		}

		final UrgentNotify urgentNotify = new UrgentNotify(
				(hintFlags & 256) != 0);
		final ProtocolEvent<UrgentNotify> urgentEvent = new ProtocolEvent<UrgentNotify>(
				ProtocolEvent.URGENT_NOTIFY, urgentNotify);
		this.xDesktopProtocol.updateProtocolEvent(client, urgentEvent);

		// TODO update geometry preferences' visibility
		// TODO update Icon preferences
		// TODO update urgent notify

	}
}
