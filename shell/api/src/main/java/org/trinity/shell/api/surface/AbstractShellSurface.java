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
package org.trinity.shell.api.surface;

import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.display.event.DestroyNotify;
import org.trinity.foundation.api.display.event.GeometryNotify;
import org.trinity.foundation.api.display.event.HideNotify;
import org.trinity.foundation.api.display.event.ShowNotify;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.bindingkey.ShellRootNode;
import org.trinity.shell.api.bindingkey.ShellScene;
import org.trinity.shell.api.scene.ShellNodeParent;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ListeningExecutorService;

/***************************************
 * An abstract base implementation of {@link ShellSurface}. Implementations that
 * wish to concretely represent an on-screen area are encouraged to extend from
 * <code>AbstractShellSurface</code>.
 ***************************************
 */
@ExecutionContext(ShellExecutor.class)
@NotThreadSafe
public abstract class AbstractShellSurface extends AbstractAsyncShellSurface {

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

	protected AbstractShellSurface(@Nullable @ShellRootNode final ShellNodeParent shellRootNode,
                                   @Nonnull @ShellScene final AsyncListenable shellScene,
                                   @Nonnull @ShellExecutor final ListeningExecutorService shellExecutor) {
		super(shellRootNode,
				shellScene,
				shellExecutor);
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

	/* start display event handling: */

	// TODO button input handling?
	// TODO focus handling?
	// TODO key input handling?
	// TODO pointer enter/leave handling?
	// TODO stacking handling?

	/**
	 * Called when an {@code DestroyNotify} arrives for this surface.
	 * <p>
	 * This method is called by the display thread.
	 *
	 * @param destroyNotify
	 *            a {@link DestroyNotify}
	 */
	@Subscribe
	public void handleDestroyNotifyEvent(final DestroyNotify destroyNotify) {
		doDestroy(false);
	}

	/**
	 * Called when an {@code GeometryNotify} arrives for this surface.
	 * <p>
	 * This method is called by the display thread.
	 *
	 * @param geometryNotify
	 *            a {@link GeometryNotify}
	 */
	@Subscribe
	public void handleGeometryNotifyEvent(final GeometryNotify geometryNotify) {
		final Rectangle geometry = geometryNotify.getGeometry();
		setPositionImpl(geometry.getPosition());
		setSizeImpl(geometry.getSize());
		doMoveResize(false);
	}

	/**
	 * Called when an {@code HideNotify} arrives for this surface.
	 * <p>
	 * This method is called by the display thread.
	 *
	 * @param hideNotify
	 *            a {@link HideNotify}
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

	/* end display event handling */
}
