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

import org.trinity.foundation.api.display.DisplayArea;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.OwnerThread;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.scene.event.ShellNodeHiddenEvent;
import org.trinity.shell.api.scene.event.ShellNodeLowerRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeMoveRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeMoveResizeRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeRaiseRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeReparentRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeResizeRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeShowRequestEvent;

import com.google.common.util.concurrent.ListenableFuture;

// TODO javadoc
/***************************************
 * The super interface of all nodes that live in the shell scene.
 * 
 *************************************** 
 */
@OwnerThread("Shell")
public interface ShellNode extends DisplayArea, AsyncListenable {

	/***************************************
	 * @return
	 *************************************** 
	 */
	ListenableFuture<Rectangle> getGeometry();

	/***************************************
	 * @param width
	 * @param height
	 * @return
	 *************************************** 
	 */
	ListenableFuture<Void> setSize(	int width,
									int height);

	/***************************************
	 * @param size
	 * @return
	 *************************************** 
	 */
	ListenableFuture<Void> setSize(Size size);

	/***************************************
	 * @param x
	 * @param y
	 * @return
	 *************************************** 
	 */
	ListenableFuture<Void> setPosition(	int x,
										int y);

	/***************************************
	 * @param position
	 * @return
	 *************************************** 
	 */
	ListenableFuture<Void> setPosition(Coordinate position);

	/***************************************
	 * @return
	 *************************************** 
	 */
	ListenableFuture<Coordinate> getPosition();

	/***************************************
	 * @return
	 *************************************** 
	 */
	ListenableFuture<Size> getSize();

	/**
	 * Indicates if this ndoe is visible. This is implementation dependent. A
	 * <code>PaintSurfaceNode</code> is usually only visible if it's parent is
	 * visible. So even though this method may return true, the
	 * <code>PaintSurfaceNode</code> will only be physically visible if all it's
	 * parents are physically visible as well.
	 * 
	 * @return future true if visible, future false if not
	 */
	ListenableFuture<Boolean> isVisible();

	/***************************************
	 * Reset any value set by {@link #setX(int)} or {@link #setY(int)} to the
	 * this node's current position.
	 *************************************** 
	 */
	ListenableFuture<Void> cancelPendingMove();

	/***************************************
	 * Reset any value set by {@link #setWidth(int)} and {@link #setHeight(int)}
	 * to this node's current size.
	 *************************************** 
	 */
	ListenableFuture<Void> cancelPendingResize();

	/***************************************
	 * Destroy this node.
	 *************************************** 
	 */
	ListenableFuture<Void> doDestroy();

	/***************************************
	 * Lower this node.
	 *************************************** 
	 */
	ListenableFuture<Void> doLower();

	/***************************************
	 * Reparent this node to the parent that was specified in
	 * {@link #setParent(ShellNodeParent)}.
	 *************************************** 
	 */
	ListenableFuture<Void> doReparent();

	/***************************************
	 * Move this node to the coordinate that was specified in {@link #setX(int)}
	 * and {@link #setY(int)}.
	 *************************************** 
	 */
	ListenableFuture<Void> doMove();

	/***************************************
	 * Raise this node.
	 *************************************** 
	 */
	ListenableFuture<Void> doRaise();

	/***************************************
	 * Move this node to the coordinate that was specified in {@link #setX(int)}
	 * and {@link #setY(int)} and resize it to the size that was specified in
	 * {@link #setWidth(int)} and {@link #setHeight(int)}.
	 *************************************** 
	 */
	ListenableFuture<Void> doMoveResize();

	/***************************************
	 * Resize this node to the size that was specified in {@link #setWidth(int)}
	 * and {@link #setHeight(int)}.
	 *************************************** 
	 */
	ListenableFuture<Void> doResize();

	/***************************************
	 * Show this node.
	 *************************************** 
	 */
	ListenableFuture<Void> doShow();

	/***************************************
	 * Hide this node.
	 *************************************** 
	 */
	ListenableFuture<Void> doHide();

	/***************************************
	 * The node executor that is responsible for correctly executing all
	 * geometry operations of this node.
	 * 
	 * @return a {@link ShellNodeGeometryDelegate}.
	 *************************************** 
	 */
	// TODO should this be part of public api?
	ShellNodeGeometryDelegate getShellNodeExecutor();

	/***************************************
	 * Signals if this node is destroyed. A destroyed node should not be able to
	 * process any geometry changes and should be discarded.
	 * 
	 * @return true if destroyed, false if not.
	 *************************************** 
	 */
	ListenableFuture<Boolean> isDestroyed();

	/***************************************
	 * Request that this node is lowered. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeLowerRequestEvent}. Optionally, responding to the request
	 * can then be done by any of the listeners by calling {@link #doLower()}.
	 * 
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
	 * @see #addShellNodeEventHandler(Object).
	 *************************************** 
	 */
	ListenableFuture<Void> requestHide();

	/***************************************
	 * Set the desired parent of this node. Actually reparenting is done either
	 * directly through {@link #doReparent()}, or indirectly through
	 * {@link #requestReparent()}.
	 * 
	 * @param parent
	 *            the desired parent.
	 *************************************** 
	 */
	ListenableFuture<Void> setParent(final ShellNodeParent parent);

	/**
	 * The direct parent of this node.
	 */
	ListenableFuture<ShellNodeParent> getParent();

	/**
	 * The desired transformation of this node. The "0" named properties match
	 * this node's current state, the "1" named properties match this node's
	 * desired state. A call to {@link #setWidth(int)} with a value of 10 will
	 * thus be reflected by {@link ShellNodeTransformation#getWidth1()}
	 * returning 10.
	 * 
	 * @return A {@link ShellNodeTransformation}.
	 */
	ListenableFuture<ShellNodeTransformation> toGeoTransformation();
}
