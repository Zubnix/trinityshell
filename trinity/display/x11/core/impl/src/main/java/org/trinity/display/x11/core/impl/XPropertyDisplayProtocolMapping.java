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
		this.nativeProtocolMapping.put(	DisplayProtocol.NAMES,
										Arrays.asList(	"WM_NAME",
														"_NET_WM_NAME",
														"WM_CLASS"));
		this.nativeProtocolMapping.put(	DisplayProtocol.CLOSE_REQUEST,
										Arrays.asList("WM_PROTOCOLS"));

		this.nativeInverseProtocolMapping.put(	"WM_NAME",
												Arrays.asList(DisplayProtocol.NAMES));
		this.nativeInverseProtocolMapping.put(	"WM_PROTOCOLS",
												Arrays.asList(DisplayProtocol.CLOSE_REQUEST));
	}

	public List<String> toXProperties(final DisplayProtocol displayProtocol) {
		return this.nativeProtocolMapping.get(displayProtocol);
	}

	public List<DisplayProtocol> toDisplayProtocols(final String xProperty) {
		return this.nativeInverseProtocolMapping.get(xProperty);
	}
}
