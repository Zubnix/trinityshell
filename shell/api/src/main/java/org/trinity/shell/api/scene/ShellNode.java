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

import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.scene.event.ShellNodeHiddenEvent;
import org.trinity.shell.api.scene.event.ShellNodeLowerRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeMoveRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeMoveResizeRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeRaiseRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeReparentRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeResizeRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeShowRequestEvent;

import com.google.common.util.concurrent.ListenableFuture;

/***************************************
 * The super interface of all nodes that live in the shell scene.
 ***************************************
 */
@ExecutionContext(ShellExecutor.class)
public interface ShellNode extends AsyncListenable {

	/***************************************
	 * The shell geometry of the node. The relation between the on screen
	 * geometry and the shell geometry is implementation dependent but is
	 * usually in pixels.
	 *
	 * @return a future {@link Rectangle}.
	 * @see #getShellNodeGeometryDelegate()
	 ***************************************
	 */
	ListenableFuture<Rectangle> getGeometry();

	/***************************************
	 * Change the node's shell size. The relation between the on screen size and
	 * the shell size is implementation dependent but is usually in pixels.
	 *
	 * @param width
	 *            shell width
	 * @param height
	 *            shell height
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #getShellNodeGeometryDelegate()
	 * @see #getGeometry()
	 * @see #setSize(Size)
	 ***************************************
	 */
	ListenableFuture<Void> setSize(	int width,
									int height);

	/***************************************
	 * Change the node's shell size. The relation between the on screen size and
	 * the shell size is implementation dependent but is usually in pixels.
	 *
	 * @param size
	 *            a shell {@link Size}
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #getShellNodeGeometryDelegate()
	 * @see #setSize(int, int)
	 ***************************************
	 */
	ListenableFuture<Void> setSize(Size size);

	/***************************************
	 * Change the node's shell position. A node's position is relative to its
	 * parent. The relation between the on screen position and the shell
	 * position is implementation dependent but is usually in pixels.
	 *
	 * @param x
	 *            a shell X position
	 * @param y
	 *            a shell Y position.
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #getShellNodeGeometryDelegate()
	 * @see #setPosition(Coordinate)
	 ***************************************
	 */
	ListenableFuture<Void> setPosition(	int x,
										int y);

	/***************************************
	 * Change the node's shell position. A node's position is relative to its
	 * parent. The relation between the on screen position and the shell
	 * position is implementation dependent but is usually in pixels.
	 *
	 * @param position
	 *            a shell {@link Coordinate}
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #getShellNodeGeometryDelegate()
	 ***************************************
	 */
	ListenableFuture<Void> setPosition(Coordinate position);

	/***************************************
	 * The shell position of the node. A node's position is relative to its
	 * parent. The relation between the on screen position and the shell
	 * position is implementation dependent but is usually in pixels.
	 *
	 * @return a future {@link Coordinate}.
	 ***************************************
	 */
	ListenableFuture<Coordinate> getPosition();

	/***************************************
	 * The shell size of the node. The relation between the on screen size and
	 * the shell size is implementation dependent but is usually in pixels.
	 *
	 * @return a future {@link Size}.
	 ***************************************
	 */
	ListenableFuture<Size> getSize();

	/****************************************
	 * Indicates if this node is visible. This is implementation dependent. A
	 * <code>PaintSurfaceNode</code> is usually only visible if it's parent is
	 * visible. So even though this method may return true, the
	 * <code>PaintSurfaceNode</code> will only be physically visible if all it's
	 * parents are physically visible as well.
	 *
	 * @return future true if visible, future false if not
	 ***************************************
	 */
	ListenableFuture<Boolean> isVisible();

	/***************************************
	 * Reset any value set by {@link #setX(int)} or {@link #setY(int)} to this
	 * node's current position.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 ***************************************
	 */
	ListenableFuture<Void> cancelPendingMove();

	/***************************************
	 * Reset any value set by {@link #setWidth(int)} and {@link #setHeight(int)}
	 * to this node's current size.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 ***************************************
	 */
	ListenableFuture<Void> cancelPendingResize();

	/***************************************
	 * Destroy this node and all its children.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 ***************************************
	 */
	ListenableFuture<Void> doDestroy();

	/***************************************
	 * Lower this node.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 ***************************************
	 */
	ListenableFuture<Void> doLower();

	/***************************************
	 * Change the parent of this node to the parent that was specified in
	 * {@link #setParent(ShellNodeParent)}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 ***************************************
	 */
	ListenableFuture<Void> doReparent();

	/***************************************
	 * Move this node to the coordinate that was specified in
	 * {@link #setPosition(Coordinate)}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 ***************************************
	 */
	ListenableFuture<Void> doMove();

	/***************************************
	 * Raise this node to the top of the stack. A node at the top will be drawn
	 * above all it's siblings.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 ***************************************
	 */
	ListenableFuture<Void> doRaise();

	/***************************************
	 * Move this node to the coordinate that was specified in
	 * {@link #setPosition(Coordinate)} and resize it to the size that was
	 * specified in {@link #setSize(Size)}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 ***************************************
	 */
	ListenableFuture<Void> doMoveResize();

	/***************************************
	 * Resize this node to the size that was specified in {@link #setSize(Size)}
	 * .
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 ***************************************
	 */
	ListenableFuture<Void> doResize();

	/***************************************
	 * Show this node.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 ***************************************
	 */
	ListenableFuture<Void> doShow();

	/***************************************
	 * Hide this node.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 ***************************************
	 */
	ListenableFuture<Void> doHide();

	/***************************************
	 * The geometry executor that is responsible for correctly executing all
	 * on-screen geometry operations of this node.
	 *
	 * @return a {@link ShellNodeGeometryDelegate}.
	 ***************************************
	 */
	ShellNodeGeometryDelegate getShellNodeGeometryDelegate();

	/***************************************
	 * Signals if this node is destroyed. A destroyed node should not be able to
	 * process any geometry changes and should be discarded.
	 *
	 * @return a future true if destroyed, a future false if not.
	 ***************************************
	 */
	ListenableFuture<Boolean> isDestroyed();

	/***************************************
	 * Request that this node is lowered. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeLowerRequestEvent}. Optionally, responding to the request
	 * can then be done by any of the listeners by calling {@link #doLower()}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #addShellNodeEventHandler(Object).
	 ***************************************
	 */
	ListenableFuture<Void> requestLower();

	/***************************************
	 * Request that this node is moved. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeMoveRequestEvent}. Optionally, responding to the request
	 * can then be done by any of the listeners by calling {@link #doMove()}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #addShellNodeEventHandler(Object).
	 ***************************************
	 */
	ListenableFuture<Void> requestMove();

	/***************************************
	 * Request that this node is moved and resized. This will cause any
	 * subscribed node listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeMoveResizeRequestEvent}. Optionally, responding to the
	 * request can then be done by any of the listeners by calling
	 * {@link #doMoveResize()}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #addShellNodeEventHandler(Object).
	 ***************************************
	 */
	ListenableFuture<Void> requestMoveResize();

	/***************************************
	 * Request that this node is raised. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeRaiseRequestEvent}. Optionally, responding to the request
	 * can then be done by any of the listeners by calling {@link #doRaise()}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #addShellNodeEventHandler(Object).
	 ***************************************
	 */
	ListenableFuture<Void> requestRaise();

	/***************************************
	 * Request that this node is reparented. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeReparentRequestEvent}. Optionally, responding to the
	 * request can then be done by any of the listeners by calling
	 * {@link #doReparent()}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #addShellNodeEventHandler(Object).
	 ***************************************
	 */
	ListenableFuture<Void> requestReparent();

	/***************************************
	 * Request that this node is resized. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeResizeRequestEvent}. Optionally, responding to the
	 * request can then be done by any of the listeners by calling
	 * {@link #doResize()}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #addShellNodeEventHandler(Object).
	 ***************************************
	 */
	ListenableFuture<Void> requestResize();

	/***************************************
	 * Request that this node is shown. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeShowRequestEvent}. Optionally, responding to the request
	 * can then be done by any of the listeners by calling {@link #doShow()}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #addShellNodeEventHandler(Object).
	 ***************************************
	 */
	ListenableFuture<Void> requestShow();

	/***************************************
	 * Request that this node is hidden. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeHiddenEvent}. Optionally, responding to the request can
	 * then be done by any of the listeners by calling {@link #doHide()}.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #addShellNodeEventHandler(Object).
	 ***************************************
	 */
	ListenableFuture<Void> requestHide();

	/***************************************
	 * Set the desired shell parent of this node. Actually reparenting is done
	 * either directly through {@link #doReparent()}, or indirectly through
	 * {@link #requestReparent()}.
	 *
	 * @param parent
	 *            the desired parent.
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 ***************************************
	 */
	ListenableFuture<Void> setParent(final ShellNodeParent parent);

	/***************************************
	 * The shell parent of this node.
	 *
	 * @return a future {@link ShellNodeParent}.
	 ***************************************
	 */
	ListenableFuture<ShellNodeParent> getParent();

	/**
	 * The desired transformation of this node. The "0" named properties match
	 * this node's current state, the "1" named properties match this node's
	 * desired state.
	 *
	 * @return A future {@link ShellNodeTransformation}.
	 */
	ListenableFuture<ShellNodeTransformation> toGeoTransformation();
}
