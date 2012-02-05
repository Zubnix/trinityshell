/*
 * This file is part of Fusion-X11.
 * 
 * Fusion-X11 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.x11.icccm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.XWindow;
import org.hydrogen.displayinterface.PlatformRenderAreaPreferences;
import org.hydrogen.displayinterface.Property;
import org.hydrogen.displayinterface.PropertyInstance;
import org.hydrogen.displayinterface.event.DisplayEventSource;
import org.hydrogen.displayinterface.event.PropertyChangedNotifyEvent;
import org.hydrogen.eventsystem.EventHandler;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class IcccmClientManager {

	private final Set<XWindow> clients;
	private final IcccmAtoms icccmAtoms;
	private final XDisplay display;
	private final Map<Property<? extends PropertyInstance>, IcccmPropertyManager<? extends PropertyInstance>> propertyManagersMap;

	public IcccmClientManager(final Icccm icccm) {
		this.icccmAtoms = icccm.getIcccmAtoms();
		this.display = icccm.getDisplay();
		this.clients = new HashSet<XWindow>();
		this.propertyManagersMap = new HashMap<Property<? extends PropertyInstance>, IcccmPropertyManager<? extends PropertyInstance>>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -5952954021738012405L;

			{
				put(getIcccmAtoms().getWmTransientFor(),
						new WmTransientForManager());
				put(getIcccmAtoms().getWmNormalHints(),
						new WmNormalHintsManager());
				put(getIcccmAtoms().getWmHints(), new WmHintsManager(
						getIcccmAtoms()));
				put(getIcccmAtoms().getWmProtocols(), new WmProtocolsManager(
						getIcccmAtoms()));

			}
		};
		listenForPropertyChanges();
	}

	public Set<XWindow> getClients() {
		return this.clients;
	}

	public IcccmAtoms getIcccmAtoms() {
		return this.icccmAtoms;
	}

	public XDisplay getDisplay() {
		return this.display;
	}

	/**
	 * Provides partial automatic icccm protocol handling for the given client.
	 * The following properties are automatically handled:
	 * <p>
	 * <ul>
	 * <li>WM_TRANSIENT_FOR: Automatically updates the client type and client
	 * relation to other clients.</li>
	 * <li>WM_NORMAL_HINTS: Automatically interpret WM_SIZE_HINTS and provide
	 * hinting information through {@link PlatformRenderAreaPreferences} for the
	 * client.</li>
	 * <li>WM_HINTS: Automatically interpret and update client icon, input
	 * hinting & client state hinting.</li>
	 * <li>WM_PROTOCOLS: Automatically interpret client input hinting.</li>
	 * <p>
	 * The following icccm protocol can not be automatically handled and should
	 * be implemented by the top level window manager implementation. Check the
	 * ICCCM documentation for details.
	 * <p>
	 * <ul>
	 * </ul>
	 * <li>WM_CLASS</li>
	 * <li>WM_CLIENT_MACHINE</li>
	 * <li>WM_COLORMAP_WINDOWS</li>
	 * <li>WM_ICON_NAME</li>
	 * <li>WM_ICON_SIZE</li>
	 * <li>WM_NAME</li>
	 * <li>WM_STATE</li>
	 * </ul>
	 * 
	 * @param client
	 */
	public void manageClient(final XWindow client) {
		this.clients.add(client);
		handleProperties(client);
	}

	private void handleProperties(final XWindow client) {
		for (final Entry<Property<? extends PropertyInstance>, IcccmPropertyManager<? extends PropertyInstance>> propertyManagerEntry : this.propertyManagersMap
				.entrySet()) {
			final Property<? extends PropertyInstance> property = propertyManagerEntry
					.getKey();
			final PropertyInstance propertyInstance = client
					.getPropertyInstance(property);
			@SuppressWarnings("unchecked")
			final IcccmPropertyManager<PropertyInstance> propertyManager = (IcccmPropertyManager<PropertyInstance>) propertyManagerEntry
					.getValue();
			propertyManager.manageIcccmProperty(client, propertyInstance);
		}
	}

	private void listenForPropertyChanges() {
		// listen for window property changes
		getDisplay().addEventHandler(
				new EventHandler<PropertyChangedNotifyEvent>() {
					@Override
					public void handleEvent(
							final PropertyChangedNotifyEvent event) {
						final Property<? extends PropertyInstance> property = event.getChangedProperty();
						final DisplayEventSource source = event
								.getEventSource();
						if (getClients().contains(source)) {
							handleChangedProperty((XWindow) source, property);
						}

					}
				}, PropertyChangedNotifyEvent.TYPE);

	}

	private void handleChangedProperty(final XWindow client,
			final Property<? extends PropertyInstance> property) {
		if (this.propertyManagersMap.containsKey(property)) {
			@SuppressWarnings("unchecked")
			final IcccmPropertyManager<PropertyInstance> propertyManager = (IcccmPropertyManager<PropertyInstance>) this.propertyManagersMap
					.get(property);
			propertyManager.manageIcccmProperty(client,
					client.getPropertyInstance(property));
		}
	}
}