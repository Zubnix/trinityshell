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
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.shell.api.scene.event.ShellNodeEvent;
import org.trinity.shell.api.scene.event.ShellNodeHiddenEvent;
import org.trinity.shell.api.scene.event.ShellNodeLowerRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeMoveRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeMoveResizeRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeRaiseRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeReparentRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeResizeRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeShowRequestEvent;

/***************************************
 * The super interface of all nodes that live in the shell scene.
 * 
 *************************************** 
 */
public interface ShellNode extends DisplayArea, Rectangle {

	/***************************************
	 * The absolute X value of this node.
	 * <p>
	 * The absolute value is the distance, implementation dependent but usually
	 * in pixels, to root parent's position.
	 * 
	 * @return An absolute X coordinate.
	 *************************************** 
	 */
	int getAbsoluteX();

	/***************************************
	 * The absolute Y value of this node.
	 * <p>
	 * The absolute value is the distance, implementation dependent but usually
	 * in pixels, to root parent's position.
	 * 
	 * @return An absolute Y coordinate.
	 *************************************** 
	 */
	int getAbsoluteY();

	/**
	 * Indicates if this ndoe is visible. This is implementation dependent. A
	 * <code>PaintSurfaceNode</code> is usually only visible if it's parent is
	 * visible. So even though this method may return true, the
	 * <code>PaintSurfaceNode</code> will only be physically visible if all it's
	 * parents are physically visible as well.
	 * 
	 * @return true if visible, false if not
	 */
	boolean isVisible();

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
	 * Request that this node is lowered. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeLowerRequestEvent}. Optionally, responding to the request
	 * can then be done by any of the listeners by calling {@link #doLower()}.
	 * 
	 * @see #addShellNodeEventHandler(Object).
	 *************************************** 
	 */
	void requestLower();

	/***************************************
	 * Request that this node is moved. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeMoveRequestEvent}. Optionally, responding to the request
	 * can then be done by any of the listeners by calling {@link #doMove()}.
	 * 
	 * @see #addShellNodeEventHandler(Object).
	 *************************************** 
	 */
	void requestMove();

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
	void requestMoveResize();

	/***************************************
	 * Request that this node is raised. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeRaiseRequestEvent}. Optionally, responding to the request
	 * can then be done by any of the listeners by calling {@link #doRaise()}.
	 * 
	 * @see #addShellNodeEventHandler(Object).
	 *************************************** 
	 */
	void requestRaise();

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
	void requestReparent();

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
	void requestResize();

	/***************************************
	 * Request that this node is shown. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeShowRequestEvent}. Optionally, responding to the request
	 * can then be done by any of the listeners by calling {@link #doShow()}.
	 * 
	 * @see #addShellNodeEventHandler(Object).
	 *************************************** 
	 */
	void requestShow();

	/***************************************
	 * Request that this node is hidden. This will cause any subscribed node
	 * listener, e.g. a layout manager, to receive a
	 * {@link ShellNodeHiddenEvent}. Optionally, responding to the request can
	 * then be done by any of the listeners by calling {@link #doHide()}.
	 * 
	 * @see #addShellNodeEventHandler(Object).
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
	 * desired state. A call to {@link #setWidth(int)} with a value of 10 will
	 * thus be reflected by {@link ShellNodeTransformation#getWidth1()}
	 * returning 10.
	 * 
	 * @return A {@link ShellNodeTransformation}.
	 */
	ShellNodeTransformation toGeoTransformation();
}
