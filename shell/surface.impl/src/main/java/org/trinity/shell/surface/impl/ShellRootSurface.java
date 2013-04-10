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
package org.trinity.shell.surface.impl;

import javax.annotation.concurrent.ThreadSafe;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.shell.api.scene.AbstractShellNodeParent;
import org.trinity.shell.api.scene.ShellNodeGeometryDelegate;
import org.trinity.shell.api.surface.AbstractShellSurfaceParent;

import com.google.common.util.concurrent.ListeningExecutorService;

@ThreadSafe
public class ShellRootSurface extends AbstractShellSurfaceParent {

	private final ShellNodeGeometryDelegate shellNodeGeometryDelegate = new ShellSurfaceGeometryDelegateImpl(this);

	private final DisplaySurface displaySurface;

	ShellRootSurface(	final ListeningExecutorService shellExecutor,
						final DisplaySurface rootDisplaySurface) {
		super(shellExecutor);
		this.displaySurface = rootDisplaySurface;
		syncGeoToDisplaySurface();
	}

	@Override
	public Boolean isVisibleImpl() {
		return true;
	}

	@Override
	public AbstractShellNodeParent getParentImpl() {
		return this;
	}

	@Override
	public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
		return this.shellNodeGeometryDelegate;
	}

	@Override
	public DisplaySurface getDisplaySurfaceImpl() {
		return this.displaySurface;
	}
}