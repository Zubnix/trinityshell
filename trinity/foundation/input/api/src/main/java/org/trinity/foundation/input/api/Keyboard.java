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

import java.util.List;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface Keyboard {

	String L_SHIFT = "L_SHIFT";
	String R_SHIFT = "R_SHIFT";
	String L_CTRL = "L_CTRL";
	String R_CTRL = "R_CTRL";
	String CAPS_LOCK = "CAPS_LOCK";
	String SHIFT_LOCK = "SHIFT_LOCK";
	String L_META = "L_META";
	String R_META = "R_META";
	String L_ALT = "L_ALT";
	String R_ALT = "R_ALT";
	String L_SUPER = "L_SUPER";
	String R_SUPER = "R_SUPER";
	String L_HYPER = "L_HYPER";
	String R_HYPER = "R_HYPER";
	String HOME = "HOME";
	String LEFT = "LEFT";
	String UP = "UP";
	String RIGHT = "RIGHT";
	String DOWN = "DOWN";
	String PREV = "PREV";
	String PG_UP = "PG_UP";
	String NEXT = "NEXT";
	String PG_DOWN = "PG_DOWN";
	String END = "END";
	String BEGIN = "BEGIN";
	String ESCAPE = "ESCAPE";
	String ENTER = "ENTER";
	String BACKSPACE = "BACKSPACE";
	String TAB = "TAB";
	String LINEFEED = "LINEFEED";
	String CLEAR = "CLEAR";
	String PAUSE = "PAUSE";
	String SCRL_LOCK = "SCRL_LOCK";
	String SYS_REQ = "SYS_REQ";
	String DELETE = "DELETE";
	String PRINT = "PRINT";
	String INSERT = "INSERT";
	String NUM_LOCK = "NUM_LOCK";

	/**
	 * 
	 * @param key
	 * @param baseInputModifiers
	 * @return
	 * 
	 */
	String asKeySymbolName(	Key key,
							InputModifiers inputModifiers);

	/**
	 * 
	 * @param keySymbolName
	 * @return
	 * 
	 */
	List<Key> asKeys(String keySymbolName);

	/**
	 * 
	 * @param modifierKeyName
	 * @return
	 */
	InputModifier modifier(String modifierName);

	/**
	 * 
	 * @param modifierKeyNames
	 * @return
	 */
	InputModifiers modifiers(String... modifierNames);
}
