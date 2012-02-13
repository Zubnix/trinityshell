package org.hypercube.protocol;

public class ClientSystemDescriptionNotify implements ProtocolEventArguments {
	private final String systemDescription;
	private final String processId;

	public ClientSystemDescriptionNotify(final String systemdescription,
			final String processId) {
		this.systemDescription = systemdescription;
		this.processId = processId;
	}

	public String getProcessId() {
		return this.processId;
	}

	public String getSystemDescription() {
		return this.systemDescription;
	}
}
