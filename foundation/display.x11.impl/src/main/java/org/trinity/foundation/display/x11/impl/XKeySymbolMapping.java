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
package org.trinity.foundation.display.x11.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.concurrent.Immutable;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.trinity.foundation.api.display.input.Keyboard;

import com.google.inject.Singleton;

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;


@Bind
@To(IMPLEMENTATION)
@Singleton
@Immutable
public class XKeySymbolMapping {

	private final Map<Integer, String> keySymbolToName = new ConcurrentHashMap<Integer, String>();
	private final Map<String, Integer> nameToKeySymbol = new ConcurrentHashMap<String, Integer>();

	XKeySymbolMapping() {
		staticMappings();
	}

	public String toString(final Integer keySymbol) {
		String keySymbolName = this.keySymbolToName.get(keySymbol);
		if (keySymbolName == null) {
			keySymbolName = findKeySymbolName(keySymbol.intValue());
			storeMapping(	keySymbol,
							keySymbolName);
		}
		return keySymbolName;
	}

	private String findKeySymbolName(final int keySymbol) {
		// FIXME needs better conversion
		if ((keySymbol >= 0x00) && (keySymbol <= 0xFF)) {
			// Latin-1 code points represent themselves (0x00-0xFF).
			return String.valueOf((char) keySymbol);
		} else if ((keySymbol & 0x1000000) != 0) {
			// Other (non-Latin-1) Unicode code points have an offset of
			// 0x1000000,*except* when they don't.
			// we convert to 32bit unicode code point.
			return String.valueOf(Character.toChars(keySymbol & 0x000FFFFF));
		} else {
			return null;
		}
	}

	public Integer toKeySymbol(final String keySymbolName) {
		Integer keySymbol = this.nameToKeySymbol.get(keySymbolName);
		if (keySymbol == null) {
			keySymbol = findKeySymbol(keySymbolName);
			storeMapping(	keySymbol,
							keySymbolName);
		}
		return keySymbol;
	}

	private int findKeySymbol(final String keySymbolName) {
		// FIXME needs better conversion

		if (keySymbolName.length() == 1) {
			final char keySymbolChar = keySymbolName.charAt(0);

			if ((keySymbolChar >= 0x00) && (keySymbolChar <= 0xFF)) {
				// Latin-1 code points represent themselves (0x00-0xFF).
				return keySymbolChar;
			} else {
				// Other (non-Latin-1) Unicode code points have an offset of
				// 0x1000000,*except* when they don't.

				// we convert to 32bit unicode code point.
				final int keySymbolCode = keySymbolChar | 0x1000000;
				return keySymbolCode;
			}
		} else if (keySymbolName.length() > 1) {
			// TODO check if keysymbol exists as non single char key (ie, arrow
			// up, alt, etc)?
			return 0;
		} else {
			return 0;
		}
	}

	private void storeMapping(	final Integer keySymbol,
								final String keySymbolName) {
		this.keySymbolToName.put(	keySymbol,
									keySymbolName);
		this.nameToKeySymbol.put(	keySymbolName,
									keySymbol);
	}

	private void staticMappings() {
		{
			// TODO A whole lot of other key symbols that do not map directly to
			// unicode...
			/* XK_EuroSign : EURO SIGN */
			storeMapping(	Integer.valueOf(0x000020ac),
							String.valueOf((char) 0x20ac));
		}
		// KeySymbols with a non default textual representation (shift,
		// capslock, shiftlock, numlock, insert, ...)
		// TODO correctly implement all of these keysymbols.
		{
			/* InputModifiers */
			// #define XK_Shift_L 0xFFE1 /* Left shift */
			storeMapping(	Integer.valueOf(0xFFE1),
							Keyboard.L_SHIFT);
			// #define XK_Shift_R 0xFFE2 /* Right shift */
			storeMapping(	Integer.valueOf(0xFFE2),
							Keyboard.R_SHIFT);
			// #define XK_Control_L 0xFFE3 /* Left control */
			storeMapping(	Integer.valueOf(0xFFE3),
							Keyboard.L_CTRL);
			// #define XK_Control_R 0xFFE4 /* Right control */
			storeMapping(	Integer.valueOf(0xFFE4),
							Keyboard.R_CTRL);
			// #define XK_Caps_Lock 0xFFE5 /* Caps lock */
			storeMapping(	Integer.valueOf(0xFFE5),
							Keyboard.CAPS_LOCK);
			// #define XK_Shift_Lock 0xFFE6 /* Shift lock */
			storeMapping(	Integer.valueOf(0xFFE6),
							Keyboard.SHIFT_LOCK);
			//
			// #define XK_Meta_L 0xFFE7 /* Left meta */
			storeMapping(	Integer.valueOf(0xFFE7),
							Keyboard.L_META);
			// #define XK_Meta_R 0xFFE8 /* Right meta */
			storeMapping(	Integer.valueOf(0xFFE8),
							Keyboard.R_META);
			// #define XK_Alt_L 0xFFE9 /* Left alt */
			storeMapping(	Integer.valueOf(0xFFE9),
							Keyboard.L_ALT);
			// #define XK_Alt_R 0xFFEA /* Right alt */
			storeMapping(	Integer.valueOf(0xFFEA),
							Keyboard.R_ALT);
			// #define XK_Super_L 0xFFEB /* Left super */
			storeMapping(	Integer.valueOf(0xFFEB),
							Keyboard.L_SUPER);
			// #define XK_Super_R 0xFFEC /* Right super */
			storeMapping(	Integer.valueOf(0xFFEC),
							Keyboard.R_SUPER);
			// #define XK_Hyper_L 0xFFED /* Left hyper */
			storeMapping(	Integer.valueOf(0xFFED),
							Keyboard.L_HYPER);
			// #define XK_Hyper_R 0xFFEE /* Right hyper */
			storeMapping(	Integer.valueOf(0xFFEE),
							Keyboard.R_HYPER);
		}
		{
			/* Cursor control & motion */
			// #define XK_Home 0xFF50
			storeMapping(	Integer.valueOf(0xFF50),
							Keyboard.HOME);
			// #define XK_Left 0xFF51 /* Move left, left arrow */
			storeMapping(	Integer.valueOf(0xFF51),
							Keyboard.LEFT);
			// #define XK_Up 0xFF52 /* Move up, up arrow */
			storeMapping(	Integer.valueOf(0xFF52),
							Keyboard.UP);

			// #define XK_Right 0xFF53 /* Move right, right arrow */
			storeMapping(	Integer.valueOf(0xFF53),
							Keyboard.RIGHT);
			// #define XK_Down 0xFF54 /* Move down, down arrow */
			storeMapping(	Integer.valueOf(0xFF52),
							Keyboard.DOWN);
			// #define XK_Prior 0xFF55 /* Prior, previous */
			storeMapping(	Integer.valueOf(0xFF55),
							Keyboard.PREV);
			// #define XK_Page_Up 0xFF55
			storeMapping(	Integer.valueOf(0xFF56),
							Keyboard.PG_UP);
			// #define XK_Next 0xFF56 /* Next */
			storeMapping(	Integer.valueOf(0xFF56),
							Keyboard.NEXT);
			// #define XK_Page_Down 0xFF56
			storeMapping(	Integer.valueOf(0xFF56),
							Keyboard.PG_DOWN);
			// #define XK_End 0xFF57 /* EOL */
			storeMapping(	Integer.valueOf(0xFF57),
							Keyboard.END);
			// #define XK_Begin 0xFF58 /* BOL *
			storeMapping(	Integer.valueOf(0xFF58),
							Keyboard.BEGIN);
		}
		{
			/*
			 * TTY Functions, cleverly chosen to map to ascii, for convenience
			 * of programming, but could have been arbitrary (at the cost of
			 * lookup tables in client code.
			 */
			// #define XK_BackSpace 0xFF08 /* back space, back char */
			storeMapping(	Integer.valueOf(0xFF08),
							Keyboard.BACKSPACE);
			// #define XK_Tab 0xFF09
			storeMapping(	Integer.valueOf(0xFF09),
							Keyboard.TAB);
			// #define XK_Linefeed 0xFF0A /* Linefeed, LF */
			storeMapping(	Integer.valueOf(0xFF0A),
							Keyboard.LINEFEED);
			// #define XK_Clear 0xFF0B
			storeMapping(	Integer.valueOf(0xFF0B),
							Keyboard.CLEAR);
			// #define XK_Return 0xFF0D /* Return, enter */
			storeMapping(	Integer.valueOf(0xFF0D),
							Keyboard.ENTER);
			// #define XK_Pause 0xFF13 /* Pause, hold */
			storeMapping(	Integer.valueOf(0xFF13),
							Keyboard.PAUSE);
			// #define XK_Scroll_Lock 0xFF14
			storeMapping(	Integer.valueOf(0xFF14),
							Keyboard.SCRL_LOCK);
			// #define XK_Sys_Req 0xFF15
			storeMapping(	Integer.valueOf(0xFF15),
							Keyboard.SYS_REQ);
			// #define XK_Escape 0xFF1B
			storeMapping(	Integer.valueOf(0xFF1B),
							Keyboard.ESCAPE);
			// #define XK_Delete 0xFFFF /* Delete, rubout */
			storeMapping(	Integer.valueOf(0xFFFF),
							Keyboard.DELETE);
		}
		{
			/* Misc Functions */
			// #define XK_Select 0xFF60 /* Select, mark */
			// #define XK_Print 0xFF61
			storeMapping(	Integer.valueOf(0xFF61),
							Keyboard.PRINT);
			// #define XK_Execute 0xFF62 /* Execute, run, do */
			// #define XK_Insert 0xFF63 /* Insert, insert here */
			storeMapping(	Integer.valueOf(0xFF63),
							Keyboard.INSERT);
			// #define XK_Undo 0xFF65 /* Undo, oops */
			// #define XK_Redo 0xFF66 /* redo, again */
			// #define XK_Menu 0xFF67
			// #define XK_Find 0xFF68 /* Find, search */
			// #define XK_Cancel 0xFF69 /* Cancel, stop, abort, exit */
			// #define XK_Help 0xFF6A /* Help */
			// #define XK_Break 0xFF6B
			// #define XK_Mode_switch 0xFF7E /* Character set switch */
			// #define XK_script_switch 0xFF7E /* Alias for mode_switch */
			// #define XK_Num_Lock 0xFF7F
			storeMapping(	Integer.valueOf(0xFF7F),
							Keyboard.NUM_LOCK);
		}
		{
			/*
			 * Keypad Functions, keypad numbers cleverly chosen to map to ascii
			 */
			// #define XK_KP_Space 0xFF80 /* space */
			// #define XK_KP_Tab 0xFF89
			// #define XK_KP_Enter 0xFF8D /* enter */
			// #define XK_KP_F1 0xFF91 /* PF1, KP_A, ... */
			// #define XK_KP_F2 0xFF92
			// #define XK_KP_F3 0xFF93
			// #define XK_KP_F4 0xFF94
			// #define XK_KP_Home 0xFF95
			// #define XK_KP_Left 0xFF96
			// #define XK_KP_Up 0xFF97
			// #define XK_KP_Right 0xFF98
			// #define XK_KP_Down 0xFF99
			// #define XK_KP_Prior 0xFF9A
			// #define XK_KP_Page_Up 0xFF9A
			// #define XK_KP_Next 0xFF9B
			// #define XK_KP_Page_Down 0xFF9B
			// #define XK_KP_End 0xFF9C
			// #define XK_KP_Begin 0xFF9D
			// #define XK_KP_Insert 0xFF9E
			// #define XK_KP_Delete 0xFF9F
			// #define XK_KP_Equal 0xFFBD /* equals */
			// #define XK_KP_Multiply 0xFFAA
			// #define XK_KP_Add 0xFFAB
			// #define XK_KP_Separator 0xFFAC /* separator, often comma */
			// #define XK_KP_Subtract 0xFFAD
			// #define XK_KP_Decimal 0xFFAE
			// #define XK_KP_Divide 0xFFAF
			//
			// #define XK_KP_0 0xFFB0
			// #define XK_KP_1 0xFFB1
			// #define XK_KP_2 0xFFB2
			// #define XK_KP_3 0xFFB3
			// #define XK_KP_4 0xFFB4
			// #define XK_KP_5 0xFFB5
			// #define XK_KP_6 0xFFB6
			// #define XK_KP_7 0xFFB7
			// #define XK_KP_8 0xFFB8
			// #define XK_KP_9 0xFFB9
		}
		{
			/*
			 * Auxilliary Functions; note the duplicate definitions for left and
			 * right function keys; Sun keyboards and a few other manufactures
			 * have such function key groups on the left and/or right sides of
			 * the keyboard. We've not found a keyboard with more than 35
			 * function keys total.
			 */
			// #define XK_F1 0xFFBE
			// #define XK_F2 0xFFBF
			// #define XK_F3 0xFFC0
			// #define XK_F4 0xFFC1
			// #define XK_F5 0xFFC2
			// #define XK_F6 0xFFC3
			// #define XK_F7 0xFFC4
			// #define XK_F8 0xFFC5
			// #define XK_F9 0xFFC6
			// #define XK_F10 0xFFC7
			// #define XK_F11 0xFFC8
			// #define XK_L1 0xFFC8
			// #define XK_F12 0xFFC9
			// #define XK_L2 0xFFC9
			// #define XK_F13 0xFFCA
			// #define XK_L3 0xFFCA
			// #define XK_F14 0xFFCB
			// #define XK_L4 0xFFCB
			// #define XK_F15 0xFFCC
			// #define XK_L5 0xFFCC
			// #define XK_F16 0xFFCD
			// #define XK_L6 0xFFCD
			// #define XK_F17 0xFFCE
			// #define XK_L7 0xFFCE
			// #define XK_F18 0xFFCF
			// #define XK_L8 0xFFCF
			// #define XK_F19 0xFFD0
			// #define XK_L9 0xFFD0
			// #define XK_F20 0xFFD1
			// #define XK_L10 0xFFD1
			// #define XK_F21 0xFFD2
			// #define XK_R1 0xFFD2
			// #define XK_F22 0xFFD3
			// #define XK_R2 0xFFD3
			// #define XK_F23 0xFFD4
			// #define XK_R3 0xFFD4
			// #define XK_F24 0xFFD5
			// #define XK_R4 0xFFD5
			// #define XK_F25 0xFFD6
			// #define XK_R5 0xFFD6
			// #define XK_F26 0xFFD7
			// #define XK_R6 0xFFD7
			// #define XK_F27 0xFFD8
			// #define XK_R7 0xFFD8
			// #define XK_F28 0xFFD9
			// #define XK_R8 0xFFD9
			// #define XK_F29 0xFFDA
			// #define XK_R9 0xFFDA
			// #define XK_F30 0xFFDB
			// #define XK_R10 0xFFDB
			// #define XK_F31 0xFFDC
			// #define XK_R11 0xFFDC
			// #define XK_F32 0xFFDD
			// #define XK_R12 0xFFDD
			// #define XK_F33 0xFFDE
			// #define XK_R13 0xFFDE
			// #define XK_F34 0xFFDF
			// #define XK_R14 0xFFDF
			// #define XK_F35 0xFFE0
			// #define XK_R15 0xFFE0
		}

		// Other Unicode code points keysymbols are lazily initialized
	}
}
