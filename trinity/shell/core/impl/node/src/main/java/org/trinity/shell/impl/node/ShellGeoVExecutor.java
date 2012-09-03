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
package org.trinity.shell.impl.node;

import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.ShellNodeExecutor;
import org.trinity.shell.api.node.ShellNodeTransformation;

import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
/**
 * A <code>ShellGeoVExecutor</code> executes the actual geometry changes for a
 * {@link ShellGeoVNode}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */

@Bind(value = @Named("ShellGeoVExecutor"))
@Singleton
public class ShellGeoVExecutor implements ShellNodeExecutor {

	ShellGeoVExecutor() {
	}

	@Override
	public void lower(final ShellNode shellNode) {
		final ShellNode[] children = shellNode.getChildren();
		for (final ShellNode child : children) {
			child.getNodeExecutor().lower(child);
		}
	}

	@Override
	public void raise(final ShellNode shellNode) {
		final ShellNode[] children = shellNode.getChildren();
		for (final ShellNode child : children) {
			child.getNodeExecutor().raise(child);
		}
	}

	@Override
	public void move(	final ShellNode shellNode,
						final int relativeX,
						final int relativeY) {

		final ShellNodeTransformation shellNodeTransformation = shellNode.toGeoTransformation();

		final int deltaX = shellNodeTransformation.getDeltaX();
		final int deltaY = shellNodeTransformation.getDeltaY();

		final ShellNode[] children = shellNode.getChildren();
		for (final ShellNode child : children) {
			final int oldRelX = child.getX();
			final int oldRelY = child.getY();

			final int newRelX = oldRelX + deltaX;
			final int newRelY = oldRelY + deltaY;

			// directly manipulated underlying platform specific geometry of the
			// child
			child.getNodeExecutor().move(	child,
											newRelX,
											newRelY);
		}
	}

	@Override
	public void resize(	final ShellNode shellNode,
						final int width,
						final int height) {
		// do nothing
	}

	@Override
	public void moveResize(	final ShellNode shellNode,
							final int relativeX,
							final int relativeY,
							final int width,
							final int height) {
		move(	shellNode,
				relativeX,
				relativeY);
	}

	@Override
	public void show(final ShellNode shellNode) {
		final ShellNode[] children = shellNode.getChildren();
		for (final ShellNode child : children) {
			updateChildVisibility(	child,
									true);
		}
	}

	@Override
	public void hide(final ShellNode shellNode) {
		final ShellNode[] children = shellNode.getChildren();
		for (final ShellNode child : children) {
			updateChildVisibility(	child,
									false);
		}
	}

	/**
	 * @param child
	 * @param visible
	 */
	private void updateChildVisibility(	final ShellNode child,
										final boolean parentVisible) {
		final boolean childVisible = child.isVisible();
		// directly update underlying platform specific visibility of
		// the child
		if (childVisible & parentVisible) {
			child.getNodeExecutor().show(child);
		} else {
			child.getNodeExecutor().hide(child);
		}
	}

	@Override
	public void reparent(	final ShellNode shellNode,
							final ShellNode parent) {
		final ShellNode[] children = shellNode.getChildren();

		final boolean parentVisible = parent.isVisible();
		for (final ShellNode child : children) {
			// directly update underlying platform specific parent of
			// the child
			child.getNodeExecutor().reparent(	child,
												shellNode);
			updateChildVisibility(	child,
									parentVisible);
		}
	}

	@Override
	public void destroy(final ShellNode shellNode) {
		// TODO destroy all children? (Will somebody *please* think of the
		// children! :'( )
	}
}
