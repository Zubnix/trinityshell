package org.trinity.shellplugin.wm.impl;

import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.event.ShellNodeDestroyEvent;

import com.google.common.eventbus.Subscribe;

public class ShellNodeDestroyMirror {
	private final ShellNode source;
	private final ShellNode reflection;

	public ShellNodeDestroyMirror(final ShellNode source, final ShellNode reflection) {
		this.source = source;
		this.reflection = reflection;

		if (source.isDestroyed()) {
			reflection.doDestroy();
		}

		source.addShellNodeEventHandler(this);
	}

	@Subscribe
	public void handleSourceDestroy(final ShellNodeDestroyEvent showNotifyEvent) {
		this.reflection.doDestroy();
	}

	public ShellNode getSource() {
		return this.source;
	}

	public ShellNode getReflection() {
		return this.reflection;
	}
}
