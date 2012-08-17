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
package org.trinity.shell.geo.impl;

import javax.inject.Named;

import org.trinity.shell.geo.api.ShellGeoExecutor;
import org.trinity.shell.geo.api.ShellGeoNode;
import org.trinity.shell.geo.api.ShellGeoTransformation;

import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
/**
 * A <code>ShellGeoVExecutor</code> executes the actual geometry changes for a
 * {@link ShellGeoVNode}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Singleton
@Bind
@Named("GeoVirt")
public class ShellGeoVExecutor implements ShellGeoExecutor {

	/**
	 * @param virtSquare
	 *            The <code>ShellGeoVNode</code> who's geometry will be
	 *            executed by this <code>ShellGeoVExecutor</code>.
	 */
	protected ShellGeoVExecutor() {
	}

	@Override
	public void lower(final ShellGeoNode shellGeoNode) {
		final ShellGeoNode[] children = shellGeoNode
				.getChildren();
		for (final ShellGeoNode child : children) {
			child.getGeoExecutor().lower(child);
		}
	}

	@Override
	public void raise(final ShellGeoNode shellGeoNode) {
		final ShellGeoNode[] children = shellGeoNode
				.getChildren();
		for (final ShellGeoNode child : children) {
			child.getGeoExecutor().raise(child);
		}
	}

	@Override
	public void move(final ShellGeoNode shellGeoNode,
							final int relativeX,
							final int relativeY) {

		final ShellGeoTransformation shellGeoTransformation = shellGeoNode
				.toGeoTransformation();

		final int deltaX = shellGeoTransformation.getDeltaX();
		final int deltaY = shellGeoTransformation.getDeltaY();

		final ShellGeoNode[] children = shellGeoNode
				.getChildren();
		for (final ShellGeoNode child : children) {
			final int oldRelX = child.getX();
			final int oldRelY = child.getY();

			final int newRelX = oldRelX + deltaX;
			final int newRelY = oldRelY + deltaY;

			// directly manipulated underlying platform specific geometry of the
			// child
			child.getGeoExecutor().move(child, newRelX, newRelY);
		}
	}

	@Override
	public void resize(	final ShellGeoNode shellGeoNode,
							final int width,
							final int height) {
		// do nothing
	}

	@Override
	public void moveResize(final ShellGeoNode shellGeoNode,
								final int relativeX,
								final int relativeY,
								final int width,
								final int height) {
		move(shellGeoNode, relativeX, relativeY);
	}

	@Override
	public void show(final ShellGeoNode shellGeoNode) {
		final ShellGeoNode[] children = shellGeoNode
				.getChildren();
		for (final ShellGeoNode child : children) {
			updateChildVisibility(child, true);
		}
	}

	@Override
	public void hide(final ShellGeoNode shellGeoNode) {
		final ShellGeoNode[] children = shellGeoNode
				.getChildren();
		for (final ShellGeoNode child : children) {
			updateChildVisibility(child, false);
		}
	}

	/**
	 * @param child
	 * @param visible
	 */
	private void updateChildVisibility(	final ShellGeoNode child,
										final boolean parentVisible) {
		final boolean childVisible = child.isVisible();
		// directly update underlying platform specific visibility of
		// the child
		if (childVisible & parentVisible) {
			child.getGeoExecutor().show(child);
		} else {
			child.getGeoExecutor().hide(child);
		}
	}

	@Override
	public void reparent(	final ShellGeoNode shellGeoNode,
								final ShellGeoNode parent) {
		final ShellGeoNode[] children = shellGeoNode
				.getChildren();

		final boolean parentVisible = parent.isVisible();
		for (final ShellGeoNode child : children) {
			// directly update underlying platform specific parent of
			// the child
			child.getGeoExecutor().reparent(child,
												shellGeoNode);
			updateChildVisibility(child, parentVisible);
		}
	}

	@Override
	public void destroy(final ShellGeoNode shellGeoNode) {
		// TODO destroy all children? (Will somebody *please* think of the
		// children! :'( )
	}
}
