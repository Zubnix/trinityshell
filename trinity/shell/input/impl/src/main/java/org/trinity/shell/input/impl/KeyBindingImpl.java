/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.trinity.shell.input.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.trinity.foundation.input.api.InputModifierName;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.input.api.Keyboard;
import org.trinity.foundation.input.api.Modifier;
import org.trinity.foundation.input.api.Momentum;
import org.trinity.foundation.input.api.SpecialKeyName;
import org.trinity.foundation.input.api.event.KeyNotifyEvent;
import org.trinity.shell.core.api.RenderArea;
import org.trinity.shell.input.api.KeyBinding;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

// TODO documentation
/**
 * A <code>KeyBinding</code> binds a {@link BaseKey} with an optional
 * {@link Modifier} to an action. A <code>Key</code> is identified by it's name.
 * <code>Key</code> names are the same as the unmodified character they produce
 * when pressed. A list of special <code>Key</code> names can be found in
 * {@link SpecialKeyName} . A list of <code>Modifier</code>s can be found in
 * {@link InputModifierName}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class KeyBindingImpl implements KeyBinding {

	// Modifiers who's state should be ignored when checking for a key
	// binding.
	// TODO capslock, scrolllock, ...
	private static final InputModifierName[] IGNORED_MODIFIERS = new InputModifierName[] {
			InputModifierName.MOD_LOCK, InputModifierName.MOD_2 };

	// TODO release keybinding function

	private final EventBus eventBus;
	private final RenderArea root;
	private final Keyboard keyboard;

	private final String keyName;
	private final InputModifierName[] modKeyNames;
	private final Momentum momentum;

	private final Runnable runnable;

	private final InputModifiers inputModifiers;
	private final Key[] validKeys;

	@Inject
	protected KeyBindingImpl(	@Named("displayEventBus") final EventBus eventBus,
								@Named("root") final RenderArea root,
								final Keyboard keyboard,
								@Assisted final Runnable runnable,
								@Assisted final Momentum momentum,
								@Assisted final String keyName,
								@Assisted final boolean ignoreOftenUsedModifiers,
								@Assisted final InputModifierName... modKeyNames) {
		this.eventBus = eventBus;
		this.root = root;
		this.keyboard = keyboard;

		this.keyName = keyName;
		this.modKeyNames = modKeyNames;
		this.momentum = momentum;

		this.runnable = runnable;

		// translate keyname and modkeynames to keys and modmask
		this.validKeys = this.keyboard.keys(getKeyName());

		final List<InputModifiers> validInputModifiersCombinations = new LinkedList<InputModifiers>();

		this.inputModifiers = this.modKeyNames.length == 0 ? new InputModifiers(0)
				: this.keyboard.modifiers(this.modKeyNames);
		if (ignoreOftenUsedModifiers) {
			// install keybindings with variations on often active modifiers

			// This will only install an extra keybinding on an invidual
			// item from the IGNORED_MODIFIERS array.
			// FIXME do a full permutation
			for (final InputModifierName ignoredModifier : KeyBindingImpl.IGNORED_MODIFIERS) {
				final Modifier modifier = this.keyboard
						.modifier(ignoredModifier);

				final int modifierMaskWithExtraModifier = this.inputModifiers
						.getInputModifiersMask() & modifier.getModifierMask();
				final InputModifiers inputModifiersWithExtraModifier = new InputModifiers(modifierMaskWithExtraModifier);
				validInputModifiersCombinations
						.add(inputModifiersWithExtraModifier);
			}

		}
		validInputModifiersCombinations.add(this.inputModifiers);

		this.eventBus.register(this);

		for (final Key validKey : this.validKeys) {
			// install a keygrab
			this.keyboard
					.catchKeyboardInput(this.root.getPlatformRenderArea(),
										validKey,
										this.inputModifiers);
		}
	}

	@Subscribe
	public void handleKeyNotifyEvent(final KeyNotifyEvent event) {
		if (event.getInput().getMomentum() == getMomentum()) {
			for (final Key validKey : this.validKeys) {
				if (validateAction(this.inputModifiers, validKey, event)) {
					break;
				}
			}
		}
	}

	public boolean validateAction(	final InputModifiers inputModifiers,
									final Key validKey,
									final KeyNotifyEvent event) {
		final short eventKeyCode = (short) event.getInput().getKey()
				.getKeyCode();
		final short validKeyCode = (short) validKey.getKeyCode();
		final boolean gotValidKey = eventKeyCode == validKeyCode;

		final int eventModifiersMask = event.getInput().getModifiers()
				.getInputModifiersMask();
		final int validEventModifiersMask = inputModifiers
				.getInputModifiersMask();

		final boolean gotValidModifiers = eventModifiersMask == validEventModifiersMask;

		final Momentum eventMomentum = event.getInput().getMomentum();
		final Momentum validMomentum = KeyBindingImpl.this.getMomentum();
		final boolean gotValidMomentum = eventMomentum == validMomentum;

		if (gotValidKey && gotValidModifiers && gotValidMomentum) {
			KeyBindingImpl.this.performAction();
			return true;
		}
		return false;
	}

	@Override
	public String getKeyName() {
		return this.keyName;
	}

	@Override
	public InputModifierName[] getInputModifierNames() {
		return Arrays.copyOf(this.modKeyNames, this.modKeyNames.length);
	}

	@Override
	public Momentum getMomentum() {
		return this.momentum;
	}

	@Override
	public void performAction() {
		this.runnable.run();
	}
}
