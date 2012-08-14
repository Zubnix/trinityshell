package org.trinity.display.x11.core.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.trinity.foundation.display.api.DisplayProtocol;

import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(Type.IMPLEMENTATION))
@Singleton
public class XPropertyDisplayProtocolMapping {
	private final Map<DisplayProtocol, List<String>> nativeProtocolMapping = new HashMap<DisplayProtocol, List<String>>();
	private final Map<String, List<DisplayProtocol>> nativeInverseProtocolMapping = new HashMap<String, List<DisplayProtocol>>();

	XPropertyDisplayProtocolMapping() {
		this.nativeProtocolMapping.put(	DisplayProtocol.FRIENDLY_NAME,
										Arrays.asList("WM_NAME"));
		this.nativeProtocolMapping.put(	DisplayProtocol.REQUEST_CLOSE,
										Arrays.asList("WM_PROTOCOLS"));

		this.nativeInverseProtocolMapping.put("WM_NAME", Arrays
				.asList(DisplayProtocol.FRIENDLY_NAME));
		this.nativeInverseProtocolMapping.put("WM_PROTOCOLS", Arrays
				.asList(DisplayProtocol.REQUEST_CLOSE));
	}

	public List<String> toXProperties(final DisplayProtocol displayProtocol) {
		return this.nativeProtocolMapping.get(displayProtocol);
	}

	public List<DisplayProtocol> toDisplayProtocols(final String xProperty) {
		return this.nativeInverseProtocolMapping.get(xProperty);
	}
}
