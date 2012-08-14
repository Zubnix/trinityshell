package org.trinity.display.x11.core.impl;

import java.util.HashMap;
import java.util.Map;

import org.trinity.foundation.input.api.InputModifier;

import xcbjb.xcb_mod_mask_t;

import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(Type.IMPLEMENTATION))
@Singleton
public class XInputModifierMaskMapping {
	private final Map<String, Integer> nameToXInputModifierMask = new HashMap<String, Integer>();

	XInputModifierMaskMapping() {
		this.nameToXInputModifierMask.put(InputModifier.MOD_1, Integer
				.valueOf(xcb_mod_mask_t.XCB_MOD_MASK_1.swigValue()));
		this.nameToXInputModifierMask.put(InputModifier.MOD_2, Integer
				.valueOf(xcb_mod_mask_t.XCB_MOD_MASK_1.swigValue()));
		this.nameToXInputModifierMask.put(InputModifier.MOD_3, Integer
				.valueOf(xcb_mod_mask_t.XCB_MOD_MASK_1.swigValue()));
		this.nameToXInputModifierMask.put(InputModifier.MOD_4, Integer
				.valueOf(xcb_mod_mask_t.XCB_MOD_MASK_1.swigValue()));
		this.nameToXInputModifierMask.put(InputModifier.MOD_5, Integer
				.valueOf(xcb_mod_mask_t.XCB_MOD_MASK_1.swigValue()));
		this.nameToXInputModifierMask.put(InputModifier.MOD_ANY, Integer
				.valueOf(xcb_mod_mask_t.XCB_MOD_MASK_1.swigValue()));
		this.nameToXInputModifierMask.put(InputModifier.MOD_CTRL, Integer
				.valueOf(xcb_mod_mask_t.XCB_MOD_MASK_1.swigValue()));
		this.nameToXInputModifierMask.put(InputModifier.MOD_LOCK, Integer
				.valueOf(xcb_mod_mask_t.XCB_MOD_MASK_1.swigValue()));
		this.nameToXInputModifierMask.put(InputModifier.MOD_SHIFT, Integer
				.valueOf(xcb_mod_mask_t.XCB_MOD_MASK_1.swigValue()));
	}

	public int getXInputModifierMask(final String inputModifierName) {
		final Integer mask = this.nameToXInputModifierMask
				.get(inputModifierName);
		if (mask == null) {
			throw new IllegalArgumentException(String.format(	"InputModifier name %s not known. Known modifier names are: %s",
																inputModifierName,
																this.nameToXInputModifierMask
																		.keySet()));
		}
		return mask.intValue();
	}
}
