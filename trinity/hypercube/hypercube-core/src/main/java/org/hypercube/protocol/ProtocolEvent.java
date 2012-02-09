package org.hypercube.protocol;

import org.hydrogen.eventsystem.Event;

public class ProtocolEvent<T extends ProtocolEventArguments> implements
		Event<ProtocolEventType<T>> {

	public static final ProtocolEventType<InputPreferences> INPUT_PREF = new ProtocolEventType<InputPreferences>();
	public static final ProtocolEventType<SizePreferences> SIZE_PREF = new ProtocolEventType<SizePreferences>();

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