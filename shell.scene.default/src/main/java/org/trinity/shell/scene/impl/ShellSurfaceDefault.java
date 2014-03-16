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
import org.trinity.shell.scene.api.*;
import org.trinity.shell.scene.api.BufferSpace;
import org.trinity.shell.scene.api.event.*;
import org.trinity.shell.scene.api.event.ShellSurfaceMoveRequest;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.nativewindow.util.*;


@NotThreadSafe
@AutoFactory(implementing = ShellSurfaceFactory.class,
			 className = "ShellSurfaceDefaultFactory")
public class ShellSurfaceDefault extends EventBus implements ShellSurface, ShellSurfaceConfigurable, Listenable {

	@Nonnull
	private DimensionImmutable size;
    @Nonnull
    private PointImmutable position;
	@Nonnull
	private Boolean visible = Boolean.FALSE;
	@Nonnull
	private ShellSurface         parent;
	@Nonnull
	private Boolean              destroyed;
	@Nonnull
	private HasSize<BufferSpace> buffer;

	ShellSurfaceDefault(@Nonnull final ShellSurface parent,
						final HasSize<BufferSpace> buffer) {
		this.buffer = buffer;
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
    public PointImmutable getPosition() {
        return this.position;
    }

    @Override
	public HasSize<BufferSpace> getBuffer() {
		return this.buffer;
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
    public void setPosition(@Nonnull PointImmutable pointImmutable) {
        this.position = new Point(pointImmutable.getX(),
                                  pointImmutable.getY());
    }

    @Override
    public DimensionImmutable getSize() {
        return this.size;
    }

    @Override
    public void setSize(@Nonnull DimensionImmutable size) {
        this.size = new Dimension(size.getWidth(),
                                  size.getHeight());
    }

    @Override
	public void attachBuffer(@Nonnull final HasSize<BufferSpace> buffer) {
		this.buffer = buffer;
	}

	@Override
	public void requestReparent(@Nonnull final ShellSurface parent) {
		post(new ShellSurfaceReparentRequest(this,
                                             parent));
	}

	@Override
	public void requestMove(final int x,
							final int y) {
		post(new ShellSurfaceMoveRequest(this,
										 new Point(x,
										    	   y)));
	}

	@Override
	public void requestResize(@Nonnegative final int width,
							  @Nonnegative final int height) {
		post(new ShellSurfaceResizeRequest(this,
										   new Dimension(width,
														 height)));
	}

	@Override
	public void requestRaise() {
		post(new ShellSurfaceRaiseRequest(this));
	}

	@Override
	public void requestLower() {
		post(new ShellSurfaceLowerRequest(this));
	}

	@Override
	public void requestShow() {
		post(new ShellSurfaceShowRequest(this));
	}

	@Override
	public void requestHide() {
		post(new ShellSurfaceHideRequest(this));
	}
}