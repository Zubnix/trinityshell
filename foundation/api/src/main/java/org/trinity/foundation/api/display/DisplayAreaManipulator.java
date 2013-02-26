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
package org.trinity.foundation.api.display;

import org.trinity.foundation.api.display.event.ButtonNotifyEvent;
import org.trinity.foundation.api.display.event.KeyNotifyEvent;
import org.trinity.foundation.api.display.input.Button;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Key;

/****************************************
 * Provides the operations that are available for manipulating and interacting
 * with a {@link DisplayArea}. A <code>DisplayAreaManipulator</code> instance is
 * bound to exactly one <code>DisplayArea</code> instance.
 * 
 *************************************** 
 */
public interface DisplayAreaManipulator {

	/***************************************
	 * Destroy the bound {@link DisplayArea}. A destroyed {@link DisplayArea}
	 * should be disposed and should not accept any calls made by it's
	 * {@link DisplayAreaManipulator}.
	 *************************************** 
	 */
	void destroy();

	/***************************************
	 * Set the input focus to the bound {@link DisplayArea}. Generated input
	 * evens will have their source set as coming from this {@link DisplayArea}.
	 * <p>
	 * The effects of giving focus to a hidden or destroyed {@link DisplayArea}
	 * is implementation dependent.
	 * </p>
	 *************************************** 
	 */
	void setInputFocus();

	/***************************************
	 * Lower the bound {@link DisplayArea}. A lowered {@link DisplayArea} will
	 * appear as it were behind any other {@link DisplayArea}.
	 * <p>
	 * The effects of lowering a hidden or destroyed {@link DisplayArea} is
	 * implementation dependent.
	 * </p>
	 *************************************** 
	 */
	void lower();

	/***************************************
	 * Make the bound {@link DisplayArea} visible if it was previously
	 * invisible. Making an already visible {@link DisplayArea} visible has no
	 * effect.
	 *************************************** 
	 */
	void show();

	/***************************************
	 * Move the bound {@link DisplayArea} to the given coordinates, relative to
	 * the parent. The bound {@link DisplayArea}'s top left corner will be
	 * positioned at the provided coordinates.
	 * 
	 * @param x
	 *            The X coordinate. Usually in pixels but can be implementation
	 *            dependent.
	 * @param y
	 *            The Y coordinate. Usually in pixels but can be implementation
	 *            dependent.
	 *************************************** 
	 */
	void move(	int x,
				int y);

	/***************************************
	 * Perform a move and resize operation on the bound {@link DisplayArea}.
	 * 
	 * 
	 * @param x
	 *            The X coordinate. Usually in pixels but can be implementation
	 *            dependent.
	 * @param y
	 *            The Y coordinate. Usually in pixels but can be implementation
	 *            dependent.
	 * @param width
	 *            The width. Usually in pixels but can be implementation
	 *            dependent.
	 * @param height
	 *            The height. Usually in pixels but can be implementation
	 *            dependent.
	 * 
	 * @see #move(int, int)
	 * @see #resize(int, int)
	 *************************************** 
	 */
	void moveResize(int x,
					int y,
					int width,
					int height);

	/***************************************
	 * Raise the bound {@link DisplayArea}. A raised {@link DisplayArea} will
	 * appear as it were above all other {@link DisplayArea}s.
	 * <p>
	 * The effects of raising a hidden or destroyed {@link DisplayArea} is
	 * implementation dependent.
	 * </p>
	 *************************************** 
	 */
	void raise();

	/***************************************
	 * Set the parent of the bound {@link DisplayArea}. The {@link DisplayArea}
	 * 's new position will correspond to the given coordinate, relative to the
	 * new parent.
	 * 
	 * @param parent
	 *            The new parent
	 * @param x
	 *            The X coordinate. Usually in pixels but can be implementation
	 *            dependent.
	 * @param y
	 *            The Y coordinate. Usually in pixels but can be implementation
	 *            dependent.
	 * @see #move(int, int)
	 *************************************** 
	 */
	void setParent(	DisplayArea parent,
					int x,
					int y);

	/***************************************
	 * Set the size of the bound {@link DisplayArea}.
	 * 
	 * @param width
	 *            The width. Usually in pixels but can be implementation
	 *            dependent.
	 * @param height
	 *            The width. Usually in pixels but can be implementation
	 *            dependent.
	 *************************************** 
	 */
	void resize(int width,
				int height);

	/***************************************
	 * Hide the bound {@link DisplayArea}.
	 *************************************** 
	 */
	void hide();

	// Coordinate translateCoordinates(DisplayArea source,
	// int sourceX,
	// int sourceY);

	/***************************************
	 * Grab a {@link Button} of the bound {@link DisplayArea} or any of its
	 * children. Grabbing a {@link Button} will override the delivery of the
	 * corresponding {@link ButtonNotifyEvent} event so it is only delivered to
	 * the grabber instead of delivering it anyone that is interested.
	 * <p>
	 * This method is usually used to install mousebindings.
	 * 
	 * @param grabButton
	 *            The {@link Button} that should be grabbed.
	 * @param withModifiers
	 *            The {@link InputModifiers} that should be active if a grab is
	 *            to take place.
	 *************************************** 
	 */
	void grabButton(Button grabButton,
					InputModifiers withModifiers);

	/***************************************
	 * Grab the entire pointing device of the bound {@link DisplayArea} or any
	 * of its children. Every {@link ButtonNotifyEvent} shall be redirected.
	 * 
	 * @see #grabButton(Button, InputModifiers)
	 *************************************** 
	 */
	void grabPointer();

	/***************************************
	 * Release the grab on the pointing device.
	 * 
	 * @see #grabPointer()
	 *************************************** 
	 */
	void ungrabPointer();

	/***************************************
	 * Release the grab on the specific {@link Button} with the specific
	 * {@link InputModifiers}.
	 * 
	 * @param ungrabButton
	 *            The {@link Button} that will be ungrabbed.
	 * @param withModifiers
	 *            The {@link InputModifiers}.
	 * @see #grabButton(Button, InputModifiers)
	 *************************************** 
	 */
	void ungrabButton(	Button ungrabButton,
						InputModifiers withModifiers);

	/***************************************
	 * Grab a {@link Key} of the bound {@link DisplayArea} or any of its
	 * children. Grabbing a {@link Key} will override the delivery of the
	 * corresponding {@link KeyNotifyEvent} event so it is only delivered to the
	 * grabber instead of delivering it anyone that is interested.
	 * <p>
	 * This method is usually used to install keybindings.
	 * 
	 * @param grabKey
	 *            The {@link Key} that should be grabbed
	 * @param withModifiers
	 *            The {@link InputModifiers} that should be active if a grab is
	 *            to take place.
	 *************************************** 
	 */
	void grabKey(	Key grabKey,
					InputModifiers withModifiers);

	/***************************************
	 * Release the grab on the specific {@link Key} with the specific
	 * {@link InputModifiers}.
	 * 
	 * @param ungrabKey
	 *            The {@link Key} that will be ungrabbed.
	 * @param withModifiers
	 *            the {@link InputModifiers}/
	 * @see #grabKey(Key, InputModifiers)
	 *************************************** 
	 */
	void ungrabKey(	Key ungrabKey,
					InputModifiers withModifiers);

	/***************************************
	 * Release the grab of the keyboard of the bound {@link DisplayArea}.
	 * 
	 * @see #grabKeyboard()
	 *************************************** 
	 */
	void ungrabKeyboard();

	/***************************************
	 * Grab the entire keyboard of the bound {@link DisplayArea} or any of its
	 * children. All {@link KeyNotifyEvent}s will be redirected.
	 * 
	 * @see #grabKey(Key, InputModifiers)
	 *************************************** 
	 */
	void grabKeyboard();
}
