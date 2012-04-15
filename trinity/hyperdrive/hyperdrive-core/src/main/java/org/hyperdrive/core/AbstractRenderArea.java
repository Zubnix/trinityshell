/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.core;

import org.hydrogen.api.display.PlatformRenderArea;
import org.hydrogen.api.display.PlatformRenderAreaGeometry;
import org.hydrogen.api.display.Property;
import org.hydrogen.api.display.PropertyInstance;
import org.hydrogen.api.display.event.DestroyNotifyEvent;
import org.hydrogen.api.display.event.DisplayEvent;
import org.hydrogen.api.display.event.DisplayEventType;
import org.hydrogen.api.display.event.PropertyChangedNotifyEvent;
import org.hydrogen.api.event.EventHandler;
import org.hyperdrive.api.core.ManagedDisplay;
import org.hyperdrive.api.core.RenderArea;
import org.hyperdrive.api.core.event.PropertyChangedEvent;
import org.hyperdrive.api.core.event.PropertyChangedHandler;
import org.hyperdrive.api.geo.GeoTransformableRectangle;
import org.hyperdrive.api.geo.GeoTransformation;
import org.hyperdrive.geo.AbstractGeoTransformableRectangle;
import org.hyperdrive.geo.BaseGeoTransformation;
import org.hyperdrive.widget.BaseWidget;

// TODO documentation
// TODO redesign/evaluate input manager integration/method delegation.
/**
 * An <code>AbstractRenderArea</code> provides a basic abstract implementation
 * of an on-screen area. It wraps a {@link PlatformRenderArea} and provides and
 * implements additional basic functionality like state information, minimum,
 * maximum, current and requested dimensions. It is the the most basic
 * implementation of on on-screen area.
 * <p>
 * Classes that wish to concretely represent an on-screen area should extend
 * from <code>AbstractRenderArea</code>. The most important existing ones being
 * {@link ClientWindow} and {@link BaseWidget}.
 * <p>
 * <code>AbstractRenderArea</code> emits {@link DisplayEvent}s that it receives
 * from the {@link ManagedDisplay} it lives on.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 * @see GeoTransformableRectangle
 * @see ManagedDisplay
 * @see ClientWindow
 * @see BaseWidget
 */
public abstract class AbstractRenderArea extends
		AbstractGeoTransformableRectangle implements RenderArea {

	public static final boolean DEFAULT_IS_RESIZABLE = true;
	public static final boolean DEFAULT_IS_MOVABLE = true;
	public static final int DEFAULT_MIN_WIDTH = 4;
	public static final int DEFAULT_MIN_HEIGHT = 4;
	public static final int DEFAULT_MAX_WIDTH = 16384;
	public static final int DEFAULT_MAX_HEIGHT = 16384;
	public static final int DEFAULT_WIDTH_INC = 1;
	public static final int DEFAULT_HEIGHT_INC = 1;

	private ManagedDisplay managedDisplay;
	private PlatformRenderArea platformRenderArea;

	private boolean movable;
	private boolean resizable;

	private int minWidth;
	private int minHeight;

	private int maxWidth;
	private int maxHeight;

	private int widthIncrement;
	private int heightIncrement;

	/**
	 * Create new <code>AbstractRenderArea</code>
	 * 
	 * 
	 */
	protected AbstractRenderArea() {
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
		setResizable(AbstractRenderArea.DEFAULT_IS_RESIZABLE);
		setMovable(AbstractRenderArea.DEFAULT_IS_MOVABLE);
		setMinWidth(AbstractRenderArea.DEFAULT_MIN_WIDTH);
		setMinHeight(AbstractRenderArea.DEFAULT_MIN_HEIGHT);
		setMaxWidth(AbstractRenderArea.DEFAULT_MAX_WIDTH);
		setMaxHeight(AbstractRenderArea.DEFAULT_MAX_HEIGHT);
		setWidthIncrement(AbstractRenderArea.DEFAULT_WIDTH_INC);
		setHeightIncrement(AbstractRenderArea.DEFAULT_HEIGHT_INC);
	}

	@Override
	public void setInputFocus() {
		// TODO delegate to input handler
		getPlatformRenderArea().setInputFocus();
	}

	// TODO implement through input interface? (set on abstractrenderarea &
	// also implement in widget?)
	/**
	 * 
	 * @return
	 * 
	 */
	@Override
	public boolean hasInputFocus() {
		// TODO delegate to input handler
		return getManagedDisplay().getDisplay().getInputFocus()
				.getDisplayResourceHandle().getResourceHandle() == getPlatformRenderArea()
				.getDisplayResourceHandle().getResourceHandle();
	}

	/**
	 * Set the minimum height. The minimum height is guaranteed to be respected.
	 * A smaller height than the minimum height can be requested and executed
	 * but will result in the minimum height being set.
	 * 
	 * @param minHeight
	 *            The desired minimum height in pixels.
	 */
	@Override
	public void setMinHeight(final int minHeight) {
		this.minHeight = minHeight;
	}

	/**
	 * Set the minimum width. The minimum width is guaranteed to be respected. A
	 * smaller width than the minimum width can be requested and executed but
	 * will result in the minimum width being set.
	 * 
	 * @param minWidth
	 *            The desired minimum width in pixels.
	 */
	@Override
	public void setMinWidth(final int minWidth) {
		this.minWidth = minWidth;
	}

	/**
	 * 
	 * @return The minimum width in pixels.
	 * @see AbstractRenderArea#setMinWidth(int)
	 */
	@Override
	public int getMinWidth() {
		return this.minWidth;
	}

	/**
	 * 
	 * @return The minimum height in pixels.
	 * @see AbstractRenderArea#setMinHeight(int)
	 */
	@Override
	public int getMinHeight() {
		return this.minHeight;
	}

	/**
	 * Set the maximum width. The maximum width is guaranteed to be respected. A
	 * greater width than the maximum width can be requested and executed but
	 * will result in the maximum width being set.
	 * 
	 * @param maxWidth
	 *            The desired maxium width in pixels.
	 */
	@Override
	public void setMaxWidth(final int maxWidth) {
		this.maxWidth = maxWidth;
	}

	/**
	 * Set the maximum height. The maximum height is guaranteed to be respected.
	 * A greater height than the maximum height can be requested and executed
	 * but will result in the maximum height being set.
	 * 
	 * @param maxHeight
	 *            The desired maximum height in pixels.
	 */
	@Override
	public void setMaxHeight(final int maxHeight) {
		this.maxHeight = maxHeight;
	}

	/**
	 * @see AbstractRenderArea#setMaxHeight(int)
	 * @return The maximum height in pixels.
	 */
	@Override
	public int getMaxHeight() {
		return this.maxHeight;
	}

	/**
	 * @see AbstractRenderArea#setMaxWidth(int)
	 * @return the maximum width in pixels.
	 */
	@Override
	public int getMaxWidth() {
		return this.maxWidth;
	}

	/**
	 * Set the default <code>EventHandler</code>s that will respond to
	 * <code>Event</code>s that are fired by this object.
	 * <p>
	 * The default <code>EventHandler</code>s will handle the following
	 * <code>Event</code>.<code>Type</code>s:
	 * <ul>
	 * <li>DestroyNotifyEvent.TYPE</li>
	 * <li>PropertyChangedNotifyEvent.TYPE</li>
	 * </ul>
	 */
	protected void initEventHandlers() {
		// TODO use typed event handlers.
		addEventHandler(new EventHandler<DestroyNotifyEvent>() {
			@Override
			public void handleEvent(final DestroyNotifyEvent event) {
				AbstractRenderArea.this.handleDestroyNotify(event);
			}
		}, DisplayEventType.DESTROY_NOTIFY);

		addEventHandler(new EventHandler<PropertyChangedNotifyEvent>() {
			@Override
			public void handleEvent(final PropertyChangedNotifyEvent event) {
				final BasePropertyChangedEvent<Property<? extends PropertyInstance>> renderAreaPropertyChangedEvent = new BasePropertyChangedEvent<Property<? extends PropertyInstance>>(
						AbstractRenderArea.this, event.isPropertyDeleted(),
						event.getChangedProperty());
				fireEvent(renderAreaPropertyChangedEvent);
			}
		}, DisplayEventType.PROPERTY_CHANGED);
	}

	/**
	 * 
	 * @param event
	 * 
	 */
	protected void handleDestroyNotify(final DestroyNotifyEvent event) {
		if (!isDestroyed()) {
			doDestroy(false);
		}
		// unregister();
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public int getWidthIncrement() {
		return this.widthIncrement;
	}

	/**
	 * 
	 * @param widthIncrement
	 */
	@Override
	public void setWidthIncrement(final int widthIncrement) {
		if (widthIncrement > 0) {
			this.widthIncrement = widthIncrement;
		}
	}

	/**
	 * 
	 * @param heightIncrement
	 */
	@Override
	public void setHeightIncrement(final int heightIncrement) {
		if (heightIncrement > 0) {
			this.heightIncrement = heightIncrement;
		}
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public int getHeightIncrement() {
		return this.heightIncrement;
	}

	/**
	 * 
	 * @param managedDisplay
	 * 
	 */
	protected void setManagedDisplay(final ManagedDisplay managedDisplay) {
		this.managedDisplay = managedDisplay;
	}

	/**
	 * The <code>ManagedDisplay</code> where this
	 * <code>AbstractRenderArea</code> lives on.
	 * 
	 * @return A {@link ManagedDisplay}.
	 */
	@Override
	public ManagedDisplay getManagedDisplay() {
		return this.managedDisplay;
	}

	/**
	 * The <code>PlatformRenderArea</code> that this
	 * <code>AbstractRenderArea</code> wraps.
	 * <p>
	 * The returned <code>PlatformRenderArea</code> is the representation of a
	 * native window on the native display platform.
	 * <p>
	 * The returned <code>PlatformRenderArea</code> is used to represent the
	 * visualization of this <code>AbstractRenderArea</code> on the native
	 * display server. The visual representation of this
	 * <code>PlatformRenderArea</code> is never manipulated directly but through
	 * an external program or toolkit, in case this
	 * <code>AbstractRenderArea</code> is extended by a
	 * <code>ClientWindow</code> or a <code>Widget</code> respectively.
	 * <p>
	 * If this <code>AbstractRenderArea</code> is the owner of the returned
	 * <code>PlatformRenderArea</code>, the <code>AbstractRenderArea</code>'s
	 * geometry will reflect the geometry of the returned
	 * <code>PlatformRenderArea</code>.
	 * 
	 * @return A {@link PlatformRenderArea}.
	 */
	@Override
	public PlatformRenderArea getPlatformRenderArea() {
		return this.platformRenderArea;
	}

	/**
	 * Indicates if this object can movedor not. A non movable object can have a
	 * new position set but requests to execute this new position will have no
	 * effect.
	 * 
	 * @return True if movable, false if not.
	 * 
	 * @see GeoTransformableRectangle#requestMove()
	 * @see GeoTransformableRectangle#requestMoveResize()
	 * @see AbstractRenderArea#isResizable()
	 */
	@Override
	public boolean isMovable() {
		return this.movable;
	}

	/**
	 * Indicates if this object can resize or not. A non resizable object can
	 * have a new size set but requests to execute this new size will have no
	 * effect.
	 * 
	 * @return True if resizable, false if not.
	 * 
	 * @see GeoTransformableRectangle#requestMoveResize()
	 * @see GeoTransformableRectangle#requestResize()
	 * @see AbstractRenderArea#isMovable()
	 */
	@Override
	public boolean isResizable() {
		return this.resizable;
	}

	/**
	 * 
	 * @param movable
	 *            True if this <code>AbstractRenderArea</code> should be
	 *            movable, false if not.
	 * 
	 * @see AbstractRenderArea#isMovable()
	 */
	@Override
	public void setMovable(final boolean movable) {
		this.movable = movable;
	}

	/**
	 * 
	 * @param isResizable
	 *            True if this <code>AbstractRenderArea</code> can be resized,
	 *            false if not.
	 * 
	 * @see AbstractRenderArea#isResizable()
	 */
	@Override
	public void setResizable(final boolean isResizable) {
		this.resizable = isResizable;
	}

	/**
	 * Set the <code>PlatformRenderArea</code> that will be used as the native
	 * visual representation.
	 * 
	 * @param platformRenderArea
	 *            A {@link PlatformRenderArea}.
	 * 
	 * 
	 * @see AbstractRenderArea#getPlatformRenderArea()
	 */
	protected void setPlatformRenderArea(
			final PlatformRenderArea platformRenderArea) {
		this.platformRenderArea = platformRenderArea;
		this.managedDisplay.addDisplayEventManager(this, platformRenderArea);
	}

	/**
	 * A new width that lies within the interval of a minimum and maximum width
	 * (included).
	 * 
	 * @param newWidth
	 * @return
	 * 
	 * @see AbstractRenderArea#normalizedHeight(int)
	 */
	protected int normalizedWidth(final int newWidth) {
		return newWidth < getMinWidth() ? getMinWidth()
				: newWidth > getMaxWidth() ? getMaxWidth() : newWidth;
	}

	/**
	 * A new height that lies within the interval of a minimum and maximum
	 * height.
	 * 
	 * @param newHeight
	 * @return
	 * 
	 * @see AbstractRenderArea#normalizedWidth(int)
	 */
	protected int normalizedHeight(final int newHeight) {
		return newHeight < getMinHeight() ? getMinHeight()
				: newHeight > getMaxHeight() ? getMaxHeight() : newHeight;
	}

	@Override
	public void setWidth(final int width) {
		if (isResizable()) {
			super.setWidth(normalizedWidth(width));
		}
	}

	@Override
	public void setHeight(final int height) {
		if (isResizable()) {
			super.setHeight(normalizedHeight(height));
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
		return String.format("AbstractRenderArea <%s>: %d+%d : %dx%d",
				getPlatformRenderArea(), getX(), getX(), getWidth(),
				getHeight());
	}

	/**
	 * Update the geometric information of this <code>AbstractRenderArea</code>
	 * so it reflects it's <code>PlatformRenderArea</code>.
	 * <p>
	 * This method is only useful if this <code>AbstractRenderArea</code> is the
	 * owner of it's <code>PlatformRenderArea</code>. Extending classes that do
	 * not have their own <code>PlatformRenderArea</code> should override this
	 * method so it has no effect. Failure to do so can result in unexpected
	 * behavior.
	 * 
	 */
	@Override
	public void syncGeoToPlatformRenderAreaGeo() {
		final PlatformRenderAreaGeometry wg = getPlatformRenderArea()
				.getPlatformRenderAreaGeometry();
		setX(wg.getRelativeX());
		setY(wg.getRelativeY());

		setWidth(wg.getWidth());
		setHeight(wg.getHeight());
		doUpdateSizePlaceValue(false);

		setVisibility(getPlatformRenderArea().getPlatformRenderAreaAttributes()
				.isViewable());
		doUpdateVisibility(false);
	}

	@Override
	public GeoTransformation toGeoTransformation() {
		final GeoTransformation geoTransformation = super.toGeoTransformation();
		// we only want our size to increase with the preferred increment value
		// of the underlying platform render area.

		// we make sure that delta of old & new size is a multiple of he
		// desired increment.
		final int deltaWidth = geoTransformation.getDeltaWidth();
		final int newDeltaWidth = (deltaWidth / getWidthIncrement())
				* getWidthIncrement();
		final int deltaHeight = geoTransformation.getDeltaHeight();
		final int newDeltaHeight = (deltaHeight / getHeightIncrement())
				* getHeightIncrement();

		final int newWidth = geoTransformation.getWidth0() + newDeltaWidth;
		final int newHeight = geoTransformation.getHeight0() + newDeltaHeight;

		final int width1 = normalizedWidth(newWidth);
		final int height1 = normalizedHeight(newHeight);

		setWidth(width1);
		setHeight(height1);

		return new BaseGeoTransformation(geoTransformation.getX0(),
				geoTransformation.getY0(), geoTransformation.getWidth0(),
				geoTransformation.getHeight0(), geoTransformation.isVisible0(),
				geoTransformation.getParent0(), geoTransformation.getX1(),
				geoTransformation.getY1(), width1, height1,
				geoTransformation.isVisible1(), geoTransformation.getParent1());
	}

	@Override
	public void addPropertyChangedHandler(
			final PropertyChangedHandler<? extends Property<? extends PropertyInstance>> handler,
			final String propertyName) {
		addEventHandler(handler, PropertyChangedEvent.TYPE.get(propertyName));
	}
}