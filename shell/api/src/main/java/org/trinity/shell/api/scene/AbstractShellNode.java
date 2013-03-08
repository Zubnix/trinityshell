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

import java.util.concurrent.Callable;

import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.shell.api.scene.event.ShellNodeDestroyedEvent;
import org.trinity.shell.api.scene.event.ShellNodeEvent;
import org.trinity.shell.api.scene.event.ShellNodeHiddenEvent;
import org.trinity.shell.api.scene.event.ShellNodeHideRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeLowerRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeLoweredEvent;
import org.trinity.shell.api.scene.event.ShellNodeMoveRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeMoveResizeRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeMovedEvent;
import org.trinity.shell.api.scene.event.ShellNodeMovedResizedEvent;
import org.trinity.shell.api.scene.event.ShellNodeRaiseRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeRaisedEvent;
import org.trinity.shell.api.scene.event.ShellNodeReparentRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeReparentedEvent;
import org.trinity.shell.api.scene.event.ShellNodeResizeRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeResizedEvent;
import org.trinity.shell.api.scene.event.ShellNodeShowRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeShowedEvent;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.name.Named;

// TODO Let geo events travel downwards to children to notify them that one of
// their parents has changed?
// TODO move all layoutmanager checking to AbstractShellNodeParent, as well as
// the javadoc that mentions it.

/***************************************
 * An abstract base implementation of a {@link ShellNode}.
 * 
 *************************************** 
 */
// TODO make threadSafe
// TODO split into async & impl part
@NotThreadSafe
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

	private final EventBus nodeEventBus = new EventBus();

	@Inject
	@Named("ShellExecutor")
	protected ListeningExecutorService shellExecutor;

	@Override
	public ListenableFuture<ShellNodeTransformation> toGeoTransformation() {
		return this.shellExecutor.submit(new Callable<ShellNodeTransformation>() {
			@Override
			public ShellNodeTransformation call() {
				return new ShellNodeTransformation(	getXImpl(),
													getYImpl(),
													getWidthImpl(),
													getHeightImpl(),
													getParentImpl(),
													getDesiredXImpl(),
													getDesiredYImpl(),
													getDesiredWidthImpl(),
													getDesiredHeightImpl(),
													getDesiredParentImpl());
			}
		});

	}

	protected EventBus getNodeEventBus() {
		return this.nodeEventBus;
	}

	@Override
	public ListenableFuture<Boolean> isDestroyed() {
		return this.shellExecutor.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() {
				return Boolean.valueOf(AbstractShellNode.this.destroyed);
			}
		});
	}

	@Override
	public ListenableFuture<ShellNodeParent> getParent() {
		return this.parent;
	}

	@Override
	public ListenableFuture<Void> setParent(final ShellNodeParent parent) {
		this.desiredParent = parent;
	}

	@Override
	public ListenableFuture<Void> requestReparent() {
		// update parent to new parent
		final ShellNodeEvent event = new ShellNodeReparentRequestEvent(	this,
																		toGeoTransformation());
		getNodeEventBus().post(event);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * If this node has no parent, its relative X position is returned.
	 */
	@Override
	public ListenableFuture<Integer> getAbsoluteX() {
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
	public ListenableFuture<Integer> getAbsoluteY() {
		if ((getParent() == null) || getParent().equals(this)) {
			return getY();
		}
		return getParent().getAbsoluteY() + getY();
	}

	@Override
	public ListenableFuture<Integer> getHeight() {
		return this.height;
	}

	@Override
	public ListenableFuture<Integer> getX() {
		return this.x;
	}

	@Override
	public ListenableFuture<Integer> getY() {
		return this.y;
	}

	@Override
	public ListenableFuture<Integer> getWidth() {
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
		Preconditions.checkArgument(width > 0,
									"Argument was %s but expected nonzero nonnegative value",
									width);
		this.desiredWidth = width;
	}

	@Override
	public void setHeight(final int height) {
		Preconditions.checkArgument(height > 0,
									"Argument was %s but expected nonzero nonnegative value",
									this.width);
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

	@Override
	public ListenableFuture<Void> requestMove() {
		final ShellNodeMoveRequestEvent event = new ShellNodeMoveRequestEvent(	this,
																				toGeoTransformation());
		getNodeEventBus().post(event);
	}

	@Override
	public ListenableFuture<Void> requestResize() {
		final ShellNodeEvent event = new ShellNodeResizeRequestEvent(	this,
																		toGeoTransformation());
		getNodeEventBus().post(event);
	}

	@Override
	public ListenableFuture<Void> requestMoveResize() {
		final ShellNodeMoveResizeRequestEvent event = new ShellNodeMoveResizeRequestEvent(	this,
																							toGeoTransformation());
		getNodeEventBus().post(event);
	}

	@Override
	public ListenableFuture<Void> requestRaise() {
		final ShellNodeEvent event = new ShellNodeRaiseRequestEvent(this,
																	toGeoTransformation());
		getNodeEventBus().post(event);
	}

	@Override
	public ListenableFuture<Void> requestLower() {
		final ShellNodeLowerRequestEvent event = new ShellNodeLowerRequestEvent(this,
																				toGeoTransformation());
		getNodeEventBus().post(event);
	}

	@Override
	public ListenableFuture<Void> doMove() {
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

	/**
	 * Make the desired position the actual position.
	 */
	protected void flushPlaceValues() {
		this.x = getDesiredX();
		this.y = getDesiredY();
	}

	@Override
	public ListenableFuture<Void> doResize() {
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

	/**
	 * Make the desired dimensions the current dimension.
	 */
	protected void flushSizeValues() {
		this.width = getDesiredWidth();
		this.height = getDesiredHeight();
	}

	@Override
	public ListenableFuture<Void> doMoveResize() {
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

	/**
	 * Make both the desired position and the desired dimension, the current
	 * position and dimension.
	 */
	protected void flushSizePlaceValues() {
		flushPlaceValues();
		flushSizeValues();
	}

	@Override
	public ListenableFuture<Void> doDestroy() {
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
	public ListenableFuture<Void> doLower() {
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
	public ListenableFuture<Void> doReparent() {
		doReparent(true);
		getParent().handleChildReparentEvent(this);
	}

	/***************************************
	 * Reparents the current node but the actual delegated execution by this
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
	public ListenableFuture<Void> cancelPendingMove() {
		setWidth(getWidth());
		setHeight(getHeight());
	}

	@Override
	public ListenableFuture<Void> cancelPendingResize() {
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
	public void addListener(final Object listener) {
		getNodeEventBus().register(listener);
	}

	@Override
	public void removeListener(final Object listener) {
		getNodeEventBus().unregister(listener);

	}

	@Override
	public void post(final Object event) {
		getNodeEventBus().post(event);
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
	public ListenableFuture<Void> doShow() {
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
	public ListenableFuture<Void> doHide() {
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

	@Override
	public ListenableFuture<Void> requestShow() {
		final ShellNodeShowRequestEvent event = new ShellNodeShowRequestEvent(	this,
																				toGeoTransformation());
		getNodeEventBus().post(event);
	}

	@Override
	public ListenableFuture<Void> requestHide() {
		final ShellNodeHideRequestEvent event = new ShellNodeHideRequestEvent(	this,
																				toGeoTransformation());
		getNodeEventBus().post(event);
	}
}