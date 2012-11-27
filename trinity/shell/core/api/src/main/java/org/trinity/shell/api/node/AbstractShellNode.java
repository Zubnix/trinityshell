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

import org.trinity.shell.api.node.event.ShellNodeDestroyedEvent;
import org.trinity.shell.api.node.event.ShellNodeEvent;
import org.trinity.shell.api.node.event.ShellNodeHiddenEvent;
import org.trinity.shell.api.node.event.ShellNodeHideRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeLowerRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeLoweredEvent;
import org.trinity.shell.api.node.event.ShellNodeMoveRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeMoveResizeRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeMovedEvent;
import org.trinity.shell.api.node.event.ShellNodeMovedResizedEvent;
import org.trinity.shell.api.node.event.ShellNodeRaiseRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeRaisedEvent;
import org.trinity.shell.api.node.event.ShellNodeReparentRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeReparentedEvent;
import org.trinity.shell.api.node.event.ShellNodeResizeRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeResizedEvent;
import org.trinity.shell.api.node.event.ShellNodeShowRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeShowedEvent;
import org.trinity.shell.api.node.manager.ShellLayoutManager;

import com.google.common.eventbus.EventBus;

// TODO Let geo events travel downwards to children to notify them that one of
// their parents has changed
// TODO move all layoutmanager checking to AbstractShellNodeParent, as well as
// the javadoc that mentions it.

/***************************************
 * An abstract base implementation of a {@link ShellNode}.
 * 
 *************************************** 
 */
public abstract class AbstractShellNode implements ShellNode {

	private int x;
	private int desiredX;

	private int y;
	private int desiredY;

	private int width;
	private int desiredWidth;

	private int height;
	private int desiredHeight;

	private boolean visible;

	private ShellNodeParent parent;
	private ShellNodeParent desiredParent;

	private boolean destroyed;

	private final EventBus nodeEventBus;

	protected AbstractShellNode(final EventBus nodeEventBus) {
		this.nodeEventBus = nodeEventBus;
		this.nodeEventBus.register(this);
	}

	@Override
	public ShellNodeTransformation toGeoTransformation() {
		return new ShellNodeTransformation(	getX(),
											getY(),
											getWidth(),
											getHeight(),
											getParent(),
											getDesiredX(),
											getDesiredY(),
											getDesiredWidth(),
											getDesiredHeight(),
											getDesiredParent());
	}

	protected EventBus getNodeEventBus() {
		return this.nodeEventBus;
	}

	@Override
	public boolean isDestroyed() {
		return this.destroyed;
	}

	@Override
	public ShellNodeParent getParent() {
		return this.parent;
	}

	@Override
	public void setParent(final ShellNodeParent parent) {
		this.desiredParent = parent;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * A {@link ShellNodeReparentRequestEvent} will be emitted by this node. If
	 * no {@link ShellLayoutManager} is set for this node, {@link #doReparent()}
	 * will take place after all node subscribers are notified of the request.
	 */
	@Override
	public void requestReparent() {
		// update parent to new parent
		final ShellNodeEvent event = new ShellNodeReparentRequestEvent(	this,
																		toGeoTransformation());
		getNodeEventBus().post(event);
		final ShellLayoutManager shellLayoutManager = getParentLayoutManager(getParent());
		if (shellLayoutManager == null) {
			doReparent();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * If this node has no parent, its relative X position is returned.
	 */
	@Override
	public int getAbsoluteX() {
		if ((getParent() == null) || getParent().equals(this)) {
			return getX();
		}
		return getParent().getAbsoluteX() + getX();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * If this node has no parent, its relative Y position will be returned.
	 */
	@Override
	public int getAbsoluteY() {
		if ((getParent() == null) || getParent().equals(this)) {
			return getY();
		}
		return getParent().getAbsoluteY() + getY();
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public void setX(final int x) {
		this.desiredX = x;
	}

	@Override
	public void setY(final int y) {
		this.desiredY = y;
	}

	@Override
	public void setWidth(final int width) {
		this.desiredWidth = width;
	}

	@Override
	public void setHeight(final int height) {
		this.desiredHeight = height;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method will only return true if this node is in the visible state an
	 * all of its parents in the hierarchy are visible as well.
	 */
	@Override
	public boolean isVisible() {
		if (!this.visible) {
			return false;
		}
		// we're in the visible state.

		// recursion safeguard
		if (getParent() == this) {
			return true;
		}

		// check if our parent is visible.
		final boolean parentVisible = (getParent() != null) && getParent().isVisible();
		return parentVisible;
	}

	protected ShellLayoutManager getParentLayoutManager(final ShellNodeParent parent) {
		if (parent == null) {
			return null;
		}

		final ShellLayoutManager parentLayoutManager = parent.getLayoutManager();
		final ShellNodeParent grandParent = parent.getParent();
		if ((parent != grandParent) && (parentLayoutManager == null)) {
			return getParentLayoutManager(parent.getParent());
		}

		return parentLayoutManager;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * A {@link ShellNodeMoveRequestEvent} will be emitted by this node. If no
	 * {@link ShellLayoutManager} is set for this node, {@link #doMove()} will
	 * take place after all node subscribers have been notified of the request.
	 */
	@Override
	public void requestMove() {
		final ShellNodeMoveRequestEvent event = new ShellNodeMoveRequestEvent(	this,
																				toGeoTransformation());
		getNodeEventBus().post(event);
		final ShellLayoutManager shellLayoutManager = getParentLayoutManager(getParent());
		if (shellLayoutManager == null) {
			doMove();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * A {@link ShellNodeResizeRequestEvent} will be emitted by this node. If no
	 * {@link ShellLayoutManager} is set for this node, {@link #doResize()} will
	 * take place after all node subscribers have been notified of the request.
	 */
	@Override
	public void requestResize() {
		final ShellNodeEvent event = new ShellNodeResizeRequestEvent(	this,
																		toGeoTransformation());
		getNodeEventBus().post(event);
		final ShellLayoutManager shellLayoutManager = getParentLayoutManager(getParent());
		if (shellLayoutManager == null) {
			doResize();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * A {@link ShellNodeMovedResizedEvent} will be emitted by this node. If no
	 * {@link ShellLayoutManager} is set for this node, {@link #doMoveResize()}
	 * will take place after all node subscribers have been notified of the
	 * request.
	 */
	@Override
	public void requestMoveResize() {
		final ShellNodeMoveResizeRequestEvent event = new ShellNodeMoveResizeRequestEvent(	this,
																							toGeoTransformation());
		getNodeEventBus().post(event);
		final ShellLayoutManager shellLayoutManager = getParentLayoutManager(getParent());
		if (shellLayoutManager == null) {
			doMoveResize();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * A {@link ShellNodeRaiseRequestEvent} will be emitted by this node. If no
	 * {@link ShellLayoutManager} is set for this node, {@link #doRaise()} will
	 * take place after all node subscribers have been notified of the request.
	 */
	@Override
	public void requestRaise() {
		final ShellNodeEvent event = new ShellNodeRaiseRequestEvent(this,
																	toGeoTransformation());
		getNodeEventBus().post(event);
		final ShellLayoutManager shellLayoutManager = getParentLayoutManager(getParent());
		if (shellLayoutManager == null) {
			doRaise();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * A {@link ShellNodeLowerRequestEvent} will be emitted by this node. If no
	 * {@link ShellLayoutManager} is set for this node, {@link #doLower()} will
	 * take place after all node subscribers have been notified of the request.
	 */
	@Override
	public void requestLower() {
		final ShellNodeLowerRequestEvent event = new ShellNodeLowerRequestEvent(this,
																				toGeoTransformation());
		getNodeEventBus().post(event);
		if (getParentLayoutManager(getParent()) == null) {
			doLower();
		}
	}

	@Override
	public void doMove() {
		doMove(true);
	}

	/***************************************
	 * Move the current node but the actual delegated execution by this node's
	 * {@link ShellNodeExecutor} is conditional. This call will affect the
	 * node's state.
	 * 
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeExecutor}, false to ignore the low level
	 *            execution.
	 *************************************** 
	 */
	protected void doMove(final boolean execute) {
		flushPlaceValues();
		if (execute) {
			execMove();
		}
		final ShellNodeMovedEvent geoEvent = new ShellNodeMovedEvent(	this,
																		toGeoTransformation());
		getNodeEventBus().post(geoEvent);
	}

	/***************************************
	 * Execute the move process by this node's {@link ShellNodeExecutor}. This
	 * call does not affect the node's state.
	 *************************************** 
	 */
	protected void execMove() {
		getShellNodeExecutor().move(getDesiredX(),
									getDesiredY());
	}

	private void flushPlaceValues() {
		this.x = getDesiredX();
		this.y = getDesiredY();
	}

	@Override
	public void doResize() {
		doResize(true);

	}

	/***************************************
	 * Resize the current node but the actual delegated execution by this node's
	 * {@link ShellNodeExecutor} is conditional. This call will affect the
	 * node's state.
	 * 
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeExecutor}, false to ignore the low level
	 *            execution.
	 *************************************** 
	 */
	protected void doResize(final boolean execute) {
		flushSizeValues();
		if (execute) {
			execResize();
		}
		final ShellNodeResizedEvent geoEvent = new ShellNodeResizedEvent(	this,
																			toGeoTransformation());
		getNodeEventBus().post(geoEvent);
	}

	/***************************************
	 * Execute the resize process by this node's {@link ShellNodeExecutor}. This
	 * call does not affect the node's state.
	 *************************************** 
	 */
	protected void execResize() {
		getShellNodeExecutor().resize(	getDesiredWidth(),
										getDesiredHeight());
	}

	private void flushSizeValues() {
		this.width = getDesiredWidth();
		this.height = getDesiredHeight();
	}

	@Override
	public void doMoveResize() {
		doMoveResize(true);

	}

	/***************************************
	 * Move and resize the current node but the actual delegated execution by
	 * this node's {@link ShellNodeExecutor} is conditional. This call will
	 * affect the node's state.
	 * 
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeExecutor}, false to ignore the low level
	 *            execution.
	 *************************************** 
	 */
	protected void doMoveResize(final boolean execute) {
		flushSizePlaceValues();
		if (execute) {
			execMoveResize();
		}
		final ShellNodeMovedResizedEvent geoEvent = new ShellNodeMovedResizedEvent(	this,
																					toGeoTransformation());
		getNodeEventBus().post(geoEvent);
	}

	/***************************************
	 * Execute the move and resize process by this node's
	 * {@link ShellNodeExecutor}. This call does not affect the node's state.
	 *************************************** 
	 */
	protected void execMoveResize() {
		getShellNodeExecutor().moveResize(	getDesiredX(),
											getDesiredY(),
											getDesiredWidth(),
											getDesiredHeight());
	}

	private void flushSizePlaceValues() {
		flushPlaceValues();
		flushSizeValues();
	}

	@Override
	public void doDestroy() {
		this.doDestroy(true);
	}

	/***************************************
	 * Destroy the current node but the actual delegated execution by this
	 * node's {@link ShellNodeExecutor} is conditional. This call will affect
	 * the node's state.
	 * 
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeExecutor}, false to ignore the low level
	 *            execution.
	 *************************************** 
	 */
	protected void doDestroy(final boolean execute) {
		this.destroyed = true;
		if (execute) {
			execDestroy();
		}
		final ShellNodeDestroyedEvent geoEvent = new ShellNodeDestroyedEvent(	this,
																				toGeoTransformation());
		getNodeEventBus().post(geoEvent);
	}

	/***************************************
	 * Execute the destroy process by this node's {@link ShellNodeExecutor}.
	 * This call does not affect the node's state.
	 *************************************** 
	 */
	protected void execDestroy() {
		getShellNodeExecutor().destroy();
	}

	@Override
	public void doRaise() {
		doRaise(true);
	}

	/***************************************
	 * Raise the current node but the actual delegated execution by this node's
	 * {@link ShellNodeExecutor} is conditional. This call will affect the
	 * node's state.
	 * 
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeExecutor}, false to ignore the low level
	 *            execution.
	 *************************************** 
	 */
	protected void doRaise(final boolean execute) {
		if (execute) {
			execRaise();
		}
		final ShellNodeRaisedEvent geoEvent = new ShellNodeRaisedEvent(	this,
																		toGeoTransformation());
		getNodeEventBus().post(geoEvent);
	}

	/***************************************
	 * Execute the raise process by this node's {@link ShellNodeExecutor}. This
	 * call does not affect the node's state.
	 *************************************** 
	 */
	protected void execRaise() {
		getShellNodeExecutor().raise();
	}

	@Override
	public void doLower() {
		doLower(true);
	}

	/***************************************
	 * Lower the current node but the actual delegated execution by this node's
	 * {@link ShellNodeExecutor} is conditional. This call will affect the
	 * node's state.
	 * 
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeExecutor}, false to ignore the low level
	 *            execution.
	 *************************************** 
	 */
	protected void doLower(final boolean execute) {
		if (execute) {
			execLower();
		}
		final ShellNodeLoweredEvent geoEvent = new ShellNodeLoweredEvent(	this,
																			toGeoTransformation());
		getNodeEventBus().post(geoEvent);
	}

	/***************************************
	 * Execute the lower process by this node's {@link ShellNodeExecutor}. This
	 * call does not affect the node's state.
	 *************************************** 
	 */
	protected void execLower() {
		getShellNodeExecutor().lower();
	}

	@Override
	public void doReparent() {
		doReparent(true);
		getParent().handleChildReparentEvent(this);
	}

	/***************************************
	 * Reparent the current node but the actual delegated execution by this
	 * node's {@link ShellNodeExecutor} is conditional. This call will affect
	 * the node's state.
	 * 
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeExecutor}, false to ignore the low level
	 *            execution.
	 *************************************** 
	 */
	protected void doReparent(final boolean execute) {
		flushParentValue();
		if (execute) {
			execReparent();
		}
		// Make sure we have the same size
		// and place in our new parent
		// as in our old parent.
		doMoveResize(execute);
		final ShellNodeReparentedEvent geoEvent = new ShellNodeReparentedEvent(	this,
																				toGeoTransformation());

		getNodeEventBus().post(geoEvent);
	}

	/***************************************
	 * Execute the reparent process by this node's {@link ShellNodeExecutor}.
	 * This call does not affect the node's state.
	 *************************************** 
	 */
	protected void execReparent() {
		getShellNodeExecutor().reparent(getDesiredParent());
	}

	private void flushParentValue() {
		this.parent = getDesiredParent();
	}

	@Override
	public void cancelPendingMove() {
		setWidth(getWidth());
		setHeight(getHeight());
	}

	@Override
	public void cancelPendingResize() {
		setX(getX());
		setY(getY());
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Subscription is done through Google Guava's {@link EventBus}'s
	 * subscription mechanism.
	 */
	@Override
	public void addShellNodeEventHandler(final Object geoEventHandler) {
		getNodeEventBus().register(geoEventHandler);
	}

	@Override
	public void removeShellNodeEventHandler(final Object geoEventHandler) {
		getNodeEventBus().unregister(geoEventHandler);
	}

	/***************************************
	 * The desired height as set by {@link #setHeight(int)}.
	 * 
	 * @return height, implementation dependent but usually in pixels.
	 *************************************** 
	 */
	protected int getDesiredHeight() {
		return this.desiredHeight;
	}

	/***************************************
	 * The desired width as set by {@link #setWidth(int)}.
	 * 
	 * @return width, implementation dependent but usually in pixels.
	 *************************************** 
	 */
	protected int getDesiredWidth() {
		return this.desiredWidth;
	}

	/***************************************
	 * The desired X coordinate as set by {@link #setX(int)}.
	 * 
	 * @return X coordinate, implementation dependent but usually in pixels.
	 *************************************** 
	 */
	protected int getDesiredX() {
		return this.desiredX;
	}

	/***************************************
	 * The desired Y coordinate as set by {@link #setY(int)}.
	 * 
	 * @return Y coordinate, implementation dependent but usually in pixels.
	 *************************************** 
	 */
	protected int getDesiredY() {
		return this.desiredY;
	}

	/***************************************
	 * The desired parent as set by {@link #setParent(ShellNodeParent)}.
	 * 
	 * @return a {@link ShellNodeParent}.
	 *************************************** 
	 */
	protected ShellNodeParent getDesiredParent() {
		return this.desiredParent;
	}

	@Override
	public void doShow() {
		doShow(true);
	}

	/***************************************
	 * Show the current node but the actual delegated execution by this node's
	 * {@link ShellNodeExecutor} is conditional. This call will affect the
	 * node's state.
	 * 
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeExecutor}, false to ignore the low level
	 *            execution.
	 *************************************** 
	 */
	protected void doShow(final boolean execute) {
		this.visible = true;
		if (execute) {
			execShow();
		}
		final ShellNodeShowedEvent geoEvent = new ShellNodeShowedEvent(	this,
																		toGeoTransformation());
		getNodeEventBus().post(geoEvent);
	}

	/***************************************
	 * Execute the show process by this node's {@link ShellNodeExecutor}. This
	 * call does not affect the node's state.
	 *************************************** 
	 */
	protected void execShow() {
		getShellNodeExecutor().show();
	}

	@Override
	public void doHide() {
		doHide(true);
	}

	/***************************************
	 * Hide the current node but the actual delegated execution by this node's
	 * {@link ShellNodeExecutor} is conditional. This call will affect the
	 * node's state.
	 * 
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeExecutor}, false to ignore the low level
	 *            execution.
	 *************************************** 
	 */
	protected void doHide(final boolean execute) {
		this.visible = false;
		if (execute) {
			execHide();
		}
		final ShellNodeHiddenEvent geoEvent = new ShellNodeHiddenEvent(	this,
																		toGeoTransformation());
		getNodeEventBus().post(geoEvent);
	}

	/***************************************
	 * Execute the hide process by this node's {@link ShellNodeExecutor}. This
	 * call does not affect the node's state.
	 *************************************** 
	 */
	protected void execHide() {
		getShellNodeExecutor().hide();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * A {@link ShellNodeShowRequestEvent} will be emitted by this node. If no
	 * {@link ShellLayoutManager} is set for this node, {@link #doShow()} will
	 * take place after all node subscribers have been notified of the request.
	 */
	@Override
	public void requestShow() {
		final ShellNodeShowRequestEvent event = new ShellNodeShowRequestEvent(	this,
																				toGeoTransformation());
		getNodeEventBus().post(event);
		if (getParentLayoutManager(getParent()) == null) {
			doShow();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * A {@link ShellNodeHiddenEvent} will be emitted by this node. If no
	 * {@link ShellLayoutManager} is set for this node, {@link #doHide()} will
	 * take place after all node subscribers have been notified of the request.
	 */
	@Override
	public void requestHide() {
		final ShellNodeHideRequestEvent event = new ShellNodeHideRequestEvent(	this,
																				toGeoTransformation());
		getNodeEventBus().post(event);
		if (getParentLayoutManager(getParent()) == null) {
			doHide();
		}
	}
}