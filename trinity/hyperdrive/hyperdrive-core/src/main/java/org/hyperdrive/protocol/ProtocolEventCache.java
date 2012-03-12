/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.protocol;

import java.util.HashMap;
import java.util.Map;

import org.hyperdrive.api.core.RenderArea;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ProtocolEventCache {
	private final Map<RenderArea, Map<ProtocolEventType, ProtocolEvent>> clientEventCache;

	public ProtocolEventCache() {
		this.clientEventCache = new HashMap<RenderArea, Map<ProtocolEventType, ProtocolEvent>>();
	}

	public ProtocolEvent query(final RenderArea client,
			final ProtocolEventType eventType) {
		if (!this.clientEventCache.containsKey(client)) {
			return null;
		}
		return this.clientEventCache.get(client).get(eventType);
	}

	public void updateCache(final RenderArea client, final ProtocolEvent event) {

		final Map<ProtocolEventType, ProtocolEvent> protocolEvents;
		if (this.clientEventCache.containsKey(client)) {
			protocolEvents = this.clientEventCache.get(client);
		} else {
			protocolEvents = new HashMap<ProtocolEventType, ProtocolEvent>();
		}
		updateProtocolEvent(protocolEvents, event, event.getType());
	}

	protected void updateProtocolEvent(
			final Map<ProtocolEventType, ProtocolEvent> protocolEvents,
			final ProtocolEvent event, final ProtocolEventType eventType) {

		protocolEvents.put(eventType, event);
	}
}
