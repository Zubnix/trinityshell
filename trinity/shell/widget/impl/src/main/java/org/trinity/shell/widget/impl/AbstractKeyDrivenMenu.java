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
package org.trinity.shell.widget.impl;

import java.util.ArrayList;
import java.util.List;

import org.trinity.foundation.display.api.event.KeyNotifyEvent;
import org.trinity.foundation.input.api.Momentum;
import org.trinity.foundation.input.api.SpecialKeyName;
import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.core.api.ManagedDisplay;
import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.geo.api.event.GeoEventFactory;
import org.trinity.shell.input.api.KeyInputStringBuilder;
import org.trinity.shell.input.api.ManagedKeyboard;
import org.trinity.shell.widget.api.KeyDrivenMenu;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

// TODO documentation
/**
 * A <code>KeyDrivenMenu</code> presents the user with a list of options based
 * on the text input it receives from the user. It filters a list of given
 * options based on the text that was inputted by the user. This filtered list
 * is presented to the user who can then navigate and selected a desired option.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public abstract class AbstractKeyDrivenMenu extends WidgetImpl implements
		KeyDrivenMenu {

	private final KeyDrivenMenu.View view;
	private final List<String> filteredChoices = new ArrayList<String>(25);
	private final ManagedKeyboard managedKeyboard;
	private final KeyInputStringBuilder keyInputStringBuilder;

	private int activeChoiceIdx = 0;

	private final EventBus displayEventBus;

	protected AbstractKeyDrivenMenu(final EventBus displayEventBus,
									final EventBus eventBus,
									final GeoEventFactory geoEventFactory,
									final ManagedDisplay managedDisplay,
									final PainterFactory painterFactory,
									final GeoExecutor geoExecutor,
									final ManagedKeyboard managedKeyboard,
									final KeyInputStringBuilder keyInputStringBuilder,
									final KeyDrivenMenu.View view) {
		super(	eventBus,
				geoEventFactory,
				managedDisplay,
				painterFactory,
				geoExecutor,
				view);
		this.displayEventBus = displayEventBus;
		this.view = view;
		this.managedKeyboard = managedKeyboard;
		this.keyInputStringBuilder = keyInputStringBuilder;
	}

	// TODO instead of referencing a keyname based on a string, we want to
	// reference a key instance which can be queried by a string (good idea?)
	/**
	 * @return
	 */
	public abstract String getConfirmChoiceKeyName();

	// TODO instead of referencing a keyname based on a string, we want to
	// reference a key which can be queried by a string (good idea?)
	/**
	 * @return
	 */
	public abstract String getCancelKeyName();

	/**
	 * @param choice
	 */
	public abstract void choiceConfirmed(String choice);

	@Subscribe
	public void handleKeyNotify(final KeyNotifyEvent event) {
		if (event.getInput().getMomentum() != Momentum.STARTED) {
			return;
		}

		final String keyName = this.managedKeyboard.keyEventToString(event);

		if (keyName.equals(getCancelKeyName())) {
			// cancel key
			deactivate();
			clear();
			return;
		}

		if (keyName.equals(getConfirmChoiceKeyName())) {
			// confirm key
			deactivate();
			confirm();
			clear();
			return;
		}

		if (keyName.equals(SpecialKeyName.LEFT.name())) {
			// left arrow key
			if ((this.activeChoiceIdx - 1) >= 0) {
				this.activeChoiceIdx--;
				this.keyInputStringBuilder.clearBuffer();

				final String desired = getFilteredChoices()
						.get(AbstractKeyDrivenMenu.this.activeChoiceIdx);
				this.keyInputStringBuilder.getStringBuffer().append(desired);

				// update view
				this.view.update(	desired,
									getFilteredChoices(),
									this.activeChoiceIdx);
			}
			return;
		}

		if (keyName.equals(SpecialKeyName.RIGHT.name())) {
			// righ arrow key
			if ((this.activeChoiceIdx + 1) < getFilteredChoices().size()) {
				this.activeChoiceIdx++;
				final String desired = getFilteredChoices()
						.get(this.activeChoiceIdx);

				this.keyInputStringBuilder.clearBuffer();
				this.keyInputStringBuilder.getStringBuffer().append(desired);

				// update view
				this.view.update(desired,

				getFilteredChoices(), this.activeChoiceIdx);
			}
			return;
		}

		// other keys
		this.keyInputStringBuilder.build(event);
		final String desired = this.keyInputStringBuilder.getStringBuffer()
				.toString();
		this.activeChoiceIdx = 0;

		updatePossibleChoices(desired);

		// update view
		this.view.update(desired, getFilteredChoices(), this.activeChoiceIdx);
	}

	/**
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

	@Override
	public void clear() {
		this.activeChoiceIdx = 0;
		// clear buffer
		this.keyInputStringBuilder.clearBuffer();
		// clear view
		this.view.clear();
	}

	/**
	 * 
	 * 
	 */
	public void confirm() {
		final String choice = searchFirstMatchingChoice(getInput());
		if (choice.isEmpty()) {
			return;
		}
		choiceConfirmed(choice);
	}

	/**
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

	@Override
	public void activate() {
		this.displayEventBus.register(this);
		this.managedKeyboard.grab();
		this.view.activate();
	}

	@Override
	public void deactivate() {
		this.managedKeyboard.release();
		this.displayEventBus.unregister(this);
		this.view.deactivate();
	}

	@Override
	public String getInput() {
		return this.keyInputStringBuilder.getStringBuffer().toString();
	}

	@Override
	public List<String> getFilteredChoices() {
		return this.filteredChoices;
	}

	@Override
	public int getActiveChoiceIdx() {
		return this.activeChoiceIdx;
	}
}