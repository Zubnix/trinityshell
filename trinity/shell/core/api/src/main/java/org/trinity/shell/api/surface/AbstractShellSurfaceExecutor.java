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
package org.trinity.shell.api.surface;

import org.trinity.foundation.display.api.DisplayArea;
import org.trinity.foundation.display.api.DisplayAreaManipulator;
import org.trinity.foundation.shared.geometry.api.Coordinate;
import org.trinity.shell.api.node.AbstractShellNodeExecutor;
import org.trinity.shell.api.node.ShellNode;

public abstract class AbstractShellSurfaceExecutor extends AbstractShellNodeExecutor {

	private final ShellSurface root;

	protected AbstractShellSurfaceExecutor(final ShellSurface root) {
		this.root = root;
	}

	@Override
	public DisplayAreaManipulator<DisplayArea> getShellNodeManipulator(final ShellNode shellNode) {
		return getAreaManipulator((ShellSurface) shellNode);
	}

	@SuppressWarnings("unchecked")
	protected <T extends DisplayArea> DisplayAreaManipulator<T> getAreaManipulator(final ShellSurface shellSurface) {
		return (DisplayAreaManipulator<T>) shellSurface.getDisplaySurface();
	}

	protected boolean isAreaInitialized(final ShellSurface shellSurface) {
		if (shellSurface == null) {
			return false;
		}
		final boolean initialized = shellSurface.getDisplaySurface() != null;
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
			getShellNodeManipulator(shellNode).move(newRelativeX,
													newRelativeY);
		}
	}

	@Override
	public void resize(	final ShellNode shellNode,
						final int width,
						final int height) {
		if (isAreaInitialized((ShellSurface) shellNode)) {
			super.resize(	shellNode,
							width,
							height);
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

			final DisplayAreaManipulator<DisplayArea> areaManipulator = getShellNodeManipulator(shellNode);
			areaManipulator.moveResize(	newRelativeX,
										newRelativeY,
										newWidth,
										newHeight);
		}
	}

	protected Coordinate calculatePositionRelativeToTypedArea(	final ShellNode directParent,
																final int directRelativeX,
																final int directRelativeY) {

		final ShellSurface parentRenderArea = findClosestSameTypeSurface(directParent);

		if (parentRenderArea == null) {
			return new Coordinate(	directRelativeX,
									directRelativeY);
		}

		final int newAbsX = directParent.getAbsoluteX() + directRelativeX;
		final int newAbsY = directParent.getAbsoluteY() + directRelativeY;

		final Coordinate absCorParent = getAreaManipulator(this.root)
				.translateCoordinates(	getAreaPeer(parentRenderArea),
										0,
										0);

		final int newRelX = newAbsX - absCorParent.getX();
		final int newRelY = newAbsY - absCorParent.getY();

		final Coordinate corRelativeToTypedParent = new Coordinate(	newRelX,
																	newRelY);

		return corRelativeToTypedParent;
	}

	protected void initialize(	final ShellNode parent,
								final ShellNode nodeToInit) {
		initializeShellSurface(	parent,
								nodeToInit);
		for (final ShellNode child : nodeToInit.getChildren()) {
			initialize(	nodeToInit,
						child);
		}
	}

	protected abstract void initializeShellSurface(	final ShellNode parent,
													final ShellNode area);

	@Override
	public void reparent(	final ShellNode shellNode,
							final ShellNode parent) {
		final ShellSurface currentSurface = (ShellSurface) shellNode;
		final ShellNode newParent = parent;
		final ShellSurface newParentSurface = findClosestSameTypeSurface(newParent);

		final boolean newParentInitialized = isAreaInitialized(newParentSurface);
		final boolean currentShellSurfaceInitialized = isAreaInitialized(currentSurface);

		if (newParentInitialized && !currentShellSurfaceInitialized) {
			// parent is ready but we are not. we initialize ourself with
			// our ready parent as argument.
			initialize(	newParent,
						currentSurface);
		} else if (newParentInitialized && currentShellSurfaceInitialized) {
			// we are ready and our new parent is ready. we start the
			// procedure to change to our new ready parent.
			final int newX = shellNode.getX();
			final int newY = shellNode.getY();

			if (currentSurface.equals(newParentSurface)) {
				throw new IllegalArgumentException("Can not reparent to self.");
			} else {
				final Coordinate newRelativePosition = calculatePositionRelativeToTypedArea(newParent,
																							newX,
																							newY);

				final int newRelativeX = newRelativePosition.getX();
				final int newRelativeY = newRelativePosition.getY();

				reparent(	shellNode,
							getAreaPeer(newParentSurface),
							newRelativeX,
							newRelativeY);
			}
		} else if (currentShellSurfaceInitialized && !newParentInitialized) {
			// we are ready but our new parent isn't.
			// getShellNodeManipulator(shellNode).hide();
		}
	}

	protected abstract DisplayArea getAreaPeer(final ShellSurface shellSurface);

	protected abstract ShellSurface findClosestSameTypeSurface(final ShellNode square);

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
		getShellNodeManipulator(shellNode).destroy();
	}
}
