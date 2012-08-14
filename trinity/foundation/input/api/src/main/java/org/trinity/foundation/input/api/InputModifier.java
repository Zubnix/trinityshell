/*
 * This file is part of Hydrogen. Hydrogen is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Hydrogen is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Hydrogen. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.input.api;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface InputModifier {
	/**
	 * shift key
	 */
	String MOD_SHIFT = "MOD_SHIFT";
	/**
	 * shift lock
	 */
	String MOD_LOCK = "MOD_LOCK";
	/**
	 * control
	 */
	String MOD_CTRL = "MOD_CTRL";
	/**
	 * Alt
	 */
	String MOD_1 = "MOD_1";
	/**
	 * numlock
	 */
	String MOD_2 = "MOD_2";
	String MOD_3 = "MOD_3";
	/**
	 * Meta key (windows, cmd, ..)
	 */
	String MOD_4 = "MOD_4";
	String MOD_5 = "MOD_5";
	String MOD_ANY = "MOD_ANY";

	int getMask();

	/**
	 * 
	 * @return
	 */
	String getName();
}
