package org.trinity.shellplugin.wm.impl;

import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.event.ShellNodeHideEvent;
import org.trinity.shell.api.node.event.ShellNodeShowEvent;

import com.google.common.eventbus.Subscribe;

public class ShellNodeVisibilityMirror {

	private final ShellNode source;
	private final ShellNode reflection;

	public ShellNodeVisibilityMirror(final ShellNode source, final ShellNode reflection) {
		this.source = source;
		this.reflection = reflection;

		if (source.isVisible()) {
			reflection.doShow();
		}

		source.addShellNodeEventHandler(this);

	}

	@Subscribe
	public void handleSourceShow(final ShellNodeShowEvent showNotifyEvent) {
		this.reflection.doShow();
	}

	@Subscribe
	public void handleSourceHide(final ShellNodeHideEvent hideNotifyEvent) {
		this.reflection.doHide();
	}

	public ShellNode getSource() {
		return this.source;
	}

	public ShellNode getReflection() {
		return this.reflection;
	}
}
