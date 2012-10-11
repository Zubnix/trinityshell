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
package org.trinity.display.x11.core.impl.property;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.trinity.display.x11.core.impl.XDisplayProtocolHandler;
import org.trinity.display.x11.core.impl.XPropertyCache;
import org.trinity.display.x11.core.impl.XWindow;
import org.trinity.foundation.display.api.DisplayProtocol;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class NamesProtocolHandler implements XDisplayProtocolHandler {

	private final XPropertyCache xPropertyCache;

	@Inject
	NamesProtocolHandler(final XPropertyCache xPropertyCache) {
		this.xPropertyCache = xPropertyCache;
	}

	@Override
	public Map<String, Object> handleDipslayProtocol(	final XWindow xWindow,
														final DisplayProtocol displayProtocol,
														final Map<String, Object> arguments) {
		final Map<String, Object> wmNameValue = this.xPropertyCache.getXProperty(	xWindow,
																					"WM_NAME");
		final String name = (String) wmNameValue.get("STRING");

		final Map<String, Object> result = Collections.unmodifiableMap(new HashMap<String, Object>());
		result.put(	"name",
					name);

		return result;
	}

	@Override
	public DisplayProtocol getDisplayProtocol() {
		return DisplayProtocol.NAMES;
	}

}
