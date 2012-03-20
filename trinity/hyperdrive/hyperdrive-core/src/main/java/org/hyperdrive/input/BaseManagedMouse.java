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
package org.hyperdrive.input;

import org.hydrogen.api.display.event.ButtonNotifyEvent;
import org.hydrogen.api.display.input.Mouse;
import org.hydrogen.api.geometry.Coordinates;
import org.hydrogen.geometry.BaseCoordinates;
import org.hyperdrive.api.core.ManagedDisplay;
import org.hyperdrive.api.core.event.MouseButtonPressedHandler;
import org.hyperdrive.api.core.event.MouseButtonReleasedHandler;
import org.hyperdrive.api.input.ManagedMouse;

/**
 * A <code>ManagedMouse</code> represents a mouse pointer from a
 * <code>ManagedDisplay</code>. A <code>ManagedMouse</code> provides location
 * based input by wrapping a native implementation of a {@link Mouse}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class BaseManagedMouse extends BaseInputDevice implements ManagedMouse {

	private final class ButtonPressedForwarder extends
			MouseButtonPressedHandler {
		@Override
		public void handleEvent(final ButtonNotifyEvent event) {
			delegateInputEventToInputEventManagers(event);
		}
	}

	private final class ButtonReleasedForwarder extends
			MouseButtonReleasedHandler {
		@Override
		public void handleEvent(final ButtonNotifyEvent event) {
			delegateInputEventToInputEventManagers(event);
		}
	}

	private final Mouse mouse;
	private final ButtonPressedForwarder buttonPressedForwarder;
	private final ButtonReleasedForwarder buttonReleasedForwarder;

	/**
	 * Create a new <code>ManagedMouse</code>. This <code>ManagedMouse</code>
	 * will be backed by the existing <code>Mouse</code> of the given
	 * <code>ManagedDisplay</code>. This means that multiple
	 * <code>ManagedMouse</code>s, if created with the same
	 * <code>ManagedDisplay</code>, will manage the same <code>Mouse</code>.
	 * 
	 * @param managedDisplay
	 *            A {@link ManagedDisplay}. @ Thrown when the given
	 *            <code>ManagedDisplay</code> has an illegal state.
	 */
	public BaseManagedMouse(final ManagedDisplay managedDisplay) {
		super(managedDisplay);
		this.mouse = getManagedDisplay().getDisplay().getMouse();
		this.buttonPressedForwarder = new ButtonPressedForwarder();
		this.buttonReleasedForwarder = new ButtonReleasedForwarder();
	}

	@Override
	public void release() {
		getManagedDisplay().removeDisplayEventHandler(
				this.buttonPressedForwarder);
		getManagedDisplay().removeDisplayEventHandler(
				this.buttonReleasedForwarder);
		getManagedDisplay().getRoot().getPlatformRenderArea()
				.stopMouseInputCatching();
	}

	@Override
	protected void delegateInputEventsAndGrab() {
		getManagedDisplay().addDisplayEventHandler(this.buttonPressedForwarder);
		getManagedDisplay()
				.addDisplayEventHandler(this.buttonReleasedForwarder);
		getManagedDisplay().getRoot().getPlatformRenderArea()
				.catchAllMouseInput();
	}

	@Override
	public Coordinates getAbsolutePosition() {
		return new BaseCoordinates(this.mouse.getRootX(), this.mouse.getRootY());
	}

	/**
	 * Refresh the geometric values of this <code>ManagedMouse</code> so it
	 * represents the values at the time of this call.
	 * 
	 */
	@Override
	public void refreshPositionInfo() {
		this.mouse.refreshInfo();
	}
}