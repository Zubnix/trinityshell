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
package org.trinity.shell.impl.surface;

import org.trinity.foundation.display.api.DisplayArea;
import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.ShellNodeExecutor;
import org.trinity.shell.api.node.ShellNodeTransformation;
import org.trinity.shell.api.surface.AbstractShellSurfaceExecutor;
import org.trinity.shell.api.surface.ShellSurface;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

// TODO documentation
/**
 * A <code>RenderAreaGeoExecutor</code> is a delegate class for directly
 * manipulating a {@link ShellSurface}'s geometry.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * @see ShellNodeExecutor
 */
@Bind(value = @Named("shellSurfaceGeoExecutor"), to = @To(value = Type.CUSTOM, customs = ShellNodeExecutor.class))
@Singleton
public class ShellSurfaceExecutor extends AbstractShellSurfaceExecutor {

	@Inject
	ShellSurfaceExecutor(@Named("ShellRootSurface") final ShellSurface root) {
		super(root);
	}

	@Override
	protected void initializeShellSurface(	final ShellNode parent,
											final ShellNode area) {
		// No need to initialize
	}

	/**
	 * @param shellSurface
	 * @return
	 */
	@Override
	protected DisplayArea getAreaPeer(final ShellSurface shellSurface) {
		return shellSurface.getDisplaySurface();
	}

	/**
	 * Find the the closest parent in the area tree hierarchy that matches the
	 * type of {@link AbstractShellSurfaceExecutor#getManipulatedArea()},
	 * starting from the given square.
	 * 
	 * @param square
	 *            The {@link ShellNode} to start searching from upwards in the
	 *            tree hierarchy.
	 * @return The closest parent with type {@link ShellSurface}.
	 */
	@Override
	protected ShellSurface findClosestSameTypeSurface(final ShellNode square) {

		// find the closest ancestor that is of type ShellSurface
		if (square instanceof ShellSurface) {

			return (ShellSurface) square;

		} else {
			final ShellNodeTransformation transformation = square.toGeoTransformation();
			final ShellNode currentParent = transformation.getParent0();
			final ShellNode newParent = transformation.getParent1();
			if (currentParent == null) {
				if (newParent == null) {
					return null;
				} else {
					return findClosestSameTypeSurface(newParent);
				}
			} else {
				return findClosestSameTypeSurface(currentParent);
			}
		}
	}
}
