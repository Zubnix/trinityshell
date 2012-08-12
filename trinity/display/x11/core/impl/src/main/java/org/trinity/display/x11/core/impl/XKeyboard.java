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

import org.trinity.foundation.input.api.InputModifierName;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.input.api.Keyboard;
import org.trinity.foundation.input.api.Modifier;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class XKeyboard implements Keyboard {

	private final XConnection xConnection;

	@Inject
	XKeyboard(final XConnection xConnection) {
		this.xConnection = xConnection;
	}

	@Override
	public String keyName(final Key key, final InputModifiers inputModifiers) {

	}

	@Override
	public Key[] keys(final String keyName) {

	}

	@Override
	public Modifier modifier(final InputModifierName modifierKeyName) {

	}

	@Override
	public InputModifiers modifiers(final InputModifierName... modifierKeyNames) {

	}
}
