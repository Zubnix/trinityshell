package org.trinity.display.x11.core.impl.property;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.trinity.display.x11.core.impl.XPropertyCache;
import org.trinity.display.x11.core.impl.XDisplayProtocolHandler;
import org.trinity.display.x11.core.impl.XWindow;
import org.trinity.foundation.display.api.DisplayProtocol;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class NamesProtocolHandler implements
		XDisplayProtocolHandler {

	private final XPropertyCache xPropertyCache;

	@Inject
	NamesProtocolHandler(final XPropertyCache xPropertyCache) {
		this.xPropertyCache = xPropertyCache;
	}

	@Override
	public Map<String, Object> handleDipslayProtocol(	final XWindow xWindow,
												final DisplayProtocol displayProtocol,
												final Map<String, Object> arguments) {
		final Map<String, Object> wmNameValue = this.xPropertyCache
				.getXProperty(xWindow, "WM_NAME");
		final String name = (String) wmNameValue.get("STRING");

		final Map<String, Object> result = Collections
				.unmodifiableMap(new HashMap<String, Object>());
		result.put("name", name);

		return result;
	}

	@Override
	public DisplayProtocol getDisplayProtocol() {
		return DisplayProtocol.NAMES;
	}

}
