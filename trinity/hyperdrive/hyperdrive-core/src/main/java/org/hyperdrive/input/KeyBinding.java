/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */

package org.hyperdrive.input;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hydrogen.displayinterface.event.KeyNotifyEvent;
import org.hydrogen.displayinterface.input.InputModifiers;
import org.hydrogen.displayinterface.input.Key;
import org.hydrogen.displayinterface.input.Keyboard.ModifierName;
import org.hydrogen.displayinterface.input.Modifier;
import org.hydrogen.displayinterface.input.Momentum;
import org.hydrogen.eventsystem.EventHandler;
import org.hyperdrive.core.ManagedDisplay;

// TODO documentation
/**
 * A <code>KeyBinding</code> binds a {@link Key} with an optional
 * {@link Modifier} to an action. A <code>Key</code> is identified by it's name.
 * <code>Key</code> names are the same as the unmodified character they produce
 * when pressed. A list of special <code>Key</code> names can be found in
 * {@link SpecialKeyName} . A list of <code>Modifier</code>s can be found in
 * {@link ModifierName}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public abstract class KeyBinding {

	// Modifiers who's state should be ignored when checking for a key
	// binding.
	// TODO capslock, scrolllock, ...
	private static final ModifierName[] IGNORED_MODIFIERS = new ModifierName[] {
			ModifierName.MOD_LOCK, ModifierName.MOD_2 };

	// TODO release keybinding function

	private final ManagedDisplay managedDisplay;
	private final String keyName;
	private final ModifierName[] modKeyNames;
	private final Momentum momentum;

	/**
	 * 
	 * @param sourceWindow
	 * @param momentum
	 * @param keyName
	 * @param ignoreOftenUsedModifiers
	 * @param modKeyNames
	 * 
	 */
	public KeyBinding(final ManagedDisplay display, final Momentum momentum,
			final String keyName, final boolean ignoreOftenUsedModifiers,
			final ModifierName... modKeyNames) {
		this.managedDisplay = display;
		this.keyName = keyName;
		this.modKeyNames = modKeyNames;
		this.momentum = momentum;

		installKeyBindingListener(ignoreOftenUsedModifiers);
	}

	/**
	 * 
	 * @param sourceWindow
	 * @param momentum
	 * @param keyName
	 * @param modKeyNames
	 * 
	 */
	public KeyBinding(final ManagedDisplay display, final Momentum momentum,
			final String keyName, final ModifierName... modKeyNames) {
		this(display, momentum, keyName, true, modKeyNames);
	}

	/**
	 * 
	 * @param sourceWindow
	 * @param momentum
	 * @param keyName
	 * @param modKeyNames
	 * 
	 */
	public KeyBinding(final ManagedDisplay display, final Momentum momentum,
			final String keyName, final List<ModifierName> modKeyNames) {
		this(display, momentum, keyName, modKeyNames
				.toArray(new ModifierName[modKeyNames.size()]));
	}

	/**
	 * 
	 * @param ignoreOftenUsedModifiers
	 * 
	 */
	protected void installKeyBindingListener(
			final boolean ignoreOftenUsedModifiers) {
		// translate keyname and modkeynames to keys and modmask

		final Key[] validKeys = this.managedDisplay.getDisplay().getKeyBoard()
				.keys(getKeyName());

		final List<InputModifiers> validInputModifiersCombinations = new LinkedList<InputModifiers>();

		final InputModifiers inputModifiers = getModKeyNames().length == 0 ? new InputModifiers()
				: this.managedDisplay.getDisplay().getKeyBoard()
						.modifiers(getModKeyNames());
		if (ignoreOftenUsedModifiers) {
			// install keybindings with variations on often active modifiers

			// This will only install an extra keybinding on an invidual
			// item from the IGNORED_MODIFIERS array.
			// TODO full permutation
			for (final ModifierName ignoredModifier : KeyBinding.IGNORED_MODIFIERS) {
				final Modifier modifier = this.managedDisplay.getDisplay()
						.getKeyBoard().modifier(ignoredModifier);

				final int modifierMaskWithExtraModifier = inputModifiers
						.getInputModifiersMask() & modifier.getModifierMask();
				final InputModifiers inputModifiersWithExtraModifier = new InputModifiers(
						modifierMaskWithExtraModifier);
				validInputModifiersCombinations
						.add(inputModifiersWithExtraModifier);
			}

		}
		validInputModifiersCombinations.add(inputModifiers);

		for (final Key validKey : validKeys) {
			// install a keygrab
			getManagedDisplay().getRealRootRenderArea().getPlatformRenderArea()
					.catchKeyboardInput(validKey, inputModifiers);

			getManagedDisplay().addEventHandler(
					new EventHandler<KeyNotifyEvent>() {
						@Override
						public void handleEvent(final KeyNotifyEvent event) {

							final short eventKeyCode = event.getInput()
									.getKey().getKeyCode().shortValue();
							final short validKeyCode = validKey.getKeyCode()
									.shortValue();
							final boolean gotValidKey = eventKeyCode == validKeyCode;

							final int eventModifiersMask = event.getInput()
									.getModifiers().getInputModifiersMask();
							final int validEventModifiersMask = inputModifiers
									.getInputModifiersMask();

							final boolean gotValidModifiers = eventModifiersMask == validEventModifiersMask;

							final Momentum eventMomentum = event.getInput()
									.getMomentum();
							final Momentum validMomentum = KeyBinding.this
									.getMomentum();
							final boolean gotValidMomentum = eventMomentum == validMomentum;

							if (gotValidKey && gotValidModifiers
									&& gotValidMomentum) {
								KeyBinding.this.action();
							}
						}
					},
					getMomentum() == Momentum.STARTED ? KeyNotifyEvent.KEY_PRESSED
							: KeyNotifyEvent.KEY_RELEASED, 0);

		}
	}

	/**
	 * 
	 * @return
	 */
	public ManagedDisplay getManagedDisplay() {
		return this.managedDisplay;
	}

	/**
	 * 
	 * @return
	 */
	public String getKeyName() {
		return this.keyName;
	}

	/**
	 * 
	 * @return
	 */
	public ModifierName[] getModKeyNames() {
		// return a copy so manipulation of the returned instance can take
		// place without interfering with the source.
		return Arrays.copyOf(this.modKeyNames, this.modKeyNames.length);
	}

	/**
	 * 
	 * @return
	 */
	public Momentum getMomentum() {
		return this.momentum;
	}

	/**
	 * 
	 */
	public abstract void action();
}
