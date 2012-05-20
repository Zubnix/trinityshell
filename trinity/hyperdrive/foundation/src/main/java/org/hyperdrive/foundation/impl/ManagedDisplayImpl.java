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
package org.hyperdrive.foundation.impl;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.hydrogen.display.api.Display;
import org.hydrogen.display.api.base.EventPropagator;
import org.hydrogen.display.api.event.ButtonNotifyEvent;
import org.hydrogen.display.api.event.ConfigureRequestEvent;
import org.hydrogen.display.api.event.DestroyNotifyEvent;
import org.hydrogen.display.api.event.DisplayEvent;
import org.hydrogen.display.api.event.DisplayEventSource;
import org.hydrogen.display.api.event.FocusNotifyEvent;
import org.hydrogen.display.api.event.KeyNotifyEvent;
import org.hydrogen.display.api.event.MapRequestEvent;
import org.hydrogen.display.api.event.MouseEnterLeaveNotifyEvent;
import org.hydrogen.display.api.event.PropertyChangedNotifyEvent;
import org.hydrogen.display.api.event.UnmappedNotifyEvent;
import org.hydrogen.event.api.EventManager;
import org.hydrogen.event.api.base.EventBus;
import org.hydrogen.paint.api.PainterFactory;
import org.hyperdrive.foundation.api.ManagedDisplay;
import org.hyperdrive.foundation.api.event.ClientCreatedHandler;
import org.hyperdrive.foundation.api.event.DisplayEventHandler;
import org.hyperdrive.input.api.ManagedKeyboard;
import org.hyperdrive.input.api.ManagedMouse;

import org.hyperdrive.widget.api.Root;
import org.hyperdrive.widget.api.Widget;

import com.google.inject.Inject;

// TODO documentation
/**
 * Provides extra functionality by wrapping a platform specific implementation
 * of a {@link org.hydrogen.display.api.Display}. The
 * <code>ManagedDisplay</code> is at the core of any hyperdrive object
 * interacting with the platform display server.
 * <p>
 * The <code>ManagedDisplay</code> is at the root of the display event
 * hierarchy. This means that any <code>DisplayEvent</code> will first pass
 * through it's respective <code>ManagedDisplay</code> before being delivered at
 * the intended source. The following {@link DisplayEvent}s are emitted:
 * <ul>
 * <li> {@link ConfigureRequestEvent}</li>
 * <li> {@link MapRequestEvent}</li>
 * <li> {@link ButtonNotifyEvent}</li>
 * <li> {@link KeyNotifyEvent}</li>
 * <li> {@link DestroyNotifyEvent}</li>
 * <li> {@link UnmappedNotifyEvent}</li>
 * <li> {@link MouseEnterLeaveNotifyEvent}</li>
 * <li> {@link PropertyChangedNotifyEvent}</li>
 * <li> {@link FocusNotifyEvent}</li>
 * </ul>
 * <p>
 * The <code>ManagedDisplay</code> gives access to:
 * <ul>
 * <li>the display's root window: {@link ManagedDisplay#getRealRootRenderArea()}
 * </li>
 * <li>the display's default virtual root window (default desktop window):
 * {@link ManagedDisplay#getDefaultVirtualRootRenderArea()}</li>
 * <li>the display's keyboard: {@link ManagedDisplay#getManagedKeyboard()}</li>
 * <li>the display's mouse: {@link ManagedDisplay#getManagedMouse()}</li>
 * <li>the display's window management bookkeeping:
 * {@link ManagedDisplay#getWindowManagementInfo()}</li>
 * <li>the visual representation of widgets created for this display:
 * {@link ManagedDisplay#getWidgetViewFactory()}</li>
 * </ul>
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ManagedDisplayImpl extends EventBus implements ManagedDisplay {

	private final Display display;

	private final DisplayEventDispatcher displayEventDispatcher;
	private final PainterFactory painterFactory;
	private final Executor managedDisplayEventExecutor;

	private ManagedMouse managedMouse;
	private ManagedKeyboard managedKeyboard;
	private Root root;

	/**
	 * Wrap the given native <code>Display</code> implementation with the given
	 * <code>ViewFactory</code> implementation.
	 * <p>
	 * The <code>Display</code> argument will be managed by the newly created
	 * <code>ManagedDisplay</code>. <code>DisplayEvent</code>s will be fetched
	 * and dispatched to their corresponding event source. Any other call to the
	 * native display will go through this created <code>ManagedDisplay</code>
	 * to the <code>Display</code>.
	 * <p>
	 * The <code>ViewFactory</code> argument will function as a
	 * <code>View</code> constructor for <code>Widget</code>s that have this
	 * <code>ManagedDisplay</code> set.
	 * 
	 * @param display
	 *            A native <code>Display</code> implementation.
	 * @param widgetViewFactory
	 *            A <code>ViewFactory</code> implementation.
	 * 
	 */
	@Inject
	protected ManagedDisplayImpl(final Display display,
			ManagedMouse managedMouse, ManagedKeyboard managedKeyboard,
			Root root) {

		// protected to reduce visibility and encourage dependency injection
		{
			// this.windowManagementInfo = new WindowManagementInfo(this);
		}
		{
			this.painterFactory = display.getPainterFactory();
			this.display = display;
			this.managedDisplayEventExecutor = Executors
					.newSingleThreadExecutor();
		}
		{
			this.managedMouse = managedMouse;
			this.managedKeyboard = managedKeyboard;
		}
		{
			this.displayEventDispatcher = new DisplayEventDispatcher(this);
		}
		{
			this.root = root;
		}
	}

	// public WindowManagementInfo getWindowManagementInfo() {
	// return this.windowManagementInfo;
	// }

	/**
	 * 
	 */
	@Override
	public void start() {
		// TODO this is more an X specific thing, create a more platform
		// neutral mechanism/interface and hide any reference to RealRoot.
		getRoot().getPlatformRenderArea().propagateEvent(
				EventPropagator.REDIRECT_CHILD_WINDOW_GEOMTRY_CHANGES);
		this.managedDisplayEventExecutor.execute(getEventDispatcher());
	}

	/**
	 * 
	 * @return
	 */
	protected DisplayEventDispatcher getEventDispatcher() {
		return this.displayEventDispatcher;
	}

	/**
	 * 
	 *  
	 */
	public void manageUnmanagedClientWindows() {
		// TODO
		// final PlatformRenderArea[] children =
		// getRoot().getPlatformRenderArea()
		// .getChildren();
		// for (final PlatformRenderArea clientRenderArea : children) {
		// final PlatformRenderAreaAttributes attributes = clientRenderArea
		// .getPlatformRenderAreaAttributes();
		// final boolean viewable = attributes.isViewable();
		// final boolean overrideRedirect = attributes.isOverrideRedirect();
		// final boolean registered = isEventSourceRegistered(clientRenderArea);
		// if (!registered && !overrideRedirect && viewable) {
		// final ClientWindow client = new ClientWindow(this,
		// clientRenderArea);
		// client.syncGeoToPlatformRenderAreaGeo();
		// }
		// }
	}

	/**
	 * The <code>Display</code> peer of this <code>ManagedDisplay</code>.
	 * <p>
	 * Calls to the native underlying display are done using this peer.
	 * 
	 * @return A {@link Display} peer.
	 */
	@Override
	public Display getDisplay() {
		return this.display;
	}

	/**
	 * The <code>ManagedMouse</code> of this <code>ManagedDisplay</code>.
	 * <p>
	 * A <code>ManagedMouse</code> is backed by a <code>Mouse</code> which
	 * provides native mouse pointer operations.
	 * 
	 * @return A {@link BaseManagedMouse}.
	 */
	@Override
	public ManagedMouse getManagedMouse() {
		return this.managedMouse;
	}

	/**
	 * The <code>PainterFactory</code> of this <code>ManagedDisplay</code>.
	 * <p>
	 * A <code>PainterFactory</code> is responsible for providing
	 * <code>Painter</code> implementations for <code>Paintable</code>s that
	 * 'live' on this <code>ManagedDisplay</code>.
	 * 
	 * @return A {@link PainterFactory}.
	 */
	@Override
	public PainterFactory getPainterFactory() {
		return this.painterFactory;
	}

	/**
	 * Shut down all <code>Paintable</code>s, shut down the backing paint engine
	 * for these <code>PaintAble</code>s, shut down the event handling mechanism
	 * and release all resources.
	 * <p>
	 * This does not shut down any <code>ClientWindow</code>s nor does it shut
	 * down the native display.
	 * 
	 */
	@Override
	public void shutDown() {
		// TODO Shut down any paint back end and
		// release all resources.
		getEventDispatcher().shutDown();
		// this.eventConductorMap.clear();
		this.display.shutDown();

	}

	@Override
	public String toString() {
		return String.format("Display: %s", this.display);
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public ManagedKeyboard getManagedKeyboard() {
		return this.managedKeyboard;
	}

	@Override
	public Widget getRoot() {
		return this.root;
	}

	public void addEventManagerForDisplayEventSource(
			final EventManager manager, final DisplayEventSource forSource) {
		this.displayEventDispatcher.addEventManagerForDisplayEventSource(
				manager, forSource);
	}

	@Override
	public void addDisplayEventHandler(
			final DisplayEventHandler<? extends DisplayEvent> displayEventHandler) {
		this.displayEventDispatcher.addDisplayEventHandler(displayEventHandler);
	}

	@Override
	public void addDisplayEventManager(final EventManager manager,
			final DisplayEventSource forDisplayEventSource) {
		this.displayEventDispatcher.addEventManagerForDisplayEventSource(
				manager, forDisplayEventSource);
	}

	@Override
	public void deliverNextDisplayEvent(final boolean block) {
		this.displayEventDispatcher.dispatchNextEvent(block);
	}

	@Override
	public void removeDisplayEventHandler(
			final DisplayEventHandler<? extends DisplayEvent> displayEventHandler) {
		this.displayEventDispatcher
				.removeDisplayEventHandler(displayEventHandler);
	}

	@Override
	public void addClientCreatedHandler(
			final ClientCreatedHandler clientCreatedHandler) {
		this.displayEventDispatcher
				.addClientCreatedHandler(clientCreatedHandler);
	}

	@Override
	public void removeClientCreatedHandler(
			final ClientCreatedHandler clientCreatedHandler) {
		this.displayEventDispatcher
				.removeClientCreatedHandler(clientCreatedHandler);
	}
}
