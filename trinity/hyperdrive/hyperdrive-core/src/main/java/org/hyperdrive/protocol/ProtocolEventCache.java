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

import org.hyperdrive.core.ClientWindow;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ProtocolEventCache {
	private final Map<ClientWindow, Map<ProtocolEventType<?>, ProtocolEvent<?>>> clientEventCache;

	public ProtocolEventCache() {
		this.clientEventCache = new HashMap<ClientWindow, Map<ProtocolEventType<?>, ProtocolEvent<?>>>();
	}

	@SuppressWarnings("unchecked")
	public <A extends ProtocolEventArguments, T extends ProtocolEventType<A>> ProtocolEvent<A> query(
			final ClientWindow client, final T eventType) {
		if (!this.clientEventCache.containsKey(client)) {
			return null;
		}
		return (ProtocolEvent<A>) this.clientEventCache.get(client).get(
				eventType);
	}

	public <A extends ProtocolEventArguments, T extends ProtocolEventType<A>> void updateCache(
			final ClientWindow client, final ProtocolEvent<A> event) {

		final Map<ProtocolEventType<?>, ProtocolEvent<?>> protocolEvents;
		if (this.clientEventCache.containsKey(client)) {
			protocolEvents = this.clientEventCache.get(client);
		} else {
			protocolEvents = new HashMap<ProtocolEventType<?>, ProtocolEvent<?>>();
		}
		updateProtocolEvent(protocolEvents, event, event.getType());
	}

	protected <A extends ProtocolEventArguments, T extends ProtocolEventType<A>> void updateProtocolEvent(
			final Map<ProtocolEventType<?>, ProtocolEvent<?>> protocolEvents,
			final ProtocolEvent<A> event, final T eventType) {

		protocolEvents.put(eventType, event);
	}
}
