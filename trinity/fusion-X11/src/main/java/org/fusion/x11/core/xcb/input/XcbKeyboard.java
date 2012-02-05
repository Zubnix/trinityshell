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

import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.input.XKeySymbol;
import org.fusion.x11.core.input.XKeyboard;
import org.fusion.x11.core.input.XModifier;
import org.fusion.x11.core.xcb.XcbCoreInterfaceImpl;
import org.fusion.x11.core.xcb.error.UnknownKeyException;
import org.hydrogen.displayinterface.input.InputModifiers;
import org.hydrogen.displayinterface.input.Key;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XcbKeyboard implements XKeyboard {

	private final XDisplay display;

	private final Map<String, Key[]> keyCache;
	private final Map<ModifierName, XModifier> modifiers;

	private final XcbKeySymbolRegistry keySymbolRegistry;

	/**
	 * 
	 * @param display
	 * @param displayInterface
	 */
	public XcbKeyboard(final XDisplay display,
			final XcbCoreInterfaceImpl displayInterface) {
		this.keyCache = new HashMap<String, Key[]>();

		// fill map with keyname<->modifier
		this.modifiers = new HashMap<ModifierName, XModifier>();
		for (final XModifier xModifier : XModifier.values()) {
			this.modifiers.put(xModifier.getModifierName(), xModifier);
		}

		this.display = display;
		this.keySymbolRegistry = new XcbKeySymbolRegistry(display,
				displayInterface);
	}

	/**
	 * 
	 * @return
	 */
	public XDisplay getDisplay() {
		return this.display;
	}

	@Override
	public Key[] keys(final String keyChars) {
		if (this.keyCache.containsKey(keyChars)) {
			return this.keyCache.get(keyChars);
		} else {
			// TODO translate string to keycode and store it in key cache.
			final XKeySymbol keySymbol = this.keySymbolRegistry
					.getKeysymbol(keyChars);
			final Key[] keys = this.keySymbolRegistry.getKeys(keySymbol);
			this.keyCache.put(keyChars, keys);
			return keys;
		}
	}

	@Override
	public XModifier modifier(final ModifierName modifierKeyName) {
		return this.modifiers.get(modifierKeyName);
	}

	@Override
	public String keyName(final Key key, final InputModifiers inputModifiers) {
		final XcbKeySymbol keySymbol = this.keySymbolRegistry.getKeySymbol(key,
				inputModifiers);
		if (keySymbol == null) {
			// TODO keys without a predefined symbol (modifiers keys)
			throw new UnknownKeyException(key, inputModifiers);
		}
		return keySymbol.stringValue();
	}

	@Override
	public InputModifiers modifiers(final ModifierName... modifierKeyNames) {
		final InputModifiers inputModifiers = new InputModifiers();
		for (final ModifierName modifierKeyName : modifierKeyNames) {
			inputModifiers.setModifiers(modifier(modifierKeyName));
		}
		return inputModifiers;
	}
}
