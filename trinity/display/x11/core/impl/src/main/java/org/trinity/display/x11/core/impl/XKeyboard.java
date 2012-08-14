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
package org.trinity.display.x11.core.impl;

import java.util.ArrayList;
import java.util.List;

import org.trinity.foundation.input.api.InputModifier;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.input.api.Keyboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class XKeyboard implements Keyboard {

	private final XKeySymbolMapping xKeySymbolMapping;
	private final XKeySymbolCache xKeySymbolCache;
	private final XInputModifierMaskMapping xInputModifierMaskMapping;

	@Inject
	XKeyboard(	final XKeySymbolMapping xKeySymbolMapping,
				final XKeySymbolCache xKeySymbolCache,
				final XInputModifierMaskMapping xInputModifierMaskMapping) {
		this.xKeySymbolMapping = xKeySymbolMapping;
		this.xKeySymbolCache = xKeySymbolCache;
		this.xInputModifierMaskMapping = xInputModifierMaskMapping;
	}

	@Override
	public String asKeySymbolName(	final Key key,
									final InputModifiers inputModifiers) {

		final int keyCode = key.getKeyCode();
		final int inputModifiersState = inputModifiers.getInputModifiersState();

		final Integer keySymbol = this.xKeySymbolCache
				.getKeySymbol(keyCode, inputModifiersState);

		final String keySymbolName = this.xKeySymbolMapping.toString(keySymbol);
		return keySymbolName;
	}

	@Override
	public List<Key> asKeys(final String keySymbolName) {
		final Integer keySymbol = this.xKeySymbolMapping
				.toKeySymbol(keySymbolName);
		final List<Integer> keyCodes = this.xKeySymbolCache
				.getKeyCodes(keySymbol);

		final List<Key> keys = new ArrayList<Key>(keyCodes.size());
		for (final Integer keyCode : keyCodes) {
			final Key key = new Key(keyCode.intValue());
			keys.add(key);
		}
		return keys;
	}

	@Override
	public InputModifier modifier(final String modifierName) {
		final int mask = this.xInputModifierMaskMapping
				.getXInputModifierMask(modifierName);
		final XInputModifier xInputModifier = new XInputModifier(	mask,
																	modifierName);
		return xInputModifier;
	}

	@Override
	public InputModifiers modifiers(final String... modifierNames) {
		int inputModifiersState = 0;
		for (final String modifierName : modifierNames) {
			inputModifiersState |= this.xInputModifierMaskMapping
					.getXInputModifierMask(modifierName);
		}
		return new InputModifiers(inputModifiersState);
	}
}