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

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ClientWindowDescriptionNotify implements ProtocolEvent {

	public static final ProtocolEventType TYPE = new ProtocolEventType();

	private final String name;
	private final String description;

	public ClientWindowDescriptionNotify(final String name,
			final String description) {
		this.description = description;
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public ProtocolEventType getType() {
		return ClientWindowDescriptionNotify.TYPE;
	}

}
