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

import org.hydrogen.eventsystem.Event;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ProtocolEvent<T extends ProtocolEventArguments> implements
		Event<ProtocolEventType<T>> {

	public static final ProtocolEventType<GeometryPreferences> GEO_PREFERENCES = new ProtocolEventType<GeometryPreferences>();
	public static final ProtocolEventType<IconPreferences> ICON_PREFERENCES = new ProtocolEventType<IconPreferences>();
	public static final ProtocolEventType<ClientWindowDescriptionNotify> DESCRIPTION_NOTIFY = new ProtocolEventType<ClientWindowDescriptionNotify>();
	public static final ProtocolEventType<PopupNotify> POPUP_NOTIFY = new ProtocolEventType<PopupNotify>();
	public static final ProtocolEventType<UrgentNotify> URGENT_NOTIFY = new ProtocolEventType<UrgentNotify>();

	private final ProtocolEventType<T> type;
	private final T eventArguments;

	public ProtocolEvent(final ProtocolEventType<T> type, final T eventArguments) {
		this.type = type;
		this.eventArguments = eventArguments;
	}

	@Override
	public ProtocolEventType<T> getType() {
		return this.type;
	}

	public T getEventArguments() {
		return this.eventArguments;
	}
}