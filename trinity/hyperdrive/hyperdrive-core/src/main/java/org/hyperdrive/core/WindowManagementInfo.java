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

import java.util.ArrayList;
import java.util.List;

import org.hydrogen.displayinterface.event.FocusNotifyEvent;
import org.hydrogen.eventsystem.EventHandler;
import org.hyperdrive.geo.GeoEvent;
import org.hyperdrive.widget.VirtualRoot;
import org.hyperdrive.widget.VirtualRootEvent;
import org.hyperdrive.widget.Widget;
import org.hyperdrive.widget.WidgetEvent;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class WindowManagementInfo implements WindowManagementInfoListener {

	private final ManagedDisplay managedDisplay;

	private final List<VirtualRoot> virtualRoots;
	private final List<ClientWindow> clientWindows;
	private final List<ClientWindow> clientWindowsStacking;

	private VirtualRoot virtualRootIsActive;
	private ClientWindow clientWindowHasFocus;

	private final List<WindowManagementInfoListener> wmInfoListeners;

	/**
	 * 
	 * @param managedDisplay
	 */
	protected WindowManagementInfo(final ManagedDisplay managedDisplay) {
		this.wmInfoListeners = new ArrayList<WindowManagementInfoListener>();
		this.managedDisplay = managedDisplay;
		this.virtualRoots = new ArrayList<VirtualRoot>(10);
		this.clientWindows = new ArrayList<ClientWindow>(30);
		this.clientWindowsStacking = new ArrayList<ClientWindow>(30);
		initDisplayListeners(getManagedDisplay());
	}

	/**
	 * 
	 * @param wmInfoListener
	 */
	public void addWindowManagementInfoListener(
			final WindowManagementInfoListener wmInfoListener) {
		this.wmInfoListeners.add(wmInfoListener);
	}

	/**
	 * 
	 * @param wmInfoListener
	 */
	public void deleteWindowManagementInfoListener(
			final BaseWindowManagementInfoListener wmInfoListener) {
		this.wmInfoListeners.remove(wmInfoListener);
	}

	/**
	 * 
	 * @param managedDisplay
	 */
	protected void initDisplayListeners(final ManagedDisplay managedDisplay) {
		managedDisplay.addEventHandler(new EventHandler<ClientEvent>() {
			@Override
			public void handleEvent(final ClientEvent event) {
				final ClientWindow client = event.getRenderArea();
				initClientListeners(client);
			}
		}, ClientEvent.CLIENT_INITIALIZED);
		managedDisplay.addEventHandler(new EventHandler<WidgetEvent<Widget>>() {

			@Override
			public void handleEvent(final WidgetEvent<Widget> event) {
				final Widget widget = event.getRenderArea();
				if (widget instanceof VirtualRoot) {
					final VirtualRoot virtualRoot = (VirtualRoot) widget;
					initVirtualRootListeners(virtualRoot);
				}
			}
		}, WidgetEvent.WIDGET_INITIALIZED);

	}

	/**
	 * 
	 * @param clientWindow
	 */
	protected void initClientListeners(final ClientWindow clientWindow) {
		// handle new client window
		this.clientWindows.add(clientWindow);

		// notify of new client window
		onClientWindowNew(clientWindow);

		// listen for client window destroyed
		clientWindow.addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				WindowManagementInfo.this.clientWindows.remove(clientWindow);
				onClientWindowDestroyed(clientWindow);
			}
		}, GeoEvent.DESTROYED);

		clientWindow.addEventHandler(new EventHandler<FocusNotifyEvent>() {

			@Override
			public void handleEvent(final FocusNotifyEvent event) {
				setClientWindowHasFocus(clientWindow);
				onClientWindowGainFocus(clientWindow);
			}
		}, FocusNotifyEvent.IN_TYPE);
		clientWindow.addEventHandler(new EventHandler<FocusNotifyEvent>() {
			@Override
			public void handleEvent(final FocusNotifyEvent event) {
				setClientWindowHasFocus(null);
				onClientWindowLostFocus(clientWindow);
			}
		}, FocusNotifyEvent.OUT_TYPE);

		// TODO when a parent of the client is raised we do not get an event...
		// =>
		// Fix this.
		clientWindow.addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				WindowManagementInfo.this.clientWindowsStacking
						.remove(clientWindow);
				WindowManagementInfo.this.clientWindowsStacking
						.add(clientWindow);
				onClientWindowRaise(clientWindow);
			}
		}, GeoEvent.RAISE);
		clientWindow.addEventHandler(new EventHandler<GeoEvent>() {

			@Override
			public void handleEvent(final GeoEvent event) {
				WindowManagementInfo.this.clientWindowsStacking
						.remove(clientWindow);
				WindowManagementInfo.this.clientWindowsStacking.add(0,
						clientWindow);
				onClientWindowLower(clientWindow);
			}
		}, GeoEvent.LOWER);
	}

	/**
	 * 
	 * @param virtualRoot
	 */
	protected void initVirtualRootListeners(final VirtualRoot virtualRoot) {
		// handle new virtual root
		onVirtualRootNew(virtualRoot);

		this.virtualRoots.add(virtualRoot);

		// add virtual root listeners:

		// listen for virtual root activation
		virtualRoot.addEventHandler(new EventHandler<VirtualRootEvent>() {
			@Override
			public void handleEvent(final VirtualRootEvent event) {
				setVirtualRootIsActive(virtualRoot);
				onVirtualRootActivate(virtualRoot);
			}
		}, VirtualRootEvent.VIRTUAL_ROOT_ACTIVATED);
		// listen for virtual root deletion
		virtualRoot.addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				WindowManagementInfo.this.virtualRoots.remove(virtualRoot);
				onVirtualRootDestroyed(virtualRoot);
			}
		}, GeoEvent.DESTROYED);

		// TODO listen for virtual root state changes: rearangement(?)
	}

	/**
	 * 
	 * @return
	 */
	public VirtualRoot getVirtualRootIsActive() {
		return this.virtualRootIsActive;
	}

	/**
	 * 
	 * @param activeVirtualRoot
	 */
	protected void setVirtualRootIsActive(final VirtualRoot activeVirtualRoot) {
		this.virtualRootIsActive = activeVirtualRoot;
		// TODO deactivate(=hide) other virtual roots
	}

	/**
	 * 
	 * @return
	 */
	public ClientWindow getClientWindowHasFocus() {
		return this.clientWindowHasFocus;
	}

	/**
	 * 
	 * @param focusClientWindow
	 */
	protected void setClientWindowHasFocus(final ClientWindow focusClientWindow) {
		this.clientWindowHasFocus = focusClientWindow;
	}

	@Override
	public void onVirtualRootNew(final VirtualRoot virtualRoot) {
		for (final WindowManagementInfoListener wmInfoListener : this.wmInfoListeners) {
			wmInfoListener.onVirtualRootNew(virtualRoot);
		}
	}

	@Override
	public void onVirtualRootDestroyed(final VirtualRoot virtualRoot) {
		for (final WindowManagementInfoListener wmInfoListener : this.wmInfoListeners) {
			wmInfoListener.onVirtualRootDestroyed(virtualRoot);
		}
	}

	@Override
	public void onVirtualRootActivate(final VirtualRoot virtualRootActive) {
		for (final WindowManagementInfoListener wmInfoListener : this.wmInfoListeners) {
			wmInfoListener.onVirtualRootActivate(virtualRootActive);
		}
	}

	@Override
	public void onClientWindowGainFocus(final ClientWindow clientWindow) {
		for (final WindowManagementInfoListener wmInfoListener : this.wmInfoListeners) {
			wmInfoListener.onClientWindowGainFocus(clientWindow);
		}
	}

	@Override
	public void onClientWindowLostFocus(final ClientWindow clientWindow) {
		for (final WindowManagementInfoListener wmInfoListener : this.wmInfoListeners) {
			wmInfoListener.onClientWindowLostFocus(clientWindow);
		}
	}

	@Override
	public void onClientWindowRaise(final ClientWindow clientWindow) {
		for (final WindowManagementInfoListener wmInfoListener : this.wmInfoListeners) {
			wmInfoListener.onClientWindowRaise(clientWindow);
		}
	}

	@Override
	public void onClientWindowLower(final ClientWindow clientWindow) {
		for (final WindowManagementInfoListener wmInfoListener : this.wmInfoListeners) {
			wmInfoListener.onClientWindowLower(clientWindow);
		}
	}

	@Override
	public void onClientWindowNew(final ClientWindow clientWindow) {
		for (final WindowManagementInfoListener wmInfoListener : this.wmInfoListeners) {
			wmInfoListener.onClientWindowNew(clientWindow);
		}
	}

	@Override
	public void onClientWindowDestroyed(final ClientWindow clientWindow) {
		for (final WindowManagementInfoListener wmInfoListener : this.wmInfoListeners) {
			wmInfoListener.onClientWindowDestroyed(clientWindow);
		}
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
	 * @return
	 */
	public VirtualRoot[] getVirtualRootsCreated() {
		return this.virtualRoots.toArray(new VirtualRoot[this.virtualRoots
				.size()]);
	}

	/**
	 * 
	 * @return
	 */
	public ClientWindow[] getClientWindowsCreated() {
		return this.clientWindows.toArray(new ClientWindow[this.clientWindows
				.size()]);
	}

	/**
	 * 
	 * @return
	 */
	public ClientWindow[] getClientWindowsStacking() {
		return this.clientWindowsStacking
				.toArray(new ClientWindow[this.clientWindows.size()]);
	}
}
