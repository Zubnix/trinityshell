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
package org.trinity.display.x11.core.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.trinity.foundation.display.api.DisplayProtocol;
import org.trinity.foundation.display.api.DisplayProtocols;
import org.trinity.foundation.display.api.DisplayRenderArea;

import xcbjb.LibXcb;
import xcbjb.xcb_generic_error_t;
import xcbjb.xcb_get_property_cookie_t;
import xcbjb.xcb_icccm_get_text_property_reply_t;
import xcbjb.xcb_property_notify_event_t;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class XDisplayProtocol implements DisplayProtocols {

	// FIXME + TODO seperate into xpropertycache & displayprotocol mapping
	// classes.

	private final Map<DisplayRenderArea, Map<String, Map<String, Object>>> nativeProtocolsValuesCache = new HashMap<DisplayRenderArea, Map<String, Map<String, Object>>>();
	private final Map<DisplayRenderArea, Set<String>> nativeProtocolValidityCache = new HashMap<DisplayRenderArea, Set<String>>();

	private final Map<DisplayProtocol, String[]> nativeProtocolMapping = new HashMap<DisplayProtocol, String[]>();
	private final Map<String, DisplayProtocol[]> nativeInverseProtocolMapping = new HashMap<String, DisplayProtocol[]>();

	private final XConnection xConnection;
	private final EventBus xEventBus;
	private final XWindowCache windowCache;
	private final XAtomCache atomCache;

	@Inject
	XDisplayProtocol(	final XConnection xConnection,
						@Named("xEventBus") final EventBus xEventBus,
						final XWindowCache windowCache,
						final XAtomCache atomCache) {
		this.xConnection = xConnection;
		this.xEventBus = xEventBus;
		this.windowCache = windowCache;
		this.atomCache = atomCache;

		this.xEventBus.register(this);
		fillNativeProtocolMapping();
	}

	private void fillNativeProtocolMapping() {
		this.nativeProtocolMapping.put(	DisplayProtocol.FRIENDLY_NAME,
										new String[] { "WM_NAME" });
		this.nativeProtocolMapping.put(	DisplayProtocol.REQUEST_CLOSE,
										new String[] { "WM_PROTOCOLS" });

		this.nativeInverseProtocolMapping
				.put(	"WM_NAME",
						new DisplayProtocol[] { DisplayProtocol.FRIENDLY_NAME });
		this.nativeInverseProtocolMapping
				.put(	"WM_PROTOCOLS",
						new DisplayProtocol[] { DisplayProtocol.REQUEST_CLOSE });

	}

	@Subscribe
	public void handleNativeProtocolChanged(final xcb_property_notify_event_t event_t) {
		final int windowId = event_t.getWindow();
		final XWindow window = this.windowCache.getWindow(windowId);
		final int atomId = event_t.getAtom();
		final String atom = this.atomCache.getAtom(atomId);

		getAllInvalidNativeProtocols(window).add(atom);
	}

	private Set<String> getAllInvalidNativeProtocols(final DisplayRenderArea displayRenderArea) {
		Set<String> changedNativeProtocols = this.nativeProtocolValidityCache
				.get(displayRenderArea);
		if (changedNativeProtocols == null) {
			changedNativeProtocols = new HashSet<String>();
			this.nativeProtocolValidityCache.put(	displayRenderArea,
													changedNativeProtocols);
		}
		return changedNativeProtocols;
	}

	@Override
	public Map<String, Object> queryProtocol(	final DisplayRenderArea displayRenderArea,
												final DisplayProtocol displayProtocol) {
		return queryProtocol(displayRenderArea, displayProtocol, null);
	}

	Map<String, Map<String, Object>> getNativeProtocolsValues(	final DisplayRenderArea displayRenderArea,
																final DisplayProtocol displayProtocol) {
		final String[] nativeProtocols = this.nativeProtocolMapping
				.get(displayProtocol);

		final Map<String, Map<String, Object>> nativeProtocolsValues = new HashMap<String, Map<String, Object>>(nativeProtocols.length);

		for (final String nativeProtocol : nativeProtocols) {
			nativeProtocolsValues
					.put(	nativeProtocol,
							getNativeProcolValue(	displayRenderArea,
													nativeProtocol));
		}
		return nativeProtocolsValues;
	}

	Map<String, Object> getNativeProcolValue(	final DisplayRenderArea displayRenderArea,
												final String nativeProtocol) {

		final Map<String, Map<String, Object>> allValues = getAllValues(displayRenderArea);
		Map<String, Object> values = allValues.get(nativeProtocol);

		if (values == null) {
			values = new HashMap<String, Object>();
			allValues.put(nativeProtocol, values);
			getAllInvalidNativeProtocols(displayRenderArea).add(nativeProtocol);
			return getNativeProcolValue(displayRenderArea, nativeProtocol);
		}

		final Set<String> changedNativeProtocols = this.nativeProtocolValidityCache
				.get(displayRenderArea);

		if (changedNativeProtocols.remove(nativeProtocol)) {
			final Map<String, Object> nativePropertyVaue = queryNativeProperty(	displayRenderArea,
																				nativeProtocol);
			values.putAll(nativePropertyVaue);
		}

		return values;
	}

	private Map<String, Map<String, Object>> getAllValues(final DisplayRenderArea displayRenderArea) {
		Map<String, Map<String, Object>> allValues = this.nativeProtocolsValuesCache
				.get(displayRenderArea);
		if (allValues == null) {
			allValues = new HashMap<String, Map<String, Object>>();
			this.nativeProtocolsValuesCache.put(displayRenderArea, allValues);
		}
		return allValues;
	}

	@Override
	public Map<String, Object> queryProtocol(	final DisplayRenderArea displayRenderArea,
												final DisplayProtocol displayProtocol,
												final Map<String, Object> arguments) {

		return null;
	}

	public Map<String, Object> queryNativeProperty(	final DisplayRenderArea displayRenderArea,
													final String nativeProperty) {
		// TODO rewrite this method. Move logic to separate classes.

		final int windowId = ((XResourceHandle) displayRenderArea
				.getResourceHandle()).getNativeHandle();

		final Map<String, Object> valueMap = new HashMap<String, Object>();

		if (nativeProperty.equals("WM_NAME")) {

			final xcb_get_property_cookie_t cookie_t = LibXcb
					.xcb_icccm_get_wm_name(this.xConnection
							.getConnectionReference(), windowId);

			final xcb_generic_error_t e = new xcb_generic_error_t();
			final xcb_icccm_get_text_property_reply_t prop = new xcb_icccm_get_text_property_reply_t();
			LibXcb.xcb_icccm_get_wm_name_reply(this.xConnection
					.getConnectionReference(), cookie_t, prop, e);

			final String name = prop.getName();
			valueMap.put("name", name);

		} else if (nativeProperty.equals("WM_PROTOCOLS")) {

		}
		return valueMap;
	}

}
