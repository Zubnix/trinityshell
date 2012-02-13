package org.hypercube.protocol;

public class ClientWindowDescriptionNotify implements ProtocolEventArguments {

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

}
