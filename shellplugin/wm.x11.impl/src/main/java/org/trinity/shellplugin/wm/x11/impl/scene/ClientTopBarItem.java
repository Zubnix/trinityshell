package org.trinity.shellplugin.wm.x11.impl.scene;

import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shellplugin.wm.api.HasText;
import org.trinity.shellplugin.wm.api.ReceivesPointerInput;

public class ClientTopBarItem implements HasText, ReceivesPointerInput {

	private final ShellSurface client;

	ClientTopBarItem(final ShellSurface client) {
		this.client = client;
	}

	@Override
	public String getText() {
		// TODO implement icccm+ewmh shell protocol

		return "client=" + this.client;
	}

	@Override
	public void onPointerInput(final PointerInput pointerInput) {
		// do something with client
	}
}