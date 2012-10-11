/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
