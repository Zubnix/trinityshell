package org.trinity.foundation.display.x11.impl;

import javax.inject.Inject;

//TODO implement as shell plugin
public class ShellPluginX11 {

	private final XEventChannel   xEventChannel;
	private final XEventHandlers  xEventHandlers;
	private final XClientExplorer xClientExplorer;

	@Inject
	ShellPluginX11(final XEventChannel xEventChannel,
				   final XEventHandlers xEventHandlers,
				   final XClientExplorer xClientExplorer) {
		this.xEventChannel = xEventChannel;
		this.xEventHandlers = xEventHandlers;
		this.xClientExplorer = xClientExplorer;
	}

	public void start() {
		// FIXME from config?
		final String displayName = System.getenv("DISPLAY");
		final int targetScreen = 0;

		this.xEventChannel.register(this.xEventHandlers);
		this.xEventChannel.open(displayName,
								targetScreen);
		this.xClientExplorer.findClientDisplaySurfaces();
	}
}
