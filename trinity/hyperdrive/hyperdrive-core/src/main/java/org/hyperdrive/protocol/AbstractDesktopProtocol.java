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

import org.hyperdrive.api.core.RenderArea;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public abstract class AbstractDesktopProtocol implements DesktopProtocol {

	private final ProtocolEventCache protocolEventCache;

	public AbstractDesktopProtocol() {
		this.protocolEventCache = new ProtocolEventCache();
	}

	@Override
	public ProtocolEvent query(final RenderArea client,
			final ProtocolEventType eventType) {
		return this.protocolEventCache.query(client, eventType);
	}

	@Override
	public void updateProtocolEvent(final RenderArea client,
			final ProtocolEvent protocolEvent) {
		this.protocolEventCache.updateCache(client, protocolEvent);
		// TODO client should listen to the protocol, protocol should fire the
		// event.
		client.fireEvent(protocolEvent);
	}
}
