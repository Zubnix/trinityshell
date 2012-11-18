/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.input.api;

/***************************************
 * Can modify the behavior of user input by accompanying {@link Input}.
 * 
 *************************************** 
 */
public interface InputModifier {
	/**
	 * shift key modifier name.
	 */
	String MOD_SHIFT = "MOD_SHIFT";
	/**
	 * shift lock modifier name.
	 */
	String MOD_LOCK = "MOD_LOCK";
	/**
	 * control modifier name.
	 */
	String MOD_CTRL = "MOD_CTRL";
	/**
	 * Alt modifier name.
	 */
	String MOD_1 = "MOD_1";
	/**
	 * numlock modifier name.
	 */
	String MOD_2 = "MOD_2";
	// TODO which key is mod3?
	/**
	 * 
	 */
	String MOD_3 = "MOD_3";
	/**
	 * Meta key (windows, cmd, ..) modifier name.
	 */
	String MOD_4 = "MOD_4";
	// TODO which key is mod5?
	/**
	 * 
	 */
	String MOD_5 = "MOD_5";
	/**
	 * All modifiers name.
	 */
	String MOD_ANY = "MOD_ANY";

	/***************************************
	 * The native mask of this <code>InputModifier</code>. This mask can be
	 * logically and'ed with a logically or'ed integer of masks to verifiy if
	 * this <code>InputModifier</code> is active.
	 * 
	 * @return
	 *************************************** 
	 */
	int getMask();

	/***************************************
	 * The name of this <code>InputModifier</code>. For a list of common names
	 * check any of the statically defined names in the {@link InputModifier}
	 * class.
	 * 
	 * @return A unique name.
	 *************************************** 
	 */
	String getName();
}
