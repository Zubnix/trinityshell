package org.hypercube.protocol;

public class ClientSoftwareDescriptionNotify implements ProtocolEventArguments {
	private final String className;
	private final String clientInstanceName;

	public ClientSoftwareDescriptionNotify(final String className,
			final String clientInstanceName) {
		this.className = className;
		this.clientInstanceName = clientInstanceName;
	}

	public String getClassName() {
		return this.className;
	}

	public String getClientInstanceName() {
		return this.clientInstanceName;
	}
}
