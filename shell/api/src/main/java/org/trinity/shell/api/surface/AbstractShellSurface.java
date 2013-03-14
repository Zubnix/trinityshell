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

import javax.annotation.concurrent.ThreadSafe;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.DestroyNotify;
import org.trinity.foundation.api.display.event.GeometryNotify;
import org.trinity.foundation.api.display.event.GeometryRequest;
import org.trinity.foundation.api.display.event.HideNotify;
import org.trinity.foundation.api.display.event.ShowNotify;
import org.trinity.foundation.api.display.event.ShowRequest;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.shell.api.scene.ShellNode;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.FutureCallback;
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

	private boolean movable;
	private boolean resizable;

	private int minWidth;
	private int minHeight;

	private int maxWidth;
	private int maxHeight;

	private int widthIncrement;
	private int heightIncrement;

	private final ListeningExecutorService shellExecutor;

	/**
	 * Create new <code>AbstractShellSurface</code>
	 */
	@Inject
	protected AbstractShellSurface(@Named("ShellExecutor") final ListeningExecutorService shellExecutor) {
		super(shellExecutor);
		this.shellExecutor = shellExecutor;
		initBasics();
	}

	public void subscribeToDisplaySurfaceEvents() {
		addCallback(getDisplaySurface(),
					new FutureCallback<DisplaySurface>() {
						@Override
						public void onSuccess(final DisplaySurface result) {
							result.addListener(AbstractShellSurface.this);
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
							result.removeListener(AbstractShellSurface.this);
						}

						@Override
						public void onFailure(final Throwable t) {
							// TODO Auto-generated method stub
							t.printStackTrace();
						}
					},
					this.shellExecutor);
	}

	/**
	 * Set the default geometric preferences.
	 * <p>
	 * The default geometric preferences are:
	 * <ul>
	 * <li>resizable: {@value #DEFAULT_IS_RESIZABLE}</li>
	 * <li>moveable: {@value #DEFAULT_IS_MOVABLE}</li>
	 * <li>minimum width: {@value #DEFAULT_MIN_WIDTH} pixels</li>
	 * <li>maximum width: {@value #DEFAULT_MAX_WIDTH} pixels</li>
	 * <li>minimum height: {@value #DEFAULT_MIN_HEIGHT} pixels</li>
	 * <li>maximum height: {@value #DEFAULT_MAX_HEIGHT} pixels</li>
	 * </ul>
	 */
	protected void initBasics() {
		setResizableImpl(AbstractShellSurface.DEFAULT_IS_RESIZABLE);
		setMovableImpl(AbstractShellSurface.DEFAULT_IS_MOVABLE);
		setMinWidthImpl(AbstractShellSurface.DEFAULT_MIN_WIDTH);
		setMinHeightImpl(AbstractShellSurface.DEFAULT_MIN_HEIGHT);
		setMaxWidthImpl(AbstractShellSurface.DEFAULT_MAX_WIDTH);
		setMaxHeightImpl(AbstractShellSurface.DEFAULT_MAX_HEIGHT);
		setWidthIncrementImpl(AbstractShellSurface.DEFAULT_WIDTH_INC);
		setHeightIncrementImpl(AbstractShellSurface.DEFAULT_HEIGHT_INC);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The minimum height is guaranteed to be respected. A smaller height than
	 * the minimum height can be requested and executed but will result in the
	 * minimum height being set.
	 * 
	 * @param minHeight
	 *            The desired minimum height in pixels.
	 */
	@Override
	public Void setMinHeightImpl(final int minHeight) {
		this.minHeight = minHeight;
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The minimum width is guaranteed to be respected. A smaller width than the
	 * minimum width can be requested and executed but will result in the
	 * minimum width being set.
	 * 
	 * @param minWidth
	 *            The desired minimum width in pixels.
	 */
	@Override
	public Void setMinWidthImpl(final int minWidth) {
		this.minWidth = minWidth;
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return The minimum width in pixels.
	 * @see AbstractShellSurface#setMinWidth(int)
	 */
	@Override
	public Integer getMinWidthImpl() {
		return this.minWidth;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return The minimum height in pixels.
	 * @see AbstractShellSurface#setMinHeight(int)
	 */
	@Override
	public Integer getMinHeightImpl() {
		return this.minHeight;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The maximum width is guaranteed to be respected. A greater width than the
	 * maximum width can be requested and executed but will result in the
	 * maximum width being set.
	 * 
	 * @param maxWidth
	 *            The desired maxium width in pixels.
	 */
	@Override
	public Void setMaxWidthImpl(final int maxWidth) {
		this.maxWidth = maxWidth;
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The maximum height is guaranteed to be respected. A greater height than
	 * the maximum height can be requested and executed but will result in the
	 * maximum height being set.
	 * 
	 * @param maxHeight
	 *            The desired maximum height in pixels.
	 */
	@Override
	public Void setMaxHeightImpl(final int maxHeight) {
		this.maxHeight = maxHeight;
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see AbstractShellSurface#setMaxHeight(int)
	 * @return The maximum height in pixels.
	 */
	@Override
	public Integer getMaxHeightImpl() {
		return this.maxHeight;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see AbstractShellSurface#setMaxWidth(int)
	 * @return the maximum width in pixels.
	 */
	@Override
	public Integer getMaxWidthImpl() {
		return this.maxWidth;
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

	/**
	 * A new width that lies within the interval of a minimum and maximum width
	 * (included).
	 * 
	 * @param newWidth
	 * @return
	 * @see AbstractShellSurface#normalizedHeight(int)
	 */
	protected int normalizedWidth(final int newWidth) {

		int normalizedWidth = newWidth < getMinWidthImpl() ? getMinWidthImpl()
				: newWidth > getMaxWidthImpl() ? getMaxWidthImpl() : newWidth;

		normalizedWidth -= (normalizedWidth - getGeometryImpl().getWidth()) % getWidthIncrementImpl();

		return normalizedWidth;
	}

	/**
	 * A new height that lies within the interval of a minimum and maximum
	 * height.
	 * 
	 * @param newHeight
	 * @return
	 * @see AbstractShellSurface#normalizedWidth(int)
	 */
	protected int normalizedHeight(final int newHeight) {
		int normalizedHeight = newHeight < getMinHeightImpl() ? getMinHeightImpl()
				: newHeight > getMaxHeightImpl() ? getMaxHeightImpl() : newHeight;

		normalizedHeight -= (normalizedHeight - getGeometryImpl().getHeight()) % getHeightIncrementImpl();

		return normalizedHeight;
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
	public String toString() {
		return String.format(	"%s<%s>|%s|",
								getClass().getSimpleName(),
								getDisplaySurface(),
								getGeometryImpl());
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

		addCallback(getDisplaySurface(),
					new FutureCallback<DisplaySurface>() {
						@Override
						public void onSuccess(final DisplaySurface result) {
							syncGeoGetDisplaySurfaceCbImpl(result);
						}

						@Override
						public void onFailure(final Throwable t) {
							// TODO Auto-generated method stub
							t.printStackTrace();
						}
					},
					this.shellExecutor);
		return null;
	}

	protected void syncGeoGetDisplaySurfaceCbImpl(final DisplaySurface result) {

		// callback executed in same thread, should block (?)
		addCallback(result.getGeometry(),
					new FutureCallback<Rectangle>() {
						@Override
						public void onSuccess(final Rectangle result) {
							syncGeoGetGeometryCbImpl(result);
						}

						@Override
						public void onFailure(final Throwable t) {
							// TODO Auto-generated method stub
							t.printStackTrace();
						}
					},
					this.shellExecutor);
	}

	protected void syncGeoGetGeometryCbImpl(final Rectangle result) {
		// TODO in future version we might want to take a
		// geometry delegate into
		// account to map from and to shell scene geometry
		// and on screen
		// geometry.

		final int x = result.getX();
		final int y = result.getY();
		final int width = result.getWidth();
		final int height = result.getHeight();
		setPositionImpl(x,
						y);
		setSizeImpl(width,
					height);
		doMoveResize(false);
	}

	@Override
	public int getDesiredWidth() {
		return normalizedWidth(super.getDesiredWidth());
	}

	@Override
	public int getDesiredHeight() {
		return normalizedHeight(super.getDesiredHeight());
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
	public final void handleDestroyNotifyEvent(final DestroyNotify destroyNotify) {
		this.shellExecutor.submit(new Runnable() {
			@Override
			public void run() {
				doDestroy(false);
			}
		});
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
	public final void handleGeometryNotifyEvent(final GeometryNotify geometryNotify) {
		this.shellExecutor.submit(new Runnable() {
			@Override
			public void run() {
				final Rectangle geometry = geometryNotify.getGeometry();
				setPositionImpl(geometry.getX(),
								geometry.getY());
				setSizeImpl(geometry.getWidth(),
							geometry.getHeight());
				doMoveResize(false);
			}
		});
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
	public final void handleGeometryRequestEvent(final GeometryRequest geometryRequest) {
		this.shellExecutor.submit(new Runnable() {
			@Override
			public void run() {
				handleGeometryRequestEventImpl(geometryRequest);
			}
		});
	}

	protected void handleGeometryRequestEventImpl(final GeometryRequest geometryRequest) {
		final Rectangle currentGeometry = getGeometryImpl();
		final Rectangle requestedGeometry = geometryRequest.getGeometry();
		final int newX = geometryRequest.configureX() ? requestedGeometry.getX() : currentGeometry.getX();
		final int newY = geometryRequest.configureY() ? requestedGeometry.getY() : currentGeometry.getY();
		final int newWidth = geometryRequest.configureWidth() ? requestedGeometry.getWidth() : currentGeometry
				.getWidth();
		final int newHeight = geometryRequest.configureHeight() ? requestedGeometry.getHeight() : currentGeometry
				.getHeight();

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
	public final void handleHideNotifyEvent(final HideNotify hideNotify) {
		this.shellExecutor.submit(new Runnable() {
			@Override
			public void run() {
				handleHideNotifyEventImpl(hideNotify);
			}
		});
	}

	protected void handleHideNotifyEventImpl(final HideNotify hideNotify) {
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
	public final void handleShowNotifyEvent(final ShowNotify showNotify) {
		this.shellExecutor.submit(new Runnable() {
			@Override
			public void run() {
				handleShowNotifyEventImpl(showNotify);
			}
		});
	}

	protected void handleShowNotifyEventImpl(final ShowNotify showNotify) {
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
		this.shellExecutor.submit(new Runnable() {
			@Override
			public void run() {
				handleShowRequestEventImpl(showRequest);
			}
		});
	}

	protected void handleShowRequestEventImpl(final ShowRequest showRequest) {
		requestShowImpl();
	}
	/* end display event handling */
}