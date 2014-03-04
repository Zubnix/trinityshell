/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.shell.api.scene;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import org.trinity.foundation.api.shared.Listenable;
import org.trinity.shell.api.scene.event.ShellNodeHiddenEvent;
import org.trinity.shell.api.scene.event.ShellNodeLowerRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeMoveRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeRaiseRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeReparentRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeResizeRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeShowRequestEvent;

import javax.media.nativewindow.util.Dimension;
import javax.media.nativewindow.util.Point;
import javax.media.nativewindow.util.Rectangle;

/**
 * ************************************
 * The super interface of all nodes that live in the shell scene.
 * **************************************
 */
public interface ShellNode extends Listenable {

	/**
	 * ************************************
	 * The shell geometry of the node. The relation between the on screen
	 * geometry and the shell geometry is implementation dependent but is
	 * usually in pixels.
	 *
	 * @return a {@link }.
	 * @see #getShellNodeGeometryDelegate()
	 * **************************************
	 */
	Rectangle getGeometry();

	/**
	 * ************************************
	 * Change the node's shell size. The relation between the on screen size and
	 * the shell size is implementation dependent but is usually in pixels.
	 *
	 * @param width  shell width
	 * @param height shell height
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * @see #getShellNodeGeometryDelegate()
	 * @see #getGeometry()
	 * @see #setSize(Dimension)
	 * **************************************
	 */
	void setSize(int width,
				 int height);

	/**
	 * ************************************
	 * Change the node's shell size. The relation between the on screen size and
	 * the shell size is implementation dependent but is usually in pixels.
	 *
	 * @param size a shell {@link Dimension}
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * @see #getShellNodeGeometryDelegate()
	 * @see #setSize(int, int)
	 * **************************************
	 */
	void setSize(Dimension size);

	/**
	 * ************************************
	 * Change the node's shell position. A node's position is relative to its
	 * parent. The relation between the on screen position and the shell
	 * position is implementation dependent but is usually in pixels.
	 *
	 * @param x a shell X position
	 * @param y a shell Y position.
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * @see #getShellNodeGeometryDelegate()
	 * @see #setPosition(Point)
	 * **************************************
	 */
	void setPosition(int x,
					 int y);

	/**
	 * ************************************
	 * Change the node's shell position. A node's position is relative to its
	 * parent. The relation between the on screen position and the shell
	 * position is implementation dependent but is usually in pixels.
	 *
	 * @param position a shell {@link Point}
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * @see #getShellNodeGeometryDelegate()
	 * **************************************
	 */
	void setPosition(Point position);

	/**
	 * ************************************
	 * The shell position of the node. A node's position is relative to its
	 * parent. The relation between the on screen position and the shell
	 * position is implementation dependent but is usually in pixels.
	 *
	 * @return a future {@link Point}.
	 * **************************************
	 */
	Point getPosition();

	/**
	 * ************************************
	 * The shell size of the node. The relation between the on screen size and
	 * the shell size is implementation dependent but is usually in pixels.
	 *
	 * @return a {@link Dimension}.
	 * **************************************
	 */
	Dimension getSize();

	/**
	 * *************************************
	 * Indicates if this node is visible. This is implementation dependent. A
	 * <code>PaintSurfaceNode</code> is usually only visible if it's parent is
	 * visible. So even though this method may return true, the
	 * <code>PaintSurfaceNode</code> will only be physically visible if all it's
	 * parents are physically visible as well.
	 *
	 * @return future true if visible, future false if not
	 * **************************************
	 */
	Boolean isVisible();

	/**
	 * ************************************
	 * Reset any value set by {@link #setPosition(Point)} to this
	 * node's current position.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * **************************************
	 */
	void cancelPendingMove();

	/**
	 * ************************************
	 * Reset any value set by {@link #setSize(int, int)}
	 * to this node's current size.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * **************************************
	 */
	void cancelPendingResize();

	/**
	 * ************************************
	 * Destroy this node and all its children.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * **************************************
	 */
	void doDestroy();

	/**
	 * ************************************
	 * Lower this node.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * **************************************
	 */
	void doLower();

	/**
	 * ************************************
	 * Change the parent of this node to the parent that was specified in
	 * {@link #setParent(Optional)}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * **************************************
	 */
	void doReparent();

	/**
	 * ************************************
	 * Move this node to the coordinate that was specified in
	 * {@link #setPosition(Point)}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * **************************************
	 */
	void doMove();

	/**
	 * ************************************
	 * Raise this node to the top of the stack. A node at the top will be drawn
	 * above all it's siblings.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * **************************************
	 */
	void doRaise();

	/**
	 * ************************************
	 * Resize this node to the size that was specified in {@link #setSize(Dimension)}
	 * .
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * **************************************
	 */
	void doResize();

	/**
	 * ************************************
	 * Show this node.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * **************************************
	 */
	void doShow();

	/**
	 * ************************************
	 * Hide this node.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * **************************************
	 */
	void doHide();

	/**
	 * ************************************
	 * The geometry executor that is responsible for correctly executing all
	 * on-screen geometry operations of this node.
	 *
	 * @return a {@link ShellNodeGeometryDelegate}.
	 * **************************************
	 */
	ShellNodeGeometryDelegate getShellNodeGeometryDelegate();

	/**
	 * ************************************
	 * Signals if this node is destroyed. A destroyed node should not be able to
	 * process any geometry changes and should be discarded.
	 *
	 * @return a future true if destroyed, a future false if not.
	 * **************************************
	 */
	Boolean isDestroyed();

	/**
	 * ************************************
	 * Request that this node is lowered. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeLowerRequestEvent}. Optionally, responding to the request
	 * can then be done by any of the listeners by calling {@link #doLower()}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * **************************************
	 */
	void requestLower();

	/**
	 * ************************************
	 * Request that this node is moved. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeMoveRequestEvent}. Optionally, responding to the request
	 * can then be done by any of the listeners by calling {@link #doMove()}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * **************************************
	 */
	void requestMove();

	/**
	 * ************************************
	 * Request that this node is raised. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeRaiseRequestEvent}. Optionally, responding to the request
	 * can then be done by any of the listeners by calling {@link #doRaise()}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * **************************************
	 */
	void requestRaise();

	/**
	 * ************************************
	 * Request that this node is reparented. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeReparentRequestEvent}. Optionally, responding to the
	 * request can then be done by any of the listeners by calling
	 * {@link #doReparent()}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * **************************************
	 */
	void requestReparent();

	/**
	 * ************************************
	 * Request that this node is resized. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeResizeRequestEvent}. Optionally, responding to the
	 * request can then be done by any of the listeners by calling
	 * {@link #doResize()}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * **************************************
	 */
	void requestResize();

	/**
	 * ************************************
	 * Request that this node is shown. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeShowRequestEvent}. Optionally, responding to the request
	 * can then be done by any of the listeners by calling {@link #doShow()}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * **************************************
	 */
	void requestShow();

	/**
	 * ************************************
	 * Request that this node is hidden. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeHiddenEvent}. Optionally, responding to the request can
	 * then be done by any of the listeners by calling {@link #doHide()}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * **************************************
	 */
	void requestHide();

	/**
	 * ************************************
	 * Set the desired shell parent of this node. Actually reparenting is done
	 * either directly through {@link #doReparent()}, or indirectly through
	 * {@link #requestReparent()}.
	 *
	 * @param parent the desired parent.
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 * done.
	 * **************************************
	 */
	void setParent(final Optional<ShellNodeParent> parent);

	/**
	 * ************************************
	 * The shell parent of this node.
	 *
	 * @return a future {@link ShellNodeParent}.
	 * **************************************
	 */
	Optional<ShellNodeParent> getParent();

	/**
	 * The desired transformation of this node. The "0" named properties match
	 * this node's current state, the "1" named properties match this node's
	 * desired state.
	 *
	 * @return A future {@link ShellNodeTransformation}.
	 */
	ShellNodeTransformation toGeoTransformation();
}
