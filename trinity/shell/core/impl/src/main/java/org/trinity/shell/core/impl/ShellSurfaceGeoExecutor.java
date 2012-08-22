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
package org.trinity.shell.core.impl;

import org.trinity.foundation.display.api.DisplayArea;
import org.trinity.foundation.display.api.DisplayAreaManipulator;
import org.trinity.foundation.shared.geometry.api.Coordinate;
import org.trinity.shell.core.api.ShellSurface;
import org.trinity.shell.geo.api.AbstractShellNodeExecutor;
import org.trinity.shell.geo.api.ShellNode;
import org.trinity.shell.geo.api.ShellNodeExecutor;
import org.trinity.shell.geo.api.ShellNodeTransformation;

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
public class ShellSurfaceGeoExecutor extends AbstractShellNodeExecutor {

	private final ShellSurface root;

	@Inject
	protected ShellSurfaceGeoExecutor(@Named("shellRootRenderArea") final ShellSurface root) {
		this.root = root;
	}

	@Override
	public DisplayAreaManipulator<DisplayArea> getAreaManipulator(final ShellNode shellNode) {
		return this.getAreaManipulator(shellNode);
	}

	@SuppressWarnings("unchecked")
	protected <T extends DisplayArea> DisplayAreaManipulator<T> getAreaManipulator(final ShellSurface shellSurface) {
		return (DisplayAreaManipulator<T>) shellSurface.getDisplayRenderArea();
	}

	protected boolean isAreaInitialized(final ShellSurface shellSurface) {
		if (shellSurface == null) {
			return false;
		}
		final boolean initialized = shellSurface.getDisplayRenderArea() != null;
		return initialized;
	}

	@Override
	public void move(	final ShellNode shellNode,
						final int relativeX,
						final int relativeY) {
		if (isAreaInitialized((ShellSurface) shellNode)) {
			final int newX = relativeX;
			final int newY = relativeY;
			final ShellNode currentParent = shellNode.getParent();
			final Coordinate newRelativePosition = calculatePositionRelativeToTypedArea(currentParent,
																						newX,
																						newY);

			final int newRelativeX = newRelativePosition.getX();
			final int newRelativeY = newRelativePosition.getY();
			this.getAreaManipulator(shellNode).move(newRelativeX, newRelativeY);
		}
	}

	@Override
	public void resize(	final ShellNode shellNode,
						final int width,
						final int height) {
		if (isAreaInitialized((ShellSurface) shellNode)) {
			super.resize(shellNode, width, height);
		}
	}

	@Override
	public void moveResize(	final ShellNode shellNode,
							final int relativeX,
							final int relativeY,
							final int width,
							final int height) {
		final boolean areaInitialized = isAreaInitialized((ShellSurface) shellNode);
		if (areaInitialized) {
			final int newX = relativeX;
			final int newY = relativeY;
			final int newWidth = width;
			final int newHeight = height;
			final ShellNode currentParent = shellNode.getParent();

			final Coordinate newRelativePosition = calculatePositionRelativeToTypedArea(currentParent,
																						newX,
																						newY);

			final int newRelativeX = newRelativePosition.getX();
			final int newRelativeY = newRelativePosition.getY();

			final DisplayAreaManipulator<DisplayArea> areaManipulator = this
					.getAreaManipulator(shellNode);
			areaManipulator.moveResize(	newRelativeX,
										newRelativeY,
										newWidth,
										newHeight);
		}
	}

	protected Coordinate calculatePositionRelativeToTypedArea(	final ShellNode directParent,
																final int directRelativeX,
																final int directRelativeY) {

		final ShellSurface parentRenderArea = findClosestSameTypeArea(directParent);

		if (parentRenderArea == null) {
			return new Coordinate(directRelativeX, directRelativeY);
		}

		final int newAbsX = directParent.getAbsoluteX() + directRelativeX;
		final int newAbsY = directParent.getAbsoluteY() + directRelativeY;

		final Coordinate absCorParent = getAreaManipulator(this.root)
				.translateCoordinates(getAreaPeer(parentRenderArea), 0, 0);

		final int newRelX = newAbsX - absCorParent.getX();
		final int newRelY = newAbsY - absCorParent.getY();

		final Coordinate corRelativeToTypedParent = new Coordinate(	newRelX,
																	newRelY);

		return corRelativeToTypedParent;
	}

	protected void initialize(final ShellNode parent, final ShellNode area) {
		initializeGeoTransformableSquare(parent, area);
		for (final ShellNode child : area.getChildren()) {
			initialize(area, child);
		}
	}

	protected void initializeGeoTransformableSquare(final ShellNode parent,
													final ShellNode area) {
		// Sublcasses should override & implement this method to make sure the
		// given area is ready to make back-end calls.
	}

	/**
	 * Perform any required operations on the parent before the manipulated
	 * render area is reparented.
	 * 
	 * @param newParentRenderArea
	 */
	protected void preProcesNewSameTypeParent(final ShellSurface newParentRenderArea) {
		// newParentRenderArea
		// .getPlatformRenderArea()
		// .selectEvent(DisplayEventSelector.REDIRECT_CHILD_WINDOW_GEOMETRY_CHANGES);
	}

	@Override
	public void reparent(final ShellNode shellNode, final ShellNode parent) {
		final ShellSurface currentRenderArea = (ShellSurface) shellNode;
		final ShellNode newParent = parent;
		final ShellSurface newParentRenderArea = findClosestSameTypeArea(newParent);

		final boolean newParentInitialized = isAreaInitialized(newParentRenderArea);
		final boolean currentRenderAreaInitialized = isAreaInitialized(currentRenderArea);

		if (newParentInitialized && !currentRenderAreaInitialized) {
			// parent is ready but we are not. we initialize ourself with
			// our ready parent as argument.
			initialize(newParent, currentRenderArea);
		} else if (newParentInitialized && currentRenderAreaInitialized) {
			// we are ready and our new parent is ready. we start the
			// procedure to change to our new ready parent.

			// call back-end to reparent current manipulated area
			final int newX = shellNode.getX();
			final int newY = shellNode.getY();

			if (currentRenderArea.equals(newParentRenderArea)) {
				throw new IllegalArgumentException("Can not reparent to a child of self.");
			} else {
				final Coordinate newRelativePosition = calculatePositionRelativeToTypedArea(newParent,
																							newX,
																							newY);

				final int newRelativeX = newRelativePosition.getX();
				final int newRelativeY = newRelativePosition.getY();

				preProcesNewSameTypeParent(newParentRenderArea);
				reparent(	shellNode,
							getAreaPeer(newParentRenderArea),
							newRelativeX,
							newRelativeY);
			}
		} else if (currentRenderAreaInitialized && !newParentInitialized) {
			// we are ready but our new parent isn't. we hide ourself.

			// call back-end to hide current manipulated area
			this.getAreaManipulator(shellNode).hide();
		} else {
			// If both the new parent and the current render area are
			// not initialized, there isn't much we can do. We don't want to
			// call any back-end.
		}
	}

	/**
	 * @param shellSurface
	 * @return
	 */
	protected DisplayArea getAreaPeer(final ShellSurface shellSurface) {
		return shellSurface.getDisplayRenderArea();
	}

	/**
	 * Find the the closest parent in the area tree hierarchy that matches the
	 * type of {@link ShellSurfaceGeoExecutor#getManipulatedArea()}, starting
	 * from the given square.
	 * 
	 * @param square
	 *            The {@link ShellNode} to start searching from upwards in the
	 *            tree hierarchy.
	 * @return The closest parent with type {@link ShellSurface}.
	 */
	protected ShellSurface findClosestSameTypeArea(final ShellNode square) {

		// find the closest ancestor that is of type ShellSurface
		if (square instanceof ShellSurface) {

			return (ShellSurface) square;

		} else {
			final ShellNodeTransformation transformation = square
					.toGeoTransformation();
			final ShellNode currentParent = transformation.getParent0();
			final ShellNode newParent = transformation.getParent1();
			if (currentParent == null) {
				if (newParent == null) {
					return null;
				} else {
					return findClosestSameTypeArea(newParent);
				}
			} else {
				return findClosestSameTypeArea(currentParent);
			}
		}
	}

	@Override
	public void hide(final ShellNode shellNode) {
		if (isAreaInitialized((ShellSurface) shellNode)) {
			super.hide(shellNode);
		}
	}

	@Override
	public void show(final ShellNode shellNode) {
		if (isAreaInitialized((ShellSurface) shellNode)) {
			super.show(shellNode);
		}
	}

	@Override
	public void destroy(final ShellNode shellNode) {
		this.getAreaManipulator(shellNode).destroy();
	}
}
