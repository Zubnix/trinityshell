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
import org.trinity.common.ListenableEventBus;
import org.trinity.shell.scene.api.ShellNode;
import org.trinity.shell.scene.api.ShellNodeParent;
import org.trinity.shell.scene.api.event.ShellNodeEvent;
import org.trinity.shell.scene.api.event.ShellNodeHideRequestEvent;
import org.trinity.shell.scene.api.event.ShellNodeLowerRequestEvent;
import org.trinity.shell.scene.api.event.ShellNodeMoveRequestEvent;
import org.trinity.shell.scene.api.event.ShellNodeRaiseRequestEvent;
import org.trinity.shell.scene.api.event.ShellNodeReparentRequestEvent;
import org.trinity.shell.scene.api.event.ShellNodeResizeRequestEvent;
import org.trinity.shell.scene.api.event.ShellNodeShowRequestEvent;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.nativewindow.util.Dimension;
import javax.media.nativewindow.util.DimensionImmutable;
import javax.media.nativewindow.util.Point;
import javax.media.nativewindow.util.PointImmutable;
import javax.media.nativewindow.util.Rectangle;
import javax.media.nativewindow.util.RectangleImmutable;


@NotThreadSafe
@AutoFactory
public class ShellNodeImpl extends ListenableEventBus implements ShellNode {

	@Nonnull
	private PointImmutable      position = new Point();
	@Nonnull
	private DimensionImmutable  size    = new Dimension();
	@Nonnull
	private Boolean             visible = Boolean.FALSE;
	@Nonnull
	private ShellNodeParent     parent;
	@Nonnull
	private Boolean             destroyed;

	ShellNodeImpl(@Nonnull final ShellNodeParent parent) {
		this.parent = parent;
	}

	@Override
	public Boolean isDestroyed() {
		return this.destroyed;
	}

    public void setDestroyed(@Nonnull final Boolean destroyed) {
        this.destroyed = destroyed;
    }

    @Override
	public ShellNodeParent getParent() {
		return this.parent;
	}

    public void setParent(@Nonnull final ShellNodeParent parent) {
        this.parent = parent;
    }

	@Override
	public RectangleImmutable getShape() {

		final PointImmutable position = getPosition();
		final DimensionImmutable size = getSize();

		return new Rectangle(position.getX(),
							 position.getY(),
							 size.getWidth(),
							 size.getHeight());
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

    public void setVisible(@Nonnull final Boolean visible) {
        this.visible = visible;
    }

    @Override
    public PointImmutable getPosition() {
        return this.position;
    }

    public void setPosition(@Nonnull final PointImmutable position) {
        this.position = position;
    }

    @Override
    public DimensionImmutable getSize() {
        return this.size;
    }

    public void setSize(@Nonnull final DimensionImmutable size) {
        this.size = size;
    }

    @Override
    public void requestReparent(@Nonnull final ShellNodeParent parent) {
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
		post(new ShellNodeResizeRequestEvent(this,
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
		post(new ShellNodeShowRequestEvent(this));

	}

	@Override
	public void requestHide() {
		post(new ShellNodeHideRequestEvent(this));

	}
}