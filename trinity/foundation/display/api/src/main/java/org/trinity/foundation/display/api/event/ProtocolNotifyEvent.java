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
package org.trinity.foundation.display.api.event;

/***************************************
 * Indicates a native underlying protocol hint. The implementation and meaning
 * is implementation dependent.
 * <p>
 * For X this would be the name of a changed atom of a
 * {@link DisplayEventSource}.
 * 
 *************************************** 
 */
public class ProtocolNotifyEvent extends DisplayEvent {

	private final String protocol;

	/***************************************
	 * Construct a new <code>ProtocolNotifyEvent</code> that originated from the
	 * given {@link DisplayEventSource} who's native protocol has changed.
	 * 
	 * @param displayEventSource
	 *            a {@link DisplayEventSource}
	 * @param protocol
	 *            a native protocol id.
	 *************************************** 
	 */
	public ProtocolNotifyEvent(final DisplayEventSource displayEventSource, final String protocol) {
		super(displayEventSource);
		this.protocol = protocol;
	}

	/***************************************
	 * @return The changed native protocol id.
	 *************************************** 
	 */
	public String getDisplayProtocol() {
		return this.protocol;
	}
}