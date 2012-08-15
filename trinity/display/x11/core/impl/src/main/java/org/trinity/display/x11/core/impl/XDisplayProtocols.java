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
import java.util.Map;
import java.util.Set;

import org.trinity.foundation.display.api.DisplayProtocol;
import org.trinity.foundation.display.api.DisplayProtocols;
import org.trinity.foundation.display.api.DisplayRenderArea;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class XDisplayProtocols implements DisplayProtocols {

	private final Map<DisplayProtocol, XDisplayProtocolHandler> xDisplayProtocolHandlers = new HashMap<DisplayProtocol, XDisplayProtocolHandler>();

	@Inject
	XDisplayProtocols(final Set<XDisplayProtocolHandler> xPropertyConversions) {
		for (final XDisplayProtocolHandler xPropertyConversion : xPropertyConversions) {
			this.xDisplayProtocolHandlers.put(xPropertyConversion
					.getDisplayProtocol(), xPropertyConversion);
		}
	}

	@Override
	public Map<String, Object> queryProtocol(	final DisplayRenderArea displayRenderArea,
												final DisplayProtocol displayProtocol) {
		return queryProtocol(displayRenderArea, displayProtocol, null);
	}

	@Override
	public Map<String, Object> queryProtocol(	final DisplayRenderArea displayRenderArea,
												final DisplayProtocol displayProtocol,
												final Map<String, Object> arguments) {
		final Map<String, Object> result = this.xDisplayProtocolHandlers
				.get(displayProtocol)
				.handleDipslayProtocol((XWindow) displayRenderArea,
								displayProtocol,
								arguments);
		return result;
	}
}