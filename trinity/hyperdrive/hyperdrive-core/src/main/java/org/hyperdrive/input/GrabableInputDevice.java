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

import org.hydrogen.eventsystem.Event;
import org.hydrogen.eventsystem.EventBus;
import org.hydrogen.eventsystem.EventHandler;
import org.hydrogen.eventsystem.Type;
import org.hyperdrive.core.ManagedDisplay;

// TODO documentation
/**
 * A <code>GrabableInputDevice</code> is an input device who's input can be
 * redirected (grabbed) so that all input events originating from the device are
 * reported to the grabber.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public abstract class GrabableInputDevice {
	private final Type[] grabEventTypes;
	private final ManagedDisplay managedDisplay;

	private boolean grabbed;
	private EventHandler<Event<Type>> grabHandler;

	/**
	 * 
	 * @param managedDisplay
	 * @param grabEventTypes
	 */
	public GrabableInputDevice(final ManagedDisplay managedDisplay,
			final Type... grabEventTypes) {
		this.grabEventTypes = grabEventTypes;
		this.managedDisplay = managedDisplay;
	}

	/**
	 * 
	 * @param grabber
	 * @throws HyperdriveException
	 */
	public void grab(final EventBus grabber) {
		if (isGrabbed()) {
			throw new IllegalStateException("Managed keyboard already grabbed.");
		}

		setGrabbed(true);

		// pass all keyevents to the given eventbus, regardless of their source
		this.grabHandler = new EventHandler<Event<Type>>() {
			@Override
			public void handleEvent(final Event<Type> event) {
				if (grabber != null) {
					grabber.fireEvent(event);
				}
			}
		};

		for (final Type type : this.grabEventTypes) {
			getManagedDisplay().addEventHandler(this.grabHandler, type);
		}

		doEffectiveGrab();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isGrabbed() {
		return this.grabbed;
	}

	/**
	 * 
	 * @param grabbed
	 */
	protected void setGrabbed(final boolean grabbed) {
		this.grabbed = grabbed;
	}

	/**
	 * 
	 * @throws HyperdriveException
	 */
	public void release() {
		for (final Type type : this.grabEventTypes) {
			getManagedDisplay().removeEventHandler(this.grabHandler, type);
		}

		this.grabHandler = null;
		setGrabbed(false);

		doEffectiveRelease();
	}

	/**
	 * 
	 * @return
	 */
	public ManagedDisplay getManagedDisplay() {
		return this.managedDisplay;
	}

	/**
	 * 
	 * @throws HyperdriveException
	 */
	protected abstract void doEffectiveGrab();

	/**
	 * 
	 * @throws HyperdriveException
	 */
	protected abstract void doEffectiveRelease();
}
