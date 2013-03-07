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
package org.trinity.shell.api.surface;

import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.display.DisplayArea;
import org.trinity.foundation.api.display.DisplayAreaManipulator;
import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.shell.api.scene.AbstractShellNodeExecutor;
import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellNodeExecutor;
import org.trinity.shell.api.scene.ShellNodeParent;

/***************************************
 * A {@link ShellNodeExecutor} for use with an {@link AbstractShellSurface}.
 * 
 *************************************** 
 */
@NotThreadSafe
public abstract class AbstractShellSurfaceExecutor extends AbstractShellNodeExecutor implements ShellNodeExecutor {

	@Override
	public abstract AbstractShellSurface getShellNode();

	@Override
	public void move(	final int relativeX,
						final int relativeY) {
		final ShellNode shellNode = getShellNode();

		final int newX = relativeX;
		final int newY = relativeY;
		final ShellNodeParent directParent = shellNode.getParent();
		final Coordinate newRelativePosition = calculateRelativePosition(	directParent,
																			newX,
																			newY);

		final int newRelativeX = newRelativePosition.getX();
		final int newRelativeY = newRelativePosition.getY();
		getShellNodeManipulator().move(	newRelativeX,
										newRelativeY);
	}

	@Override
	public void moveResize(	final int relativeX,
							final int relativeY,
							final int width,
							final int height) {
		final ShellNode shellNode = getShellNode();

		final int newX = relativeX;
		final int newY = relativeY;
		final int newWidth = width;
		final int newHeight = height;
		final ShellNodeParent directParent = shellNode.getParent();

		final Coordinate newRelativePosition = calculateRelativePosition(	directParent,
																			newX,
																			newY);

		final int newRelativeX = newRelativePosition.getX();
		final int newRelativeY = newRelativePosition.getY();

		final DisplayAreaManipulator areaManipulator = getShellNodeManipulator();
		areaManipulator.moveResize(	newRelativeX,
									newRelativeY,
									newWidth,
									newHeight);
	}

	/***************************************
	 * Calculate a new position, relative to the closest parent node of the same
	 * type. This is needed as not all shell nodes are known by the underlying
	 * display system ie they live in a different space. This method translates
	 * the current relative coordinates in 'shell space' to 'display space' used
	 * by managed {@link ShellSurface}.
	 * 
	 * @param directParent
	 *            the current direct parent of the managed {@code ShellSurface}.
	 * @param directRelativeX
	 *            The current relative X coordinate of the managed
	 *            {@code ShellSurface}, in 'shell space'.
	 * @param directRelativeY
	 *            the current relative Y coordinate of the managed
	 *            {@code ShellSurface}, in 'shell space'.
	 * @return a {@link Coordinate} in 'display space'.
	 *************************************** 
	 */
	protected Coordinate calculateRelativePosition(	final ShellNodeParent directParent,
													final int directRelativeX,
													final int directRelativeY) {

		final ShellSurface parentSameTypeSurface = findClosestSameTypeSurface(directParent);

		if (parentSameTypeSurface == null) {
			return new Coordinate(	directRelativeX,
									directRelativeY);
		}

		final int newAbsX = directParent.getAbsoluteX() + directRelativeX;
		final int newAbsY = directParent.getAbsoluteY() + directRelativeY;

		final Coordinate absCorParent = new Coordinate(	parentSameTypeSurface.getAbsoluteX(),
														parentSameTypeSurface.getAbsoluteY());

		final int newRelX = newAbsX - absCorParent.getX();
		final int newRelY = newAbsY - absCorParent.getY();

		final Coordinate corRelativeToTypedParent = new Coordinate(	newRelX,
																	newRelY);

		return corRelativeToTypedParent;
	}

	@Override
	public void reparent(final ShellNodeParent parent) {
		final ShellNode currentSurface = getShellNode();
		final ShellNodeParent newParent = parent;
		final ShellSurface newParentSurface = findClosestSameTypeSurface(newParent);

		final int newX = currentSurface.getX();
		final int newY = currentSurface.getY();

		if (currentSurface.equals(newParentSurface)) {
			throw new IllegalArgumentException("Can not reparent to self.");
		} else {
			final Coordinate newRelativePosition = calculateRelativePosition(	newParent,
																				newX,
																				newY);

			final int newRelativeX = newRelativePosition.getX();
			final int newRelativeY = newRelativePosition.getY();

			final DisplayArea parentAreaPeer = getSurfacePeer(newParentSurface);

			getShellNodeManipulator().setParent(parentAreaPeer,
												newRelativeX,
												newRelativeY);
		}
	}

	/***************************************
	 * Find the closest parent node that lives in the same space as the given
	 * node.
	 * 
	 * @param node
	 *            The {@link ShellNode} who's parent to search for.
	 * @return A found {@link ShellSurface} parent.
	 *************************************** 
	 */
	protected abstract ShellSurface findClosestSameTypeSurface(final ShellNode node);

	/***************************************
	 * The object functioning as the {@code DisplayArea} for the given
	 * {@link ShellSurface}.
	 * 
	 * @param shellSurface
	 *            The {@link ShellSurface} who's {@code DisplayArea} to return.
	 * @return a {@link DisplayArea}.
	 *************************************** 
	 */
	protected abstract DisplayArea getSurfacePeer(final ShellSurface shellSurface);
}