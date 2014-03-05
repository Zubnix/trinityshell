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

import com.google.common.eventbus.Subscribe;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.DestroyNotify;
import org.trinity.foundation.api.display.event.GeometryNotify;
import org.trinity.foundation.api.display.event.GeometryRequest;
import org.trinity.foundation.api.display.event.HideNotify;
import org.trinity.foundation.api.display.event.ShowNotify;
import org.trinity.foundation.api.display.event.ShowRequest;
import org.trinity.foundation.api.shared.Listenable;
import org.trinity.shell.api.bindingkey.ShellScene;
import org.trinity.shell.api.scene.AbstractShellNodeParent;
import org.trinity.shell.api.scene.event.ShellNodeMovedResizedEvent;
import org.trinity.shell.api.surface.ShellSurface;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.nativewindow.util.Dimension;
import javax.media.nativewindow.util.Rectangle;

import static com.google.common.base.Preconditions.checkArgument;

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
public final class ShellSurfaceImpl extends AbstractShellNodeParent implements ShellSurface {

	public static final boolean DEFAULT_IS_RESIZABLE = true;
	public static final boolean DEFAULT_IS_MOVABLE   = true;
	public static final int     DEFAULT_MIN_WIDTH    = 4;
	public static final int     DEFAULT_MIN_HEIGHT   = 4;
	public static final int     DEFAULT_MAX_WIDTH    = 16384;
	public static final int     DEFAULT_MAX_HEIGHT   = 16384;
	public static final int     DEFAULT_WIDTH_INC    = 1;
	public static final int     DEFAULT_HEIGHT_INC   = 1;

	private final Dimension minSize         = new Dimension(DEFAULT_MIN_WIDTH,
													 DEFAULT_MIN_HEIGHT);
	private       boolean movable         = DEFAULT_IS_MOVABLE;
	private       boolean resizable       = DEFAULT_IS_RESIZABLE;
	private       Dimension    maxSize         = new Dimension(DEFAULT_MAX_WIDTH,
													 DEFAULT_MAX_HEIGHT);
	private       int     widthIncrement  = DEFAULT_WIDTH_INC;
	private       int     heightIncrement = DEFAULT_HEIGHT_INC;

	private final ShellSurfaceGeometryDelegate shellSurfaceGeometryDelegateImpl;
	private final DisplaySurface               displaySurface;

	// created by a custom factory so inject annotations are not needed.
	ShellSurfaceImpl(@Nonnull //TODO with autofactory
					 final DisplaySurface displaySurface,
					 @Nonnull @ShellScene final Listenable shellScene) {
		super(
				shellScene);
		this.displaySurface = displaySurface;
		this.shellSurfaceGeometryDelegateImpl = new ShellSurfaceGeometryDelegate(this);
		syncGeoToDisplaySurface();
		displaySurface.register(this);
	}

	@Override
	public void doDestroy() {
		getDisplaySurface().destroy();
		super.doDestroy();
	}

	@Override
	public void doShow() {
		getDisplaySurface().show();
		super.doShow();
	}

	@Override
	public void doHide() {
		getDisplaySurface().hide();
		super.doHide();
	}

	@Override
	public Dimension getMaxSize() {
		return this.maxSize;
	}

	@Override
	public void setMaxSize(@Nonnull final Dimension maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public Dimension getMinSize() {
		return this.minSize;
	}

	@Override
	public DisplaySurface getDisplaySurface() {
		return this.displaySurface;
	}

	@Override
	public void setMinSize(@Nonnull final Dimension maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public Integer getWidthIncrement() {
		return this.widthIncrement;
	}

	@Override
	public void setWidthIncrement(final int widthIncrement) {
		checkArgument(widthIncrement > 0);
		this.widthIncrement = widthIncrement;
	}

	@Override
	public void setHeightIncrement(final int heightIncrement) {
		checkArgument(heightIncrement > 0);
		this.heightIncrement = heightIncrement;
	}

	@Override
	public Integer getHeightIncrement() {
		return this.heightIncrement;
	}

	@Override
	public Boolean isMovable() {
		return this.movable;
	}

	@Override
	public Boolean isResizable() {
		return this.resizable;
	}

	@Override
	public void setMovable(final boolean movable) {
		this.movable = movable;
	}

	@Override
	public void setResizable(final boolean isResizable) {
		this.resizable = isResizable;
	}

	protected Dimension normalizedSize(@Nonnull final Dimension newSize) {

		final int newWidth = newSize.getWidth();
		final int newHeight = newSize.getHeight();

		final Dimension minSize = getMinSize();
		final int minWidth = minSize.getWidth();
		final int minHeight = minSize.getHeight();

		final Dimension maxSize = getMaxSize();
        final int maxWidth = maxSize.getWidth();
        final int maxHeight = maxSize.getHeight();

        final Dimension currentSize = getSize();
        final int currentWidth = currentSize.getWidth();
        final int currentHeight = currentSize.getHeight();

        int normalizedWidth = newWidth < minWidth ? minWidth : newWidth > maxWidth ? maxWidth : newWidth;
        normalizedWidth -= (normalizedWidth - currentWidth) % getWidthIncrement();

        int normalizedHeight = newHeight < minHeight ? minHeight : newHeight > maxHeight ? maxHeight : newHeight;
        normalizedHeight -= (normalizedHeight - currentHeight) % getHeightIncrement();

        return new Dimension(normalizedWidth,
                normalizedHeight);
    }

    @Override
    public void setSize(final int width,
                            final int height) {
        if (isResizable()) {
            super.setSize(width,
                    height);
        }
    }

    @Override
    public void setPosition(final int x,
                                final int y) {
        if (isMovable()) {
            super.setPosition(x,
                    y);
        }
    }

    @Override
    public Dimension getDesiredSize() {
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
        final Rectangle currentGeometry = getGeometry();

        setPosition(configureX ? newGeometry.getX() : currentGeometry.getX(),
                configureY ? newGeometry.getY() : currentGeometry.getY());
        setSize(configureWidth ? newGeometry.getWidth() : currentGeometry.getWidth(),
                configureHeight ? newGeometry.getHeight() : currentGeometry.getHeight());
        requestMove();
        requestResize();
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
        setPosition(geometry.getX(),geometry.getY());
        setSize(geometry.getWidth(),geometry.getHeight());

		flushSizePlaceValues();
		final ShellNodeMovedResizedEvent geoEvent = new ShellNodeMovedResizedEvent(	this,
																					   toGeoTransformation());
		post(geoEvent);

		updateChildrenPosition();
		layout();
    }

    @Subscribe
    public void handleDestroyedNotifyEvent(final DestroyNotify destroyNotify){
        super.doDestroy();
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
        doHide();
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
        doShow();
    }

	private void syncGeoToDisplaySurface() {
        final Rectangle displaySurfaceGeo = getDisplaySurface().getGeometry();

							setPosition(displaySurfaceGeo.getX(),displaySurfaceGeo.getY());
							setSize(displaySurfaceGeo.getWidth(),displaySurfaceGeo.getHeight());
							flushSizePlaceValues();
	}

	@Override
	public ShellSurfaceGeometryDelegate getShellNodeGeometryDelegate() {
		return this.shellSurfaceGeometryDelegateImpl;
	}
}