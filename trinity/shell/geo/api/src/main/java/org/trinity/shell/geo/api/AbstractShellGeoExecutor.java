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
package org.trinity.shell.geo.api;

import org.trinity.foundation.display.api.DisplayArea;
import org.trinity.foundation.display.api.DisplayAreaManipulator;

// TODO move to api?
public abstract class AbstractShellGeoExecutor implements ShellGeoExecutor {

	protected AbstractShellGeoExecutor() {
	}

	/**
	 * @return
	 */
	public abstract <T extends DisplayArea> DisplayAreaManipulator<T> getAreaManipulator(ShellGeoNode shellGeoNode);

	@Override
	public void lower(final ShellGeoNode shellGeoNode) {
		this.getAreaManipulator(shellGeoNode).lower();
	}

	@Override
	public void raise(final ShellGeoNode shellGeoNode) {
		this.getAreaManipulator(shellGeoNode).raise();
	}

	@Override
	public void resize(	final ShellGeoNode shellGeoNode,
						final int width,
						final int height) {
		this.getAreaManipulator(shellGeoNode).resize(width, height);
	}

	@Override
	public void show(final ShellGeoNode shellGeoNode) {
		this.getAreaManipulator(shellGeoNode).show();
	}

	@Override
	public void hide(final ShellGeoNode shellGeoNode) {
		this.getAreaManipulator(shellGeoNode).hide();
	}

	/**
	 * @param newParentArea
	 * @param newX
	 * @param newY
	 */
	protected void reparent(final ShellGeoNode shellGeoNode,
							final DisplayArea newParentArea,
							final int newX,
							final int newY) {
		getAreaManipulator(shellGeoNode).setParent(newParentArea, newX, newY);
	}
}
