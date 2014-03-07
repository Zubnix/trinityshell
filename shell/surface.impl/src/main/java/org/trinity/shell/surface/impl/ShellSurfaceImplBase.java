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

import com.google.auto.factory.AutoFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.Listenable;
import org.trinity.shell.api.bindingkey.ShellScene;
import org.trinity.shell.api.scene.ShellNodeBase;
import org.trinity.shell.api.surface.ShellSurface;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.nativewindow.util.Dimension;
import javax.media.nativewindow.util.DimensionImmutable;

import static com.google.common.base.Preconditions.checkArgument;

// TODO documentation
/**
 * A <code>ShellSurfaceImplBase</code> wraps a {@link DisplaySurface} that was
 * created by an independent program. As such the visual content can not be
 * directly manipulated. A <code>ShellSurfaceImplBase</code> is the owner of the
 * <code>PlatformRenderArea</code> it wraps. A <code>ShellSurfaceImplBase</code>
 * provides functionality to manage and manipulate the geometry and visibility
 * of the <code>PlatformRenderArea</code> it wraps.
 */
@NotThreadSafe
@AutoFactory
public final class ShellSurfaceImplBase extends ShellNodeBase implements ShellSurface {

	public static final int     DEFAULT_MIN_WIDTH    = 4;
	public static final int     DEFAULT_MIN_HEIGHT   = 4;
	public static final int     DEFAULT_MAX_WIDTH    = 16384;
	public static final int     DEFAULT_MAX_HEIGHT   = 16384;
	public static final int     DEFAULT_WIDTH_INC    = 1;
	public static final int     DEFAULT_HEIGHT_INC   = 1;

	private final DimensionImmutable minSize         = new Dimension(DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);
	private       DimensionImmutable    maxSize         = new Dimension(DEFAULT_MAX_WIDTH, DEFAULT_MAX_HEIGHT);

	private       int     widthIncrement  = DEFAULT_WIDTH_INC;
	private       int     heightIncrement = DEFAULT_HEIGHT_INC;

	private final DisplaySurface               displaySurface;

	// created by a custom factory so inject annotations are not needed.
	ShellSurfaceImplBase(@Nonnull //TODO with autofactory
                         final DisplaySurface displaySurface,
                         @Nonnull @ShellScene final Listenable shellScene) {
		super(
				shellScene);
		this.displaySurface = displaySurface;
	}

	@Override
	public DimensionImmutable getMaxSize() {
		return this.maxSize;
	}

	@Override
	public void setMaxSize(@Nonnull final DimensionImmutable maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public DimensionImmutable getMinSize() {
		return this.minSize;
	}

	@Override
	public DisplaySurface getDisplaySurface() {
		return this.displaySurface;
	}

	@Override
	public void setMinSize(@Nonnull final DimensionImmutable maxSize) {
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

	protected DimensionImmutable normalizedSize(@Nonnull final Dimension newSize) {

		final int newWidth = newSize.getWidth();
		final int newHeight = newSize.getHeight();

		final DimensionImmutable minSize = getMinSize();
		final int minWidth = minSize.getWidth();
		final int minHeight = minSize.getHeight();

		final DimensionImmutable maxSize = getMaxSize();
        final int maxWidth = maxSize.getWidth();
        final int maxHeight = maxSize.getHeight();

        final DimensionImmutable currentSize = getSize();
        final int currentWidth = currentSize.getWidth();
        final int currentHeight = currentSize.getHeight();

        int normalizedWidth = newWidth < minWidth ? minWidth : newWidth > maxWidth ? maxWidth : newWidth;
        normalizedWidth -= (normalizedWidth - currentWidth) % getWidthIncrement();

        int normalizedHeight = newHeight < minHeight ? minHeight : newHeight > maxHeight ? maxHeight : newHeight;
        normalizedHeight -= (normalizedHeight - currentHeight) % getHeightIncrement();

        return new Dimension(normalizedWidth,
                normalizedHeight);
    }
}