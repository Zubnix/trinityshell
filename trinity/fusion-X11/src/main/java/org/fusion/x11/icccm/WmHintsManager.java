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

package org.fusion.x11.icccm;

import org.fusion.x11.core.XPropertyInstanceXAtoms;
import org.fusion.x11.core.XWindow;
import org.fusion.x11.core.XWindowRelation;
import org.fusion.x11.core.event.XMapRequest;
import org.fusion.x11.core.event.XUnmapNotify;
import org.hydrogen.displayinterface.PlatformRenderAreaType;
 

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class WmHintsManager implements
		IcccmPropertyManager<WmHintsInstance> {

	private final InputPreferenceHandler inputPreferenceHandler;
	private final IcccmAtoms icccmAtoms;

	/**
	 * 
	 */
	public WmHintsManager(IcccmAtoms icccmAtoms) {
		this.inputPreferenceHandler = new InputPreferenceHandler();
		this.icccmAtoms = icccmAtoms;
	}

	@Override
	public void manageIcccmProperty(final XWindow window,
			final WmHintsInstance wmHintsInstance)
			   {
		final int hintFlags = wmHintsInstance.getFlags();

		// InputHint 1 input
		// StateHint 2 initial_state
		// IconPixmapHint 4 icon_pixmap
		// IconWindowHint 8 icon_window
		// IconPositionHint 16 icon_x & icon_y
		// IconMaskHint 32 icon_mask
		// WindowGroupHint 64 window_group
		// MessageHint 128 (this bit is obsolete)
		// UrgencyHint 256 urgency

		final XPropertyInstanceXAtoms wmProtocolsReply = window
				.getPropertyInstance(this.icccmAtoms.getWmProtocols());

		this.inputPreferenceHandler.handleInputPreference(window,
				wmHintsInstance, wmProtocolsReply);

		if ((hintFlags & 2) != 0) {
			final WmStateEnum initialStateEnum = wmHintsInstance
					.getInitialState();
			switch (initialStateEnum) {
			case NormalState: {
				final XMapRequest mapRequest = new XMapRequest(window);
				window.getDisplayResourceHandle().getDisplay()
						.addEventToMasterQueue(mapRequest);
				break;
			}
			case IconicState: {

				final XUnmapNotify unmapNotify = new XUnmapNotify(window);
				window.getDisplayResourceHandle().getDisplay()
						.addEventToMasterQueue(unmapNotify);
				break;
			}
			case WithdrawnState: {
				final XUnmapNotify unmapNotify = new XUnmapNotify(window);
				window.getDisplayResourceHandle().getDisplay()
						.addEventToMasterQueue(unmapNotify);
				break;
			}
			default:
				break;
			}
		}
		// if ((hintFlags & 4) != 0) {
		// // TODO iconpixmaphint
		// }
		if ((hintFlags & 8) != 0) {
			wmHintsInstance.getIconWindow().setPlatformRenderAreaType(
					PlatformRenderAreaType.ICON_RENDER_AREA);
			window.addXWindowRelation(new XWindowRelation(wmHintsInstance
					.getIconWindow()));
		}
		if ((hintFlags & 16) != 0) {
			final int iconX = wmHintsInstance.getIconX();
			final int iconY = wmHintsInstance.getIconY();
			window.getPreferences().getSizePlacePreferences().setIconX(iconX);
			window.getPreferences().getSizePlacePreferences().setIconY(iconY);
		}
		// if ((hintFlags & 32) != 0) {
		// // TODO iconmaskhint
		// }
		// if ((hintFlags & 64) != 0) {
		// // TODO windowgrouphint
		// }
		// if ((hintFlags & 128) != 0) {
		// // TODO messagehint (obsolete)
		// }
		// if ((hintFlags & 256) != 0) {
		// // TODO urgencyhint
		// }
		// TODO further handling + construct map request and place
		// it on display's main event queue.

	}
}
