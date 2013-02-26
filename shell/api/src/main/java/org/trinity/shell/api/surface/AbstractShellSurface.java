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

import org.trinity.foundation.api.display.event.DestroyNotifyEvent;
import org.trinity.foundation.api.display.event.GeometryNotifyEvent;
import org.trinity.foundation.api.display.event.GeometryRequestEvent;
import org.trinity.foundation.api.display.event.HideNotifyEvent;
import org.trinity.foundation.api.display.event.ShowNotifyEvent;
import org.trinity.foundation.api.display.event.ShowRequestEvent;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.shell.api.scene.AbstractShellNode;
import org.trinity.shell.api.scene.ShellNode;

import com.google.common.eventbus.Subscribe;

/**
 * An abstract base implementation of {@link ShellSurface}. Implementations that
 * wish to concretely represent an on-screen area are encouraged to extend from
 * <code>AbstractShellSurface</code>.
 * 
 */
public abstract class AbstractShellSurface extends AbstractShellNode implements
		ShellSurface {

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

	/**
	 * Create new <code>AbstractShellSurface</code>
	 */
	protected AbstractShellSurface() {
		initBasics();
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
		setResizable(AbstractShellSurface.DEFAULT_IS_RESIZABLE);
		setMovable(AbstractShellSurface.DEFAULT_IS_MOVABLE);
		setMinWidth(AbstractShellSurface.DEFAULT_MIN_WIDTH);
		setMinHeight(AbstractShellSurface.DEFAULT_MIN_HEIGHT);
		setMaxWidth(AbstractShellSurface.DEFAULT_MAX_WIDTH);
		setMaxHeight(AbstractShellSurface.DEFAULT_MAX_HEIGHT);
		setWidthIncrement(AbstractShellSurface.DEFAULT_WIDTH_INC);
		setHeightIncrement(AbstractShellSurface.DEFAULT_HEIGHT_INC);
	}

	@Override
	public void setInputFocus() {
		getDisplaySurface().setInputFocus();
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
	public void setMinHeight(final int minHeight) {
		this.minHeight = minHeight;
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
	public void setMinWidth(final int minWidth) {
		this.minWidth = minWidth;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return The minimum width in pixels.
	 * @see AbstractShellSurface#setMinWidth(int)
	 */
	@Override
	public int getMinWidth() {
		return this.minWidth;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return The minimum height in pixels.
	 * @see AbstractShellSurface#setMinHeight(int)
	 */
	@Override
	public int getMinHeight() {
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
	public void setMaxWidth(final int maxWidth) {
		this.maxWidth = maxWidth;
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
	public void setMaxHeight(final int maxHeight) {
		this.maxHeight = maxHeight;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see AbstractShellSurface#setMaxHeight(int)
	 * @return The maximum height in pixels.
	 */
	@Override
	public int getMaxHeight() {
		return this.maxHeight;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see AbstractShellSurface#setMaxWidth(int)
	 * @return the maximum width in pixels.
	 */
	@Override
	public int getMaxWidth() {
		return this.maxWidth;
	}

	@Override
	public int getWidthIncrement() {
		return this.widthIncrement;
	}

	@Override
	public void setWidthIncrement(final int widthIncrement) {
		if (widthIncrement > 0) {
			this.widthIncrement = widthIncrement;
		}
	}

	@Override
	public void setHeightIncrement(final int heightIncrement) {
		if (heightIncrement > 0) {
			this.heightIncrement = heightIncrement;
		}
	}

	@Override
	public int getHeightIncrement() {
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
	public boolean isMovable() {
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
	public boolean isResizable() {
		return this.resizable;
	}

	/**
	 * @param movable
	 *            True if this <code>AbstractShellSurface</code> should be
	 *            movable, false if not.
	 * @see AbstractShellSurface#isMovable()
	 */
	@Override
	public void setMovable(final boolean movable) {
		this.movable = movable;
	}

	/**
	 * @param isResizable
	 *            True if this <code>AbstractShellSurface</code> can be resized,
	 *            false if not.
	 * @see AbstractShellSurface#isResizable()
	 */
	@Override
	public void setResizable(final boolean isResizable) {
		this.resizable = isResizable;
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

		int normalizedWidth = newWidth < getMinWidth() ? getMinWidth()
				: newWidth > getMaxWidth() ? getMaxWidth() : newWidth;

		normalizedWidth -= (normalizedWidth - getWidth()) % getWidthIncrement();

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
		int normalizedHeight = newHeight < getMinHeight() ? getMinHeight()
				: newHeight > getMaxHeight() ? getMaxHeight() : newHeight;

		normalizedHeight -= (normalizedHeight - getHeight())
				% getHeightIncrement();

		return normalizedHeight;
	}

	@Override
	public void setWidth(final int width) {
		if (isResizable()) {
			super.setWidth(width);
		}
	}

	@Override
	public void setHeight(final int height) {
		if (isResizable()) {
			super.setHeight(height);
		}
	}

	@Override
	public void setX(final int x) {
		if (isMovable()) {
			super.setX(x);
		}
	}

	@Override
	public void setY(final int y) {
		if (isMovable()) {
			super.setY(y);
		}
	}

	@Override
	public String toString() {
		return String.format(	"%s<%s>|%d+%d::%dx%d|",
								getClass().getSimpleName(),
								getDisplaySurface(),
								getX(),
								getX(),
								getWidth(),
								getHeight());
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
	public void syncGeoToDisplaySurface() {
		// TODO in future version we might want to take a geometry delegate into
		// account to map from and to shell scene geometry and on screen
		// geometry.

		final Rectangle rectangle = getDisplaySurface().getGeometry();
		setX(rectangle.getX());
		setY(rectangle.getY());

		setWidth(rectangle.getWidth());
		setHeight(rectangle.getHeight());
		doMoveResize(false);
	}

	@Override
	protected int getDesiredWidth() {
		return normalizedWidth(super.getDesiredWidth());
	}

	@Override
	protected int getDesiredHeight() {
		return normalizedHeight(super.getDesiredHeight());
	}

	/* start display event handling: */

	// TODO button input handling?
	// TODO focus handling?
	// TODO key input handling?
	// TODO pointer enter/leave handling?
	// TODO protocol handling?
	// TODO stacking handling?

	/**
	 * Called when an {@code DestroyNotifyEvent} arrives for this surface.
	 * 
	 * @param destroyNotifyEvent
	 *            a {@link DestroyNotifyEvent}
	 */
	@Subscribe
	public
			void
			handleDestroyNotifyEvent(final DestroyNotifyEvent destroyNotifyEvent) {
		doDestroy(false);
	}

	/**
	 * Called when an {@code GeometryNotifyEvent} arrives for this surface.
	 * 
	 * @param geometryNotifyEvent
	 *            a {@link GeometryNotifyEvent}
	 */
	@Subscribe
	public
			void
			handleGeometryNotifyEvent(final GeometryNotifyEvent geometryNotifyEvent) {
		final Rectangle geometry = geometryNotifyEvent.getGeometry();
		setX(geometry.getX());
		setY(geometry.getY());
		setWidth(geometry.getWidth());
		setHeight(geometry.getHeight());
		doMoveResize(false);
	}

	/**
	 * Called when an {@code GeometryRequestEvent} arrives for this surface.
	 * 
	 * @param geometryRequestEvent
	 *            a {@link GeometryRequestEvent}
	 */
	@Subscribe
	public
			void
			handleGeometryRequestEvent(final GeometryRequestEvent geometryRequestEvent) {

		final Rectangle geometry = geometryRequestEvent.getGeometry();
		if (geometryRequestEvent.configureX()) {
			setX(geometry.getX());
		}
		if (geometryRequestEvent.configureY()) {
			setY(geometry.getY());
		}
		if (geometryRequestEvent.configureWidth()) {
			setWidth(geometry.getWidth());
		}
		if (geometryRequestEvent.configureHeight()) {
			setHeight(geometry.getHeight());
		}

		requestMoveResize();
	}

	/**
	 * Called when an {@code HideNotifyEvent} arrives for this surface.
	 * 
	 * @param hideNotifyEvent
	 *            a {@link HideNotifyEvent}
	 */
	@Subscribe
	public void handleHideNotifyEvent(final HideNotifyEvent hideNotifyEvent) {
		doHide(false);
	}

	/**
	 * Called when an {@code ShowNotifyEventt} arrives for this surface.
	 * 
	 * @param showNotifyEvent
	 *            a {@link ShowNotifyEvent}
	 */
	@Subscribe
	public void handleShowNotifyEvent(final ShowNotifyEvent showNotifyEvent) {
		doShow(false);
	}

	/**
	 * Called when an {@code ShowRequestEvent} arrives for this surface.
	 * 
	 * @param showRequestEvent
	 *            a {@link ShowRequestEvent}
	 */
	@Subscribe
	public void handleShowRequestEvent(final ShowRequestEvent showRequestEvent) {
		requestShow();
	}
	/* end display event handling */
}