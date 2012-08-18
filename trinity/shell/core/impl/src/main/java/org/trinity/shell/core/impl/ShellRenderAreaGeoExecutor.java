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
import org.trinity.shell.core.api.ShellRenderArea;
import org.trinity.shell.geo.api.AbstractShellGeoExecutor;
import org.trinity.shell.geo.api.ShellGeoExecutor;
import org.trinity.shell.geo.api.ShellGeoNode;
import org.trinity.shell.geo.api.ShellGeoTransformation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

// TODO documentation
/**
 * A <code>RenderAreaGeoExecutor</code> is a delegate class for directly
 * manipulating a {@link ShellRenderArea}'s geometry.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * @see ShellGeoExecutor
 */
@Bind(value = @Named("ShellRenderAreaGeoExecutor"), to = @To(value = Type.CUSTOM, customs = ShellGeoExecutor.class))
@Singleton
public class ShellRenderAreaGeoExecutor extends AbstractShellGeoExecutor {

	private final ShellRenderArea root;

	@Inject
	protected ShellRenderAreaGeoExecutor(@Named("shellRootRenderArea") final ShellRenderArea root) {
		this.root = root;
	}

	@Override
	public DisplayAreaManipulator<DisplayArea> getAreaManipulator(final ShellGeoNode shellGeoNode) {
		return this.getAreaManipulator(shellGeoNode);
	}

	@SuppressWarnings("unchecked")
	protected <T extends DisplayArea> DisplayAreaManipulator<T> getAreaManipulator(final ShellRenderArea shellRenderArea) {
		return (DisplayAreaManipulator<T>) shellRenderArea
				.getDisplayRenderArea();
	}

	protected boolean isAreaInitialized(final ShellRenderArea shellRenderArea) {
		if (shellRenderArea == null) {
			return false;
		}
		final boolean initialized = shellRenderArea.getDisplayRenderArea() != null;
		return initialized;
	}

	@Override
	public void move(	final ShellGeoNode shellGeoNode,
						final int relativeX,
						final int relativeY) {
		if (isAreaInitialized((ShellRenderArea) shellGeoNode)) {
			final int newX = relativeX;
			final int newY = relativeY;
			final ShellGeoNode currentParent = shellGeoNode.getParent();
			final Coordinate newRelativePosition = calculatePositionRelativeToTypedArea(currentParent,
																						newX,
																						newY);

			final int newRelativeX = newRelativePosition.getX();
			final int newRelativeY = newRelativePosition.getY();
			this.getAreaManipulator(shellGeoNode).move(	newRelativeX,
														newRelativeY);
		}
	}

	@Override
	public void resize(	final ShellGeoNode shellGeoNode,
						final int width,
						final int height) {
		if (isAreaInitialized((ShellRenderArea) shellGeoNode)) {
			super.resize(shellGeoNode, width, height);
		}
	}

	@Override
	public void moveResize(	final ShellGeoNode shellGeoNode,
							final int relativeX,
							final int relativeY,
							final int width,
							final int height) {
		final boolean areaInitialized = isAreaInitialized((ShellRenderArea) shellGeoNode);
		if (areaInitialized) {
			final int newX = relativeX;
			final int newY = relativeY;
			final int newWidth = width;
			final int newHeight = height;
			final ShellGeoNode currentParent = shellGeoNode.getParent();

			final Coordinate newRelativePosition = calculatePositionRelativeToTypedArea(currentParent,
																						newX,
																						newY);

			final int newRelativeX = newRelativePosition.getX();
			final int newRelativeY = newRelativePosition.getY();

			final DisplayAreaManipulator<DisplayArea> areaManipulator = this
					.getAreaManipulator(shellGeoNode);
			areaManipulator.moveResize(	newRelativeX,
										newRelativeY,
										newWidth,
										newHeight);
		}
	}

	protected Coordinate calculatePositionRelativeToTypedArea(	final ShellGeoNode directParent,
																final int directRelativeX,
																final int directRelativeY) {

		final ShellRenderArea parentRenderArea = findClosestSameTypeArea(directParent);

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

	protected void initialize(final ShellGeoNode parent, final ShellGeoNode area) {
		initializeGeoTransformableSquare(parent, area);
		for (final ShellGeoNode child : area.getChildren()) {
			initialize(area, child);
		}
	}

	protected void initializeGeoTransformableSquare(final ShellGeoNode parent,
													final ShellGeoNode area) {
		// Sublcasses should override & implement this method to make sure the
		// given area is ready to make back-end calls.
	}

	/**
	 * Perform any required operations on the parent before the manipulated
	 * render area is reparented.
	 * 
	 * @param newParentRenderArea
	 */
	protected void preProcesNewSameTypeParent(final ShellRenderArea newParentRenderArea) {
		// newParentRenderArea
		// .getPlatformRenderArea()
		// .selectEvent(DisplayEventSelector.REDIRECT_CHILD_WINDOW_GEOMETRY_CHANGES);
	}

	@Override
	public void reparent(	final ShellGeoNode shellGeoNode,
							final ShellGeoNode parent) {
		final ShellRenderArea currentRenderArea = (ShellRenderArea) shellGeoNode;
		final ShellGeoNode newParent = parent;
		final ShellRenderArea newParentRenderArea = findClosestSameTypeArea(newParent);

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
			final int newX = shellGeoNode.getX();
			final int newY = shellGeoNode.getY();

			if (currentRenderArea.equals(newParentRenderArea)) {
				throw new IllegalArgumentException("Can not reparent to a child of self.");
			} else {
				final Coordinate newRelativePosition = calculatePositionRelativeToTypedArea(newParent,
																							newX,
																							newY);

				final int newRelativeX = newRelativePosition.getX();
				final int newRelativeY = newRelativePosition.getY();

				preProcesNewSameTypeParent(newParentRenderArea);
				reparent(	shellGeoNode,
							getAreaPeer(newParentRenderArea),
							newRelativeX,
							newRelativeY);
			}
		} else if (currentRenderAreaInitialized && !newParentInitialized) {
			// we are ready but our new parent isn't. we hide ourself.

			// call back-end to hide current manipulated area
			this.getAreaManipulator(shellGeoNode).hide();
		} else {
			// If both the new parent and the current render area are
			// not initialized, there isn't much we can do. We don't want to
			// call any back-end.
		}
	}

	/**
	 * @param shellRenderArea
	 * @return
	 */
	protected DisplayArea getAreaPeer(final ShellRenderArea shellRenderArea) {
		return shellRenderArea.getDisplayRenderArea();
	}

	/**
	 * Find the the closest parent in the area tree hierarchy that matches the
	 * type of {@link ShellRenderAreaGeoExecutor#getManipulatedArea()}, starting
	 * from the given square.
	 * 
	 * @param square
	 *            The {@link ShellGeoNode} to start searching from upwards in
	 *            the tree hierarchy.
	 * @return The closest parent with type {@link ShellRenderArea}.
	 */
	protected ShellRenderArea findClosestSameTypeArea(final ShellGeoNode square) {

		// find the closest ancestor that is of type ShellRenderArea
		if (square instanceof ShellRenderArea) {

			return (ShellRenderArea) square;

		} else {
			final ShellGeoTransformation transformation = square
					.toGeoTransformation();
			final ShellGeoNode currentParent = transformation.getParent0();
			final ShellGeoNode newParent = transformation.getParent1();
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
	public void hide(final ShellGeoNode shellGeoNode) {
		if (isAreaInitialized((ShellRenderArea) shellGeoNode)) {
			super.hide(shellGeoNode);
		}
	}

	@Override
	public void show(final ShellGeoNode shellGeoNode) {
		if (isAreaInitialized((ShellRenderArea) shellGeoNode)) {
			super.show(shellGeoNode);
		}
	}

	@Override
	public void destroy(final ShellGeoNode shellGeoNode) {
		this.getAreaManipulator(shellGeoNode).destroy();
	}
}
