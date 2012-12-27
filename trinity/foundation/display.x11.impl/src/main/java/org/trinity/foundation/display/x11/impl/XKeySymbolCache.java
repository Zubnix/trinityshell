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

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import xcb.LibXcb;
import xcb.LibXcbConstants;
import xcb.SWIGTYPE_p__XCBKeySymbols;
import xcb.xcb_mapping_notify_event_t;
import xcb.xcb_mod_mask_t;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(Type.IMPLEMENTATION))
@Singleton
public class XKeySymbolCache {

	private final Map<Integer, Map<Integer, Integer>> resolvedKeySymbols = new HashMap<Integer, Map<Integer, Integer>>();

	private final SWIGTYPE_p__XCBKeySymbols xcbKeySymbols;

	@Inject
	XKeySymbolCache(final XConnection xConnection, @Named("xEventBus") final EventBus xEventBus) {
		this.xcbKeySymbols = LibXcb.xcb_key_symbols_alloc(xConnection.getConnectionReference());
		xEventBus.register(this);
	}

	@Subscribe
	public void handleMappingNotify(final xcb_mapping_notify_event_t mapping_notify_event_t) {
		this.resolvedKeySymbols.clear();
	}

	public Integer getKeySymbol(final int keyCode,
								final int inputModifiersState) {
		final Integer key = Integer.valueOf(keyCode);
		final Map<Integer, Integer> keyResolutions = getResolutionsForKey(key);

		final Integer inputModifiersCode = Integer.valueOf(inputModifiersState);
		Integer keySymbol = keyResolutions.get(inputModifiersCode);

		if (keySymbol == null) {
			keySymbol = Integer.valueOf(resolveKeySymbol(	key,
															inputModifiersState));
			keyResolutions.put(	inputModifiersCode,
								keySymbol);
		}
		return keySymbol;
	}

	public List<Integer> getKeyCodes(final Integer keySymbol) {
		final ByteBuffer keys = LibXcb.xcb_key_symbols_get_keycode(	this.xcbKeySymbols,
																	keySymbol.intValue());
		final List<Integer> keyCodes = new LinkedList<Integer>();
		final Integer keyCode = null;
		while (Integer.valueOf(keys.getInt()).intValue() != LibXcbConstants.XCB_NO_SYMBOL) {
			keyCodes.add(keyCode);
		}
		return keyCodes;
	}

	private Map<Integer, Integer> getResolutionsForKey(final Integer key) {
		Map<Integer, Integer> keyResolutions = this.resolvedKeySymbols.get(key);
		if (keyResolutions == null) {
			keyResolutions = new HashMap<Integer, Integer>();
			this.resolvedKeySymbols.put(key,
										keyResolutions);
		}
		return keyResolutions;
	}

	private int resolveKeySymbol(	final int key,
									final int modifiers) {
		int k0, k1;
		// 'col' (third parameter) is used to get the proper KeySym according to
		// modifier (XCB doesn't provide an equivalent to XLookupString()). If
		// Mod5 is ON we look into second group.
		if ((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_5) != 0) {
			k0 = LibXcb.xcb_key_symbols_get_keysym(	this.xcbKeySymbols,
													(short) key,
													4);
			k1 = LibXcb.xcb_key_symbols_get_keysym(	this.xcbKeySymbols,
													(short) key,
													5);
		} else {
			k0 = LibXcb.xcb_key_symbols_get_keysym(	this.xcbKeySymbols,
													(short) key,
													0);
			k1 = LibXcb.xcb_key_symbols_get_keysym(	this.xcbKeySymbols,
													(short) key,
													1);
		}
		// If the second column does not exists use the first one.
		if (k1 == 0) {
			k1 = k0;
		}
		// The numlock modifier is on and the second KeySym is a keypad KeySym
		if (((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_2) != 0) && (LibXcb.xcb_is_keypad_key(k1) != 0)) {
			// The Shift modifier is on, or if the Lock modifier is on and is
			// interpreted as ShiftLock, use the first KeySym
			if (((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_SHIFT) != 0)
					|| ((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_LOCK) != 0)) {
				return k0;
			} else {
				return k1;
			}
		}
		// The Shift and Lock modifers are both off, use the first KeySym
		else if (((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_SHIFT) == 0)
				&& ((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_LOCK) == 0)) {
			return k0;
		} else if (((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_SHIFT) == 0)
				&& ((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_LOCK) != 0)) {
			// The first Keysym is used but if that KeySym is lowercase
			// alphabetic, then the corresponding uppercase KeySym is used
			// instead
			return k1;
		} else if (((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_SHIFT) != 0)
				&& ((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_LOCK) != 0)) {
			// The second Keysym is used but if that KeySym is lowercase
			// alphabetic, then the corresponding uppercase KeySym is used
			// instead
			return k1;
		} else if (((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_SHIFT) != 0)
				|| ((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_LOCK) != 0)) {
			return k1;
		}
		// unknown
		return 0;
	}

	@Override
	protected void finalize() throws Throwable {
		LibXcb.xcb_key_symbols_free(this.xcbKeySymbols);
		super.finalize();
	}
}