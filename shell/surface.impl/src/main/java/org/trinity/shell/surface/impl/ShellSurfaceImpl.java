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
package org.trinity.shell.surface.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.util.concurrent.Futures.addCallback;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.google.common.eventbus.Subscribe;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.DestroyNotify;
import org.trinity.foundation.api.display.event.GeometryNotify;
import org.trinity.foundation.api.display.event.GeometryRequest;
import org.trinity.foundation.api.display.event.HideNotify;
import org.trinity.foundation.api.display.event.ShowNotify;
import org.trinity.foundation.api.display.event.ShowRequest;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.bindingkey.ShellScene;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.assistedinject.Assisted;
import org.trinity.shell.api.surface.AbstractAsyncShellSurface;

// TODO documentation
/**
 * A <code>ShellSurfaceImpl</code> wraps a {@link DisplaySurface} that was
 * created by an independent program. As such the visual content can not be
 * directly manipulated. A <code>ShellSurfaceImpl</code> is the owner of the
 * <code>PlatformRenderArea</code> it wraps. A <code>ShellSurfaceImpl</code>
 * provides functionality to manage and manipulate the geometry and visibility
 * of the <code>PlatformRenderArea</code> it wraps.
 */
@NotThreadSafe
@ExecutionContext(ShellExecutor.class)
public final class ShellSurfaceImpl extends AbstractAsyncShellSurface {

    public static final boolean DEFAULT_IS_RESIZABLE = true;
    public static final boolean DEFAULT_IS_MOVABLE = true;
    public static final int DEFAULT_MIN_WIDTH = 4;
    public static final int DEFAULT_MIN_HEIGHT = 4;
    public static final int DEFAULT_MAX_WIDTH = 16384;
    public static final int DEFAULT_MAX_HEIGHT = 16384;
    public static final int DEFAULT_WIDTH_INC = 1;
    public static final int DEFAULT_HEIGHT_INC = 1;

    private final Size minSize = new Size(	DEFAULT_MIN_WIDTH,
            DEFAULT_MIN_HEIGHT);
    private boolean movable = DEFAULT_IS_MOVABLE;
    private boolean resizable = DEFAULT_IS_RESIZABLE;
    private Size maxSize = new Size(DEFAULT_MAX_WIDTH,
            DEFAULT_MAX_HEIGHT);
    private int widthIncrement = DEFAULT_WIDTH_INC;
    private int heightIncrement = DEFAULT_HEIGHT_INC;

	private final ShellSurfaceGeometryDelegate shellSurfaceGeometryDelegateImpl;
	@Nonnull
	private final ListeningExecutorService shellExecutor;

	// created by a custom factory so inject annotations are not needed.
	ShellSurfaceImpl(	@Nonnull @Assisted final DisplaySurface displaySurface,
						@Nonnull @ShellScene final AsyncListenable shellScene,
						@Nonnull @ShellExecutor final ListeningExecutorService shellExecutor) {
		super(	displaySurface,
				shellScene,
				shellExecutor);
		this.shellExecutor = shellExecutor;
		this.shellSurfaceGeometryDelegateImpl = new ShellSurfaceGeometryDelegate(this);
		syncGeoToDisplaySurface();
        displaySurface.register(this,shellExecutor);
	}

    @Override
    public Void doDestroyImpl() {
        getDisplaySurface().destroy();
        return super.doDestroyImpl();
    }

    @Override
    public Void doShowImpl() {
        getDisplaySurface().show();
        return super.doShowImpl();
    }

    @Override
    public Void doHideImpl() {
        getDisplaySurface().hide();
        return super.doHideImpl();
    }

    @Override
    public Size getMaxSizeImpl() {
        return this.maxSize;
    }

    @Override
    public Void setMaxSizeImpl(@Nonnull final Size maxSize) {
        this.maxSize = maxSize;
        return null;
    }

    @Override
    public Size getMinSizeImpl() {
        return this.minSize;
    }

    @Override
    public Void setMinSizeImpl(@Nonnull final Size maxSize) {
        this.maxSize = maxSize;
        return null;
    }

    @Override
    public Integer getWidthIncrementImpl() {
        return this.widthIncrement;
    }

    @Override
    public Void setWidthIncrementImpl(final int widthIncrement) {
        checkArgument(widthIncrement > 0);
        this.widthIncrement = widthIncrement;
        return null;
    }

    @Override
    public Void setHeightIncrementImpl(final int heightIncrement) {
        checkArgument(heightIncrement > 0);
        this.heightIncrement = heightIncrement;
        return null;
    }

    @Override
    public Integer getHeightIncrementImpl() {
        return this.heightIncrement;
    }

    @Override
    public Boolean isMovableImpl() {
        return this.movable;
    }

    @Override
    public Boolean isResizableImpl() {
        return this.resizable;
    }

    @Override
    public Void setMovableImpl(final boolean movable) {
        this.movable = movable;
        return null;
    }

    @Override
    public Void setResizableImpl(final boolean isResizable) {
        this.resizable = isResizable;
        return null;
    }

    protected Size normalizedSize(@Nonnull final Size newSize) {

        final int newWidth = newSize.getWidth();
        final int newHeight = newSize.getHeight();

        final Size minSize = getMinSizeImpl();
        final int minWidth = minSize.getWidth();
        final int minHeight = minSize.getHeight();

        final Size maxSize = getMaxSizeImpl();
        final int maxWidth = maxSize.getWidth();
        final int maxHeight = maxSize.getHeight();

        final Size currentSize = getSizeImpl();
        final int currentWidth = currentSize.getWidth();
        final int currentHeight = currentSize.getHeight();

        int normalizedWidth = newWidth < minWidth ? minWidth : newWidth > maxWidth ? maxWidth : newWidth;
        normalizedWidth -= (normalizedWidth - currentWidth) % getWidthIncrementImpl();

        int normalizedHeight = newHeight < minHeight ? minHeight : newHeight > maxHeight ? maxHeight : newHeight;
        normalizedHeight -= (normalizedHeight - currentHeight) % getHeightIncrementImpl();

        return new Size(normalizedWidth,
                normalizedHeight);
    }

    @Override
    public Void setSizeImpl(final int width,
                            final int height) {
        if (isResizableImpl()) {
            super.setSizeImpl(	width,
                    height);
        }
        return null;
    }

    @Override
    public Void setPositionImpl(final int x,
                                final int y) {
        if (isMovableImpl()) {
            super.setPositionImpl(	x,
                    y);
        }
        return null;
    }

    @Override
    public Size getDesiredSize() {
        return normalizedSize(super.getDesiredSize());
    }

	/*
	 * start display event handling: should all be performed by the shell
	 * execution context
	 */

    @Subscribe
    public void handleShowRequest(final ShowRequest showRequest) {
        requestShow();
    }

    @Subscribe
    public void handleGeometryRequest(final GeometryRequest geometryRequest) {
        final boolean configureWidth = geometryRequest.configureWidth();
        final boolean configureHeight = geometryRequest.configureHeight();
        final boolean configureX = geometryRequest.configureX();
        final boolean configureY = geometryRequest.configureY();

        final Rectangle newGeometry = geometryRequest.getGeometry();
        final Coordinate newPosition = newGeometry.getPosition();
        final Size newSize = newGeometry.getSize();

        final Rectangle currentGeometry = getGeometryImpl();
        final Coordinate currentPosition = currentGeometry.getPosition();
        final Size currentSize = currentGeometry.getSize();

        setPositionImpl(configureX ? newPosition.getX() : currentPosition.getX(),
                configureY ? newPosition.getY() : currentPosition.getY());
        setSizeImpl(configureWidth ? newSize.getWidth() : currentSize.getWidth(),
                configureHeight ? newSize.getHeight() : currentSize.getHeight());
        requestMoveResizeImpl();
    }

    /**
     * Called when an {@code GeometryNotify} arrives for this surface.
     * <p>
     * This method is called by the display thread.
     *
     * @param geometryNotify
     *            a {@link org.trinity.foundation.api.display.event.GeometryNotify}
     */
    @Subscribe
    public void handleGeometryNotifyEvent(final GeometryNotify geometryNotify) {
        final Rectangle geometry = geometryNotify.getGeometry();
        setPositionImpl(geometry.getPosition());
        setSizeImpl(geometry.getSize());
        doMoveResize(false);
    }

    @Subscribe
    public void handleDestroyedNotifyEvent(final DestroyNotify destroyNotify){
        super.doDestroyImpl();
    }

    /**
     * Called when an {@code HideNotify} arrives for this surface.
     * <p>
     * This method is called by the display thread.
     *
     * @param hideNotify
     *            a {@link org.trinity.foundation.api.display.event.HideNotify}
     */
    @Subscribe
    public void handleHideNotifyEvent(final HideNotify hideNotify) {
        doHideImpl();
    }

    /**
     * Called when an {@code ShowNotifyEventt} arrives for this surface.
     * <p>
     * This method is called by the display thread.
     *
     * @param showNotify
     *            a {@link ShowNotify}
     */
    @Subscribe
    public void handleShowNotifyEvent(final ShowNotify showNotify) {
        doShowImpl();
    }

	private void syncGeoToDisplaySurface() {
		final ListenableFuture<Rectangle> geometryFuture = getDisplaySurface().getGeometry();
		addCallback(geometryFuture,
					new FutureCallback<Rectangle>() {
						@Override
						public void onSuccess(final Rectangle displaySurfaceGeo) {
							setPositionImpl(displaySurfaceGeo.getPosition());
							setSizeImpl(displaySurfaceGeo.getSize());
							flushSizePlaceValues();
						}

						@Override
						public void onFailure(final Throwable t) {

						}
					},
					this.shellExecutor);
	}

	@Override
	public ShellSurfaceGeometryDelegate getShellNodeGeometryDelegate() {
		return this.shellSurfaceGeometryDelegateImpl;
	}

	// repeated for package level visibility
	@Override
	protected void doMoveResize(final boolean execute) {
		super.doMoveResize(execute);
	}
}