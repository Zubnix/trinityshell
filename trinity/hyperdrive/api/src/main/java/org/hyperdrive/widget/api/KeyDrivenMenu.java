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
package org.hyperdrive.widget.api;

import java.util.List;

import org.hydrogen.display.api.input.KeyboardInput;
import org.hydrogen.paint.api.PaintCall;

public interface KeyDrivenMenu extends Widget {

	public interface View extends Widget.View {
		PaintCall<Void, ?> clear();

		PaintCall<Void, ?> update(String input, List<String> possibleValues,
				int activeValue);

		PaintCall<Void, ?> startedKeyListening();

		PaintCall<Void, ?> stoppedKeyListening();
	}

	@Override
	public View getView();

	List<String> getAllChoices();

	String getFilter();

	List<String> getFilteredChoices();

	int getChoosenFilteredChoiceIdx();

	@Override
	void onKeyboardPressed(final KeyboardInput input);

	@Override
	void onKeyboardReleased(final KeyboardInput input);
}
