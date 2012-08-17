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
package org.trinity.shell.geo.api;

// TODO documentation
/**
 * A <code>ShellGeoExecutor</code> is responsible for executing the actual
 * geometry changes for a <code>ShellGeoNode</code>. In essence a
 * <code>ShellGeoExecutor</code> is a delegate for a <code>ShellGeoNode</code>
 * to execute the requested geometry changes.
 * <p>
 * A <code>ShellGeoExecutor</code> is needed because a tree structure of
 * different <code>ShellGeoNode</code> subclasses can have non-uniform and
 * undesired on-screen behavior when one of the node's geometry changes. A
 * <code>ShellGeoExecutor</code> implementation is needed to accommodate for
 * this and make sure a change in the geometry of its requester has the correct
 * and desired on-screen effect.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface ShellGeoExecutor {

	/**
	 * Execute the actual lowering of the handled <code>ShellGeoNode</code>.
	 */
	void lower(ShellGeoNode shellGeoNode);

	/**
	 * Execute the actual raising the handled <code>ShellGeoNode</code>.
	 */
	void raise(ShellGeoNode shellGeoNode);

	/**
	 * Execute the actual moving of the handled <code>ShellGeoNode</code>.
	 * 
	 * @param relativeX
	 * @param relativeY
	 */
	void move(ShellGeoNode shellGeoNode, int relativeX, int relativeY);

	/**
	 * Execute the actual resizing of the handled <code>ShellGeoNode</code>.
	 * 
	 * @param width
	 * @param height
	 */
	void resize(ShellGeoNode shellGeoNode, int width, int height);

	/**
	 * Execute the actual resizing and moving of the handled
	 * <code>ShellGeoNode</code>.
	 * 
	 * @param relativeX
	 * @param relativeY
	 * @param width
	 * @param height
	 */
	void moveResize(ShellGeoNode shellGeoNode,
					int relativeX,
					int relativeY,
					int width,
					int height);

	void show(ShellGeoNode shellGeoNode);

	void hide(ShellGeoNode shellGeoNode);

	/**
	 * Execute the actual parent update of the handled <code>ShellGeoNode</code>
	 * .
	 * 
	 * @param parent
	 */
	void reparent(ShellGeoNode shellGeoNode, ShellGeoNode parent);

	/**
	 * Execute the actual destroy process of the handled
	 * <code>ShellGeoNode</code>.
	 */
	void destroy(ShellGeoNode shellGeoNode);
}
