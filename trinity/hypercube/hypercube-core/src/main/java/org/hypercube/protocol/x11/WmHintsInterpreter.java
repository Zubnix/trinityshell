package org.hypercube.protocol.x11;

import org.fusion.x11.core.XID;
import org.fusion.x11.core.XWindow;
import org.fusion.x11.icccm.WmHintsInstance;
import org.fusion.x11.icccm.WmStateEnum;
import org.hypercube.protocol.GeometryPreferences;
import org.hypercube.protocol.IconPreferences;
import org.hypercube.protocol.ProtocolEvent;
import org.hypercube.protocol.UrgentNotify;
import org.hyperdrive.core.ClientWindow;

final class WmHintsInterpreter {

	private final XDesktopProtocol xDesktopProtocol;

	WmHintsInterpreter(final XDesktopProtocol xDesktopProtocol) {
		this.xDesktopProtocol = xDesktopProtocol;
	}

	void handleWmHint(final ClientWindow client, final WmHintsInstance instance) {

		final ProtocolEvent<IconPreferences> iconEvent = this.xDesktopProtocol
				.query(client, ProtocolEvent.ICON_PREF);
		final ProtocolEvent<GeometryPreferences> geoEvent = this.xDesktopProtocol
				.query(client, ProtocolEvent.GEO_PREF);

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
