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

import org.hydrogen.api.display.event.DisplayEventType;
import org.hydrogen.api.display.input.Mouse;
import org.hyperdrive.api.core.ManagedDisplay;

/**
 * A <code>ManagedMouse</code> represents a mouse pointer from a
 * <code>ManagedDisplay</code>. A <code>ManagedMouse</code> provides location
 * based input by wrapping a native implementation of a {@link Mouse}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ManagedMouse extends GrabableInputDevice {

	private final Mouse mouse;

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
	public ManagedMouse(final ManagedDisplay managedDisplay) {
		super(managedDisplay, DisplayEventType.BUTTON_PRESSED,
				DisplayEventType.BUTTON_RELEASED);
		this.mouse = getManagedDisplay().getDisplay().getMouse();
	}

	public int getAbsoluteX() {

		final int returnint = this.mouse.getRootX();

		return returnint;
	}

	public int getAbsoluteY() {
		final int returnint = this.mouse.getRootY();
		return returnint;
	}

	/**
	 * Refresh the geometric values of this <code>ManagedMouse</code> so it
	 * represents the values at the time of this call.
	 * 
	 * @ Thrown when the native mouse pointer has an illegal state.
	 */
	public void refresh() {
		this.mouse.refreshInfo();
	}

	@Override
	protected void doEffectiveGrab() {
		getManagedDisplay().getRoot().getPlatformRenderArea()
				.catchAllMouseInput();
	}

	@Override
	protected void doEffectiveRelease() {
		getManagedDisplay().getRoot().getPlatformRenderArea()
				.stopMouseInputCatching();
	}
}