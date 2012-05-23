/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.core.input.api;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public interface MouseInputFactory {

	@Named("Started")
	MouseInput createMouseInputStarted(	Button button,
										InputModifiers inputModifiers,
										@Assisted("rootX") int rootX,
										@Assisted("rootY") int rootY,
										@Assisted("relativeX") int relativeX,
										@Assisted("relativeY") int relativeY);

	@Named("Stopped")
	MouseInput createMouseInputStopped(	Button button,
										InputModifiers inputModifiers,
										@Assisted("rootX") int rootX,
										@Assisted("rootY") int rootY,
										@Assisted("relativeX") int relativeX,
										@Assisted("relativeY") int relativeY);
}
