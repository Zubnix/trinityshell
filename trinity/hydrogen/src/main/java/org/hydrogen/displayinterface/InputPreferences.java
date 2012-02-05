/*
 * This file is part of Hydrogen.
 * 
 * Hydrogen is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hydrogen is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hydrogen. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.displayinterface;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface InputPreferences {

	// TODO this is pretty X specific, abstract it away?
	/**
	 * 
	 * @author Erik De Rijcke
	 * 
	 */
	public enum InputPreference {
		/**
		 * The client does not expect keyboard input and does not want the
		 * window manager to set focus to any of its windows
		 */
		NO_INPUT,
		/**
		 * The client expects keyboard input and wants the window manager to set
		 * focus to its top-level window. It does not set focus itself.
		 */
		PASSIVE_INPUT,
		/**
		 * The client expects keyboard input and wants the window manager to set
		 * focus to its top-level window. It may also set focus to one of its
		 * subwindows when one of its windows already has the focus. It does not
		 * set focus itself when the current focus is in a window that the
		 * client does not own.
		 */
		LOCAL_INPUT,
		/**
		 * The client expects keyboard input but does not want the window
		 * manager to set focus to any of its windows. Instead, it sets focus
		 * itself, even when the current focus is in a window that the client
		 * does not own.
		 */
		GLOBAL_INPUT
	}

	/**
	 * 
	 * @return
	 */
	InputPreference getInputPreference();
}
