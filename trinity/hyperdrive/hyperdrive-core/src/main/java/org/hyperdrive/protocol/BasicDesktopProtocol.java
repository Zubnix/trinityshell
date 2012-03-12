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

/**
 * The most simple implementation of a <code>DesktopProtocol</code>. It simply
 * accepts or obeys any offer or request that is made.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class BasicDesktopProtocol implements DesktopProtocol {

	@Override
	public void registerClient(final RenderArea client) {
		// do nothig
	}

	@Override
	public boolean offerInput(final RenderArea client) {
		client.setInputFocus();
		return true;
	}

	@Override
	public boolean requestDelete(final RenderArea client) {
		client.doDestroy();
		return true;
	}

	@Override
	public ProtocolEvent query(final RenderArea client,
			final ProtocolEventType eventType) {
		// do nothing
		return null;
	}

	@Override
	public void updateProtocolEvent(final RenderArea client,
			final ProtocolEvent protocolEvent) {
		// do nothing
	}

}
