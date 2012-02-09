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
package org.hyperdrive.core;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.hydrogen.displayinterface.Display;
import org.hydrogen.displayinterface.EventPropagator;
import org.hydrogen.displayinterface.PlatformRenderArea;
import org.hydrogen.displayinterface.PlatformRenderAreaAttributes;
import org.hydrogen.displayinterface.event.ButtonNotifyEvent;
import org.hydrogen.displayinterface.event.ConfigureRequestEvent;
import org.hydrogen.displayinterface.event.DestroyNotifyEvent;
import org.hydrogen.displayinterface.event.DisplayEvent;
import org.hydrogen.displayinterface.event.DisplayEventSource;
import org.hydrogen.displayinterface.event.FocusNotifyEvent;
import org.hydrogen.displayinterface.event.KeyNotifyEvent;
import org.hydrogen.displayinterface.event.MapRequestEvent;
import org.hydrogen.displayinterface.event.MouseEnterLeaveNotifyEvent;
import org.hydrogen.displayinterface.event.PropertyChangedNotifyEvent;
import org.hydrogen.displayinterface.event.UnmappedNotifyEvent;
import org.hydrogen.eventsystem.EventBus;
import org.hydrogen.paintinterface.PaintCall;
import org.hydrogen.paintinterface.PainterFactory;
import org.hyperdrive.input.ManagedKeyboard;
import org.hyperdrive.input.ManagedMouse;
import org.hyperdrive.widget.RealRoot;
import org.hyperdrive.widget.ViewFactory;
import org.hyperdrive.widget.VirtualRoot;

// TODO documentation
/**
 * Provides extra functionality by wrapping a platform specific implementation
 * of a {@link org.hydrogen.displayinterface.Display}. The
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
public class ManagedDisplay extends EventBus {

	private final Display display;
	private final EventDispatcher eventDispatcher;
	private final ManagedMouse managedMouse;
	private final ManagedKeyboard managedKeyboard;
	private final PainterFactory painterFactory;
	private final RealRoot realRoot;
	private final VirtualRoot defaultVirtualRoot;
	private final Map<DisplayEventSource, LinkedHashSet<EventBus>> eventConductorMap;
	private final Map<EventBus, DisplayEventSource> reverseEventConductorMap;
	private final Executor managedDisplayEventExecutor;
	private final WindowManagementInfo windowManagementInfo;

	private final ViewFactory<? extends PaintCall<?, ?>> viewFactory;

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
	public ManagedDisplay(final Display display,
			final ViewFactory<? extends PaintCall<?, ?>> widgetViewFactory) {
		{
			this.windowManagementInfo = new WindowManagementInfo(this);
		}
		{
			this.viewFactory = widgetViewFactory;
		}
		{
			this.painterFactory = display.getPainterFactory();
			this.eventConductorMap = new HashMap<DisplayEventSource, LinkedHashSet<EventBus>>(
					30);
			this.reverseEventConductorMap = new HashMap<EventBus, DisplayEventSource>(
					60);
			this.display = display;
			this.managedDisplayEventExecutor = Executors
					.newSingleThreadExecutor();
		}
		{
			this.realRoot = new RealRoot(this);
		}
		{
			this.defaultVirtualRoot = new VirtualRoot(getRealRootRenderArea());
			this.defaultVirtualRoot.setVisibility(true);
			this.defaultVirtualRoot.requestVisibilityChange();
			this.defaultVirtualRoot.activate();
		}
		{
			this.managedMouse = new ManagedMouse(this);
			this.managedKeyboard = new ManagedKeyboard(this);
		}
		{
			this.eventDispatcher = new EventDispatcher(this);
		}
	}

	public WindowManagementInfo getWindowManagementInfo() {
		return this.windowManagementInfo;
	}

	/**
	 * 
	 */
	public void startEventDispatcher() {
		// TODO this is more an X specific thing, create a more platform
		// neutral mechanism/interface.
		getRealRootRenderArea().getPlatformRenderArea().propagateEvent(
				EventPropagator.REDIRECT_CHILD_WINDOW_GEOMTRY_CHANGES);
		this.managedDisplayEventExecutor.execute(getEventDispatcher());
	}

	/**
	 * 
	 * @return
	 */
	public EventDispatcher getEventDispatcher() {
		return this.eventDispatcher;
	}

	/**
	 * 
	 *  
	 */
	public void manageUnmanagedClientWindows() {
		final PlatformRenderArea[] children = getRealRootRenderArea()
				.getPlatformRenderArea().getChildren();
		for (final PlatformRenderArea clientRenderArea : children) {
			final PlatformRenderAreaAttributes attributes = clientRenderArea
					.getPlatformRenderAreaAttributes();
			final boolean viewable = attributes.isViewable();
			final boolean overrideRedirect = attributes.isOverrideRedirect();
			final boolean registered = isEventSourceRegistered(clientRenderArea);
			if (!registered && !overrideRedirect && viewable) {
				final ClientWindow client = new ClientWindow(this,
						clientRenderArea);
				client.syncGeoToPlatformRenderAreaGeo();
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public ViewFactory<? extends PaintCall<?, ?>> getWidgetViewFactory() {
		return this.viewFactory;
	}

	/**
	 * The default <code>VirtualRoot</code> of this <code>ManagedDisplay</code>.
	 * <p>
	 * A <code>VirtualRoot</code> is a <code>BaseWidget</code> subclass that is
	 * used as a fake root for <code>ClientWindow</code>s. This means that every
	 * <code>ClientWindow</code> has the default <code>VirtualRoot</code> as
	 * it's initial parent.
	 * 
	 * @return The default {@link _NetVirtualRoots}.
	 */
	public VirtualRoot getDefaultVirtualRootRenderArea() {
		return this.defaultVirtualRoot;
	}

	/**
	 * The <code>Display</code> peer of this <code>ManagedDisplay</code>.
	 * <p>
	 * Calls to the native underlying display are done using this peer.
	 * 
	 * @return A {@link Display} peer.
	 */
	public Display getDisplay() {
		return this.display;
	}

	/**
	 * The <code>ManagedMouse</code> of this <code>ManagedDisplay</code>.
	 * <p>
	 * A <code>ManagedMouse</code> is backed by a <code>Mouse</code> which
	 * provides native mouse pointer operations.
	 * 
	 * @return A {@link ManagedMouse}.
	 */
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
	public PainterFactory getPainterFactory() {
		return this.painterFactory;
	}

	/**
	 * The <code>RealRoot</code> of this <code>ManagedDisplay</code>
	 * <p>
	 * A <code>RealRoot</code> is a <code>BaseWidget</code> subclass that is
	 * backed by the real native root window.
	 * 
	 * @return The {@link RealRoot}.
	 */
	public RealRoot getRealRootRenderArea() {
		return this.realRoot;
	}

	/**
	 * 
	 * @param eventSource
	 * @return
	 */
	public LinkedHashSet<EventBus> getEventConductors(
			final DisplayEventSource eventSource) {
		if (this.eventConductorMap.containsKey(eventSource)) {
			return this.eventConductorMap.get(eventSource);
		} else {
			final LinkedHashSet<EventBus> newEventBus = new LinkedHashSet<EventBus>();
			this.eventConductorMap.put(eventSource, newEventBus);
			return newEventBus;
		}
	}

	/**
	 * 
	 * @param eventSource
	 * @return
	 */
	boolean isEventSourceRegistered(final DisplayEventSource eventSource) {
		final boolean returnboolean = this.eventConductorMap
				.containsKey(eventSource);
		return returnboolean;
	}

	/**
	 * 
	 * @param eventSource
	 * @param eventBus
	 * 
	 */
	public void registerEventBus(final DisplayEventSource eventSource,
			final EventBus eventBus) {
		getEventConductors(eventSource).add(eventBus);
		this.reverseEventConductorMap.put(eventBus, eventSource);
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
	public void shutDown() {
		// TODO Shut down any paint back end and
		// release all resources.
		getEventDispatcher().shutDown();
		this.eventConductorMap.clear();
		this.display.shutDown();

	}

	@Override
	public String toString() {
		return String.format("Display: %s", this.display);
	}

	void unregisterEventBus(final EventBus eventBus) {
		final DisplayEventSource eventSource = this.reverseEventConductorMap
				.get(eventBus);
		getEventConductors(eventSource).remove(eventBus);
	}

	/**
	 * 
	 * @return
	 */
	public ManagedKeyboard getManagedKeyboard() {
		return this.managedKeyboard;
	}
}
