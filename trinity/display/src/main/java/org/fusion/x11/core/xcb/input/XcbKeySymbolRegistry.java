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
package org.fusion.x11.core.xcb.input;

import java.util.HashMap;
import java.util.Map;

import org.fusion.x11.core.XCoreInterface;
import org.fusion.x11.core.input.XKeySymbol;
import org.fusion.x11.core.input.XKeySymbolRegistry;
import org.fusion.x11.core.input.XModifier;
import org.trinity.display.x11.impl.XServerImpl;
import org.trinity.display.x11.impl.xcb.jni.Xcb4J;
import org.trinity.foundation.input.api.InputModifierName;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.input.api.SpecialKeyName;

// TODO This is becoming a bit of a mess, rewrite?
// TODO documentation
/**
 * Development code. Currently unused.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class XcbKeySymbolRegistry implements XKeySymbolRegistry {
	private final XCoreInterface xCoreInterface;

	// needed to translate keycode+modifiers to keysym.
	private final long nativeXKeySymbolsPeer;

	private final Map<Integer, Map<Integer, XcbKeySymbol>> xresolvedKeySymbolsCach;

	private final Map<Long, XcbKeySymbol> xKeysymbolCodes;
	private final Map<String, XcbKeySymbol> xKeysymbolTextValues;

	/**
	 * 
	 * @param display
	 * @param xCoreInterface
	 */
	public XcbKeySymbolRegistry(final XServerImpl display,
			final XCoreInterface xCoreInterface) {
		this.xCoreInterface = xCoreInterface;
		final Long displayPeer = display.getNativePeer();
		this.nativeXKeySymbolsPeer = allocateNativeXKeysymbols(displayPeer);

		this.xKeysymbolCodes = new HashMap<Long, XcbKeySymbol>();
		this.xKeysymbolTextValues = new HashMap<String, XcbKeySymbol>();

		this.xresolvedKeySymbolsCach = new HashMap<Integer, Map<Integer, XcbKeySymbol>>();

		initXKeysymbols();
	}

	/**
	 * 
	 */
	protected void initXKeysymbols() {
		{
			// TODO A whole lot of other key symbols that do not map directly to
			// unicode...
			/* XK_EuroSign : EURO SIGN */
			new XcbKeySymbol(0x000020ac, String.valueOf((char) 0x20ac), this);
		}

		// KeySymbols with a non default textual representation (shift,
		// capslock, shiftlock, numlock, insert, ...)
		// TODO correctly implement these keysymbols.
		{
			/* InputModifiers */
			// #define XK_Shift_L 0xFFE1 /* Left shift */
			// #define XK_Shift_R 0xFFE2 /* Right shift */
			// #define XK_Control_L 0xFFE3 /* Left control */
			new XcbKeySymbol(0xFFE3, InputModifierName.MOD_CTRL.name(), this);
			// #define XK_Control_R 0xFFE4 /* Right control */
			// #define XK_Caps_Lock 0xFFE5 /* Caps lock */
			// #define XK_Shift_Lock 0xFFE6 /* Shift lock */
			//
			// #define XK_Meta_L 0xFFE7 /* Left meta */
			new XcbKeySymbol(0xFFE7, InputModifierName.MOD_4.name(), this);
			// #define XK_Meta_R 0xFFE8 /* Right meta */
			// #define XK_Alt_L 0xFFE9 /* Left alt */
			new XcbKeySymbol(0xFFE9, InputModifierName.MOD_1.name(), this);
			// #define XK_Alt_R 0xFFEA /* Right alt */
			// #define XK_Super_L 0xFFEB /* Left super */
			// #define XK_Super_R 0xFFEC /* Right super */
			// #define XK_Hyper_L 0xFFED /* Left hyper */
			// #define XK_Hyper_R 0xFFEE /* Right hyper */
		}
		{
			/* Cursor control & motion */
			// #define XK_Home 0xFF50
			// #define XK_Left 0xFF51 /* Move left, left arrow */
			// final XcbKeySymbol left =
			new XcbKeySymbol(0xFF51, SpecialKeyName.LEFT.name(), this);
			// Key[] keys;
			// try {
			// keys = getKeys(left);
			// final XcbKeySymbol keySymbol = getKeySymbol(keys[0],
			// new InputModifiers(0));
			//
			// // #define XK_Up 0xFF52 /* Move up, up arrow */
			// new XcbKeySymbol(0xFF52,
			// SpecialKeyName.UP.name(),
			// this);
			// } catch (final HydrogenDisplayInterfaceException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			// #define XK_Up 0xFF52 /* Move up, up arrow */
			new XcbKeySymbol(0xFF52, SpecialKeyName.UP.name(), this);

			// #define XK_Right 0xFF53 /* Move right, right arrow */
			new XcbKeySymbol(0xFF53, SpecialKeyName.RIGHT.name(), this);
			// #define XK_Down 0xFF54 /* Move down, down arrow */
			new XcbKeySymbol(0xFF52, SpecialKeyName.DOWN.name(), this);
			// #define XK_Prior 0xFF55 /* Prior, previous */
			// #define XK_Page_Up 0xFF55
			// #define XK_Next 0xFF56 /* Next */
			// #define XK_Page_Down 0xFF56
			// #define XK_End 0xFF57 /* EOL */
			// #define XK_Begin 0xFF58 /* BOL *
		}
		{
			/*
			 * TTY Functions, cleverly chosen to map to ascii, for convenience
			 * of programming, but could have been arbitrary (at the cost of
			 * lookup tables in client code.
			 */
			// #define XK_BackSpace 0xFF08 /* back space, back char */
			new XcbKeySymbol(0xFF08, SpecialKeyName.BACKSPACE.name(), this);
			// #define XK_Tab 0xFF09
			new XcbKeySymbol(0xFF09, SpecialKeyName.TAB.name(), this);
			// #define XK_Linefeed 0xFF0A /* Linefeed, LF */
			// #define XK_Clear 0xFF0B
			// #define XK_Return 0xFF0D /* Return, enter */
			new XcbKeySymbol(0xFF0D, SpecialKeyName.ENTER.name(), this);
			// #define XK_Pause 0xFF13 /* Pause, hold */
			// #define XK_Scroll_Lock 0xFF14
			// #define XK_Sys_Req 0xFF15
			// #define XK_Escape 0xFF1B
			new XcbKeySymbol(0xFF1B, SpecialKeyName.ESCAPE.name(), this);
			// #define XK_Delete 0xFFFF /* Delete, rubout */
		}
		{
			/* Misc Functions */
			// #define XK_Select 0xFF60 /* Select, mark */
			// #define XK_Print 0xFF61
			// #define XK_Execute 0xFF62 /* Execute, run, do */
			// #define XK_Insert 0xFF63 /* Insert, insert here */
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

	/**
	 * 
	 * @param keysymbol
	 */
	void addXKeysymbol(final XcbKeySymbol keysymbol) {
		if (this.xKeysymbolCodes.containsValue(keysymbol)
				|| this.xKeysymbolTextValues.containsValue(keysymbol)) {
			throw new AssertionError("XKeySymbol already registered");
		}
		this.xKeysymbolCodes.put(keysymbol.getSymbolCode(), keysymbol);
		this.xKeysymbolTextValues.put(keysymbol.stringValue(), keysymbol);
	}

	/**
	 * 
	 * @param displayPointer
	 * @return
	 */
	private long allocateNativeXKeysymbols(final Long displayPointer) {
		final long nativeXkeySymbolsPeer = Xcb4J
				.nativeAllocateKeysyms(displayPointer.longValue());
		return nativeXkeySymbolsPeer;
	}

	@Override
	protected void finalize() throws Throwable {
		freeNativeXKeysymbols(this.nativeXKeySymbolsPeer);
		super.finalize();
	}

	/**
	 * 
	 * @param nativeXkeySymbolsPeer
	 */
	private void freeNativeXKeysymbols(final long nativeXkeySymbolsPeer) {
		Xcb4J.nativeFreeKeysyms(nativeXkeySymbolsPeer);
	}

	@Override
	public XcbKeySymbol getKeySymbol(final Key baseKey,
			final InputModifiers baseInputModifiers) {

		final Integer xKeyCode = Integer.valueOf(baseKey.getKeyCode());

		final Integer xModifiersMask = Integer.valueOf(baseInputModifiers
				.getInputModifiersMask());

		final Map<Integer, XcbKeySymbol> keySymbolCombos;

		// check keysymbol cache
		if (this.xresolvedKeySymbolsCach.containsKey(xKeyCode)) {
			// We retrieve the key symbol columns for the given key code.
			keySymbolCombos = this.xresolvedKeySymbolsCach.get(xKeyCode);

			if (!keySymbolCombos.containsKey(xModifiersMask)) {
				// The key symbol combo map is not filled yet with data. We fill
				// it with correct values.
				final XcbKeySymbol keySymbol = updateKeySymbolColumns(
						keySymbolCombos, baseKey, xModifiersMask);
				return keySymbol;
			}
			// We retrieve the correct key symbol from the key symbol
			// column, based on the given modifiers.
			final XcbKeySymbol keySymbol = keySymbolCombos.get(xModifiersMask);
			return keySymbol;
		} else {
			// No key symbol columns exist yet for the given key code. We create
			// key symbol columns and do a native call to fill it with correct
			// values.
			keySymbolCombos = new HashMap<Integer, XcbKeySymbol>();
			final XcbKeySymbol keySymbol = updateKeySymbolColumns(
					keySymbolCombos, baseKey, xModifiersMask);
			this.xresolvedKeySymbolsCach.put(xKeyCode, keySymbolCombos);
			return keySymbol;
		}
	}

	private XcbKeySymbol updateKeySymbolColumns(
			final Map<Integer, XcbKeySymbol> keySymbolCombos,
			final Key baseKey, final Integer modifiersMask) {
		final long keysymbolCode = getKeysymbolCode(baseKey, modifiersMask);
		final XcbKeySymbol keySymbol = this.getKeysymbol(keysymbolCode);
		keySymbolCombos.put(modifiersMask, keySymbol);
		return keySymbol;
	}

	private long getKeysymbolCode(final Key baseKey, final Integer modifiers) {
		// TODO get keysymbol code from keycode and modifiers from native call

		long k0, k1;

		/*
		 * 'col' (third parameter) is used to get the proper KeySym according to
		 * modifier (XCB doesn't provide an equivalent to XLookupString()).
		 * 
		 * If Mod5 is ON we look into second group.
		 */
		if ((modifiers & XModifier.MOD5_MODIFIER.getModifierMask()) != 0) {
			k0 = this.xCoreInterface.getXKeySymbol(this.nativeXKeySymbolsPeer,
					baseKey, 4);
			k1 = this.xCoreInterface.getXKeySymbol(this.nativeXKeySymbolsPeer,
					baseKey, 5);
		} else {
			k0 = this.xCoreInterface.getXKeySymbol(this.nativeXKeySymbolsPeer,
					baseKey, 0);
			k1 = this.xCoreInterface.getXKeySymbol(this.nativeXKeySymbolsPeer,
					baseKey, 1);
		}

		/* If the second column does not exists use the first one. */
		if (k1 == 0) {
			k1 = k0;
		}

		/*
		 * The numlock modifier is on and the second KeySym is a keypad KeySym
		 */
		if (((modifiers & XModifier.MOD2_MODIFIER.getModifierMask()) != 0)
				&& isKeypadKey(k1)) {
			/*
			 * The Shift modifier is on, or if the Lock modifier is on and is
			 * interpreted as ShiftLock, use the first KeySym
			 */
			if (((modifiers & XModifier.SHIFT_MODIFIER.getModifierMask()) != 0)
					|| ((modifiers & XModifier.LOCK_MODIFIER.getModifierMask()) != 0)) {
				return k0;
			} else {
				return k1;
			}
		}

		/*
		 * The Shift and Lock modifers are both off, use the first KeySym
		 */
		else if (((modifiers & XModifier.SHIFT_MODIFIER.getModifierMask()) == 0)
				&& ((modifiers & XModifier.LOCK_MODIFIER.getModifierMask()) == 0)) {
			return k0;
		} else if (((modifiers & XModifier.SHIFT_MODIFIER.getModifierMask()) == 0)
				&& ((modifiers & XModifier.LOCK_MODIFIER.getModifierMask()) != 0)) {
			/*
			 * The first Keysym is used but if that KeySym is lowercase
			 * alphabetic, then the corresponding uppercase KeySym is used
			 * instead
			 */
			return k1;
		} else if (((modifiers & XModifier.SHIFT_MODIFIER.getModifierMask()) != 0)
				&& ((modifiers & XModifier.LOCK_MODIFIER.getModifierMask()) != 0)) {
			/*
			 * The second Keysym is used but if that KeySym is lowercase
			 * alphabetic, then the corresponding uppercase KeySym is used
			 * instead
			 */
			return k1;
		} else if (((modifiers & XModifier.SHIFT_MODIFIER.getModifierMask()) != 0)
				|| ((modifiers & XModifier.LOCK_MODIFIER.getModifierMask()) != 0)) {
			return k1;
		}

		return 0;
	}

	private boolean isKeypadKey(final long keySymbolCode) {
		return (keySymbolCode >= 0xFF80) && (keySymbolCode <= 0xFFB9);
	}

	private XcbKeySymbol initKeySymbolFromCode(final long keySymCode) {
		// FIXME needs better conversion

		if ((keySymCode >= 0x00) && (keySymCode <= 0xFF)) {
			// Latin-1 code points represent themselves (0x00-0xFF).
			final XcbKeySymbol keySymbol = new XcbKeySymbol(keySymCode,
					String.valueOf((char) keySymCode), this);
			return keySymbol;
		} else if ((keySymCode & 0x1000000) != 0) {
			// Other (non-Latin-1) Unicode code points have an offset of
			// 0x1000000,*except* when they don't.

			// we convert to 32bit unicode code point.
			final XcbKeySymbol keySymbol = new XcbKeySymbol(keySymCode,
					String.valueOf(Character
							.toChars((int) (keySymCode & 0x000FFFFF))), this);
			return keySymbol;
		} else {
			return null;
		}
	}

	private XcbKeySymbol initKeySymbolFromChars(final String keySymbolChars) {
		// FIXME needs better conversion

		if (keySymbolChars.length() == 1) {
			final char keySymbolChar = keySymbolChars.charAt(0);

			if ((keySymbolChar >= 0x00) && (keySymbolChar <= 0xFF)) {
				// Latin-1 code points represent themselves (0x00-0xFF).
				final XcbKeySymbol keySymbol = new XcbKeySymbol(keySymbolChar,
						keySymbolChars, this);
				return keySymbol;
			} else {
				// Other (non-Latin-1) Unicode code points have an offset of
				// 0x1000000,*except* when they don't.

				// we convert to 32bit unicode code point.
				final long keySymbolCode = keySymbolChar | 0x1000000;
				final XcbKeySymbol keySymbol = new XcbKeySymbol(keySymbolCode,
						keySymbolChars, this);
				return keySymbol;
			}
		} else if (keySymbolChars.length() > 1) {

			// TODO check if keysymbol exists as non single char key (ie, arrow
			// up, alt, etc)
			return null;
		} else {
			return null;
		}
	}

	@Override
	public XcbKeySymbol getKeysymbol(final String textValue) {
		if (this.xKeysymbolTextValues.containsKey(textValue)) {
			return this.xKeysymbolTextValues.get(textValue);
		} else {
			final XcbKeySymbol keySymbol = initKeySymbolFromChars(textValue);
			this.xKeysymbolTextValues.put(textValue, keySymbol);
			return keySymbol;
		}
	}

	@Override
	public XcbKeySymbol getKeysymbol(final long keySymbolPlatformCode) {
		final Long keySymbolplatformCode = Long.valueOf(keySymbolPlatformCode);
		if (this.xKeysymbolCodes.containsKey(keySymbolplatformCode)) {
			return this.xKeysymbolCodes.get(keySymbolplatformCode);
		} else {
			final XcbKeySymbol keySymbol = initKeySymbolFromCode(keySymbolPlatformCode);
			this.xKeysymbolCodes.put(keySymbolplatformCode, keySymbol);
			return keySymbol;
		}
	}

	@Override
	public Key[] getKeys(final XKeySymbol keySymbol) {
		return this.xCoreInterface.getXKeyCodes(this.nativeXKeySymbolsPeer,
				keySymbol);
	}
}
