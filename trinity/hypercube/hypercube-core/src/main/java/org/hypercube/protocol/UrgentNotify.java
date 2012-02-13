package org.hypercube.protocol;

public class UrgentNotify implements ProtocolEventArguments {
	private final boolean urgent;

	public UrgentNotify(final boolean urgent) {
		this.urgent = urgent;
	}

	public boolean isUrgent() {
		return this.urgent;
	}
}
