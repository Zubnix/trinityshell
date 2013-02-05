/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.display.x11.impl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_property_cookie_t;
import org.freedesktop.xcb.xcb_icccm_get_text_property_reply_t;
import org.freedesktop.xcb.xcb_icccm_get_wm_protocols_reply_t;
import org.freedesktop.xcb.xcb_property_notify_event_t;
import org.trinity.foundation.api.display.DisplaySurface;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(Type.IMPLEMENTATION))
@Singleton
public class XPropertyCache {
	// TODO make use of guava cache?

	private final Map<DisplaySurface, Map<String, Map<String, Object>>> nativeProtocolsValuesCache = new HashMap<DisplaySurface, Map<String, Map<String, Object>>>();
	private final Map<DisplaySurface, Set<String>> nativeProtocolValidityCache = new HashMap<DisplaySurface, Set<String>>();

	private final XConnection xConnection;
	private final EventBus xEventBus;
	private final XWindowCache windowCache;
	private final XAtomCache atomCache;

	@Inject
	XPropertyCache(	final XConnection xConnection,
					@Named("XEventBus") final EventBus xEventBus,
					final XWindowCache windowCache,
					final XAtomCache atomCache) {
		this.xConnection = xConnection;
		this.xEventBus = xEventBus;
		this.windowCache = windowCache;
		this.atomCache = atomCache;

		this.xEventBus.register(this);
	}

	@Subscribe
	public void handlePropertyChanged(final xcb_property_notify_event_t event_t) {
		final int windowId = event_t.getWindow();
		final XWindow window = this.windowCache.getWindow(windowId);
		final int atomId = event_t.getAtom();
		final String atom = this.atomCache.getAtom(atomId);

		getAllInvalidNativeProtocols(window).add(atom);
	}

	private Set<String> getAllInvalidNativeProtocols(final DisplaySurface displaySurface) {
		Set<String> changedNativeProtocols = this.nativeProtocolValidityCache.get(displaySurface);
		if (changedNativeProtocols == null) {
			changedNativeProtocols = new HashSet<String>();
			this.nativeProtocolValidityCache.put(	displaySurface,
													changedNativeProtocols);
		}
		return changedNativeProtocols;
	}

	private Map<String, Object> queryNativeProperty(final DisplaySurface displaySurface,
													final String nativeProperty) {
		// TODO rewrite this method. Move lookup logic to separate classes.

		final int windowId = ((XWindowHandle) displaySurface.getDisplaySurfaceHandle()).getNativeHandle().intValue();

		final Map<String, Object> valueMap = new HashMap<String, Object>();

		if (nativeProperty.equals("WM_NAME")) {

			final xcb_get_property_cookie_t cookie_t = LibXcb.xcb_icccm_get_wm_name(this.xConnection
																							.getConnectionReference(),
																					windowId);

			final xcb_generic_error_t e = new xcb_generic_error_t();
			final xcb_icccm_get_text_property_reply_t prop = new xcb_icccm_get_text_property_reply_t();
			final int success = LibXcb.xcb_icccm_get_wm_name_reply(	this.xConnection.getConnectionReference(),
																	cookie_t,
																	prop,
																	e);
			if (success != 0) {
				final String name = prop.getName();
				valueMap.put(	"STRING",
								name);
			}
		}

		else if (nativeProperty.equals("_NET_WM_NAME")) {

		}

		else if (nativeProperty.equals("WM_CLASS")) {

		}

		else if (nativeProperty.equals("WM_PROTOCOLS")) {
			final int wmProtocolsAtomId = this.atomCache.getAtom(nativeProperty);

			final xcb_get_property_cookie_t cookie = LibXcb
					.xcb_icccm_get_wm_protocols(this.xConnection.getConnectionReference(),
												windowId,
												wmProtocolsAtomId);

			final xcb_icccm_get_wm_protocols_reply_t protocols = new xcb_icccm_get_wm_protocols_reply_t();
			final xcb_generic_error_t e = new xcb_generic_error_t();

			final int success = LibXcb.xcb_icccm_get_wm_protocols_reply(this.xConnection.getConnectionReference(),
																		cookie,
																		protocols,
																		e);

			if (success != 0) {
				final ByteBuffer atomsBuf = protocols.getAtoms();
				int nroAtoms = protocols.getAtoms_len();
				final List<String> protocolNames = new ArrayList<String>(nroAtoms);
				while (nroAtoms > 0) {
					nroAtoms--;
					final int atomId = atomsBuf.getInt();
					final String atom = this.atomCache.getAtom(atomId);
					protocolNames.add(atom);
				}
				valueMap.put(	"ATOM",
								protocolNames);
			}
		}

		return valueMap;
	}

	public Map<String, Object> getXProperty(final DisplaySurface displaySurface,
											final String xProperty) {

		final Map<String, Map<String, Object>> allProperties = getAllProperties(displaySurface);
		Map<String, Object> propertyValues = allProperties.get(xProperty);

		if (propertyValues == null) {
			propertyValues = new HashMap<String, Object>();
			allProperties.put(	xProperty,
								propertyValues);
			getAllInvalidNativeProtocols(displaySurface).add(xProperty);
			return getXProperty(displaySurface,
								xProperty);
		}

		final Set<String> changedNativeProtocols = this.nativeProtocolValidityCache.get(displaySurface);

		if (changedNativeProtocols.remove(xProperty)) {
			final Map<String, Object> nativePropertyVaue = queryNativeProperty(	displaySurface,
																				xProperty);
			propertyValues.putAll(nativePropertyVaue);
		}

		return propertyValues;
	}

	private Map<String, Map<String, Object>> getAllProperties(final DisplaySurface displaySurface) {
		Map<String, Map<String, Object>> allValues = this.nativeProtocolsValuesCache.get(displaySurface);
		if (allValues == null) {
			allValues = new HashMap<String, Map<String, Object>>();
			this.nativeProtocolsValuesCache.put(displaySurface,
												allValues);
		}
		return allValues;
	}
}
