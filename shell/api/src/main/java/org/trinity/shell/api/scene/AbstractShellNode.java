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
import org.trinity.foundation.api.shared.*;
import org.trinity.foundation.api.shared.Listenable;
import org.trinity.shell.api.bindingkey.ShellScene;
import org.trinity.shell.api.scene.event.*;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.nativewindow.util.Dimension;
import javax.media.nativewindow.util.Point;
import javax.media.nativewindow.util.Rectangle;

import static com.google.common.base.Preconditions.checkArgument;

@NotThreadSafe
public abstract class AbstractShellNode implements ShellNode {

	private final ListenableEventBus nodeEventBus;
	private Point position        = new Point(0,
														   0);
	private Point desiredPosition = new Point(0,
														   0);
	private Dimension       size            = new Dimension(5,
													 5);
	private Dimension       desiredSize     = new Dimension(5,
													 5);
	private boolean visible;
	private Optional<ShellNodeParent> optionalParent        = Optional.absent();
	private Optional<ShellNodeParent> optionalDesiredParent = Optional.absent();
	private boolean destroyed;

	protected AbstractShellNode(@Nonnull @ShellScene final Listenable shellScene) {
		this.nodeEventBus = new ListenableEventBus();

		register(shellScene);
	}

	@Override
	public ShellNodeTransformation toGeoTransformation() {
        final Point desiredPosition = getDesiredPosition();
        final Dimension desiredSize = getDesiredSize();
        return new ShellNodeTransformation(getGeometry(),
										   Optional.fromNullable(getParent().orNull()),
										   new Rectangle(desiredPosition.getX(),desiredPosition.getY(),desiredSize.getWidth(),desiredSize.getHeight()
															),
										   Optional.<ShellNodeParent>fromNullable(getDesiredParent().orNull()));
	}

	@Override
	public Boolean isDestroyed() {
		return this.destroyed;
	}

	@Override
	public Optional<ShellNodeParent> getParent() {
		return Optional.<ShellNodeParent>fromNullable(this.optionalParent.orNull());
	}

	@Override
	public void setParent(final Optional<ShellNodeParent> parent) {

		this.optionalDesiredParent = parent;
	}

	@Override
	public void requestReparent() {
		// update parent to new parent
		final ShellNodeEvent event = new ShellNodeReparentRequestEvent(this,
																	   toGeoTransformation());
		post(event);
	}

	@Override
	public Rectangle getGeometry() {
        final Point position = getPosition();
        final Dimension size = getSize();

        return new Rectangle(position.getX(),position.getY(),size.getWidth(),size.getHeight());
	}

	@Override
	public void setSize(final int width,
						final int height) {
		checkArgument(width > 0,
					  "Argument was %s but expected nonzero nonnegative value",
					  width);
		checkArgument(height > 0,
					  "Argument was %s but expected nonzero nonnegative value",
					  height);

		this.desiredSize = new Dimension(width,
									   height);

	}

	@Override
	public void setPosition(final int x,
								final int y) {
		this.desiredPosition = new Point(x,
												 y);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method will only return true if this node is in the visible state an
	 * all of its parents in the hierarchy are visible as well.
	 */
	@Override
	public Boolean isVisible() {
		if (!this.visible) {
			return false;
		}
		// we're in the visible state.

		// check if our parent is visible.
		if (getParent().isPresent()) {
			return (getParent().get()).isVisible();
		} else {
			return true;
		}
	}

	@Override
	public void requestMove() {
		final ShellNodeMoveRequestEvent event = new ShellNodeMoveRequestEvent(	this,
																				toGeoTransformation());
		post(event);
	}

	@Override
	public void requestResize() {
		final ShellNodeEvent event = new ShellNodeResizeRequestEvent(	this,
																		toGeoTransformation());
		post(event);
	}

	@Override
	public void requestMoveResize() {
		final ShellNodeMoveResizeRequestEvent event = new ShellNodeMoveResizeRequestEvent(	this,
																							toGeoTransformation());
		post(event);
	}

	@Override
	public void requestRaise() {
		final ShellNodeEvent event = new ShellNodeRaiseRequestEvent(this,
																	toGeoTransformation());
		post(event);
	}

	@Override
	public void requestLower() {
		final ShellNodeLowerRequestEvent event = new ShellNodeLowerRequestEvent(this,
																				toGeoTransformation());
		post(event);
	}

	@Override
	public void doMove() {
		flushPlaceValues();
		getShellNodeGeometryDelegate().move(getPosition ());
		final ShellNodeMovedEvent geoEvent = new ShellNodeMovedEvent(this,
																	 toGeoTransformation());
		post(geoEvent);
	}

	/**
	 * Make the desired position the actual position.
	 */
	protected void flushPlaceValues() {
		this.position = getDesiredPosition();
	}

	@Override
	public void doResize() {
		flushSizeValues();
		getShellNodeGeometryDelegate().resize(getSize ());
		final ShellNodeResizedEvent geoEvent = new ShellNodeResizedEvent(	this,
																			 toGeoTransformation());
		post(geoEvent);
	}

	/**
	 * Make the desired dimensions the current dimension.
	 */
	protected void flushSizeValues() {
		this.size = getDesiredSize();
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
	public void doDestroy () {
		this.destroyed = true;
		final ShellNodeDestroyedEvent geoEvent = new ShellNodeDestroyedEvent(	this,
																				toGeoTransformation ());
		post(geoEvent);

	}

	@Override
	public void doRaise () {
		if (getParent ().isPresent()) {
			((AbstractShellNodeParent) getParent ().get()).handleChildStacking(	this,
																					true);
		}
		final ShellNodeRaisedEvent geoEvent = new ShellNodeRaisedEvent(	this,
																		toGeoTransformation ());
		post(geoEvent);

	}

	@Override
	public void doLower () {
		if (getParent ().isPresent()) {
			((AbstractShellNodeParent) getParent ().get()).handleChildStacking(	this,
																					false);
		}
		final ShellNodeLoweredEvent geoEvent = new ShellNodeLoweredEvent(	this,
																			toGeoTransformation ());
		post(geoEvent);

	}

	@Override
	public void doReparent () {
		flushParentValue();
		if (getParent ().isPresent()) {
			((AbstractShellNodeParent) getParent ().get()).handleChildReparent(this);
		}
		final ShellNodeReparentedEvent geoEvent = new ShellNodeReparentedEvent(	this,
																				toGeoTransformation ());
		post(geoEvent);

	}

	protected void flushParentValue() {
		this.optionalParent = getDesiredParent();
	}

	@Override
	public void cancelPendingMove () {
		setPosition(this.position);

	}

	@Override
	public void cancelPendingResize () {
		setSize(this.size);

	}

	public Point getDesiredPosition() {
		return this.desiredPosition;
	}

	public Dimension getDesiredSize() {
		return this.desiredSize;
	}

	/**************************************
	 * The desired parent as set by {@link #setParent(Optional)}.
	 *
	 * @return a {@link ShellNodeParent}.
	 ***************************************
	 */
	public Optional<ShellNodeParent> getDesiredParent() {
		return this.optionalDesiredParent;
	}

	@Override
	public void doShow () {
		this.visible = true;

		final ShellNodeShowedEvent geoEvent = new ShellNodeShowedEvent(	this,
																		toGeoTransformation ());
		post(geoEvent);

	}

	@Override
	public void doHide () {
		this.visible = false;

		final ShellNodeHiddenEvent geoEvent = new ShellNodeHiddenEvent(	this,
																		toGeoTransformation ());
		post(geoEvent);

	}

	@Override
	public void requestShow () {
		final ShellNodeShowRequestEvent event = new ShellNodeShowRequestEvent(	this,
																				toGeoTransformation ());
		post(event);

	}

	@Override
	public void requestHide () {
		final ShellNodeHideRequestEvent event = new ShellNodeHideRequestEvent(	this,
																				toGeoTransformation ());
		post(event);

	}

	@Override
	public Point getPosition () {
		return this.position;
	}

	@Override
	public Dimension getSize () {
		return this.size;
	}

	@Override
	public void setPosition(@Nonnull final Point position) {
		this.desiredPosition = position;

	}

	@Override
	public void setSize(@Nonnull final Dimension size) {
		this.desiredSize = size;

	}

	@Override
	public void register(@Nonnull final Object listener) {
		this.nodeEventBus.register(listener);
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
