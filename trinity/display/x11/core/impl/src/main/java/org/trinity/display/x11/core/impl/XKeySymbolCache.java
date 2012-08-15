package org.trinity.display.x11.core.impl;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import xcbjb.LibXcb;
import xcbjb.LibXcbConstants;
import xcbjb.SWIGTYPE_p__XCBKeySymbols;
import xcbjb.xcb_mapping_notify_event_t;
import xcbjb.xcb_mod_mask_t;

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
	XKeySymbolCache(final XConnection xConnection,
					@Named("xEventBus") final EventBus xEventBus) {
		this.xcbKeySymbols = LibXcb.xcb_key_symbols_alloc(xConnection
				.getConnectionReference());
		xEventBus.register(this);
	}

	@Subscribe
	public void handleMappingNotify(final xcb_mapping_notify_event_t mapping_notify_event_t) {
		this.resolvedKeySymbols.clear();
	}

	public Integer getKeySymbol(final int keyCode, final int inputModifiersState) {
		final Integer key = Integer.valueOf(keyCode);
		final Map<Integer, Integer> keyResolutions = getResolutionsForKey(key);

		final Integer inputModifiersCode = Integer.valueOf(inputModifiersState);
		Integer keySymbol = keyResolutions.get(inputModifiersCode);

		if (keySymbol == null) {
			keySymbol = Integer
					.valueOf(resolveKeySymbol(key, inputModifiersState));
			keyResolutions.put(inputModifiersCode, keySymbol);
		}
		return keySymbol;
	}

	public List<Integer> getKeyCodes(final Integer keySymbol) {
		final ByteBuffer keys = LibXcb
				.xcb_key_symbols_get_keycode(	this.xcbKeySymbols,
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
			this.resolvedKeySymbols.put(key, keyResolutions);
		}
		return keyResolutions;
	}

	private int resolveKeySymbol(final int key, final int modifiers) {
		int k0, k1;
		// 'col' (third parameter) is used to get the proper KeySym according to
		// modifier (XCB doesn't provide an equivalent to XLookupString()). If
		// Mod5 is ON we look into second group.
		if ((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_5.swigValue()) != 0) {
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
		if (((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_2.swigValue()) != 0)
				&& (LibXcb.xcb_is_keypad_key(k1) != 0)) {
			// The Shift modifier is on, or if the Lock modifier is on and is
			// interpreted as ShiftLock, use the first KeySym
			if (((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_SHIFT.swigValue()) != 0)
					|| ((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_LOCK
							.swigValue()) != 0)) {
				return k0;
			} else {
				return k1;
			}
		}
		// The Shift and Lock modifers are both off, use the first KeySym
		else if (((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_SHIFT.swigValue()) == 0)
				&& ((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_LOCK.swigValue()) == 0)) {
			return k0;
		} else if (((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_SHIFT.swigValue()) == 0)
				&& ((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_LOCK.swigValue()) != 0)) {
			// The first Keysym is used but if that KeySym is lowercase
			// alphabetic, then the corresponding uppercase KeySym is used
			// instead
			return k1;
		} else if (((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_SHIFT.swigValue()) != 0)
				&& ((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_LOCK.swigValue()) != 0)) {
			// The second Keysym is used but if that KeySym is lowercase
			// alphabetic, then the corresponding uppercase KeySym is used
			// instead
			return k1;
		} else if (((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_SHIFT.swigValue()) != 0)
				|| ((modifiers & xcb_mod_mask_t.XCB_MOD_MASK_LOCK.swigValue()) != 0)) {
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