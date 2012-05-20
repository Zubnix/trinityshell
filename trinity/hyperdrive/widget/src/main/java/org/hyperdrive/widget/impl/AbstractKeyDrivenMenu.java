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
package org.hyperdrive.widget.impl;

import java.util.ArrayList;
import java.util.List;

import org.hydrogen.display.api.event.DisplayEventType;
import org.hydrogen.display.api.event.KeyNotifyEvent;
import org.hydrogen.display.api.input.SpecialKeyName;
import org.hydrogen.event.api.EventHandler;
import org.hyperdrive.foundation.api.ManagedDisplay;
import org.hyperdrive.widget.api.KeyDrivenMenu;
import org.hyperdrive.widget.api.ViewReference;

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
public abstract class AbstractKeyDrivenMenu extends BaseWidget implements
		KeyDrivenMenu {

	private final KeyInputStringBuilder keyInputStringBuilder;
	private int activeChoice;
	private final List<String> filteredChoices;

	@ViewReference
	private KeyDrivenMenu.View view;

	/**
	 * 
	 */
	public AbstractKeyDrivenMenu() {
		this.activeChoice = 0;
		this.filteredChoices = new ArrayList<String>(25);

		// TODO guice+api
		this.keyInputStringBuilder = new KeyInputStringBuilder() {
			@Override
			public ManagedDisplay getManagedDisplay() {
				return AbstractKeyDrivenMenu.this.getManagedDisplay();
			}
		};
		initKeyListeners();
	}

	@Override
	public KeyDrivenMenu.View getView() {
		return view;
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
				final String keyName = AbstractKeyDrivenMenu.this
						.getManagedDisplay().getManagedKeyboard()
						.keyEventToString(event);
				if (keyName.equals(AbstractKeyDrivenMenu.this
						.getCancelKeyName())) {
					// cancel key
					AbstractKeyDrivenMenu.this.stopKeyListening();
					AbstractKeyDrivenMenu.this.reset();
				} else if (keyName.equals(AbstractKeyDrivenMenu.this
						.getConfirmChoiceKeyName())) {
					// confirm key
					AbstractKeyDrivenMenu.this.stopKeyListening();
					AbstractKeyDrivenMenu.this.confirm();
					AbstractKeyDrivenMenu.this.reset();
				} else if (keyName.equals(SpecialKeyName.LEFT.name())) {
					// left arrow key
					if ((AbstractKeyDrivenMenu.this.activeChoice - 1) >= 0) {
						AbstractKeyDrivenMenu.this.activeChoice--;
						AbstractKeyDrivenMenu.this.keyInputStringBuilder
								.clearBuffer();

						final String desired = AbstractKeyDrivenMenu.this
								.getFilteredChoices()
								.get(AbstractKeyDrivenMenu.this.activeChoice);
						AbstractKeyDrivenMenu.this.keyInputStringBuilder
								.getStringBuffer().append(desired);

						// update view

						draw(AbstractKeyDrivenMenu.this
								.getView()
								.update(desired,
										AbstractKeyDrivenMenu.this
												.getFilteredChoices(),
										AbstractKeyDrivenMenu.this.activeChoice));
					}
				} else if (keyName.equals(SpecialKeyName.RIGHT.name())) {
					// righ arrow key
					if ((AbstractKeyDrivenMenu.this.activeChoice + 1) < AbstractKeyDrivenMenu.this
							.getFilteredChoices().size()) {
						AbstractKeyDrivenMenu.this.activeChoice++;
						final String desired = AbstractKeyDrivenMenu.this
								.getFilteredChoices()
								.get(AbstractKeyDrivenMenu.this.activeChoice);

						AbstractKeyDrivenMenu.this.keyInputStringBuilder
								.clearBuffer();
						AbstractKeyDrivenMenu.this.keyInputStringBuilder
								.getStringBuffer().append(desired);

						// update view
						AbstractKeyDrivenMenu.this
								.getView()
								.update(desired,
										AbstractKeyDrivenMenu.this
												.getFilteredChoices(),
										AbstractKeyDrivenMenu.this.activeChoice);
					}
				} else {
					// other key
					AbstractKeyDrivenMenu.this.keyInputStringBuilder
							.build(event);
					final String desired = AbstractKeyDrivenMenu.this.keyInputStringBuilder
							.getStringBuffer().toString();
					AbstractKeyDrivenMenu.this.activeChoice = 0;

					AbstractKeyDrivenMenu.this.updatePossibleChoices(desired);

					// update view

					AbstractKeyDrivenMenu.this.getView().update(desired,
							AbstractKeyDrivenMenu.this.getFilteredChoices(),
							AbstractKeyDrivenMenu.this.activeChoice);
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
		draw(getView().clear());
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
		draw(getView().startedKeyListening());
	}

	/**
	 * 
	 * 
	 */
	public void stopKeyListening() {
		getManagedDisplay().getManagedKeyboard().release();
		getManagedDisplay().getManagedKeyboard().removeInputEventManager(this);
		draw(getView().stoppedKeyListening());
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