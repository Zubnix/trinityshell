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
package org.trinity.shell.widget.api;

import java.util.List;

public interface KeyDrivenMenu extends Widget {

	interface View extends Widget.View {
		void clear();

		void activate();

		void deactivate();

		void update(String input,
					List<String> filteredChoices,
					int activeChoiceIdx);
	}

	void clear();

	void activate();

	void deactivate();

	List<String> getAllChoices();

	String getInput();

	List<String> getFilteredChoices();

	int getActiveChoiceIdx();
}
