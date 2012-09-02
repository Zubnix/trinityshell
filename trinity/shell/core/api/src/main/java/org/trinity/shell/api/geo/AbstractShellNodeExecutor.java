/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.api.geo;

import org.trinity.foundation.display.api.DisplayArea;
import org.trinity.foundation.display.api.DisplayAreaManipulator;

public abstract class AbstractShellNodeExecutor implements ShellNodeExecutor {

	public abstract <T extends DisplayArea> DisplayAreaManipulator<T> getShellNodeManipulator(ShellNode shellNode);

	@Override
	public void lower(final ShellNode shellNode) {
		this.getShellNodeManipulator(shellNode).lower();
	}

	@Override
	public void raise(final ShellNode shellNode) {
		this.getShellNodeManipulator(shellNode).raise();
	}

	@Override
	public void resize(	final ShellNode shellNode,
						final int width,
						final int height) {
		this.getShellNodeManipulator(shellNode).resize(	width,
														height);
	}

	@Override
	public void show(final ShellNode shellNode) {
		this.getShellNodeManipulator(shellNode).show();
	}

	@Override
	public void hide(final ShellNode shellNode) {
		this.getShellNodeManipulator(shellNode).hide();
	}

	protected void reparent(final ShellNode shellNode,
							final DisplayArea newParentArea,
							final int newX,
							final int newY) {
		getShellNodeManipulator(shellNode).setParent(	newParentArea,
														newX,
														newY);
	}
}
