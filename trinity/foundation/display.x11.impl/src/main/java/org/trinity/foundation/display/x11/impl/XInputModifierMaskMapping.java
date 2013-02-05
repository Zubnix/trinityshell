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

import java.util.HashMap;
import java.util.Map;

import org.freedesktop.xcb.xcb_mod_mask_t;
import org.trinity.foundation.api.display.input.InputModifier;

import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(Type.IMPLEMENTATION))
@Singleton
public class XInputModifierMaskMapping {
	private final Map<String, Integer> nameToXInputModifierMask = new HashMap<String, Integer>();

	XInputModifierMaskMapping() {
		this.nameToXInputModifierMask.put(	InputModifier.MOD_1,
											Integer.valueOf(xcb_mod_mask_t.XCB_MOD_MASK_1));
		this.nameToXInputModifierMask.put(	InputModifier.MOD_2,
											Integer.valueOf(xcb_mod_mask_t.XCB_MOD_MASK_1));
		this.nameToXInputModifierMask.put(	InputModifier.MOD_3,
											Integer.valueOf(xcb_mod_mask_t.XCB_MOD_MASK_1));
		this.nameToXInputModifierMask.put(	InputModifier.MOD_4,
											Integer.valueOf(xcb_mod_mask_t.XCB_MOD_MASK_1));
		this.nameToXInputModifierMask.put(	InputModifier.MOD_5,
											Integer.valueOf(xcb_mod_mask_t.XCB_MOD_MASK_1));
		this.nameToXInputModifierMask.put(	InputModifier.MOD_ANY,
											Integer.valueOf(xcb_mod_mask_t.XCB_MOD_MASK_1));
		this.nameToXInputModifierMask.put(	InputModifier.MOD_CTRL,
											Integer.valueOf(xcb_mod_mask_t.XCB_MOD_MASK_1));
		this.nameToXInputModifierMask.put(	InputModifier.MOD_LOCK,
											Integer.valueOf(xcb_mod_mask_t.XCB_MOD_MASK_1));
		this.nameToXInputModifierMask.put(	InputModifier.MOD_SHIFT,
											Integer.valueOf(xcb_mod_mask_t.XCB_MOD_MASK_1));
	}

	public int getXInputModifierMask(final String inputModifierName) {
		final Integer mask = this.nameToXInputModifierMask.get(inputModifierName);
		if (mask == null) {
			throw new IllegalArgumentException(String.format(	"InputModifier name %s not known. Known modifier names are: %s",
																inputModifierName,
																this.nameToXInputModifierMask.keySet()));
		}
		return mask.intValue();
	}
}
