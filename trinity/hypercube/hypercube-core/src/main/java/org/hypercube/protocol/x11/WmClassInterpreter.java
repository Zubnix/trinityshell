package org.hypercube.protocol.x11;

import org.hydrogen.displayinterface.PropertyInstanceTexts;
import org.hyperdrive.core.ClientWindow;

final class WmClassInterpreter {

	private final XDesktopProtocol xDesktopProtocol;

	public WmClassInterpreter(final XDesktopProtocol xDesktopProtocol) {
		this.xDesktopProtocol = xDesktopProtocol;
	}

	void handleWmClass(final ClientWindow client,
			final PropertyInstanceTexts propertyInstance) {
		final String classInstanceName = propertyInstance.getTexts()[0];
		final String className = propertyInstance.getTexts()[1];
		// TODO Update client description

	}
}
