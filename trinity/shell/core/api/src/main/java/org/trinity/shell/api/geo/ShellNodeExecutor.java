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
package org.trinity.shell.api.geo;

// TODO documentation
/**
 * A <code>ShellNodeExecutor</code> is responsible for executing the actual
 * geometry changes for a <code>ShellNode</code>. In essence a
 * <code>ShellNodeExecutor</code> is a delegate for a <code>ShellNode</code> to
 * execute the requested geometry changes.
 * <p>
 * A <code>ShellNodeExecutor</code> is needed because a tree structure of
 * different <code>ShellNode</code> subclasses can have non-uniform and
 * undesired on-screen behavior when one of the node's geometry changes. A
 * <code>ShellNodeExecutor</code> implementation is needed to accommodate for
 * this and make sure a change in the geometry of its requester has the correct
 * and desired on-screen effect.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface ShellNodeExecutor {

	/**
	 * Execute the actual lowering of the handled <code>ShellNode</code>.
	 */
	void lower(ShellNode shellNode);

	/**
	 * Execute the actual raising the handled <code>ShellNode</code>.
	 */
	void raise(ShellNode shellNode);

	/**
	 * Execute the actual moving of the handled <code>ShellNode</code>.
	 * 
	 * @param relativeX
	 * @param relativeY
	 */
	void move(	ShellNode shellNode,
				int relativeX,
				int relativeY);

	/**
	 * Execute the actual resizing of the handled <code>ShellNode</code>.
	 * 
	 * @param width
	 * @param height
	 */
	void resize(ShellNode shellNode,
				int width,
				int height);

	/**
	 * Execute the actual resizing and moving of the handled
	 * <code>ShellNode</code>.
	 * 
	 * @param relativeX
	 * @param relativeY
	 * @param width
	 * @param height
	 */
	void moveResize(ShellNode shellNode,
					int relativeX,
					int relativeY,
					int width,
					int height);

	void show(ShellNode shellNode);

	void hide(ShellNode shellNode);

	/**
	 * Execute the actual parent update of the handled <code>ShellNode</code> .
	 * 
	 * @param parent
	 */
	void reparent(	ShellNode shellNode,
					ShellNode parent);

	/**
	 * Execute the actual destroy process of the handled <code>ShellNode</code>.
	 */
	void destroy(ShellNode shellNode);
}
