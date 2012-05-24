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
package org.trinity.shell.foundation.impl;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.trinity.core.display.api.Display;
import org.trinity.core.display.api.DisplayEventSelector;
import org.trinity.core.display.api.event.ButtonNotifyEvent;
import org.trinity.core.display.api.event.ConfigureRequestEvent;
import org.trinity.core.display.api.event.DestroyNotifyEvent;
import org.trinity.core.display.api.event.DisplayEvent;
import org.trinity.core.display.api.event.DisplayEventSource;
import org.trinity.core.display.api.event.FocusNotifyEvent;
import org.trinity.core.display.api.event.KeyNotifyEvent;
import org.trinity.core.display.api.event.MapRequestEvent;
import org.trinity.core.display.api.event.MouseVisitationNotifyEvent;
import org.trinity.core.display.api.event.PropertyChangedNotifyEvent;
import org.trinity.core.display.api.event.UnmappedNotifyEvent;
import org.trinity.core.event.api.EventBus;
import org.trinity.core.event.api.EventManager;
import org.trinity.shell.foundation.api.ManagedDisplay;
import org.trinity.shell.foundation.api.RenderAreaFactory;
import org.trinity.shell.foundation.api.event.ClientCreatedHandler;
import org.trinity.shell.foundation.api.event.DisplayEventHandler;
import org.trinity.shell.widget.api.Root;

import com.google.inject.Inject;
import com.google.inject.Singleton;

// TODO documentation
/**
 * Provides extra functionality by wrapping a platform specific implementation
 * of a {@link org.trinity.core.display.api.Display}. The
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
 * <li> {@link MouseVisitationNotifyEvent}</li>
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
@Singleton
public class ManagedDisplayImpl extends EventBus implements ManagedDisplay {

	private final Display display;
	private final DisplayEventDispatcher displayEventDispatcher;
	private final Executor managedDisplayEventExecutor;
	private final Root root;

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
	 */
	@Inject
	protected ManagedDisplayImpl(	final Display display,
									final Root root,
									final RenderAreaFactory clientFactory) {
		this.display = display;
		this.managedDisplayEventExecutor = Executors.newSingleThreadExecutor();
		this.displayEventDispatcher = new DisplayEventDispatcher(	display,
																	clientFactory);
		this.root = root;
	}

	/**
	 * 
	 */
	@Override
	public void start() {
		// TODO this is more an X specific thing, create a more platform
		// neutral mechanism/interface and hide any reference to RealRoot.
		this.root
				.getPlatformRenderArea()
				.propagateEvent(DisplayEventSelector.REDIRECT_CHILD_WINDOW_GEOMETRY_CHANGES);
		this.managedDisplayEventExecutor.execute(getEventDispatcher());
	}

	/**
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
	 * Shut down all <code>Paintable</code>s, shut down the backing paint engine
	 * for these <code>PaintAble</code>s, shut down the event handling mechanism
	 * and release all resources.
	 * <p>
	 * This does not shut down any <code>ClientWindow</code>s nor does it shut
	 * down the native display.
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

	public void addEventManagerForDisplayEventSource(	final EventManager manager,
														final DisplayEventSource forSource) {
		this.displayEventDispatcher
				.addEventManagerForDisplayEventSource(manager, forSource);
	}

	@Override
	public void addDisplayEventHandler(final DisplayEventHandler<? extends DisplayEvent> displayEventHandler) {
		this.displayEventDispatcher.addDisplayEventHandler(displayEventHandler);
	}

	@Override
	public void addDisplayEventManager(	final EventManager manager,
										final DisplayEventSource forDisplayEventSource) {
		this.displayEventDispatcher
				.addEventManagerForDisplayEventSource(	manager,
														forDisplayEventSource);
	}

	@Override
	public void deliverNextDisplayEvent(final boolean block) {
		this.displayEventDispatcher.dispatchNextEvent(block);
	}

	@Override
	public void removeDisplayEventHandler(final DisplayEventHandler<? extends DisplayEvent> displayEventHandler) {
		this.displayEventDispatcher
				.removeDisplayEventHandler(displayEventHandler);
	}

	@Override
	public void addClientCreatedHandler(final ClientCreatedHandler clientCreatedHandler) {
		this.displayEventDispatcher
				.addClientCreatedHandler(clientCreatedHandler);
	}

	@Override
	public void removeClientCreatedHandler(final ClientCreatedHandler clientCreatedHandler) {
		this.displayEventDispatcher
				.removeClientCreatedHandler(clientCreatedHandler);
	}
}
