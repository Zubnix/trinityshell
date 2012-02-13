package org.hypercube.protocol;

import org.hydrogen.eventsystem.Event;

public class ProtocolEvent<T extends ProtocolEventArguments> implements
		Event<ProtocolEventType<T>> {

	public static final ProtocolEventType<GeometryPreferences> GEO_PREF = new ProtocolEventType<GeometryPreferences>();
	public static final ProtocolEventType<IconPreferences> ICON_PREF = new ProtocolEventType<IconPreferences>();
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