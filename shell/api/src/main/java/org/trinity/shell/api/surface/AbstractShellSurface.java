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
package org.trinity.shell.api.surface;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.util.concurrent.Futures.addCallback;
import static com.google.common.util.concurrent.Futures.transform;

import javax.annotation.concurrent.ThreadSafe;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.DestroyNotify;
import org.trinity.foundation.api.display.event.GeometryNotify;
import org.trinity.foundation.api.display.event.GeometryRequest;
import org.trinity.foundation.api.display.event.HideNotify;
import org.trinity.foundation.api.display.event.ShowNotify;
import org.trinity.foundation.api.display.event.ShowRequest;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.scene.ShellNode;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * An abstract base implementation of {@link ShellSurface}. Implementations that
 * wish to concretely represent an on-screen area are encouraged to extend from
 * <code>AbstractShellSurface</code>.
 * 
 */
@ThreadSafe
public abstract class AbstractShellSurface extends AbstractAsyncShellSurface implements ShellSurface {

	public static final boolean DEFAULT_IS_RESIZABLE = true;
	public static final boolean DEFAULT_IS_MOVABLE = true;
	public static final int DEFAULT_MIN_WIDTH = 4;
	public static final int DEFAULT_MIN_HEIGHT = 4;
	public static final int DEFAULT_MAX_WIDTH = 16384;
	public static final int DEFAULT_MAX_HEIGHT = 16384;
	public static final int DEFAULT_WIDTH_INC = 1;
	public static final int DEFAULT_HEIGHT_INC = 1;

	private boolean movable = DEFAULT_IS_MOVABLE;
	private boolean resizable = DEFAULT_IS_RESIZABLE;

	private final Size minSize = new Size(	DEFAULT_MIN_WIDTH,
											DEFAULT_MIN_HEIGHT);
	private Size maxSize = new Size(DEFAULT_MAX_WIDTH,
									DEFAULT_MAX_HEIGHT);

	private int widthIncrement = DEFAULT_WIDTH_INC;
	private int heightIncrement = DEFAULT_HEIGHT_INC;

	private final ListeningExecutorService shellExecutor;

	/**
	 * Create new <code>AbstractShellSurface</code>
	 */
	@Inject
	protected AbstractShellSurface(@Named("ShellExecutor") final ListeningExecutorService shellExecutor) {
		super(shellExecutor);
		this.shellExecutor = shellExecutor;
	}

	public void subscribeToDisplaySurfaceEvents() {
		addCallback(getDisplaySurface(),
					new FutureCallback<DisplaySurface>() {
						@Override
						public void onSuccess(final DisplaySurface result) {
							result.register(AbstractShellSurface.this,
											AbstractShellSurface.this.shellExecutor);
						}

						@Override
						public void onFailure(final Throwable t) {
							// TODO Auto-generated method stub
							t.printStackTrace();
						}
					},
					this.shellExecutor);
	}

	public void unsubscribeToDisplaySurfaceEvents() {
		addCallback(getDisplaySurface(),
					new FutureCallback<DisplaySurface>() {
						@Override
						public void onSuccess(final DisplaySurface result) {
							result.unregister(AbstractShellSurface.this);
						}

						@Override
						public void onFailure(final Throwable t) {
							// TODO Auto-generated method stub
							t.printStackTrace();
						}
					},
					this.shellExecutor);
	}

	@Override
	public Size getMaxSizeImpl() {
		return this.maxSize;
	}

	@Override
	public Void setMaxSizeImpl(final Size maxSize) {
		this.maxSize = maxSize;
		return null;
	}

	@Override
	public Size getMinSizeImpl() {
		return this.minSize;
	}

	@Override
	public Void setMinSizeImpl(final Size maxSize) {
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

	/**
	 * Indicates if this object can moved. A non movable object can have a new
	 * position set but requests to execute this new position will have no
	 * effect.
	 * 
	 * @return True if movable, false if not.
	 * @see ShellNode#requestMove()
	 * @see ShellNode#requestMoveResize()
	 * @see AbstractShellSurface#isResizable()
	 */
	@Override
	public Boolean isMovableImpl() {
		return this.movable;
	}

	/**
	 * Indicates if this object can resize. A non resizable object can have a
	 * new size set but requests to execute this new size will have no effect.
	 * 
	 * @return True if resizable, false if not.
	 * @see ShellNode#requestMoveResize()
	 * @see ShellNode#requestResize()
	 * @see AbstractShellSurface#isMovable()
	 */
	@Override
	public Boolean isResizableImpl() {
		return this.resizable;
	}

	/**
	 * @param movable
	 *            True if this <code>AbstractShellSurface</code> should be
	 *            movable, false if not.
	 * @see AbstractShellSurface#isMovable()
	 */
	@Override
	public Void setMovableImpl(final boolean movable) {
		this.movable = movable;
		return null;
	}

	/**
	 * @param isResizable
	 *            True if this <code>AbstractShellSurface</code> can be resized,
	 *            false if not.
	 * @see AbstractShellSurface#isResizable()
	 */
	@Override
	public Void setResizableImpl(final boolean isResizable) {
		this.resizable = isResizable;
		return null;
	}

	protected Size normalizedSize(final Size newSize) {

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

		final Size normalizedSize = new Size(	normalizedWidth,
												normalizedHeight);

		return normalizedSize;
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

	/**
	 * Update the geometric information of this
	 * <code>AbstractShellSurface</code> so it reflects the
	 * <code>DisplaySurface</code> returned in {@link #getDisplaySurface()}.
	 * <p>
	 * This method is only useful if this <code>AbstractShellSurface</code> is
	 * the embodiment of it's <code>DisplaySurface</code>. Extending classes
	 * that do not represent their entire <code>DisplaySurface</code> should
	 * override this method so it has no effect. Failure to do so can result in
	 * unexpected behavior.
	 */
	@Override
	public Void syncGeoToDisplaySurfaceImpl() {

		final ListenableFuture<DisplaySurface> displaySurfaceFuture = getDisplaySurface();
		final ListenableFuture<Rectangle> displaySurfaceGeoFuture = transform(	displaySurfaceFuture,
																				new AsyncFunction<DisplaySurface, Rectangle>() {
																					@Override
																					public ListenableFuture<Rectangle> apply(final DisplaySurface input)
																							throws Exception {
																						final ListenableFuture<Rectangle> displaySurfaceGeo = input
																								.getGeometry();
																						return displaySurfaceGeo;
																					}
																				});
		addCallback(displaySurfaceGeoFuture,
					new FutureCallback<Rectangle>() {
						@Override
						public void onSuccess(final Rectangle result) {
							syncGeoGetGeometryCbImpl(result);
						}

						@Override
						public void onFailure(final Throwable t) {
							// TODO how to handle error?
							t.printStackTrace();
						}
					},
					this.shellExecutor);

		return null;
	}

	protected void syncGeoGetGeometryCbImpl(final Rectangle result) {
		// TODO in future version we might want to take a geometry delegate into
		// account to map from and to shell scene geometry and on screen
		// geometry.
		setPositionImpl(result.getPosition());
		setSizeImpl(result.getSize());
		doMoveResize(false);
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
	 * Called when an {@code GeometryRequest} arrives for this surface.
	 * <p>
	 * This method is called by the display thread.
	 * 
	 * @param geometryRequest
	 *            a {@link GeometryRequest}
	 */
	@Subscribe
	public void handleGeometryRequestEvent(final GeometryRequest geometryRequest) {
		final Rectangle currentGeometry = getGeometryImpl();
		final Rectangle requestedGeometry = geometryRequest.getGeometry();
		final int newX = geometryRequest.configureX() ? requestedGeometry.getPosition().getX() : currentGeometry.getPosition().getX();
		final int newY = geometryRequest.configureY() ? requestedGeometry.getPosition().getY() : currentGeometry.getPosition().getY();
		final int newWidth = geometryRequest.configureWidth() ? requestedGeometry.getSize().getWidth() : currentGeometry.getSize().getWidth();
		final int newHeight = geometryRequest.configureHeight() ? requestedGeometry.getSize().getHeight() : currentGeometry.getSize().getHeight();

		if (geometryRequest.configureX() || geometryRequest.configureY()) {
			setPositionImpl(newX,
							newY);
		}
		if (geometryRequest.configureWidth() || geometryRequest.configureHeight()) {
			setSizeImpl(newWidth,
						newHeight);
		}
		requestMoveResizeImpl();
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
		doHide(false);
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
		doShow(false);
	}

	/**
	 * Called when an {@code ShowRequest} arrives for this surface.
	 * <p>
	 * This method is called by the display thread.
	 * 
	 * @param showRequest
	 *            a {@link ShowRequest}
	 */
	@Subscribe
	public void handleShowRequestEvent(final ShowRequest showRequest) {
		requestShowImpl();
	}
	/* end display event handling */
}