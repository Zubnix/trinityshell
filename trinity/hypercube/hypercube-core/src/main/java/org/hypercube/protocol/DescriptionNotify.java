package org.hypercube.protocol;

public class DescriptionNotify implements ProtocolEventArguments {

	private final String name;
	private final String description;
	private final String className;
	private final String clientInstanceName;

	public DescriptionNotify(final String name, final String description,
			final String className, final String clientInstanceName) {
		this.className = className;
		this.clientInstanceName = clientInstanceName;
		this.description = description;
		this.name = name;
	}

	public String getClassName() {
		return this.className;
	}

	public String getClientInstanceName() {
		return this.clientInstanceName;
	}

	public String getDescription() {
		return this.description;
	}

	public String getName() {
		return this.name;
	}

}
