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
package org.fusion.x11.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hydrogen.api.display.Display;
import org.hydrogen.api.display.DisplayPlatform;
import org.hydrogen.api.display.EventProducer;
import org.hydrogen.api.display.EventProducerFactory;
import org.hydrogen.api.display.ResourceHandle;
import org.hydrogen.api.event.Type;
import org.hydrogen.api.paint.PainterFactoryProvider;

// TODO split painting & window management function providers into separate
// objects that way we can have completely seperate painting & window management
// providers.
// TODO documentation
/**
 * An <code>XDisplayPlatform</code> provides the base methods needed to access
 * and create an X display server connection as defined by a
 * <code>XDisplayPlatform</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XDisplayPlatform implements DisplayPlatform {

	private final Set<EventProducerFactory> eventProducerFactories;
	private final PainterFactoryProvider painterFactoryProvider;
	private final XCoreInterfaceProvider xCoreInterfaceProvider;
	private final XResourcesRegistry resourcesRegistry;

	public final Map<Integer, Type> nativeXEventsMap;

	/**
	 * Create a new <code>XDisplayPlatform</code> with the given
	 * <code>PainterFactoryProvider</code> and the given
	 * <code>XCoreInterfaceProvider</code>.
	 * <p>
	 * The given <code>PainterFactoryProvider</code> will be used to create the
	 * appropriate <code>Painter</code> for a <code>Paintable</code>.
	 * <p>
	 * The given <code>XCoreInterfaceProvider</code> will be used to provide an
	 * <code>XCoreInterface</code> implementation to access to the native
	 * underlying display platform.
	 * 
	 * @param painterFactoryProvider
	 *            A {@link PainterFactoryProvider}.
	 * @param xCoreInterfaceProvider
	 *            An {@link XCoreInterfaceProvider}.
	 */
	public XDisplayPlatform(
			final PainterFactoryProvider painterFactoryProvider,
			final XCoreInterfaceProvider xCoreInterfaceProvider) {
		this.nativeXEventsMap = new HashMap<Integer, Type>();

		this.resourcesRegistry = new XResourcesRegistry();
		this.xCoreInterfaceProvider = xCoreInterfaceProvider;
		this.eventProducerFactories = new HashSet<EventProducerFactory>();
		this.painterFactoryProvider = painterFactoryProvider;

		addEventProducerFactory(new EventProducerFactory() {
			@Override
			public EventProducer getNewEventProducer(final Display display) {
				// The passed argument "display" is an XDisplay which is an
				// event producer itself. We return it so the display adds
				// itself as an event producer.
				return display;
			}
		});
	}

	/**
	 * 
	 * @param eventProducerFactory
	 */
	public void addEventProducerFactory(
			final EventProducerFactory eventProducerFactory) {
		this.eventProducerFactories.add(eventProducerFactory);
	}

	@Override
	public EventProducerFactory[] getEventProducerFactories() {
		return this.eventProducerFactories
				.toArray(new EventProducerFactory[this.eventProducerFactories
						.size()]);
	}

	@Override
	public PainterFactoryProvider getPainterFactoryProvider() {
		return this.painterFactoryProvider;
	}

	@Override
	public XDisplay newDisplay(final String displayAddress) {
		final XDisplay xDisplay = this.xCoreInterfaceProvider
				.getNewXCoreInterface(this)
				.openDisplay(this, displayAddress, 0);
		return xDisplay;
	}

	// move to painting provider(?)
	@Override
	public XWindow findPaintablePlatformRenderAreaFromId(final Display display,
			final ResourceHandle windowID) {

		final XID xid = new XID((XDisplay) display, (XResourceHandle) windowID);
		final XWindow returnPlatformRenderArea = getResourcesRegistry()
				.getClientXWindow(xid);

		return returnPlatformRenderArea;
	}

	// move to windowmanagement provider
	public XResourcesRegistry getResourcesRegistry() {
		return this.resourcesRegistry;
	}
}
