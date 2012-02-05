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

import org.fusion.x11.core.XAtom;
import org.fusion.x11.core.XPropertyInstanceXAtoms;
import org.fusion.x11.core.XWindow;
import org.hydrogen.displayinterface.InputPreferences.InputPreference;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
final class InputPreferenceHandler {
	/**
	 * 
	 * @param window
	 * @param wmHintsInstance
	 * @param wmProtocolsReply
	 */
	void handleInputPreference(final XWindow window,
	                           final WmHintsInstance wmHintsInstance,
	                           final XPropertyInstanceXAtoms wmProtocolsReply) {
		final int hintFlags = wmHintsInstance.getFlags();
		if ((hintFlags & 1) != 0) {
			if (wmHintsInstance.getInput() != 0) {
				for (final XAtom xAtom : wmProtocolsReply.getAtoms()) {
					if (xAtom.getAtomName().equals("WM_TAKE_FOCUS")) {
						window.getPreferences()
						                .getInputPreferences()
						                .setInputPreference(InputPreference.LOCAL_INPUT);
						break;
					}
					window.getPreferences()
					                .getInputPreferences()
					                .setInputPreference(InputPreference.PASSIVE_INPUT);
				}
			} else {
				for (final XAtom xAtom : wmProtocolsReply.getAtoms()) {
					if (xAtom.getAtomName().equals("WM_TAKE_FOCUS")) {
						window.getPreferences()
						                .getInputPreferences()
						                .setInputPreference(InputPreference.GLOBAL_INPUT);
						break;
					}
					window.getPreferences()
					                .getInputPreferences()
					                .setInputPreference(InputPreference.NO_INPUT);
				}
			}
		}
	}
}
