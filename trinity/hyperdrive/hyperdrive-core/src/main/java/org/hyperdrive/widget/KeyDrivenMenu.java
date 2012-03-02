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
package org.hyperdrive.widget;

import java.util.ArrayList;
import java.util.List;

import org.hydrogen.displayinterface.event.KeyNotifyEvent;
import org.hydrogen.displayinterface.input.SpecialKeyName;
import org.hydrogen.eventsystem.EventHandler;
import org.hyperdrive.core.ManagedDisplay;
import org.hyperdrive.input.KeyInputStringBuilder;

// TODO documentation
/**
 * A <code>KeyDrivenMenu</code> presents the user with a list of options based
 * on the text input it receives from the user. It filters a list of given
 * options based on the text that was inputted by the user. This filtered list
 * is presented to the user who can then navigate and selected a desired option.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public abstract class KeyDrivenMenu extends Widget {

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	@ViewDefinition
	public interface View extends Widget.View {
		PaintInstruction<Void> clear();

		PaintInstruction<Void> update(String input,
				List<String> possibleValues, int activeValue);

		PaintInstruction<Void> activate();

		PaintInstruction<Void> deactivate();
	}

	private final KeyInputStringBuilder keyInputStringBuilder;
	private int activeChoice;
	private final List<String> currentFilteredChoices;

	/**
	 * 
	 */
	public KeyDrivenMenu() {
		this.activeChoice = 0;
		this.currentFilteredChoices = new ArrayList<String>(25);
		this.keyInputStringBuilder = new KeyInputStringBuilder() {
			@Override
			public ManagedDisplay getManagedDisplay() {
				return KeyDrivenMenu.this.getManagedDisplay();
			}
		};
		initKeyListeners();
	}

	// TODO instead of referencing a keyname based on a string, we want to
	// reference a type instance which can be queried by a string
	/**
	 * 
	 * @return
	 */
	public abstract String getConfirmChoiceKeyName();

	// TODO instead of referencing a keyname based on a string, we want to
	// reference a type instance which can be queried by a string
	/**
	 * 
	 * @return
	 */
	public abstract String getCancelKeyName();

	// TODO instead of referencing a keyname based on a string, we want to
	// reference a type instance which can be queried by a string
	/**
	 * 
	 * @return
	 */
	public abstract List<String> getChoices();

	/**
	 * 
	 * @param choice
	 * 
	 */
	public abstract void choiceConfirmed(String choice);

	/**
	 * 
	 */
	protected void initKeyListeners() {
		this.addEventHandler(new EventHandler<KeyNotifyEvent>() {
			@Override
			public void handleEvent(final KeyNotifyEvent event) {
				final String keyName = KeyDrivenMenu.this.getManagedDisplay()
						.getManagedKeyboard().keyEventToString(event);
				if (keyName.equals(KeyDrivenMenu.this.getCancelKeyName())) {
					// cancel key
					KeyDrivenMenu.this.stopKeyListening();
					KeyDrivenMenu.this.reset();
				} else if (keyName.equals(KeyDrivenMenu.this
						.getConfirmChoiceKeyName())) {
					// confirm key
					KeyDrivenMenu.this.stopKeyListening();
					KeyDrivenMenu.this.confirm();
					KeyDrivenMenu.this.reset();
				} else if (keyName.equals(SpecialKeyName.LEFT.name())) {
					// left arrow key
					if ((KeyDrivenMenu.this.activeChoice - 1) >= 0) {
						KeyDrivenMenu.this.activeChoice--;
						KeyDrivenMenu.this.keyInputStringBuilder.clearBuffer();

						final String desired = KeyDrivenMenu.this
								.getCurrentFilteredChoices().get(
										KeyDrivenMenu.this.activeChoice);
						KeyDrivenMenu.this.keyInputStringBuilder
								.getStringBuffer().append(desired);

						// update view

						KeyDrivenMenu.this.getView().update(desired,
								KeyDrivenMenu.this.getCurrentFilteredChoices(),
								KeyDrivenMenu.this.activeChoice);
					}
				} else if (keyName.equals(SpecialKeyName.RIGHT.name())) {
					// righ arrow key
					if ((KeyDrivenMenu.this.activeChoice + 1) < KeyDrivenMenu.this
							.getCurrentFilteredChoices().size()) {
						KeyDrivenMenu.this.activeChoice++;
						final String desired = KeyDrivenMenu.this
								.getCurrentFilteredChoices().get(
										KeyDrivenMenu.this.activeChoice);

						KeyDrivenMenu.this.keyInputStringBuilder.clearBuffer();
						KeyDrivenMenu.this.keyInputStringBuilder
								.getStringBuffer().append(desired);

						// update view
						KeyDrivenMenu.this.getView().update(desired,
								KeyDrivenMenu.this.getCurrentFilteredChoices(),
								KeyDrivenMenu.this.activeChoice);
					}
				} else {
					// other key
					KeyDrivenMenu.this.keyInputStringBuilder.build(event);
					final String desired = KeyDrivenMenu.this.keyInputStringBuilder
							.getStringBuffer().toString();
					KeyDrivenMenu.this.activeChoice = 0;

					KeyDrivenMenu.this.updatePossibleChoices(desired);

					// update view

					KeyDrivenMenu.this.getView().update(desired,
							KeyDrivenMenu.this.getCurrentFilteredChoices(),
							KeyDrivenMenu.this.activeChoice);
				}
			}
		}, KeyNotifyEvent.TYPE_PRESSED);
	}

	/**
	 * 
	 * @param desired
	 */
	protected void updatePossibleChoices(final String desired) {
		if (desired.isEmpty()) {
			// return empty list
			return;
		}

		final String menuOption = searchFirstMatchingChoice(desired);
		final int i = getChoices().indexOf(menuOption);
		this.currentFilteredChoices.clear();
		if (i != -1) {
			final int nroChoices = getChoices().size();

			final List<String> possibleDesiredChoices = getChoices().subList(i,
					nroChoices - 1);

			this.currentFilteredChoices.addAll(possibleDesiredChoices);
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getCurrentFilteredChoices() {
		return this.currentFilteredChoices;
	}

	/**
	 * 
	 * 
	 */
	public void reset() {
		this.activeChoice = 0;
		// clear buffer
		this.keyInputStringBuilder.clearBuffer();
		// clear view
		getView().clear();
	}

	/**
	 * 
	 * 
	 */
	public void confirm() {
		final String choice = searchFirstMatchingChoice(this.keyInputStringBuilder
				.getStringBuffer().toString());
		if (choice.isEmpty()) {
			return;
		} else {
			choiceConfirmed(choice);
		}
	}

	/**
	 * 
	 * @param desiredChoice
	 * @return
	 */
	protected String searchFirstMatchingChoice(final String desiredChoice) {
		for (final String menuOption : getChoices()) {
			if (menuOption.startsWith(desiredChoice)) {
				return menuOption;
			}
		}
		return "";
	}

	@Override
	public View getView() {
		return (View) super.getView();
	}

	/**
	 * 
	 * 
	 */
	public void startKeyListening() {
		getManagedDisplay().getManagedKeyboard().grab(this);

		getView().activate();
	}

	/**
	 * 
	 * 
	 */
	public void stopKeyListening() {
		getManagedDisplay().getManagedKeyboard().release();
		getView().deactivate();
	}
}