/*
 * This file is part of Hydrogen. Hydrogen is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Hydrogen is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Hydrogen. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.display.api;

import org.trinity.foundation.input.api.Button;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.shared.geometry.api.Coordinate;

/**
 * An <code>AreaManipulator</code> provides the operations that are available
 * for manipulating and interacting with an {@link DisplayArea}. An
 * <code>AreaManipulator</code> instance is bound to exactly one
 * <code>Area</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public interface DisplayAreaManipulator<AREATYPE extends DisplayArea> {

	void destroy();

	void setInputFocus();

	void lower();

	void show();

	void move(	int x,
				int y);

	void moveResize(int x,
					int y,
					int width,
					int height);

	void raise();

	void setParent(	AREATYPE parent,
					int x,
					int y);

	void resize(int width,
				int height);

	void hide();

	Coordinate translateCoordinates(AREATYPE source,
									int sourceX,
									int sourceY);

	void grabButton(Button grabButton,
					InputModifiers withModifiers);

	void grabPointer();

	void ungrabPointer();

	void ungrabButton(	Button ungrabButton,
						InputModifiers withModifiers);

	void grabKey(	Key grabKey,
					InputModifiers withModifiers);

	void ungrabKey(	Key ungrabKey,
					InputModifiers withModifiers);

	void ungrabKeyboard();

	void grabKeyboard();
}
