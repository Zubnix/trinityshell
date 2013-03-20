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

import java.util.concurrent.ExecutorService;

import javax.annotation.concurrent.ThreadSafe;

import org.trinity.foundation.api.shared.AsyncListenableEventBus;
import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.ImmutableRectangle;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.foundation.api.shared.Size;
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
import com.google.common.util.concurrent.ListeningExecutorService;

import static com.google.common.base.Preconditions.checkArgument;

// TODO Let geo events travel downwards to children to notify them that one of
// their parents has changed?

/***************************************
 * An abstract base implementation of a {@link ShellNode}.
 * 
 *************************************** 
 */
@ThreadSafe
public abstract class AbstractShellNode extends AbstractAsyncShellNode implements ShellNode {

	private Coordinate position;
	private Coordinate desiredPosition;

	private Size size;
	private Size desiredSize;

	private boolean visible;

	private AbstractShellNodeParent parent;
	private AbstractShellNodeParent desiredParent;

	private boolean destroyed;

	private final AsyncListenableEventBus nodeEventBus;

	protected AbstractShellNode(final ListeningExecutorService shellExecutor) {
		super(shellExecutor);
		this.nodeEventBus = new AsyncListenableEventBus(shellExecutor);
	}

	@Override
	public ShellNodeTransformation toGeoTransformationImpl() {
		return new ShellNodeTransformation(	getGeometryImpl(),
											getParentImpl(),
											new ImmutableRectangle(	getDesiredPosition(),
																	getDesiredSize()),
											getDesiredParent());
	}

	@Override
	public Boolean isDestroyedImpl() {
		return Boolean.valueOf(AbstractShellNode.this.destroyed);
	}

	@Override
	public AbstractShellNodeParent getParentImpl() {
		return this.parent;
	}

	@Override
	public Void setParentImpl(final ShellNodeParent parent) {
		checkArgument(parent instanceof AbstractShellNodeParent);
		this.desiredParent = (AbstractShellNodeParent) parent;
		return null;
	}

	@Override
	public Void requestReparentImpl() {
		// update parent to new parent
		final ShellNodeEvent event = new ShellNodeReparentRequestEvent(	this,
																		toGeoTransformationImpl());
		post(event);
		return null;
	}

	@Override
	public Rectangle getGeometryImpl() {
		return new ImmutableRectangle(	getPositionImpl(),
										getSizeImpl());
	}

	@Override
	public Void setSizeImpl(final int width,
							final int height) {
		Preconditions.checkArgument(width > 0,
									"Argument was %s but expected nonzero nonnegative value",
									width);
		Preconditions.checkArgument(height > 0,
									"Argument was %s but expected nonzero nonnegative value",
									height);

		this.desiredSize = new Size(width,
									height);

		return null;
	}

	@Override
	public Void setPositionImpl(final int x,
								final int y) {
		this.desiredPosition = new Coordinate(	x,
												y);
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method will only return true if this node is in the visible state an
	 * all of its parents in the hierarchy are visible as well.
	 */
	@Override
	public Boolean isVisibleImpl() {
		if (!this.visible) {
			return false;
		}
		// we're in the visible state.

		// recursion safeguard
		if (getParent() == this) {
			return true;
		}

		// check if our parent is visible.
		final boolean parentVisible = (getParent() != null) && getParentImpl().isVisibleImpl();
		return parentVisible;
	}

	@Override
	public Void requestMoveImpl() {
		final ShellNodeMoveRequestEvent event = new ShellNodeMoveRequestEvent(	this,
																				toGeoTransformationImpl());
		post(event);
		return null;
	}

	@Override
	public Void requestResizeImpl() {
		final ShellNodeEvent event = new ShellNodeResizeRequestEvent(	this,
																		toGeoTransformationImpl());
		post(event);
		return null;
	}

	@Override
	public Void requestMoveResizeImpl() {
		final ShellNodeMoveResizeRequestEvent event = new ShellNodeMoveResizeRequestEvent(	this,
																							toGeoTransformationImpl());
		post(event);
		return null;
	}

	@Override
	public Void requestRaiseImpl() {
		final ShellNodeEvent event = new ShellNodeRaiseRequestEvent(this,
																	toGeoTransformationImpl());
		post(event);
		return null;
	}

	@Override
	public Void requestLowerImpl() {
		final ShellNodeLowerRequestEvent event = new ShellNodeLowerRequestEvent(this,
																				toGeoTransformationImpl());
		post(event);
		return null;
	}

	@Override
	public Void doMoveImpl() {
		doMove(true);
		return null;
	}

	/***************************************
	 * Move the current node but the actual delegated execution by this node's
	 * {@link ShellNodeGeometryDelegate} is conditional. This call will affect
	 * the node's state.
	 * 
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeGeometryDelegate}, false to ignore the low
	 *            level execution.
	 *************************************** 
	 */
	protected void doMove(final boolean execute) {
		flushPlaceValues();
		if (execute) {
			execMove();
		}
		final ShellNodeMovedEvent geoEvent = new ShellNodeMovedEvent(	this,
																		toGeoTransformationImpl());
		post(geoEvent);
	}

	/***************************************
	 * Execute the move process by this node's {@link ShellNodeGeometryDelegate}
	 * . This call does not affect the node's state.
	 *************************************** 
	 */
	public void execMove() {
		getShellNodeExecutor().move(getDesiredPosition());
	}

	/**
	 * Make the desired position the actual position.
	 */
	public void flushPlaceValues() {
		this.position = getDesiredPosition();
	}

	@Override
	public Void doResizeImpl() {
		doResize(true);
		return null;
	}

	/***************************************
	 * Resize the current node but the actual delegated execution by this node's
	 * {@link ShellNodeGeometryDelegate} is conditional. This call will affect
	 * the node's state.
	 * 
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeGeometryDelegate}, false to ignore the low
	 *            level execution.
	 *************************************** 
	 */
	protected void doResize(final boolean execute) {
		flushSizeValues();
		if (execute) {
			execResize();
		}
		final ShellNodeResizedEvent geoEvent = new ShellNodeResizedEvent(	this,
																			toGeoTransformationImpl());
		post(geoEvent);
	}

	/***************************************
	 * Execute the resize process by this node's
	 * {@link ShellNodeGeometryDelegate}. This call does not affect the node's
	 * state.
	 *************************************** 
	 */
	public Void execResize() {
		getShellNodeExecutor().resize(getDesiredSize());
		return null;
	}

	/**
	 * Make the desired dimensions the current dimension.
	 */
	public Void flushSizeValues() {
		this.size = getDesiredSize();
		return null;
	}

	@Override
	public Void doMoveResizeImpl() {
		doMoveResize(true);
		return null;
	}

	/***************************************
	 * Move and resize the current node but the actual delegated execution by
	 * this node's {@link ShellNodeGeometryDelegate} is conditional. This call
	 * will affect the node's state.
	 * 
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeGeometryDelegate}, false to ignore the low
	 *            level execution.
	 *************************************** 
	 */
	protected void doMoveResize(final boolean execute) {
		flushSizePlaceValues();
		if (execute) {
			execMoveResize();
		}
		final ShellNodeMovedResizedEvent geoEvent = new ShellNodeMovedResizedEvent(	this,
																					toGeoTransformationImpl());
		post(geoEvent);
	}

	/***************************************
	 * Execute the move and resize process by this node's
	 * {@link ShellNodeGeometryDelegate}. This call does not affect the node's
	 * state.
	 *************************************** 
	 */
	public Void execMoveResize() {
		getShellNodeExecutor().moveResize(	getDesiredPosition(),
											getDesiredSize());
		return null;
	}

	/**
	 * Make both the desired position and the desired dimension, the current
	 * position and dimension.
	 */
	public Void flushSizePlaceValues() {
		flushPlaceValues();
		flushSizeValues();
		return null;
	}

	@Override
	public Void doDestroyImpl() {
		this.doDestroy(true);
		return null;
	}

	/***************************************
	 * Destroy the current node but the actual delegated execution by this
	 * node's {@link ShellNodeGeometryDelegate} is conditional. This call will
	 * affect the node's state.
	 * 
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeGeometryDelegate}, false to ignore the low
	 *            level execution.
	 *************************************** 
	 */
	protected void doDestroy(final boolean execute) {
		this.destroyed = true;
		if (execute) {
			execDestroy();
		}
		final ShellNodeDestroyedEvent geoEvent = new ShellNodeDestroyedEvent(	this,
																				toGeoTransformationImpl());
		post(geoEvent);
	}

	/***************************************
	 * Execute the destroy process by this node's
	 * {@link ShellNodeGeometryDelegate}. This call does not affect the node's
	 * state.
	 *************************************** 
	 */
	public void execDestroy() {
		getShellNodeExecutor().destroy();
	}

	@Override
	public Void doRaiseImpl() {
		doRaise(true);
		return null;
	}

	/***************************************
	 * Raise the current node but the actual delegated execution by this node's
	 * {@link ShellNodeGeometryDelegate} is conditional. This call will affect
	 * the node's state.
	 * 
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeGeometryDelegate}, false to ignore the low
	 *            level execution.
	 *************************************** 
	 */
	protected void doRaise(final boolean execute) {
		if (execute) {
			execRaise();
		}
		final ShellNodeRaisedEvent geoEvent = new ShellNodeRaisedEvent(	this,
																		toGeoTransformationImpl());
		post(geoEvent);
	}

	/***************************************
	 * Execute the raise process by this node's
	 * {@link ShellNodeGeometryDelegate}. This call does not affect the node's
	 * state.
	 *************************************** 
	 */
	public void execRaise() {
		getShellNodeExecutor().raise();
	}

	@Override
	public Void doLowerImpl() {
		doLower(true);
		return null;
	}

	/***************************************
	 * Lower the current node but the actual delegated execution by this node's
	 * {@link ShellNodeGeometryDelegate} is conditional. This call will affect
	 * the node's state.
	 * 
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeGeometryDelegate}, false to ignore the low
	 *            level execution.
	 *************************************** 
	 */
	protected void doLower(final boolean execute) {
		if (execute) {
			execLower();
		}
		final ShellNodeLoweredEvent geoEvent = new ShellNodeLoweredEvent(	this,
																			toGeoTransformationImpl());
		post(geoEvent);
	}

	/***************************************
	 * Execute the lower process by this node's
	 * {@link ShellNodeGeometryDelegate}. This call does not affect the node's
	 * state.
	 *************************************** 
	 */
	public void execLower() {
		getShellNodeExecutor().lower();
	}

	@Override
	public Void doReparentImpl() {
		doReparent(true);
		getParentImpl().handleChildReparentEvent(this);
		return null;
	}

	/***************************************
	 * Reparents the current node but the actual delegated execution by this
	 * node's {@link ShellNodeGeometryDelegate} is conditional. This call will
	 * affect the node's state.
	 * 
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeGeometryDelegate}, false to ignore the low
	 *            level execution.
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
																				toGeoTransformationImpl());
		post(geoEvent);
	}

	/***************************************
	 * Execute the reparent process by this node's
	 * {@link ShellNodeGeometryDelegate}. This call does not affect the node's
	 * state.
	 *************************************** 
	 */
	public void execReparent() {
		getShellNodeExecutor().reparent(getDesiredParent());
	}

	private void flushParentValue() {
		this.parent = (AbstractShellNodeParent) getDesiredParent();
	}

	@Override
	public Void cancelPendingMoveImpl() {
		setPositionImpl(this.position);
		return null;
	}

	@Override
	public Void cancelPendingResizeImpl() {
		setSizeImpl(this.size);
		return null;
	}

	public Coordinate getDesiredPosition() {
		return this.desiredPosition;
	}

	public Size getDesiredSize() {
		return this.desiredSize;
	}

	/***************************************
	 * The desired parent as set by {@link #setParent(ShellNodeParent)}.
	 * 
	 * @return a {@link ShellNodeParent}.
	 *************************************** 
	 */
	public ShellNodeParent getDesiredParent() {
		return this.desiredParent;
	}

	@Override
	public Void doShowImpl() {
		doShow(true);
		return null;
	}

	/***************************************
	 * Show the current node but the actual delegated execution by this node's
	 * {@link ShellNodeGeometryDelegate} is conditional. This call will affect
	 * the node's state.
	 * 
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeGeometryDelegate}, false to ignore the low
	 *            level execution.
	 *************************************** 
	 */
	protected void doShow(final boolean execute) {
		this.visible = true;
		if (execute) {
			execShow();
		}
		final ShellNodeShowedEvent geoEvent = new ShellNodeShowedEvent(	this,
																		toGeoTransformationImpl());
		post(geoEvent);
	}

	/***************************************
	 * Execute the show process by this node's {@link ShellNodeGeometryDelegate}
	 * . This call does not affect the node's state.
	 *************************************** 
	 */
	public Void execShow() {
		getShellNodeExecutor().show();
		return null;
	}

	@Override
	public Void doHideImpl() {
		doHide(true);
		return null;
	}

	/***************************************
	 * Hide the current node but the actual delegated execution by this node's
	 * {@link ShellNodeGeometryDelegate} is conditional. This call will affect
	 * the node's state.
	 * 
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeGeometryDelegate}, false to ignore the low
	 *            level execution.
	 *************************************** 
	 */
	protected void doHide(final boolean execute) {
		this.visible = false;
		if (execute) {
			execHide();
		}
		final ShellNodeHiddenEvent geoEvent = new ShellNodeHiddenEvent(	this,
																		toGeoTransformationImpl());
		post(geoEvent);
	}

	/***************************************
	 * Execute the hide process by this node's {@link ShellNodeGeometryDelegate}
	 * . This call does not affect the node's state.
	 *************************************** 
	 */
	public void execHide() {
		getShellNodeExecutor().hide();
	}

	@Override
	public Void requestShowImpl() {
		final ShellNodeShowRequestEvent event = new ShellNodeShowRequestEvent(	this,
																				toGeoTransformationImpl());
		post(event);
		return null;
	}

	@Override
	public Void requestHideImpl() {
		final ShellNodeHideRequestEvent event = new ShellNodeHideRequestEvent(	this,
																				toGeoTransformationImpl());
		post(event);
		return null;
	}

	@Override
	public Coordinate getPositionImpl() {
		return this.position;
	}

	@Override
	public Size getSizeImpl() {
		return this.size;
	}

	@Override
	public Void setPositionImpl(final Coordinate position) {
		this.desiredPosition = position;
		return null;
	}

	@Override
	public Void setSizeImpl(final Size size) {
		this.desiredSize = size;
		return null;
	}

	@Override
	public void register(final Object listener) {
		this.nodeEventBus.register(listener);
	}

	@Override
	public void register(	final Object listener,
							final ExecutorService executor) {
		this.nodeEventBus.register(	listener,
									executor);
	}

	@Override
	public void unregister(final Object listener) {
		this.nodeEventBus.unregister(listener);
	}

	@Override
	public void post(final Object event) {
		this.nodeEventBus.post(event);
	}
}