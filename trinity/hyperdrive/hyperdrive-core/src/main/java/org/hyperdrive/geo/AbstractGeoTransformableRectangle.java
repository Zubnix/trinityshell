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

import org.hydrogen.event.EventBus;
import org.hyperdrive.api.geo.GeoEvent;
import org.hyperdrive.api.geo.GeoEventHandler;
import org.hyperdrive.api.geo.GeoExecutor;
import org.hyperdrive.api.geo.GeoManager;
import org.hyperdrive.api.geo.GeoOperation;
import org.hyperdrive.api.geo.GeoTransformableRectangle;
import org.hyperdrive.api.geo.GeoTransformation;
import org.hyperdrive.api.geo.HasGeoManager;

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
public abstract class AbstractGeoTransformableRectangle extends EventBus
		implements GeoTransformableRectangle {

	private final class LowerRequestHandler implements GeoEventHandler {
		@Override
		public void handleEvent(final GeoEvent event) {
			handleLowerEvent(event);
		}

		@Override
		public GeoOperation getType() {
			return GeoOperation.LOWER_REQUEST;
		}
	}

	private final class RaiseRequestHandler implements GeoEventHandler {
		@Override
		public void handleEvent(final GeoEvent event) {
			handleRaiseEvent(event);
		}

		@Override
		public GeoOperation getType() {
			return GeoOperation.RAISE_REQUEST;
		}
	}

	private final class MoveRequestHandler implements GeoEventHandler {
		@Override
		public void handleEvent(final GeoEvent event) {
			handleMoveEvent(event);
		}

		@Override
		public GeoOperation getType() {
			return GeoOperation.MOVE_REQUEST;
		}
	}

	private final class ResizeRequestHandler implements GeoEventHandler {
		@Override
		public void handleEvent(final GeoEvent event) {
			handleResizeEvent(event);
		}

		@Override
		public GeoOperation getType() {
			return GeoOperation.RESIZE_REQUEST;
		}
	}

	private final class MoveResizeRequestHandler implements GeoEventHandler {
		@Override
		public void handleEvent(final GeoEvent event) {
			handleMoveResizeEvent(event);

		}

		@Override
		public GeoOperation getType() {
			return GeoOperation.MOVE_RESIZE_REQUEST;
		}
	}

	private final class VisibilityRequestHandler implements GeoEventHandler {
		@Override
		public void handleEvent(final GeoEvent event) {
			handleVisibilityEvent(event);

		}

		@Override
		public GeoOperation getType() {
			return GeoOperation.VISIBILITY_REQUEST;
		}
	}

	private final class ReparentRequestHandler implements GeoEventHandler {
		@Override
		public void handleEvent(final GeoEvent event) {
			handleOwnReparentEvent(event);
		}

		@Override
		public GeoOperation getType() {
			return GeoOperation.REPARENT_REQUEST;
		}
	}

	private final class GeoEventFromParentHandler implements GeoEventHandler {
		@Override
		public void handleEvent(final GeoEvent event) {
			handleGeoEventFromParent(event);
		}

		@Override
		public GeoOperation getType() {
			return GeoOperation.FROM_PARENT;
		}
	}

	private final class ChildLeftHandler implements GeoEventHandler {
		@Override
		public void handleEvent(final GeoEvent event) {
			handlechildLeft(event);
		}

		@Override
		public GeoOperation getType() {
			// TODO Auto-generated method stub
			return null;
		}
	}

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

	private final LowerRequestHandler lowerRequestHandler;
	private final RaiseRequestHandler raiseRequestHandler;
	private final MoveRequestHandler moveRequestHandler;
	private final ResizeRequestHandler resizeRequestHandler;
	private final MoveResizeRequestHandler moveResizeRequestHandler;
	private final VisibilityRequestHandler visibilityRequestHandler;
	private final ReparentRequestHandler reparentRequestHandler;
	private final GeoEventFromParentHandler geoEventFromParentHandler;
	private final ChildLeftHandler childLeftHandler;

	/**
	 * 
	 */
	public AbstractGeoTransformableRectangle() {
		this.children = new ArrayList<GeoTransformableRectangle>();

		this.lowerRequestHandler = new LowerRequestHandler();
		this.raiseRequestHandler = new RaiseRequestHandler();
		this.moveRequestHandler = new MoveRequestHandler();
		this.resizeRequestHandler = new ResizeRequestHandler();
		this.moveResizeRequestHandler = new MoveResizeRequestHandler();
		this.visibilityRequestHandler = new VisibilityRequestHandler();
		this.geoEventFromParentHandler = new GeoEventFromParentHandler();
		this.reparentRequestHandler = new ReparentRequestHandler();
		this.childLeftHandler = new ChildLeftHandler();

		addGeoEventHandler(this.lowerRequestHandler);
		addGeoEventHandler(this.raiseRequestHandler);
		addGeoEventHandler(this.moveRequestHandler);
		addGeoEventHandler(this.resizeRequestHandler);
		addGeoEventHandler(this.moveResizeRequestHandler);
		addGeoEventHandler(this.visibilityRequestHandler);
		addGeoEventHandler(this.reparentRequestHandler);
	}

	@Override
	public GeoTransformation toGeoTransformation() {
		return new BaseGeoTransformation(getX(), getY(), getWidth(),
				getHeight(), isVisible(), getParent(), this.desiredRelativeX,
				this.desiredRelativeY, this.desiredWidth, this.desiredHeight,
				this.desiredVisible, this.desiredParent);
	}

	/**
	 * The <code>GeoExecutor</code> that will execute the final geometry change
	 * for this <code>GeoTransformableRectangle</code>
	 * 
	 * @return The <code>GeoExecutor</code> of this this
	 *         <code>GeoTransformableRectangle</code>.
	 */
	@Override
	public abstract GeoExecutor getGeoExecutor();

	/**
	 * Indicates if this <code>GeoTransformableRectangle</code> is destroyed. A
	 * destroyed <code>GeoTransformableRectangle</code> should be discarded from
	 * the tree hierarchy and should not undergo any geometry chances or
	 * requests. Ideally it should be ignored and not be referenced again.
	 * 
	 * @return
	 */
	@Override
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
	@Override
	public void setParent(final GeoTransformableRectangle parent) {
		this.desiredParent = parent;
	}

	/**
	 * 
	 */
	@Override
	public void requestReparent() {
		// update parent to new parent
		fireEvent(new BaseGeoEvent(GeoOperation.REPARENT_REQUEST, this,
				toGeoTransformation()));
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public GeoTransformableRectangle[] getChildren() {
		return this.children
				.toArray(new GeoTransformableRectangle[this.children.size()]);
	}

	@Override
	public int getAbsoluteX() {
		if (getParent() == null) {
			return getX();
		}
		return getParent().getAbsoluteX() + getX();
	}

	@Override
	public int getAbsoluteY() {
		if (getParent() == null) {
			return getY();
		}
		return getParent().getAbsoluteY() + getY();
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public int getX() {
		return this.relativeX;
	}

	@Override
	public int getY() {
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
	@Override
	public void setX(final int x) {
		this.desiredRelativeX = x;
	}

	/**
	 * 
	 * @param y
	 */
	@Override
	public void setY(final int y) {
		this.desiredRelativeY = y;
	}

	/**
	 * 
	 * @param width
	 */
	@Override
	public void setWidth(final int width) {
		this.desiredWidth = width;
	}

	/**
	 * 
	 * @param height
	 */
	@Override
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
	@Override
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
	@Override
	public GeoManager getDominantGeoManager() {
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
	@Override
	public void requestMove() {
		fireEvent(new BaseGeoEvent(GeoOperation.MOVE_REQUEST, this,
				toGeoTransformation()));
	}

	/**
	 * 
	 * 
	 */
	@Override
	public void requestResize() {
		fireEvent(new BaseGeoEvent(GeoOperation.RESIZE_REQUEST, this,
				toGeoTransformation()));
	}

	/**
	 * 
	 * 
	 */
	@Override
	public void requestMoveResize() {
		fireEvent(new BaseGeoEvent(GeoOperation.MOVE_RESIZE_REQUEST, this,
				toGeoTransformation()));
	}

	/**
	 * 
	 * 
	 */
	@Override
	public void requestVisibilityChange() {
		fireEvent(new BaseGeoEvent(GeoOperation.VISIBILITY_REQUEST, this,
				toGeoTransformation()));
	}

	/**
	 * 
	 * 
	 */
	@Override
	public void requestRaise() {
		fireEvent(new BaseGeoEvent(GeoOperation.RAISE_REQUEST, this,
				toGeoTransformation()));
	}

	/**
	 * 
	 * 
	 */
	@Override
	public void requestLower() {
		fireEvent(new BaseGeoEvent(GeoOperation.LOWER_REQUEST, this,
				toGeoTransformation()));
	}

	/**
	 * 
	 * @param event
	 * 
	 */
	protected void handleLowerEvent(final GeoEvent event) {
		if (event.getGeoTransformableRectangle().equals(this)
				&& (getDominantGeoManager() != null)) {
			getDominantGeoManager().onLowerRequest(this,
					event.getGeoTransformation());
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
		if (event.getGeoTransformableRectangle().equals(this)
				&& (getDominantGeoManager() != null)) {
			getDominantGeoManager().onRaiseRequest(this,
					event.getGeoTransformation());
		} else {
			this.doUpdateRaise();
		}
	}

	/**
	 * 
	 * 
	 */
	@Override
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
		updateChildrenPosition();
		fireGeoEvent(GeoOperation.MOVE);
	}

	/**
	 * 
	 * 
	 */
	protected void updateChildrenPosition() {
		for (final GeoTransformableRectangle child : getChildren()) {
			child.getGeoExecutor().updatePlace(child.getX(), child.getY());
		}
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
		if (event.getGeoTransformableRectangle().equals(this)
				&& (getDominantGeoManager() != null)) {
			getDominantGeoManager().onMoveRequest(this,
					event.getGeoTransformation());
		} else {
			this.doUpdatePlaceValue();
		}
	}

	/**
	 * 
	 * 
	 */
	@Override
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
		fireGeoEvent(GeoOperation.RESIZE);
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
		if (event.getGeoTransformableRectangle().equals(this)
				&& (getDominantGeoManager() != null)) {
			getDominantGeoManager().onResizeRequest(this,
					event.getGeoTransformation());
		} else {
			this.doUpdateSizeValue();
		}
	}

	/**
	 * 
	 * 
	 */
	@Override
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
		updateChildrenPosition();
		fireGeoEvent(GeoOperation.MOVE_RESIZE);
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
		if (event.getGeoTransformableRectangle().equals(this)
				&& (getDominantGeoManager() != null)) {
			final GeoManager dominantGeoManager = getDominantGeoManager();
			dominantGeoManager.onMoveResizeRequest(this,
					event.getGeoTransformation());
		} else {
			this.doUpdateSizePlaceValue();
		}
	}

	/**
	 * 
	 * 
	 */
	@Override
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
		fireGeoEvent(GeoOperation.DESTROYED);
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
	@Override
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
		fireGeoEvent(GeoOperation.VISIBILITY);
	}

	// /**
	// *
	// * @param geoEvent
	// */
	// protected void notifiyChildrenOfGeoEvent(final GeoEvent geoEvent) {
	// notifiyChildrenOfGeoEventFromParent(geoEvent);
	// }
	//
	// /**
	// *
	// * @param geoEventFromParent
	// */
	// protected void notifiyChildrenOfGeoEventFromParent(
	// final GeoEvent geoEventFromParent) {
	// for (final GeoTransformableRectangle child : getChildren()) {
	// // TODO children should listen for FROM_PARENT events on their
	// // parent and fire them
	// // themself.
	// child.fireEvent(geoEventFromParent);
	// }
	// }

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
	@Override
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
		fireGeoEvent(GeoOperation.RAISE);

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
	@Override
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
		fireGeoEvent(GeoOperation.LOWER);
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
		if (event.getGeoTransformableRectangle().equals(this)
				&& (getDominantGeoManager() != null)) {
			getDominantGeoManager().onChangeVisibilityRequest(this,
					event.getGeoTransformation());
		} else {
			this.doUpdateVisibility();
		}
	}

	/**
	 * 
	 * 
	 */
	@Override
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
			getParent().removeGeoEventHandler(this.geoEventFromParentHandler);
			fireEvent(new BaseGeoEvent(GeoOperation.CHILD_LEFT, this,
					toGeoTransformation()));
		}
		// actual parent update
		if (execute) {
			execUpdateParentValue();
		}
		flushParentValue();

		// notify parent that it received a new child
		getParent().notifyChildAdded(this);

		// listen to from_parent events.
		getParent().addGeoEventHandler(this.geoEventFromParentHandler);

		// Make sure we have the same size
		// and place in our new parent
		// as in our old parent.
		this.doUpdateSizePlaceValue();

		fireGeoEvent(GeoOperation.REPARENT);
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
					event.getGeoTransformation());
		} else {
			this.doUpdateParentValue();
		}

	}

	/**
	 * 
	 */
	@Override
	public void cancelPendingMove() {
		this.desiredWidth = this.width;
		this.desiredHeight = this.height;
	}

	/**
	 * 
	 */
	@Override
	public void cancelPendingResize() {
		this.desiredRelativeX = this.relativeX;
		this.desiredRelativeY = this.relativeY;
	}

	/**
	 * 
	 * @param operation
	 */
	protected void fireGeoEvent(final GeoOperation operation) {
		final GeoTransformation geoTransformation = toGeoTransformation();
		final GeoEvent geoEvent = new BaseGeoEvent(operation, this,
				geoTransformation);
		fireEvent(geoEvent);
		final GeoEvent fromParentGeoEvent = new BaseGeoEvent(
				GeoOperation.FROM_PARENT, this, geoTransformation);
		fireEvent(fromParentGeoEvent);
	}

	/**
	 * 
	 * @param geoEventFromParent
	 */
	protected void handleGeoEventFromParent(final GeoEvent geoEventFromParent) {
		fireEvent(geoEventFromParent);
	}

	@Override
	public void addGeoEventHandler(final GeoEventHandler geoEventHandler) {
		addTypedEventHandler(geoEventHandler);
	}

	protected void handlechildLeft(final GeoEvent event) {
		final GeoTransformableRectangle child = event
				.getGeoTransformableRectangle();
		child.removeGeoEventHandler(this.childLeftHandler);
		this.children.remove(child);
	}

	@Override
	public void notifyChildAdded(final GeoTransformableRectangle newChild) {
		newChild.addGeoEventHandler(this.childLeftHandler);
		this.children.add(newChild);
	}

	@Override
	public void removeGeoEventHandler(final GeoEventHandler geoEventHandler) {
		removeTypedEventHandler(geoEventHandler);
	}
}
