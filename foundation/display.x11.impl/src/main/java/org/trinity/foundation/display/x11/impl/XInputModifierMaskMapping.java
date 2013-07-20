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

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;
import static org.freedesktop.xcb.xcb_mod_mask_t.XCB_MOD_MASK_1;
import static org.freedesktop.xcb.xcb_mod_mask_t.XCB_MOD_MASK_2;
import static org.freedesktop.xcb.xcb_mod_mask_t.XCB_MOD_MASK_3;
import static org.freedesktop.xcb.xcb_mod_mask_t.XCB_MOD_MASK_4;
import static org.freedesktop.xcb.xcb_mod_mask_t.XCB_MOD_MASK_5;
import static org.freedesktop.xcb.xcb_mod_mask_t.XCB_MOD_MASK_ANY;
import static org.freedesktop.xcb.xcb_mod_mask_t.XCB_MOD_MASK_CONTROL;
import static org.freedesktop.xcb.xcb_mod_mask_t.XCB_MOD_MASK_LOCK;
import static org.freedesktop.xcb.xcb_mod_mask_t.XCB_MOD_MASK_SHIFT;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.display.input.InputModifier;

import com.google.inject.Singleton;
import org.trinity.foundation.api.shared.ExecutionContext;

@To(IMPLEMENTATION)
@Singleton
@ExecutionContext(DisplayExecutor.class)
@Immutable
public class XInputModifierMaskMapping {
	private final Map<String, Integer> nameToXInputModifierMask = new ConcurrentHashMap<String, Integer>();

	XInputModifierMaskMapping() {
		this.nameToXInputModifierMask.put(	InputModifier.MOD_1,
											XCB_MOD_MASK_1);
		this.nameToXInputModifierMask.put(	InputModifier.MOD_2,
											XCB_MOD_MASK_2);
		this.nameToXInputModifierMask.put(	InputModifier.MOD_3,
											XCB_MOD_MASK_3);
		this.nameToXInputModifierMask.put(	InputModifier.MOD_4,
											XCB_MOD_MASK_4);
		this.nameToXInputModifierMask.put(	InputModifier.MOD_5,
											XCB_MOD_MASK_5);
		this.nameToXInputModifierMask.put(	InputModifier.MOD_ANY,
											XCB_MOD_MASK_ANY);
		this.nameToXInputModifierMask.put(	InputModifier.MOD_CTRL,
											XCB_MOD_MASK_CONTROL);
		this.nameToXInputModifierMask.put(	InputModifier.MOD_LOCK,
											XCB_MOD_MASK_LOCK);
		this.nameToXInputModifierMask.put(	InputModifier.MOD_SHIFT,
											XCB_MOD_MASK_SHIFT);
	}

	public int getXInputModifierMask(@Nonnull final String inputModifierName) {
		final Integer mask = this.nameToXInputModifierMask.get(inputModifierName);
		if (mask == null) {
			throw new IllegalArgumentException(String.format(	"InputModifier name %s not known. Known modifier names are: %s",
																inputModifierName,
																this.nameToXInputModifierMask.keySet()));
		}
		return mask.intValue();
	}
}
