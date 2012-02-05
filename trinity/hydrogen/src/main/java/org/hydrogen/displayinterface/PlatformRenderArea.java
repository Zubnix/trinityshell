/*
 * This file is part of Hydrogen.
 * 
 * Hydrogen is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hydrogen is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hydrogen. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.displayinterface;

import org.hydrogen.displayinterface.event.ClientMessageEvent;
import org.hydrogen.displayinterface.event.DisplayEventSource;
import org.hydrogen.displayinterface.input.Button;
import org.hydrogen.displayinterface.input.InputModifiers;
import org.hydrogen.displayinterface.input.Key;

// TODO documentation
/**
 * A <code>PlatformRenderArea</code> is represents a native isolated graphical
 * area created by a <code>Display</code>. Usually a
 * <code>PlatformRenderArea</code> manages a native window from a native
 * display.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface PlatformRenderArea extends DisplayResource, Area,
		AreaManipulator<PlatformRenderArea>, DisplayEventSource
// ,EventHandlerManager
{

	/**
	 * Returns all child <code>PlatformRenderArea</code>s where this
	 * <code>PlatformRenderArea</code> is directly or indirectly the parent.
	 * 
	 * @return An array of child <code>PlatformRenderArea</code>s.
	 */
	PlatformRenderArea[] getChildren();

	/**
	 * 
	 * @return
	 */
	PlatformRenderAreaType getPlatformRenderAreaType();

	/**
	 * Override any <code>EventPropagation</code>s set on this
	 * <code>PlatformRenderArea</code> so these <code>EventPropagation</code>s
	 * are ignored.
	 * 
	 * @param override
	 *            True if override should be enabled. False if not.
	 */
	void overrideRedirect(boolean override);

	/**
	 * Select which <code>DisplayEvent</code> types this
	 * <code> PlatformRenderArea</code> should propagate for further event
	 * handling.
	 * 
	 * @param eventMask
	 *            An number of different {@link EventPropagator}s.
	 */
	void propagateEvent(EventPropagator... eventMask);

	/**
	 * Get the current <code>PlatformRenderArea</code> geometry information. The
	 * returned <code>PlatformRenderAreaGeometry</code> represents this
	 * <code>PlatformRenderArea</code> native geometry at the time of the call.
	 * 
	 * @return This <code>PlatformRenderArea</code>'s
	 *         {@link PlatformRenderAreaGeometry}.
	 */
	PlatformRenderAreaGeometry getPlatformRenderAreaGeometry();

	/**
	 * Get the current <code>PlatformRenderArea</code> attributes information.
	 * The returned <code>PlatformRenderAreaAttributes</code> represents this
	 * <code>PlatformRenderArea</code> native attributes at the time of the
	 * call.
	 * 
	 * @return the {@link PlatformRenderAreaAttributes} of this
	 *         <code>PlatformRenderArea</code> at the time of the call.
	 */
	PlatformRenderAreaAttributes getPlatformRenderAreaAttributes();

	/**
	 * Send a request to the program owning this <code>PlatformRenderArea</code>
	 * to terminate this <code>PlatformRenderArea</code>.
	 */
	void requestDestroy();

	/**
	 * 
	 * @return
	 */
	PlatformRenderAreaRelation[] getPlatformRenderAreaRelations();

	/**
	 * 
	 * @param clientMessageEvent
	 * 
	 */
	void sendMessage(ClientMessageEvent clientMessageEvent);

	/**
	 * 
	 * @return
	 */
	PlatformRenderAreaPreferences getPreferences();

	/**
	 * 
	 * @param catchKey
	 * @param withModifiers
	 * 
	 */
	void catchKeyboardInput(Key catchKey, InputModifiers withModifiers);

	/**
	 * 
	 * 
	 */
	void catchAllKeyboardInput();

	/**
	 * 
	 * 
	 */
	void stopKeyboardInputCatching();

	/**
	 * 
	 * @param likeButton
	 * @param withModifiers
	 * 
	 */
	void catchMouseInput(Button catchButton, InputModifiers withModifiers);

	/**
	 * 
	 */
	void catchAllMouseInput();

	/**
	 * 
	 */
	void stopMouseInputCatching();

	/**
	 * 
	 * @param likeKey
	 * @param withModifiers
	 * 
	 */
	void disableKeyboardInputCatching(Key likeKey, InputModifiers withModifiers);

	/**
	 * 
	 * @param likeButton
	 * @param withModifiers
	 * 
	 */
	void disableMouseInputCatching(Button likeButton,
			InputModifiers withModifiers);

	/**
	 * 
	 * @param property
	 * @return
	 * 
	 */
	<T extends PropertyInstance> T getPropertyInstance(
			final Property<T> property);

	/**
	 * 
	 * @param property
	 * @param propertyInstance
	 * 
	 */
	<T extends PropertyInstance> void setPropertyInstance(
			final Property<T> property, T propertyInstance);
}
