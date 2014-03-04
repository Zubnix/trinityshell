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
package org.trinity.shell.surface.impl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.shell.api.scene.AbstractShellNode;
import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellNodeGeometryDelegate;
import org.trinity.shell.api.scene.ShellNodeParent;

import com.google.common.base.Optional;
import org.trinity.shell.api.surface.ShellSurface;

/***************************************
 * A {@link ShellNodeGeometryDelegate} for use with an
 * {@link ShellSurfaceImpl}.
 *
 ***************************************
 */
@NotThreadSafe
public class ShellSurfaceGeometryDelegate implements ShellNodeGeometryDelegate {

	private final ShellSurface shellSurface;

	public ShellSurfaceGeometryDelegate(final ShellSurface shellSurface) {
		this.shellSurface = shellSurface;
	}

	@Override
	public ShellSurface getShellNode() {
		return this.shellSurface;
	}

	public DisplaySurface getShellNodeManipulator() {
		return getShellNode().getDisplaySurface();
	}

	@Override
	public void resize(@Nonnull final Size size) {
		getShellNodeManipulator().resize(size.getWidth(),
				size.getHeight());
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

	private Coordinate getAbsolutePosition(@Nonnull final ShellNode node) {
		final Coordinate nodePosition = node.getPosition();
		final Optional<ShellNodeParent> optionalParent = node.getParent();

		if ((!optionalParent.isPresent())) {
			return nodePosition;
		}

		final Coordinate absoluteParentPosition = getAbsolutePosition((AbstractShellNode) optionalParent.get());
		final Coordinate absolutePosition = nodePosition.add(absoluteParentPosition);

		return absolutePosition;
	}
}
