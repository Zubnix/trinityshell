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

import org.hydrogen.api.display.event.DisplayEventType;
import org.hydrogen.api.display.event.KeyNotifyEvent;
import org.hydrogen.api.display.input.SpecialKeyName;
import org.hydrogen.api.event.EventHandler;
import org.hyperdrive.api.core.ManagedDisplay;
import org.hyperdrive.api.widget.HasView;
import org.hyperdrive.api.widget.KeyDrivenMenu;
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
@HasView(KeyDrivenMenu.View.class)
public abstract class BaseKeyDrivenMenu extends BaseWidget implements
		KeyDrivenMenu {

	private final KeyInputStringBuilder keyInputStringBuilder;
	private int activeChoice;
	private final List<String> filteredChoices;

	/**
	 * 
	 */
	public BaseKeyDrivenMenu() {
		this.activeChoice = 0;
		this.filteredChoices = new ArrayList<String>(25);
		this.keyInputStringBuilder = new KeyInputStringBuilder() {
			@Override
			public ManagedDisplay getManagedDisplay() {
				return BaseKeyDrivenMenu.this.getManagedDisplay();
			}
		};
		initKeyListeners();
	}

	@Override
	public KeyDrivenMenu.View getView() {
		return (KeyDrivenMenu.View) super.getView();
	}

	// TODO instead of referencing a keyname based on a string, we want to
	// reference a key instance which can be queried by a string
	/**
	 * 
	 * @return
	 */
	public abstract String getConfirmChoiceKeyName();

	// TODO instead of referencing a keyname based on a string, we want to
	// reference a key which can be queried by a string
	/**
	 * 
	 * @return
	 */
	public abstract String getCancelKeyName();

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
				final String keyName = BaseKeyDrivenMenu.this
						.getManagedDisplay().getManagedKeyboard()
						.keyEventToString(event);
				if (keyName.equals(BaseKeyDrivenMenu.this.getCancelKeyName())) {
					// cancel key
					BaseKeyDrivenMenu.this.stopKeyListening();
					BaseKeyDrivenMenu.this.reset();
				} else if (keyName.equals(BaseKeyDrivenMenu.this
						.getConfirmChoiceKeyName())) {
					// confirm key
					BaseKeyDrivenMenu.this.stopKeyListening();
					BaseKeyDrivenMenu.this.confirm();
					BaseKeyDrivenMenu.this.reset();
				} else if (keyName.equals(SpecialKeyName.LEFT.name())) {
					// left arrow key
					if ((BaseKeyDrivenMenu.this.activeChoice - 1) >= 0) {
						BaseKeyDrivenMenu.this.activeChoice--;
						BaseKeyDrivenMenu.this.keyInputStringBuilder
								.clearBuffer();

						final String desired = BaseKeyDrivenMenu.this
								.getFilteredChoices().get(
										BaseKeyDrivenMenu.this.activeChoice);
						BaseKeyDrivenMenu.this.keyInputStringBuilder
								.getStringBuffer().append(desired);

						// update view

						BaseKeyDrivenMenu.this.getView().update(desired,
								BaseKeyDrivenMenu.this.getFilteredChoices(),
								BaseKeyDrivenMenu.this.activeChoice);
					}
				} else if (keyName.equals(SpecialKeyName.RIGHT.name())) {
					// righ arrow key
					if ((BaseKeyDrivenMenu.this.activeChoice + 1) < BaseKeyDrivenMenu.this
							.getFilteredChoices().size()) {
						BaseKeyDrivenMenu.this.activeChoice++;
						final String desired = BaseKeyDrivenMenu.this
								.getFilteredChoices().get(
										BaseKeyDrivenMenu.this.activeChoice);

						BaseKeyDrivenMenu.this.keyInputStringBuilder
								.clearBuffer();
						BaseKeyDrivenMenu.this.keyInputStringBuilder
								.getStringBuffer().append(desired);

						// update view
						BaseKeyDrivenMenu.this.getView().update(desired,
								BaseKeyDrivenMenu.this.getFilteredChoices(),
								BaseKeyDrivenMenu.this.activeChoice);
					}
				} else {
					// other key
					BaseKeyDrivenMenu.this.keyInputStringBuilder.build(event);
					final String desired = BaseKeyDrivenMenu.this.keyInputStringBuilder
							.getStringBuffer().toString();
					BaseKeyDrivenMenu.this.activeChoice = 0;

					BaseKeyDrivenMenu.this.updatePossibleChoices(desired);

					// update view

					BaseKeyDrivenMenu.this.getView().update(desired,
							BaseKeyDrivenMenu.this.getFilteredChoices(),
							BaseKeyDrivenMenu.this.activeChoice);
				}
			}
		}, DisplayEventType.KEY_PRESSED);
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
		final int i = getAllChoices().indexOf(menuOption);
		this.filteredChoices.clear();
		if (i != -1) {
			final int nroChoices = getAllChoices().size();

			final List<String> possibleDesiredChoices = getAllChoices()
					.subList(i, nroChoices - 1);

			this.filteredChoices.addAll(possibleDesiredChoices);
		}
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
		final String choice = searchFirstMatchingChoice(getFilter());
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
		for (final String menuOption : getAllChoices()) {
			if (menuOption.startsWith(desiredChoice)) {
				return menuOption;
			}
		}
		return "";
	}

	/**
	 * 
	 * 
	 */
	public void startKeyListening() {
		getManagedDisplay().getManagedKeyboard().addInputEventManager(this);
		getManagedDisplay().getManagedKeyboard().grab();
		getView().startedKeyListening();
	}

	/**
	 * 
	 * 
	 */
	public void stopKeyListening() {
		getManagedDisplay().getManagedKeyboard().release();
		getManagedDisplay().getManagedKeyboard().removeInputEventManager(this);
		getView().stoppedKeyListening();
	}

	@Override
	public String getFilter() {
		return this.keyInputStringBuilder.getStringBuffer().toString();
	}

	@Override
	public List<String> getFilteredChoices() {
		return this.filteredChoices;
	}

	@Override
	public int getChoosenFilteredChoiceIdx() {
		// TODO
		return 0;
	}
}