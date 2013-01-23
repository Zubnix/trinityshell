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
package org.trinity.shell.api.scene;

// TODO documentation
/**
 * Executes the actual geometry changes for a {@link ShellNode}. A
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
 */
public interface ShellNodeExecutor {

	/***************************************
	 * The <code>ShellNode</code> on who's behalf this executer operates.
	 * 
	 * @return a {@link ShellNode}
	 *************************************** 
	 */
	ShellNode getShellNode();

	/**
	 * Execute the actual lowering of the handled <code>ShellNode</code>.
	 */
	void lower();

	/**
	 * Execute the actual raising the handled <code>ShellNode</code>.
	 */
	void raise();

	/**
	 * Execute the actual moving of the handled <code>ShellNode</code>.
	 * 
	 * @param relativeX
	 * @param relativeY
	 */
	void move(	int relativeX,
				int relativeY);

	/**
	 * Execute the actual resizing of the handled <code>ShellNode</code>.
	 * 
	 * @param width
	 * @param height
	 */
	void resize(int width,
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
	void moveResize(int relativeX,
					int relativeY,
					int width,
					int height);

	/***************************************
	 * Execute the actual showing of the handled <code>ShellNode</code>.
	 *************************************** 
	 */
	void show();

	/***************************************
	 * Execute the actual hiding of the handled <code>ShellNode</code>.
	 *************************************** 
	 */
	void hide();

	/**
	 * Execute the actual parent update of the handled <code>ShellNode</code> .
	 * 
	 * @param parent
	 */
	void reparent(ShellNodeParent parent);

	/**
	 * Execute the actual destroy process of the handled <code>ShellNode</code>.
	 */
	void destroy();
}
