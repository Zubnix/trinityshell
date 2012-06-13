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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fusion.x11.core.input.XKeySymbol;
import org.fusion.x11.core.input.XModifier;
import org.fusion.x11.core.xcb.XcbCoreInterfaceImpl;
import org.fusion.x11.core.xcb.error.UnknownKeyException;
import org.trinity.core.input.impl.BaseInputModifiers;
import org.trinity.display.x11.api.XKeyboard;
import org.trinity.display.x11.impl.XServerImpl;
import org.trinity.foundation.input.api.InputModifierName;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.input.api.Modifier;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XcbKeyboard implements XKeyboard {

	private final XServerImpl display;

	private final Map<String, Key[]> keyCache;
	private final Map<InputModifierName, XModifier> modifiers;

	private final XcbKeySymbolRegistry keySymbolRegistry;

	/**
	 * 
	 * @param display
	 * @param displayInterface
	 */
	public XcbKeyboard(final XServerImpl display,
			final XcbCoreInterfaceImpl displayInterface) {
		this.keyCache = new HashMap<String, Key[]>();

		// fill map with keyname<->modifier
		this.modifiers = new HashMap<InputModifierName, XModifier>();
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
	public XServerImpl getDisplay() {
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
	public XModifier modifier(final InputModifierName modifierKeyName) {
		return this.modifiers.get(modifierKeyName);
	}

	@Override
	public String keyName(final Key baseKey,
			final InputModifiers baseInputModifiers) {
		final XcbKeySymbol keySymbol = this.keySymbolRegistry.getKeySymbol(
				baseKey, baseInputModifiers);
		if (keySymbol == null) {
			// TODO keys without a predefined symbol (modifiers keys)
			throw new UnknownKeyException(baseKey, baseInputModifiers);
		}
		return keySymbol.stringValue();
	}

	@Override
	public InputModifiers modifiers(final InputModifierName... modifierKeyNames) {
		final List<Modifier> modifiers = new ArrayList<Modifier>(
				modifierKeyNames.length);
		for (final InputModifierName modifierKeyName : modifierKeyNames) {
			modifiers.add(modifier(modifierKeyName));
		}
		final InputModifiers baseInputModifiers = new BaseInputModifiers(
				modifiers.toArray(new Modifier[] {}));

		return baseInputModifiers;
	}
}
