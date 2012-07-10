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
package org.trinity.shell.input.impl;

import org.trinity.foundation.display.api.event.ButtonNotifyEvent;
import org.trinity.foundation.input.api.Mouse;
import org.trinity.foundation.shared.geometry.api.Coordinates;
import org.trinity.foundation.shared.geometry.api.GeometryFactory;
import org.trinity.shell.core.api.ManagedDisplay;
import org.trinity.shell.core.api.RenderArea;
import org.trinity.shell.input.api.ManagedMouse;

import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

/**
 * A <code>ManagedMouse</code> represents a mouse pointer from a
 * <code>ManagedDisplay</code>. A <code>ManagedMouse</code> provides location
 * based input by wrapping a native implementation of a {@link Mouse}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
@Singleton
public class ManagedMouseImpl extends AbstractInputDevice implements
		ManagedMouse {

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

	private final ManagedDisplay managedDisplay;
	private final RenderArea root;
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
	protected ManagedMouseImpl(	final ManagedDisplay managedDisplay,
								@Named("root") final RenderArea root,
								final GeometryFactory geometryFactory,
								final Mouse mouse) {
		this.managedDisplay = managedDisplay;
		this.root = root;
		this.mouse = mouse;
		this.buttonPressedForwarder = new ButtonPressedForwarder();
		this.buttonReleasedForwarder = new ButtonReleasedForwarder();
	}

	@Override
	public void release() {
		this.managedDisplay
				.removeDisplayEventHandler(this.buttonPressedForwarder);
		this.managedDisplay
				.removeDisplayEventHandler(this.buttonReleasedForwarder);
		this.root.getPlatformRenderArea().stopMouseInputCatching();
	}

	@Override
	protected void delegateInputEventsAndGrab() {
		this.managedDisplay.addDisplayEventHandler(this.buttonPressedForwarder);
		this.managedDisplay
				.addDisplayEventHandler(this.buttonReleasedForwarder);
		this.root.getPlatformRenderArea().catchAllMouseInput();
	}

	@Override
	public Coordinates getAbsolutePosition() {
		return this.mouse.getRootCoordinates();
	}
}