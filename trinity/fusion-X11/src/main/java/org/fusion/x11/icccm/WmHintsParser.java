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

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public abstract class WmHintsParser {

	private final InputPreferenceHandler inputPreferenceHandler;
	private final IcccmAtoms icccmAtoms;

	/**
	 * 
	 */
	public WmHintsParser(final IcccmAtoms icccmAtoms) {
		this.inputPreferenceHandler = new InputPreferenceHandler();
		this.icccmAtoms = icccmAtoms;
	}

	public void parseWmHints(final XWindow window,
			final WmHintsInstance wmHintsInstance) {
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

		final InputPreference input = this.inputPreferenceHandler
				.parseInputPreference(window, wmHintsInstance, wmProtocolsReply);

		WmStateEnum state = null;
		XWindow iconWindow = null;
		int iconX = 0;
		int iconY = 0;
		if ((hintFlags & 2) != 0) {
			state = wmHintsInstance.getInitialState();
		}
		// if ((hintFlags & 4) != 0) {
		// // TODO iconpixmaphint
		// }
		if ((hintFlags & 8) != 0) {
			iconWindow = wmHintsInstance.getIconWindow();
		}
		if ((hintFlags & 16) != 0) {
			iconX = wmHintsInstance.getIconX();
			iconY = wmHintsInstance.getIconY();
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

		handleParsedValues(state, input, iconWindow, iconX, iconY);
	}

	public abstract void handleParsedValues(WmStateEnum state,
			InputPreference input, XWindow iconWindow, int iconX, int iconY);
}
