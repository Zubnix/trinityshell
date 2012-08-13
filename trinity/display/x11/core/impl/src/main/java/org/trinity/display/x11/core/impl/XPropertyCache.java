package org.trinity.display.x11.core.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.trinity.foundation.display.api.DisplayRenderArea;

import xcbjb.LibXcb;
import xcbjb.xcb_generic_error_t;
import xcbjb.xcb_get_property_cookie_t;
import xcbjb.xcb_icccm_get_text_property_reply_t;
import xcbjb.xcb_property_notify_event_t;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(Type.IMPLEMENTATION))
@Singleton
public class XPropertyCache {
	private final Map<DisplayRenderArea, Map<String, Map<String, Object>>> nativeProtocolsValuesCache = new HashMap<DisplayRenderArea, Map<String, Map<String, Object>>>();
	private final Map<DisplayRenderArea, Set<String>> nativeProtocolValidityCache = new HashMap<DisplayRenderArea, Set<String>>();

	private final XConnection xConnection;
	private final EventBus xEventBus;
	private final XWindowCache windowCache;
	private final XAtomCache atomCache;

	XPropertyCache(	final XConnection xConnection,
					@Named("xEventBus") final EventBus xEventBus,
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

	private Map<String, Object> queryNativeProperty(final DisplayRenderArea displayRenderArea,
													final String nativeProperty) {
		// TODO rewrite this method. Move lookup logic to separate classes.

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
			// ...
		}
		return valueMap;
	}

	public Map<String, Object> getXProperty(final DisplayRenderArea displayRenderArea,
											final String xProperty) {

		final Map<String, Map<String, Object>> allProperties = getAllProperties(displayRenderArea);
		Map<String, Object> propertyValues = allProperties.get(xProperty);

		if (propertyValues == null) {
			propertyValues = new HashMap<String, Object>();
			allProperties.put(xProperty, propertyValues);
			getAllInvalidNativeProtocols(displayRenderArea).add(xProperty);
			return getXProperty(displayRenderArea, xProperty);
		}

		final Set<String> changedNativeProtocols = this.nativeProtocolValidityCache
				.get(displayRenderArea);

		if (changedNativeProtocols.remove(xProperty)) {
			final Map<String, Object> nativePropertyVaue = queryNativeProperty(	displayRenderArea,
																				xProperty);
			propertyValues.putAll(nativePropertyVaue);
		}

		return propertyValues;
	}

	private Map<String, Map<String, Object>> getAllProperties(final DisplayRenderArea displayRenderArea) {
		Map<String, Map<String, Object>> allValues = this.nativeProtocolsValuesCache
				.get(displayRenderArea);
		if (allValues == null) {
			allValues = new HashMap<String, Map<String, Object>>();
			this.nativeProtocolsValuesCache.put(displayRenderArea, allValues);
		}
		return allValues;
	}
}
