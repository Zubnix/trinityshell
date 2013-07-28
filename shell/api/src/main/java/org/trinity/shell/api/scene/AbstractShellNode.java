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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.concurrent.ExecutorService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.AsyncListenableEventBus;
import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.ImmutableRectangle;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.bindingkey.ShellRootNode;
import org.trinity.shell.api.bindingkey.ShellScene;
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

import com.google.common.util.concurrent.ListeningExecutorService;

/**
 * ************************************ An abstract base implementation of a
 * {@link ShellNode}.
 * <p/>
 * **************************************
 */
@NotThreadSafe
@ExecutionContext(ShellExecutor.class)
public abstract class AbstractShellNode extends AbstractAsyncShellNode {

	private final AsyncListenableEventBus nodeEventBus;
	private Coordinate position = new Coordinate(	0,
													0);
	private Coordinate desiredPosition = new Coordinate(0,
														0);
	private Size size = new Size(	5,
									5);
	private Size desiredSize = new Size(5,
										5);
	private boolean visible;
	private AbstractShellNodeParent parent;
	private AbstractShellNodeParent desiredParent;
	private boolean destroyed;

	protected AbstractShellNode(@Nullable @ShellRootNode final ShellNodeParent shellRootNode,
								@Nonnull @ShellScene final AsyncListenable shellScene,
								@Nonnull @ShellExecutor final ListeningExecutorService shellExecutor) {
		super(shellExecutor);
		this.nodeEventBus = new AsyncListenableEventBus(shellExecutor);

		register(shellScene);
		if (shellRootNode != null) {
			setParentImpl(shellRootNode);
			flushParentValue();
		}
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
		checkArgument(	parent instanceof AbstractShellNodeParent,
						"Expected parent %s to be of type %s",
						parent,
						parent.getClass().getName());
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
		checkArgument(	width > 0,
						"Argument was %s but expected nonzero nonnegative value",
						width);
		checkArgument(	height > 0,
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
	 * <p/>
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

	/**
	 * ************************************ Move the current node but the actual
	 * delegated execution by this node's {@link ShellNodeGeometryDelegate} is
	 * conditional. This call will affect the node's state.
	 *
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeGeometryDelegate}, false to ignore the low
	 *            level execution. **************************************
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

	/**
	 * ************************************ Execute the move process by this
	 * node's {@link ShellNodeGeometryDelegate} . This call does not affect the
	 * node's state. **************************************
	 */
	public void execMove() {
		getShellNodeGeometryDelegate().move(getDesiredPosition());
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

	/**
	 * ************************************ Resize the current node but the
	 * actual delegated execution by this node's
	 * {@link ShellNodeGeometryDelegate} is conditional. This call will affect
	 * the node's state.
	 *
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeGeometryDelegate}, false to ignore the low
	 *            level execution. **************************************
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

	/**
	 * ************************************ Execute the resize process by this
	 * node's {@link ShellNodeGeometryDelegate}. This call does not affect the
	 * node's state. **************************************
	 */
	public Void execResize() {
		getShellNodeGeometryDelegate().resize(getDesiredSize());
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

	/**
	 * ************************************ Move and resize the current node but
	 * the actual delegated execution by this node's
	 * {@link ShellNodeGeometryDelegate} is conditional. This call will affect
	 * the node's state.
	 *
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeGeometryDelegate}, false to ignore the low
	 *            level execution. **************************************
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

	/**
	 * ************************************ Execute the move and resize process
	 * by this node's {@link ShellNodeGeometryDelegate}. This call does not
	 * affect the node's state. **************************************
	 */
	public Void execMoveResize() {
		getShellNodeGeometryDelegate().moveResize(	getDesiredPosition(),
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

	/**
	 * ************************************ Destroy the current node but the
	 * actual delegated execution by this node's
	 * {@link ShellNodeGeometryDelegate} is conditional. This call will affect
	 * the node's state.
	 *
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeGeometryDelegate}, false to ignore the low
	 *            level execution. **************************************
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

	/**
	 * ************************************ Execute the destroy process by this
	 * node's {@link ShellNodeGeometryDelegate}. This call does not affect the
	 * node's state. **************************************
	 */
	public void execDestroy() {
		getShellNodeGeometryDelegate().destroy();
	}

	@Override
	public Void doRaiseImpl() {
		doRaise(true);
		return null;
	}

	/**
	 * ************************************ Raise the current node but the
	 * actual delegated execution by this node's
	 * {@link ShellNodeGeometryDelegate} is conditional. This call will affect
	 * the node's state.
	 *
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeGeometryDelegate}, false to ignore the low
	 *            level execution. **************************************
	 */
	protected void doRaise(final boolean execute) {
		if (execute) {
			execRaise();
		}
		getParentImpl().handleChildStacking(this,
											true);
		final ShellNodeRaisedEvent geoEvent = new ShellNodeRaisedEvent(	this,
																		toGeoTransformationImpl());
		post(geoEvent);
	}

	/**
	 * ************************************ Execute the raise process by this
	 * node's {@link ShellNodeGeometryDelegate}. This call does not affect the
	 * node's state. **************************************
	 */
	public void execRaise() {
		getShellNodeGeometryDelegate().raise();
	}

	@Override
	public Void doLowerImpl() {
		doLower(true);
		return null;
	}

	/**
	 * ************************************ Lower the current node but the
	 * actual delegated execution by this node's
	 * {@link ShellNodeGeometryDelegate} is conditional. This call will affect
	 * the node's state.
	 *
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeGeometryDelegate}, false to ignore the low
	 *            level execution. **************************************
	 */
	protected void doLower(final boolean execute) {
		if (execute) {
			execLower();
		}
		getParentImpl().handleChildStacking(this,
											false);
		final ShellNodeLoweredEvent geoEvent = new ShellNodeLoweredEvent(	this,
																			toGeoTransformationImpl());
		post(geoEvent);
	}

	/**
	 * ************************************ Execute the lower process by this
	 * node's {@link ShellNodeGeometryDelegate}. This call does not affect the
	 * node's state. **************************************
	 */
	public void execLower() {
		getShellNodeGeometryDelegate().lower();
	}

	@Override
	public Void doReparentImpl() {
		doReparent(true);
		return null;
	}

	/**
	 * ************************************ Reparents the current node but the
	 * actual delegated execution by this node's
	 * {@link ShellNodeGeometryDelegate} is conditional. This call will affect
	 * the node's state.
	 *
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeGeometryDelegate}, false to ignore the low
	 *            level execution. **************************************
	 */
	protected void doReparent(final boolean execute) {
		flushParentValue();
		if (execute) {
			execReparent();
		}
		getParentImpl().handleChildReparent(this);
		// Make sure we have the same size
		// and place in our new parent
		// as in our old parent.
		doMoveResize(execute);
		final ShellNodeReparentedEvent geoEvent = new ShellNodeReparentedEvent(	this,
																				toGeoTransformationImpl());
		post(geoEvent);
	}

	/**
	 * ************************************ Execute the reparent process by this
	 * node's {@link ShellNodeGeometryDelegate}. This call does not affect the
	 * node's state. **************************************
	 */
	public void execReparent() {
		getShellNodeGeometryDelegate().reparent(getDesiredParent());
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

	/**
	 * ************************************ The desired parent as set by
	 * {@link #setParent(ShellNodeParent)}.
	 *
	 * @return a {@link ShellNodeParent}. **************************************
	 */
	public ShellNodeParent getDesiredParent() {
		return this.desiredParent;
	}

	@Override
	public Void doShowImpl() {
		doShow(true);
		return null;
	}

	/**
	 * ************************************ Show the current node but the actual
	 * delegated execution by this node's {@link ShellNodeGeometryDelegate} is
	 * conditional. This call will affect the node's state.
	 *
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeGeometryDelegate}, false to ignore the low
	 *            level execution. **************************************
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

	/**
	 * ************************************ Execute the show process by this
	 * node's {@link ShellNodeGeometryDelegate} . This call does not affect the
	 * node's state. **************************************
	 */
	public Void execShow() {
		getShellNodeGeometryDelegate().show();
		return null;
	}

	@Override
	public Void doHideImpl() {
		doHide(true);
		return null;
	}

	/**
	 * ************************************ Hide the current node but the actual
	 * delegated execution by this node's {@link ShellNodeGeometryDelegate} is
	 * conditional. This call will affect the node's state.
	 *
	 * @param execute
	 *            True to execute the process by the this node's
	 *            {@link ShellNodeGeometryDelegate}, false to ignore the low
	 *            level execution. **************************************
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

	/**
	 * ************************************ Execute the hide process by this
	 * node's {@link ShellNodeGeometryDelegate} . This call does not affect the
	 * node's state. **************************************
	 */
	public void execHide() {
		getShellNodeGeometryDelegate().hide();
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
	public Void setPositionImpl(@Nonnull final Coordinate position) {
		this.desiredPosition = position;
		return null;
	}

	@Override
	public Void setSizeImpl(@Nonnull final Size size) {
		this.desiredSize = size;
		return null;
	}

	@Override
	public void register(@Nonnull final Object listener) {
		this.nodeEventBus.register(listener);
	}

	@Override
	public void register(	@Nonnull final Object listener,
							@Nonnull final ExecutorService executor) {
		this.nodeEventBus.register(	listener,
									executor);
	}

	@Override
	public void unregister(@Nonnull final Object listener) {
		this.nodeEventBus.unregister(listener);
	}

	@Override
	public void post(@Nonnull final Object event) {
		this.nodeEventBus.post(event);
	}
}
