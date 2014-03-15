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
package org.trinity.shell.scene.impl;

import com.google.auto.factory.AutoFactory;
import com.google.common.eventbus.EventBus;
import org.trinity.common.Listenable;
import org.trinity.shell.scene.api.HasSize;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.ShellSurfaceConfigurable;
import org.trinity.shell.scene.api.ShellSurfaceConfiguration;
import org.trinity.shell.scene.api.SpaceBuffer;
import org.trinity.shell.scene.api.event.*;
import org.trinity.shell.scene.api.event.ShellSurfaceResizeRequestEvent;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.nativewindow.util.Dimension;
import javax.media.nativewindow.util.Point;
import javax.media.nativewindow.util.Rectangle;
import javax.media.nativewindow.util.RectangleImmutable;


@NotThreadSafe
@AutoFactory
public class ShellSurfaceDefault extends EventBus implements ShellSurface, ShellSurfaceConfigurable, Listenable {

	@Nonnull
	private RectangleImmutable shape;
	@Nonnull
	private Boolean visible = Boolean.FALSE;
	@Nonnull
	private ShellSurface         parent;
	@Nonnull
	private Boolean              destroyed;
	@Nonnull
	private HasSize<SpaceBuffer> buffer;

	ShellSurfaceDefault(@Nonnull final ShellSurface parent) {
		this.parent = parent;
		this.destroyed = Boolean.FALSE;
	}

	@Override
	public Boolean isDestroyed() {
		return this.destroyed;
	}

	@Override
	public void markDestroyed() {
		this.destroyed = true;
	}

	@Override
	public ShellSurface getParent() {
		return this.parent;
	}

	public void setParent(@Nonnull final ShellSurface parent) {
		this.parent = parent;
	}

	@Override
	public void accept(final ShellSurfaceConfiguration shellSurfaceConfiguration) {
		shellSurfaceConfiguration.configure(this);
	}

	@Override
	public HasSize<SpaceBuffer> getBuffer() {
		return this.buffer;
	}

	@Override
	public RectangleImmutable getShape() {
		return this.shape;
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * This method will only return true if this node is in the visible state an
	 * all of its parents in the hierarchy are visible as well.
	 */
	@Override
	public Boolean isVisible() {
		return this.visible;
	}

	@Override
	public void setVisible(@Nonnull final Boolean visible) {
		this.visible = visible;
	}

	@Override
	public void setShape(final int x,
                         final int y,
                         final int width,
                         final int height) {
		this.shape = new Rectangle(x,
								   y,
								   width,
								   height);
	}

	@Override
	public void attachBuffer(@Nonnull final HasSize<SpaceBuffer> buffer) {
		this.buffer = buffer;
	}

	@Override
	public void requestReparent(@Nonnull final ShellSurface parent) {
		// update parent to new parent
		final ShellNodeEvent event = new ShellNodeReparentRequestEvent(this,
																	   parent);
		post(event);
	}

	@Override
	public void requestMove(final int x,
							final int y) {
		post(new ShellNodeMoveRequestEvent(this,
										   new Point(x,
													 y)));
	}

	@Override
	public void requestResize(@Nonnegative final int width,
							  @Nonnegative final int height) {
		post(new ShellSurfaceResizeRequestEvent(this,
											 new Dimension(width,
														   height)));
	}

	@Override
	public void requestRaise() {
		post(new ShellNodeRaiseRequestEvent(this));
	}

	@Override
	public void requestLower() {
		post(new ShellNodeLowerRequestEvent(this));
	}

	@Override
	public void requestShow() {
		post(new ShellSurfaceShowRequestEvent(this));
	}

	@Override
	public void requestHide() {
		post(new ShellSurfaceHideRequestEvent(this));
	}
}