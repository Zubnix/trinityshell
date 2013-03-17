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

import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.display.DisplayArea;
import org.trinity.foundation.api.display.DisplayAreaManipulator;
import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.scene.AbstractShellNode;
import org.trinity.shell.api.scene.AbstractShellNodeExecutor;
import org.trinity.shell.api.scene.AbstractShellNodeParent;
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
	public void move(final Coordinate position) {

		final AbstractShellNodeParent directParent = getShellNode().getParentImpl();
		final Coordinate newRelativePosition = calculateRelativePosition(	directParent,
																			position);

		final int newRelativeX = newRelativePosition.getX();
		final int newRelativeY = newRelativePosition.getY();
		getShellNodeManipulator().move(	newRelativeX,
										newRelativeY);
	}

	@Override
	public void moveResize(	final Coordinate position,
							final Size size) {

		final AbstractShellNodeParent directParent = getShellNode().getParentImpl();

		final Coordinate newRelativePosition = calculateRelativePosition(	directParent,
																			position);

		final int newRelativeX = newRelativePosition.getX();
		final int newRelativeY = newRelativePosition.getY();

		final DisplayAreaManipulator areaManipulator = getShellNodeManipulator();
		areaManipulator.moveResize(	newRelativeX,
									newRelativeY,
									size.getWidth(),
									size.getHeight());
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
	 * @param newRelativeX
	 *            The current relative X coordinate of the managed
	 *            {@code ShellSurface}, in 'shell space'.
	 * @param newRelativeY
	 *            the current relative Y coordinate of the managed
	 *            {@code ShellSurface}, in 'shell space'.
	 * @return a {@link Coordinate} in 'display space'.
	 *************************************** 
	 */
	protected Coordinate calculateRelativePosition(	final AbstractShellNodeParent directParent,
													final Coordinate newRelativePosition) {

		final AbstractShellSurface parentTypedSurface = findClosestSameTypeSurface(directParent);

		if (parentTypedSurface == null) {
			return newRelativePosition;
		}

		final Coordinate absolutePositionDirectParent = getAbsolutePosition(directParent);
		final Coordinate newAbsolutePosition = absolutePositionDirectParent.add(newRelativePosition);

		final Coordinate sameTypeParentPosition = getAbsolutePosition(parentTypedSurface);
		final Coordinate corRelativeToTypedParent = newAbsolutePosition.subtract(sameTypeParentPosition);

		return corRelativeToTypedParent;
	}

	private Coordinate getAbsolutePosition(final AbstractShellNode node) {
		final Coordinate childPosition = node.getPositionImpl();
		final AbstractShellNodeParent directParent = node.getParentImpl();

		if ((directParent == null) || directParent.equals(this)) {
			return childPosition;
		}

		final Coordinate absoluteParentPosition = getAbsolutePosition(directParent);
		final Coordinate absolutePosition = childPosition.add(absoluteParentPosition);

		return absolutePosition;
	}

	@Override
	public void reparent(final ShellNodeParent parent) {
		checkArgument(parent instanceof AbstractShellNodeParent);

		final AbstractShellSurface currentSurface = getShellNode();
		final AbstractShellNodeParent newParent = (AbstractShellNodeParent) parent;
		final ShellSurface newParentSurface = findClosestSameTypeSurface(newParent);

		final Coordinate surfacePosition = currentSurface.getPositionImpl();

		if (currentSurface.equals(newParentSurface)) {
			throw new IllegalArgumentException("Can not reparent to self.");
		} else {
			final Coordinate newRelativePosition = calculateRelativePosition(	newParent,
																				surfacePosition);

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
	protected abstract AbstractShellSurface findClosestSameTypeSurface(final ShellNode node);

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