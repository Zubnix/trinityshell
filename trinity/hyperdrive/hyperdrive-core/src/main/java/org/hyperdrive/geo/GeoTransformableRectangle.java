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
package org.hyperdrive.geo;

import java.util.ArrayList;
import java.util.List;

import org.hydrogen.eventsystem.EventBus;
import org.hydrogen.eventsystem.EventHandler;
import org.hydrogen.paintinterface.HierarchicalArea;

//TODO Let geo events travel downwards to children to notify them that one of their parents has changed
// TODO documentation
/**
 * A <code>GeoTransformableRectangle</code> is the most basic abstract
 * implementation of an <code>HierarchicalArea</code> in the hyperdrive library.
 * It keeps track of its own geometric information and notifies listeners if any
 * changes occur.
 * <p>
 * Changing a <code>GeoTransformableRectangle</code> comes in the form of
 * manually setting the desired change and manually requesting the change. After
 * a change is requested, the <code>GeoTransformableRectangle</code> presents
 * the change to an authority which comes in the form of a {@link GeoManager}.
 * The <code>GeoManager</code> decides how, with what values and if the geometry
 * change is executed. A desired geometry change can be cancelled by calling any
 * of <code>cancelPending</code> methods. This will reset the
 * <code>GeoTransformableRectangle</code>'s desired changes.
 * <p>
 * Changing a <code>GeoTransformableRectangle</code> generates
 * <code>GeoEvent</code>s. These events are emitted by the
 * <code>GeoTransformableRectangle</code> that will undergo the changes.
 * <code>GeoEvent</code>s with a type name that <b>end in '_REQUEST'</b> are
 * emitted when a geometry change is requested. <code>GeoEvent</code>s with a
 * type name that does <b>not end in '_REQUEST'</b> are emitted after a geometry
 * change is executed. The final event that is emitted is the
 * <code>GeoEventFromParent</code>. This event is emitted by children who's
 * parent has changed and cascades to children-of-children and so on. This makes
 * that the last children in the tree hierarchy will emit the
 * <code>GeoEventFromParent</code> first before the direct children of the
 * parent emit it.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public abstract class GeoTransformableRectangle extends EventBus implements
		HierarchicalArea, GeoTransformable {

	private int relativeX;
	private int desiredRelativeX;

	private int relativeY;
	private int desiredRelativeY;

	private int width;
	private int desiredWidth;

	private int height;
	private int desiredHeight;

	private boolean visible;
	private boolean desiredVisible;

	private GeoTransformableRectangle parent;
	private GeoTransformableRectangle desiredParent;

	private boolean destroyed;

	private final List<GeoTransformableRectangle> children;

	/**
	 * 
	 */
	public GeoTransformableRectangle() {
		this.children = new ArrayList<GeoTransformableRectangle>();

		// start init geo request handlers -->
		this.addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				GeoTransformableRectangle.this.handleLowerEvent(event);
			}
		}, GeoEvent.LOWER_REQUEST);

		this.addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				GeoTransformableRectangle.this.handleRaiseEvent(event);
			}
		}, GeoEvent.RAISE_REQUEST);

		this.addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				GeoTransformableRectangle.this.handleMoveEvent(event);
			}
		}, GeoEvent.MOVE_REQUEST);

		this.addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				GeoTransformableRectangle.this.handleResizeEvent(event);
			}
		}, GeoEvent.RESIZE_REQUEST);

		this.addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				GeoTransformableRectangle.this.handleMoveResizeEvent(event);
			}
		}, GeoEvent.MOVE_RESIZE_REQUEST);

		this.addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				GeoTransformableRectangle.this.handleVisibilityEvent(event);
			}
		}, GeoEvent.VISIBILITY_REQUEST);

		this.addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				GeoTransformableRectangle.this.handleOwnReparentEvent(event);
			}
		}, GeoEvent.REPARENT_REQUEST);

		// <-- end init geo request handlers

		this.addEventHandler(new EventHandler<GeoEventFromParent>() {
			@Override
			public void handleEvent(final GeoEventFromParent event) {
				handleGeoEventFromParent(event);
			}
		}, GeoEventFromParent.TYPE);
	}

	@Override
	public GeoTransformation toGeoTransformation() {
		return new GeoTransformation(getRelativeX(), getRelativeY(),
				getWidth(), getHeight(), isVisible(), getParent(),
				this.desiredRelativeX, this.desiredRelativeY,
				this.desiredWidth, this.desiredHeight, this.desiredVisible,
				this.desiredParent);
	}

	/**
	 * The <code>GeoExecutor</code> that will execute the final geometry change
	 * for this <code>GeoTransformableRectangle</code>
	 * 
	 * @return The <code>GeoExecutor</code> of this this
	 *         <code>GeoTransformableRectangle</code>.
	 */
	public abstract GeoExecutor getGeoExecutor();

	/**
	 * Indicates if this <code>GeoTransformableRectangle</code> is destroyed. A
	 * destroyed <code>GeoTransformableRectangle</code> should be discarded from
	 * the tree hierarchy and should not undergo any geometry chances or
	 * requests. Ideally it should be ignored and not be referenced again.
	 * 
	 * @return
	 */
	public boolean isDestroyed() {
		return this.destroyed;
	}

	@Override
	public GeoTransformableRectangle getParent() {
		return this.parent;
	}

	/**
	 * Set the desired parent of this <code>GeoTransformableRectangle</code>.
	 * Changing the actual parent is done by calling
	 * {@link GeoTransformableRectangle#requestReparent()} after which a
	 * {@link GeoManager} will decide to honor it or not.
	 * 
	 * @param parent
	 */
	public void setParent(final GeoTransformableRectangle parent) {
		this.desiredParent = parent;
	}

	/**
	 * 
	 */
	public void requestReparent() {
		// update parent to new parent
		fireEvent(new GeoEvent(GeoEvent.REPARENT_REQUEST, this,
				toGeoTransformation()));
	}

	/**
	 * 
	 * @return
	 */
	public GeoTransformableRectangle[] getChildren() {
		return this.children
				.toArray(new GeoTransformableRectangle[this.children.size()]);
	}

	@Override
	public int getAbsoluteX() {
		if (getParent() == null) {
			return getRelativeX();
		}
		return getParent().getAbsoluteX() + getRelativeX();
	}

	@Override
	public int getAbsoluteY() {
		if (getParent() == null) {
			return getRelativeY();
		}
		return getParent().getAbsoluteY() + getRelativeY();
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public int getRelativeX() {
		return this.relativeX;
	}

	@Override
	public int getRelativeY() {
		return this.relativeY;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	/**
	 * 
	 * @param x
	 */
	public void setRelativeX(final int x) {
		this.desiredRelativeX = x;
	}

	/**
	 * 
	 * @param y
	 */
	public void setRelativeY(final int y) {
		this.desiredRelativeY = y;
	}

	/**
	 * 
	 * @param width
	 */
	public void setWidth(final int width) {
		this.desiredWidth = width;
	}

	/**
	 * 
	 * @param height
	 */
	public void setHeight(final int height) {
		this.desiredHeight = height;
	}

	@Override
	public boolean isVisible() {
		// If our parent is invisible, so are we.
		boolean parentVisible = true;
		if ((getParent() != null) && (getParent() != this)) {
			parentVisible = getParent().isVisible();
		}
		return this.visible && parentVisible;
	}

	/**
	 * 
	 * @param visible
	 */
	public void setVisibility(final boolean visible) {
		this.desiredVisible = visible;
	}

	/**
	 * Find the dominant <code>GeoManager</code> in the tree structure. The
	 * <code>GeoManger</code> is found by traversing the parents upwards in the
	 * tree, starting with direct parent of this <code>GeoManager</code>, until
	 * a parent of type {@link HasGeoManager} is found.
	 * 
	 * @return
	 */
	protected GeoManager getDominantGeoManager() {
		if (getParent() != null) {
			if (getParent() instanceof HasGeoManager) {
				return ((HasGeoManager) getParent()).getGeoManager();
			} else {
				return getParent().getDominantGeoManager();
			}
		} else {
			return null;
		}
	}

	/**
	 * 
	 * 
	 */
	public void requestMove() {
		fireEvent(new GeoEvent(GeoEvent.MOVE_REQUEST, this,
				toGeoTransformation()));
	}

	/**
	 * 
	 * 
	 */
	public void requestResize() {
		fireEvent(new GeoEvent(GeoEvent.RESIZE_REQUEST, this,
				toGeoTransformation()));
	}

	/**
	 * 
	 * 
	 */
	public void requestMoveResize() {
		fireEvent(new GeoEvent(GeoEvent.MOVE_RESIZE_REQUEST, this,
				toGeoTransformation()));
	}

	/**
	 * 
	 * 
	 */
	public void requestVisibilityChange() {
		fireEvent(new GeoEvent(GeoEvent.VISIBILITY_REQUEST, this,
				toGeoTransformation()));
	}

	/**
	 * 
	 * 
	 */
	public void requestRaise() {
		fireEvent(new GeoEvent(GeoEvent.RAISE_REQUEST, this,
				toGeoTransformation()));
	}

	/**
	 * 
	 * 
	 */
	public void requestLower() {
		fireEvent(new GeoEvent(GeoEvent.LOWER_REQUEST, this,
				toGeoTransformation()));
	}

	/**
	 * 
	 * @param event
	 * 
	 */
	protected void handleLowerEvent(final GeoEvent event) {
		if (event.getTransformableSquare().equals(this)
				&& (getDominantGeoManager() != null)) {
			getDominantGeoManager().onLowerRequest(this,
					event.getTransformation());
		} else {
			this.doUpdateLower();
		}
	}

	/**
	 * 
	 * @param event
	 * 
	 */
	protected void handleRaiseEvent(final GeoEvent event) {
		if (event.getTransformableSquare().equals(this)
				&& (getDominantGeoManager() != null)) {
			getDominantGeoManager().onRaiseRequest(this,
					event.getTransformation());
		} else {
			this.doUpdateRaise();
		}
	}

	/**
	 * 
	 * 
	 */
	public void doUpdatePlaceValue() {
		this.doUpdatePlaceValue(true);
	}

	/**
	 * 
	 * @param execute
	 * 
	 */
	protected void doUpdatePlaceValue(final boolean execute) {
		if (execute) {
			execUpdatePlaceValue();
		}
		flushPlaceValues();
		fireGeoEvent(GeoEvent.MOVE);
	}

	/**
	 * 
	 * 
	 */
	protected void execUpdatePlaceValue() {
		getGeoExecutor().updatePlace(this.desiredRelativeX,
				this.desiredRelativeY);
	}

	private void flushPlaceValues() {
		this.relativeX = this.desiredRelativeX;
		this.relativeY = this.desiredRelativeY;
	}

	/**
	 * 
	 * @param event
	 * 
	 */
	protected void handleMoveEvent(final GeoEvent event) {
		if (event.getTransformableSquare().equals(this)
				&& (getDominantGeoManager() != null)) {
			getDominantGeoManager().onMoveRequest(this,
					event.getTransformation());
		} else {
			this.doUpdatePlaceValue();
		}
	}

	/**
	 * 
	 * 
	 */
	public void doUpdateSizeValue() {
		this.doUpdateSizeValue(true);
	}

	/**
	 * 
	 * @param execute
	 * 
	 */
	protected void doUpdateSizeValue(final boolean execute) {
		if (execute) {
			execUpdateSizeValue();
		}
		flushSizeValues();
		fireGeoEvent(GeoEvent.RESIZE);
	}

	/**
	 * 
	 * 
	 */
	protected void execUpdateSizeValue() {
		getGeoExecutor().updateSize(this.desiredWidth, this.desiredHeight);
	}

	private void flushSizeValues() {
		this.width = this.desiredWidth;
		this.height = this.desiredHeight;
	}

	/**
	 * 
	 * @param event
	 * 
	 */
	protected void handleResizeEvent(final GeoEvent event) {
		if (event.getTransformableSquare().equals(this)
				&& (getDominantGeoManager() != null)) {
			getDominantGeoManager().onResizeRequest(this,
					event.getTransformation());
		} else {
			this.doUpdateSizeValue();
		}
	}

	/**
	 * 
	 * 
	 */
	public void doUpdateSizePlaceValue() {
		this.doUpdateSizePlaceValue(true);
	}

	/**
	 * 
	 * @param execute
	 * 
	 */
	protected void doUpdateSizePlaceValue(final boolean execute) {
		if (execute) {
			execUpdateSizePlaceValue();
		}
		flushSizePlaceValues();
		fireGeoEvent(GeoEvent.MOVE_RESIZE);
	}

	/**
	 * 
	 * 
	 */
	protected void execUpdateSizePlaceValue() {
		final GeoExecutor geoExecutor = getGeoExecutor();
		geoExecutor.updateSizePlace(this.desiredRelativeX,
				this.desiredRelativeY, this.desiredWidth, this.desiredHeight);
	}

	private void flushSizePlaceValues() {
		flushPlaceValues();
		flushSizeValues();
	}

	/**
	 * 
	 * @param event
	 * 
	 */
	protected void handleMoveResizeEvent(final GeoEvent event) {
		if (event.getTransformableSquare().equals(this)
				&& (getDominantGeoManager() != null)) {
			final GeoManager dominantGeoManager = getDominantGeoManager();
			dominantGeoManager.onMoveResizeRequest(this,
					event.getTransformation());
		} else {
			this.doUpdateSizePlaceValue();
		}
	}

	/**
	 * 
	 * 
	 */
	public void doDestroy() {
		this.doDestroy(true);
	}

	/**
	 * 
	 * @param execute
	 * 
	 */
	protected void doDestroy(final boolean execute) {
		if (execute) {
			execDestroy();
		}
		this.destroyed = true;
		fireGeoEvent(GeoEvent.DESTROYED);
	}

	/**
	 * 
	 * 
	 */
	protected void execDestroy() {
		getGeoExecutor().destroy();
	}

	/**
	 * 
	 * 
	 */
	public void doUpdateVisibility() {
		this.doUpdateVisibility(true);
	}

	/**
	 * 
	 * @param execute
	 * 
	 */
	protected void doUpdateVisibility(final boolean execute) {
		if (execute) {
			execUpdateVisibility();
		}
		flushVisibilityValue();
		fireGeoEvent(GeoEvent.VISIBILITY);
	}

	/**
	 * 
	 * @param geoEvent
	 */
	protected void notifiyChildrenOfGeoEvent(final GeoEvent geoEvent) {
		notifiyChildrenOfGeoEventFromParent(new GeoEventFromParent(geoEvent));
	}

	/**
	 * 
	 * @param geoEventFromParent
	 */
	protected void notifiyChildrenOfGeoEventFromParent(
			final GeoEventFromParent geoEventFromParent) {
		for (final GeoTransformableRectangle child : getChildren()) {
			child.fireEvent(geoEventFromParent);
		}
	}

	/**
	 * 
	 * 
	 */
	protected void execUpdateVisibility() {
		getGeoExecutor().updateVisibility(this.desiredVisible);
	}

	/**
	 * 
	 * 
	 */
	public void doUpdateRaise() {
		this.doUpdateRaise(true);
	}

	/**
	 * 
	 * @param execute
	 * 
	 */
	protected void doUpdateRaise(final boolean execute) {
		if (execute) {
			execUpdateRaise();
		}
		fireGeoEvent(GeoEvent.RAISE);

	}

	/**
	 * 
	 * 
	 */
	protected void execUpdateRaise() {
		getGeoExecutor().raise();
	}

	/**
	 * 
	 * 
	 */
	public void doUpdateLower() {
		this.doUpdateLower(true);
	}

	/**
	 * 
	 * @param execute
	 * 
	 */
	protected void doUpdateLower(final boolean execute) {
		if (execute) {
			execUpdateLower();
		}
		fireGeoEvent(GeoEvent.LOWER);
	}

	/**
	 * 
	 * 
	 */
	protected void execUpdateLower() {
		getGeoExecutor().lower();
	}

	private void flushVisibilityValue() {
		this.visible = this.desiredVisible;
	}

	/**
	 * 
	 * @param event
	 * 
	 */
	protected void handleVisibilityEvent(final GeoEvent event) {
		if (event.getTransformableSquare().equals(this)
				&& (getDominantGeoManager() != null)) {
			getDominantGeoManager().onChangeVisibilityRequest(this,
					event.getTransformation());
		} else {
			this.doUpdateVisibility();
		}
	}

	/**
	 * 
	 * 
	 */
	public void doUpdateParentValue() {
		this.doUpdateParentValue(true);
	}

	/**
	 * 
	 * @param execute
	 * 
	 */
	protected void doUpdateParentValue(final boolean execute) {
		// notify the old parent
		if (getParent() != null) {
			// Don't fire event. This might confuse listeners of the
			// parent. Instead use a dedicated method.
			getParent().handleChildReparentEvent(
					new GeoEvent(GeoEvent.REPARENT_REQUEST, this,
							toGeoTransformation()));
		}

		// actual parent update
		if (execute) {
			execUpdateParentValue();
		}
		flushParentValue();

		// notify the new parent
		getParent().handleChildReparentEvent(
				new GeoEvent(GeoEvent.REPARENT_REQUEST, this,
						toGeoTransformation()));

		// Make sure we have the same size
		// and place in our new parent
		// as in our old parent.
		this.doUpdateSizePlaceValue();

		fireGeoEvent(GeoEvent.REPARENT);
	}

	/**
	 * 
	 * 
	 */
	protected void execUpdateParentValue() {
		getGeoExecutor().updateParent(this.desiredParent);
	}

	private void flushParentValue() {
		this.parent = this.desiredParent;
	}

	/**
	 * 
	 * @param event
	 * 
	 */
	protected void handleOwnReparentEvent(final GeoEvent event) {
		if (getDominantGeoManager() != null) {
			getDominantGeoManager().onChangeParentRequest(this,
					event.getTransformation());
		} else {
			this.doUpdateParentValue();
		}

	}

	/**
	 * 
	 */
	public void cancelPendingMove() {
		this.desiredWidth = this.width;
		this.desiredHeight = this.height;
	}

	/**
	 * 
	 */
	public void cancelPendingResize() {
		this.desiredRelativeX = this.relativeX;
		this.desiredRelativeY = this.relativeY;
	}

	/**
	 * 
	 * @param event
	 * 
	 */
	protected void handleChildReparentEvent(final GeoEvent event) {
		if (this.children.contains(event.getTransformableSquare())) {
			// We already have this square as a child. The child is now a child
			// of another parent. We remove it.
			this.children.remove(event.getTransformableSquare());
		} else {
			// We don't have this square as a child. The child is now a
			// child of us. We add it.
			this.children.add(event.getTransformableSquare());
		}
	}

	/**
	 * 
	 * @param operation
	 */
	protected void fireGeoEvent(final GeoOperation operation) {
		final GeoEvent geoEvent = new GeoEvent(operation, this,
				toGeoTransformation());
		fireEvent(geoEvent);
		notifiyChildrenOfGeoEvent(geoEvent);
	}

	/**
	 * 
	 * @param geoEventFromParent
	 */
	protected void handleGeoEventFromParent(
			final GeoEventFromParent geoEventFromParent) {
		notifiyChildrenOfGeoEventFromParent(geoEventFromParent);
	}
}
