/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.shell.api.surface;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.scene.AbstractShellNode;
import org.trinity.shell.api.scene.AbstractShellNodeParent;
import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellNodeGeometryDelegate;
import org.trinity.shell.api.surface.AbstractShellSurface;

/***************************************
 * A {@link ShellNodeGeometryDelegate} for use with an
 * {@link org.trinity.shell.api.surface.AbstractShellSurface}.
 *
 ***************************************
 */
@NotThreadSafe
@ExecutionContext(ShellExecutor.class)
public class ShellSurfaceGeometryDelegate implements ShellNodeGeometryDelegate {

	private final AbstractShellSurface abstractShellSurface;

	public ShellSurfaceGeometryDelegate(AbstractShellSurface abstractShellSurface) {
		this.abstractShellSurface = abstractShellSurface;
	}

	@Override
	public AbstractShellSurface getShellNode(){
		return abstractShellSurface;
	}

	public DisplaySurface getShellNodeManipulator(){
		return getShellNode().getDisplaySurface();
	}

	@Override
	public void resize(@Nonnull final Size size) {
		getShellNodeManipulator().resize(	size.getWidth(),
											size.getHeight());
	}

	@Override
	public void destroy() {
		getShellNodeManipulator().destroy();
	}

	@Override
	public void move(@Nonnull final Coordinate position) {

		final Coordinate absolutePosition = getAbsolutePosition(getShellNode());

		getShellNodeManipulator().move(	absolutePosition.getX(),
										absolutePosition.getY());
	}

	@Override
	public void moveResize(	@Nonnull final Coordinate position,
							@Nonnull final Size size) {

		final Coordinate absolutePosition = getAbsolutePosition(getShellNode());

		final DisplaySurface areaManipulator = getShellNodeManipulator();
		areaManipulator.moveResize(	absolutePosition.getX(),
									absolutePosition.getY(),
									size.getWidth(),
									size.getHeight());
	}

	private Coordinate getAbsolutePosition(@Nonnull final AbstractShellNode node) {
		final Coordinate childPosition = node.getPositionImpl();
		final AbstractShellNodeParent shellParent = node.getParentImpl();

		if ((shellParent == null) || shellParent.equals(node)) {
			return childPosition;
		}

		final Coordinate absoluteParentPosition = getAbsolutePosition(shellParent);
		final Coordinate absolutePosition = childPosition.add(absoluteParentPosition);

		return absolutePosition;
	}
}
