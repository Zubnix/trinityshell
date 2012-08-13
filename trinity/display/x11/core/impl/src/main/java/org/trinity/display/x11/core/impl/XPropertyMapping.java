package org.trinity.display.x11.core.impl;

import java.util.HashMap;
import java.util.Map;

import org.trinity.foundation.display.api.DisplayProtocol;

import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(Type.IMPLEMENTATION))
@Singleton
public class XPropertyMapping {
	private final Map<DisplayProtocol, String[]> nativeProtocolMapping = new HashMap<DisplayProtocol, String[]>();
	private final Map<String, DisplayProtocol[]> nativeInverseProtocolMapping = new HashMap<String, DisplayProtocol[]>();

	XPropertyMapping() {
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

	public String[] toXProperties(final DisplayProtocol displayProtocol) {
		return this.nativeProtocolMapping.get(displayProtocol);
	}

	public DisplayProtocol[] toDisplayProtocols(final String xProperty) {
		return this.nativeInverseProtocolMapping.get(xProperty);
	}
}
