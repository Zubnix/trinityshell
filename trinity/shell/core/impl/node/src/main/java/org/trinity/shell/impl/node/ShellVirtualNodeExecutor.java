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
import org.trinity.shell.api.node.ShellNodeParent;
import org.trinity.shell.api.node.ShellNodeTransformation;

// TODO documentation
/**
 * A <code>ShellGeoVExecutor</code> executes the actual geometry changes for a
 * {@link ShellVirtualNode}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ShellVirtualNodeExecutor implements ShellNodeExecutor {

	private final ShellVirtualNode shellVirtualNode;

	public ShellVirtualNodeExecutor(final ShellVirtualNode shellVirtualNode) {
		this.shellVirtualNode = shellVirtualNode;
	}

	@Override
	public ShellVirtualNode getShellNode() {
		return this.shellVirtualNode;
	}

	@Override
	public void lower() {
		final ShellNode[] children = getShellNode().getChildren();
		for (final ShellNode child : children) {
			child.getShellNodeExecutor().lower();
		}
	}

	@Override
	public void raise() {
		final ShellNode[] children = getShellNode().getChildren();
		for (final ShellNode child : children) {
			child.getShellNodeExecutor().raise();
		}
	}

	@Override
	public void move(	final int relativeX,
						final int relativeY) {
		final ShellVirtualNode shellNode = getShellNode();
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
			child.getShellNodeExecutor().move(	newRelX,
												newRelY);
		}
	}

	@Override
	public void resize(	final int width,
						final int height) {
		// do nothing
	}

	@Override
	public void moveResize(	final int relativeX,
							final int relativeY,
							final int width,
							final int height) {
		move(	relativeX,
				relativeY);
	}

	@Override
	public void show() {
		final ShellNode[] children = getShellNode().getChildren();
		for (final ShellNode child : children) {
			updateChildVisibility(	child,
									true);
		}
	}

	@Override
	public void hide() {
		final ShellNode[] children = getShellNode().getChildren();
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
			child.getShellNodeExecutor().show();
		} else {
			child.getShellNodeExecutor().hide();
		}
	}

	@Override
	public void reparent(final ShellNodeParent parent) {
		final ShellVirtualNode shellNode = getShellNode();

		final ShellNode[] children = shellNode.getChildren();

		final boolean nodeVisible = shellNode.isVisible();
		for (final ShellNode child : children) {
			// update underlying platform specific parent of
			// the child
			child.getShellNodeExecutor().reparent(shellNode);

			updateChildVisibility(	child,
									nodeVisible);
		}
	}

	@Override
	public void destroy() {
		final ShellNode[] children = getShellNode().getChildren();
		for (final ShellNode child : children) {
			if (!child.isDestroyed()) {
				child.doDestroy();
			}
		}
	}

	@Override
	public void init(final ShellNodeParent parent) {
		reparent(parent);
	}
}
