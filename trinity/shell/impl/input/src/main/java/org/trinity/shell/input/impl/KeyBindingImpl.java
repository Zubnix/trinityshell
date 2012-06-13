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

import org.trinity.foundation.display.api.event.KeyNotifyEvent;
import org.trinity.foundation.input.api.InputModifierName;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.InputModifiersFactory;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.input.api.Keyboard;
import org.trinity.foundation.input.api.Modifier;
import org.trinity.foundation.input.api.Momentum;
import org.trinity.foundation.input.api.SpecialKeyName;
import org.trinity.shell.foundation.api.ManagedDisplay;
import org.trinity.shell.foundation.api.event.KeyboardKeyPressedHandler;
import org.trinity.shell.foundation.api.event.KeyboardKeyReleasedHandler;
import org.trinity.shell.input.api.KeyBinding;
import org.trinity.shell.widget.api.Root;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

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

	private final ManagedDisplay managedDisplay;
	private final Root root;
	private final Keyboard keyboard;
	private final InputModifiersFactory inputModifiersFactory;

	private final String keyName;
	private final InputModifierName[] modKeyNames;
	private final Momentum momentum;

	private final Runnable runnable;

	/**
	 * @param sourceWindow
	 * @param momentum
	 * @param keyName
	 * @param ignoreOftenUsedModifiers
	 * @param modKeyNames
	 */
	@Inject
	protected KeyBindingImpl(	final ManagedDisplay managedDisplay,
								final Root root,
								final Keyboard keyboard,
								final InputModifiersFactory inputModifiersFactory,
								@Assisted final Runnable runnable,
								@Assisted final Momentum momentum,
								@Assisted final String keyName,
								@Assisted final boolean ignoreOftenUsedModifiers,
								@Assisted final InputModifierName... modKeyNames) {
		this.managedDisplay = managedDisplay;
		this.root = root;
		this.keyboard = keyboard;
		this.inputModifiersFactory = inputModifiersFactory;

		this.keyName = keyName;
		this.modKeyNames = modKeyNames;
		this.momentum = momentum;

		this.runnable = runnable;

		installKeyBindingListener(ignoreOftenUsedModifiers);
	}

	/**
	 * @param ignoreOftenUsedModifiers
	 */
	protected void installKeyBindingListener(final boolean ignoreOftenUsedModifiers) {
		// translate keyname and modkeynames to keys and modmask

		final Key[] validKeys = this.keyboard.keys(getKeyName());

		final List<InputModifiers> validInputModifiersCombinations = new LinkedList<InputModifiers>();

		final InputModifiers inputModifiers = this.modKeyNames.length == 0 ? this.inputModifiersFactory
				.createInputModifiers(0) : this.keyboard
				.modifiers(this.modKeyNames);
		if (ignoreOftenUsedModifiers) {
			// install keybindings with variations on often active modifiers

			// This will only install an extra keybinding on an invidual
			// item from the IGNORED_MODIFIERS array.
			// TODO full permutation
			for (final InputModifierName ignoredModifier : KeyBindingImpl.IGNORED_MODIFIERS) {
				final Modifier modifier = this.keyboard
						.modifier(ignoredModifier);

				final int modifierMaskWithExtraModifier = inputModifiers
						.getInputModifiersMask() & modifier.getModifierMask();
				final InputModifiers inputModifiersWithExtraModifier = this.inputModifiersFactory
						.createInputModifiers(modifierMaskWithExtraModifier);
				validInputModifiersCombinations
						.add(inputModifiersWithExtraModifier);
			}

		}
		validInputModifiersCombinations.add(inputModifiers);

		for (final Key validKey : validKeys) {
			// install a keygrab
			this.root.getPlatformRenderArea()
					.catchKeyboardInput(validKey, inputModifiers);

			if (getMomentum() == Momentum.STARTED) {
				this.managedDisplay
						.addDisplayEventHandler(new KeyboardKeyPressedHandler() {
							@Override
							public void handleEvent(final KeyNotifyEvent event) {
								validateAction(inputModifiers, validKey, event);
							}
						});
			} else {
				this.managedDisplay
						.addDisplayEventHandler(new KeyboardKeyReleasedHandler() {
							@Override
							public void handleEvent(final KeyNotifyEvent event) {
								validateAction(inputModifiers, validKey, event);
							}
						});
			}

			// getManagedDisplay()
			// .addEventHandler(
			// new EventHandler<KeyNotifyEvent>() {
			// @Override
			// public void handleEvent(
			// final KeyNotifyEvent event) {
			//
			// }
			// },
			// getMomentum() == Momentum.STARTED ? DisplayEventType.KEY_PRESSED
			// : DisplayEventType.KEY_RELEASED, 0);

		}
	}

	private void validateAction(final InputModifiers inputModifiers,
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
		}
	}

	/**
	 * @return
	 */
	@Override
	public String getKeyName() {
		return this.keyName;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.shell.input.api.KeyBinding#getInputModifierNames()
	 */
	@Override
	public InputModifierName[] getInputModifierNames() {
		return Arrays.copyOf(this.modKeyNames, this.modKeyNames.length);
	}

	/**
	 * @return
	 */
	@Override
	public Momentum getMomentum() {
		return this.momentum;
	}

	/**
	 * 
	 */
	@Override
	public void performAction() {
		this.runnable.run();
	}
}
