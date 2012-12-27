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
package org.trinity.shell.api.node;

import java.awt.LayoutManager;

import org.trinity.foundation.api.render.SurfaceNode;
import org.trinity.shell.api.node.event.ShellNodeEvent;

/***************************************
 * The super interface of all nodes that live in the shell scene graph.
 * 
 *************************************** 
 */
public interface ShellNode extends SurfaceNode {

	/***************************************
	 * Reset any value set by {@link #setX(int)} or {@link #setY(int)} to the
	 * this node's current position.
	 *************************************** 
	 */
	void cancelPendingMove();

	/***************************************
	 * Reset any value set by {@link #setWidth(int)} and {@link #setHeight(int)}
	 * to this node's current size.
	 *************************************** 
	 */
	void cancelPendingResize();

	/***************************************
	 * Destroy this node.
	 *************************************** 
	 */
	void doDestroy();

	/***************************************
	 * Lower this node.
	 *************************************** 
	 */
	void doLower();

	/***************************************
	 * Reparent this node to the parent that was specified in
	 * {@link #setParent(ShellNodeParent)}.
	 *************************************** 
	 */
	void doReparent();

	/***************************************
	 * Move this node to the coordinate that was specified in {@link #setX(int)}
	 * and {@link #setY(int)}.
	 *************************************** 
	 */
	void doMove();

	/***************************************
	 * Raise this node.
	 *************************************** 
	 */
	void doRaise();

	/***************************************
	 * Move this node to the coordinate that was specified in {@link #setX(int)}
	 * and {@link #setY(int)} and resize it to the size that was specified in
	 * {@link #setWidth(int)} and {@link #setHeight(int)}.
	 *************************************** 
	 */
	void doMoveResize();

	/***************************************
	 * Resize this node to the size that was specified in {@link #setWidth(int)}
	 * and {@link #setHeight(int)}.
	 *************************************** 
	 */
	void doResize();

	/***************************************
	 * Show this node.
	 *************************************** 
	 */
	void doShow();

	/***************************************
	 * Hide this node.
	 *************************************** 
	 */
	void doHide();

	/***************************************
	 * The node executor that is responsible for correctly executing all
	 * geometry operations of this node.
	 * 
	 * @return a {@link ShellNodeExecutor}.
	 *************************************** 
	 */
	ShellNodeExecutor getShellNodeExecutor();

	/***************************************
	 * Signals if this node is destroyed. A destroyed node should not be able to
	 * process any geometry changes and should be discarded.
	 * 
	 * @return true if destroyed, false if not.
	 *************************************** 
	 */
	boolean isDestroyed();

	/***************************************
	 * Request the closest parent {@link LayoutManager} to lower this node.
	 *************************************** 
	 */
	void requestLower();

	/***************************************
	 * Request the closest parent {@link LayoutManager} to move this node.
	 *************************************** 
	 */
	void requestMove();

	/***************************************
	 * Request the closest parent {@link LayoutManager} to move and resize this
	 * node.
	 *************************************** 
	 */
	void requestMoveResize();

	/***************************************
	 * Request the closest parent {@link LayoutManager} to raise this node.
	 *************************************** 
	 */
	void requestRaise();

	/***************************************
	 * Request the closest parent {@link LayoutManager} to reparent this node.
	 *************************************** 
	 */
	void requestReparent();

	/***************************************
	 * Request the closest parent {@link LayoutManager} to resize this node.
	 *************************************** 
	 */
	void requestResize();

	/***************************************
	 * Request the closest parent {@link LayoutManager} to show this node.
	 *************************************** 
	 */
	void requestShow();

	/***************************************
	 * Request the closest parent {@link LayoutManager} to hide this node.
	 *************************************** 
	 */
	void requestHide();

	/***************************************
	 * Set the desired width for this node. Actual resizing is done either
	 * directly through {@link #doResize()} or {@link #doMoveResize()}, or
	 * indirectly through {@link #requestResize()} or
	 * {@link #requestMoveResize()}.
	 * 
	 * @param height
	 *            the desired width, implementation dependent but usually in
	 *            pixels.
	 *************************************** 
	 */
	void setWidth(final int width);

	/***************************************
	 * Set the desired height for this node. Actual resizing is done either
	 * directly through {@link #doResize()} or {@link #doMoveResize()}, or
	 * indirectly through {@link #requestResize()} or
	 * {@link #requestMoveResize()}.
	 * 
	 * @param height
	 *            the desired height, implementation dependent but usually in
	 *            pixels.
	 *************************************** 
	 */
	void setHeight(final int height);

	/***************************************
	 * Set the desired parent of this node. Actually reparenting is done either
	 * directly through {@link #doReparent()}, or indirectly through
	 * {@link #requestReparent()}.
	 * 
	 * @param parent
	 *            the desired parent.
	 *************************************** 
	 */
	void setParent(final ShellNodeParent parent);

	/***************************************
	 * Set the desired X coordinate for this node. Actual moving is done either
	 * directly through {@link #doMove()} or {@link #doMoveResize()}, or
	 * indirectly through {@link #requestMove()} or {@link #requestMoveResize()}
	 * .
	 * 
	 * @param x
	 *            the desired X coordinate, implementation dependent but usually
	 *            in pixels.
	 *************************************** 
	 */
	void setX(final int x);

	/***************************************
	 * Set the desired Y coordinate for this node. Actual moving is done either
	 * directly through {@link #doMove()} or {@link #doMoveResize()}, or
	 * indirectly through {@link #requestMove()} or {@link #requestMoveResize()}
	 * .
	 * 
	 * @param y
	 *            the desired Y coordinate, implementation dependent but usually
	 *            in pixels.
	 *************************************** 
	 */
	void setY(final int y);

	/**
	 * The direct parent of this node.
	 */
	@Override
	ShellNodeParent getParent();

	/***************************************
	 * Subscribe a node event listener to this node. A Listener will be notified
	 * of this node's geometry changes and requests in the form of a
	 * {@link ShellNodeEvent}.
	 * 
	 * @param shellNodeEventHandler
	 *            A listener.
	 *************************************** 
	 */
	void addShellNodeEventHandler(Object shellNodeEventHandler);

	/***************************************
	 * Unsubscribe a listener that was previously subscribed to this node.
	 * 
	 * @param shellNodeEventHandler
	 *            a previosly subscribed listener.
	 *************************************** 
	 */
	void removeShellNodeEventHandler(Object shellNodeEventHandler);

	/**
	 * The desired transformation of this node. The "0" named properties match
	 * this node's current state, the "1" named properties match this node's
	 * desired state. I call to {@link #setWidth(int)} with a value of 10 will
	 * thus be reflected by the returned
	 * {@link ShellNodeTransformation#getWidth1()} also returning 10.
	 * 
	 * @return A {@link ShellNodeTransformation}.
	 */
	ShellNodeTransformation toGeoTransformation();
}
