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
package org.hyperdrive.geo.api.base;

import java.util.ArrayList;
import java.util.List;

import org.hydrogen.event.api.base.EventBus;
import org.hyperdrive.geo.api.GeoEvent;
import org.hyperdrive.geo.api.GeoEventHandler;
import org.hyperdrive.geo.api.GeoExecutor;
import org.hyperdrive.geo.api.GeoManager;
import org.hyperdrive.geo.api.GeoOperation;
import org.hyperdrive.geo.api.GeoTransformableRectangle;
import org.hyperdrive.geo.api.GeoTransformation;

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

	private final class ParentNotifyHandler implements GeoEventHandler {
		@Override
		public void handleEvent(final GeoEvent event) {
			handleParentNotifyEvent(event);
		}

		@Override
		public GeoOperation getType() {
			return GeoOperation.PARENT_NOTIFY;
		}
	}

	private final class ChildLeftNotifyHandler implements GeoEventHandler {
		@Override
		public void handleEvent(final GeoEvent event) {
			handlechildLeftNotifyEvent(event);
		}

		@Override
		public GeoOperation getType() {
			return GeoOperation.CHILD_LEFT_NOTIFY;
		}
	}

	private int x;
	private int desiredX;

	private int y;
	private int desiredY;

	private int width;
	private int desiredWidth;

	private int height;
	private int desiredHeight;

	private boolean visibility;
	private boolean desiredVisibility;

	private GeoTransformableRectangle parent;
	private GeoTransformableRectangle desiredParent;

	private boolean destroyed;

	private final List<GeoTransformableRectangle> children;

	private final ParentNotifyHandler parentNotifyHandler;
	private final ChildLeftNotifyHandler childLeftNotifyHandler;

	private GeoManager geoManager;

	/**
	 * 
	 */
	public AbstractGeoTransformableRectangle() {
		this.children = new ArrayList<GeoTransformableRectangle>();
		this.parentNotifyHandler = new ParentNotifyHandler();
		this.childLeftNotifyHandler = new ChildLeftNotifyHandler();
	}

	@Override
	public GeoTransformation toGeoTransformation() {
		return new BaseGeoTransformation(getX(), getY(), getWidth(),
				getHeight(), isVisible(), getParent(), getDesiredX(),
				getDesiredY(), getDesiredWidth(), getDesiredHeight(),
				getDesiredVisibility(), getDesiredParent());
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
		final GeoEvent event = newGeoEvent(GeoOperation.REPARENT_REQUEST);
		fireEvent(event);
		handleOwnReparentRequestEvent(event);
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
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
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
		this.desiredX = x;
	}

	/**
	 * 
	 * @param y
	 */
	@Override
	public void setY(final int y) {
		this.desiredY = y;
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
		return this.visibility && parentVisible;
	}

	/**
	 * 
	 * @param visible
	 */
	@Override
	public void setVisibility(final boolean visible) {
		this.desiredVisibility = visible;
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
	public GeoManager getParentGeoManager() {
		final GeoTransformableRectangle parent = getParent();
		if (parent == null) {
			return null;
		}

		final GeoManager parentGeoManager = parent.getGeoManager();
		if (parentGeoManager == null) {
			return parent.getParentGeoManager();
		}

		return parentGeoManager;
	}

	/**
	 * 
	 * 
	 */
	@Override
	public void requestMove() {
		final GeoEvent event = newGeoEvent(GeoOperation.MOVE_REQUEST);
		fireEvent(event);
		handleMoveRequestEvent(event);
	}

	/**
	 * 
	 * 
	 */
	@Override
	public void requestResize() {
		final GeoEvent event = newGeoEvent(GeoOperation.RESIZE_REQUEST);
		fireEvent(event);
		handleResizeRequestEvent(event);
	}

	/**
	 * 
	 * 
	 */
	@Override
	public void requestMoveResize() {
		final GeoEvent event = newGeoEvent(GeoOperation.MOVE_RESIZE_REQUEST);
		fireEvent(event);
		handleMoveResizeRequestEvent(event);
	}

	/**
	 * 
	 * 
	 */
	@Override
	public void requestVisibilityChange() {
		final GeoEvent event = newGeoEvent(GeoOperation.VISIBILITY_REQUEST);
		fireEvent(event);
		handleVisibilityRequestEvent(event);
	}

	/**
	 * 
	 * 
	 */
	@Override
	public void requestRaise() {
		final GeoEvent event = newGeoEvent(GeoOperation.RAISE_REQUEST);
		fireEvent(event);
		handleRaiseRequestEvent(event);
	}

	/**
	 * 
	 * 
	 */
	@Override
	public void requestLower() {
		final GeoEvent event = newGeoEvent(GeoOperation.LOWER_REQUEST);
		fireEvent(event);
		handleLowerRequestEvent(event);
	}

	protected GeoEvent newGeoEvent(final GeoOperation geoOperation) {
		return new BaseGeoEvent(geoOperation, this, toGeoTransformation());
	}

	/**
	 * 
	 * @param event
	 * 
	 */
	protected void handleLowerRequestEvent(final GeoEvent event) {
		if (event.getGeoTransformableRectangle().equals(this)
				&& (getParentGeoManager() != null)) {
			getParentGeoManager().onLowerRequest(this,
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
	protected void handleRaiseRequestEvent(final GeoEvent event) {
		if (event.getGeoTransformableRectangle().equals(this)
				&& (getParentGeoManager() != null)) {
			getParentGeoManager().onRaiseRequest(this,
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
	public void doUpdatePlace() {
		doUpdatePlaceValue(true);
		fireGeoEvent(handleMoveNotifyEvent(newGeoEvent(GeoOperation.MOVE)));
	}

	/**
	 * 
	 * @param execute
	 * 
	 */
	protected void doUpdatePlaceValue(final boolean execute) {
		if (execute) {
			execUpdatePlace();
		}
		flushPlaceValues();
		updateChildrenPosition();
	}

	protected GeoEvent handleMoveNotifyEvent(final GeoEvent event) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			geoManager.onMoveNotify(event.getGeoTransformableRectangle(),
					event.getGeoTransformation());
		}
		return event;
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
	protected void execUpdatePlace() {
		getGeoExecutor().updatePlace(getDesiredX(), getDesiredY());
	}

	private void flushPlaceValues() {
		this.x = getDesiredX();
		this.y = getDesiredY();
	}

	/**
	 * 
	 * @param event
	 * 
	 */
	protected void handleMoveRequestEvent(final GeoEvent event) {
		if (event.getGeoTransformableRectangle().equals(this)
				&& (getParentGeoManager() != null)) {
			getParentGeoManager().onMoveRequest(this,
					event.getGeoTransformation());
		} else {
			doUpdatePlace();
		}
	}

	/**
	 * 
	 * 
	 */
	@Override
	public void doUpdateSize() {
		this.doUpdateSize(true);
		fireGeoEvent(handleResizeNotifyEvent(newGeoEvent(GeoOperation.RESIZE)));
	}

	protected GeoEvent handleResizeNotifyEvent(final GeoEvent event) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			geoManager.onResizeNotify(event.getGeoTransformableRectangle(),
					event.getGeoTransformation());
		}
		return event;
	}

	/**
	 * 
	 * @param execute
	 * 
	 */
	protected void doUpdateSize(final boolean execute) {
		if (execute) {
			execUpdateSize();
		}
		flushSizeValues();
	}

	/**
	 * 
	 * 
	 */
	protected void execUpdateSize() {
		getGeoExecutor().updateSize(getDesiredWidth(), getDesiredHeight());
	}

	private void flushSizeValues() {
		this.width = getDesiredWidth();
		this.height = getDesiredHeight();
	}

	/**
	 * 
	 * @param event
	 * 
	 */
	protected void handleResizeRequestEvent(final GeoEvent event) {
		if (event.getGeoTransformableRectangle().equals(this)
				&& (getParentGeoManager() != null)) {
			getParentGeoManager().onResizeRequest(this,
					event.getGeoTransformation());
		} else {
			this.doUpdateSize();
		}
	}

	/**
	 * 
	 * 
	 */
	@Override
	public void doUpdateSizePlace() {
		this.doUpdateSizePlaceValue(true);
		fireGeoEvent(handleMoveResizeNotifyEvent(newGeoEvent(GeoOperation.MOVE_RESIZE)));
	}

	/**
	 * 
	 * @param execute
	 * 
	 */
	protected void doUpdateSizePlaceValue(final boolean execute) {
		if (execute) {
			execUpdateSizePlace();
		}
		flushSizePlaceValues();
		updateChildrenPosition();

	}

	protected GeoEvent handleMoveResizeNotifyEvent(final GeoEvent event) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			geoManager.onMoveResizeNotify(event.getGeoTransformableRectangle(),
					event.getGeoTransformation());
		}
		return event;
	}

	/**
	 * 
	 * 
	 */
	protected void execUpdateSizePlace() {
		final GeoExecutor geoExecutor = getGeoExecutor();
		geoExecutor.updateSizePlace(getDesiredX(), getDesiredY(),
				getDesiredWidth(), getDesiredHeight());
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
	protected void handleMoveResizeRequestEvent(final GeoEvent event) {
		if (event.getGeoTransformableRectangle().equals(this)
				&& (getParentGeoManager() != null)) {
			final GeoManager dominantGeoManager = getParentGeoManager();
			dominantGeoManager.onMoveResizeRequest(this,
					event.getGeoTransformation());
		} else {
			this.doUpdateSizePlace();
		}
	}

	/**
	 * 
	 * 
	 */
	@Override
	public void doDestroy() {
		this.doDestroy(true);
		fireGeoEvent(handleDestroyNotifyEvent(newGeoEvent(GeoOperation.DESTROYED)));
	}

	protected GeoEvent handleDestroyNotifyEvent(final GeoEvent geoEvent) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			getGeoManager().onDestroyNotify(
					geoEvent.getGeoTransformableRectangle(),
					geoEvent.getGeoTransformation());
		}
		return geoEvent;
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
		fireGeoEvent(handleVisibilityNotifyEvent(newGeoEvent(GeoOperation.VISIBILITY)));
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
	}

	protected GeoEvent handleVisibilityNotifyEvent(final GeoEvent event) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			getGeoManager().onChangeVisibilityNotify(
					event.getGeoTransformableRectangle(),
					event.getGeoTransformation());
		}
		return event;
	}

	/**
	 * 
	 * 
	 */
	protected void execUpdateVisibility() {
		getGeoExecutor().updateVisibility(getDesiredVisibility());
	}

	/**
	 * 
	 * 
	 */
	@Override
	public void doUpdateRaise() {
		this.doUpdateRaise(true);
		fireGeoEvent(handleRaiseNotifyEvent(newGeoEvent(GeoOperation.RAISE)));
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
	}

	protected GeoEvent handleRaiseNotifyEvent(final GeoEvent event) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			geoManager.onRaiseNotify(event.getGeoTransformableRectangle(),
					event.getGeoTransformation());
		}
		return event;
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
		fireGeoEvent(handleLowerNotifyEvent(newGeoEvent(GeoOperation.LOWER)));
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
	}

	protected GeoEvent handleLowerNotifyEvent(final GeoEvent event) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			geoManager.onLowerNotify(event.getGeoTransformableRectangle(),
					event.getGeoTransformation());
		}
		return event;
	}

	/**
	 * 
	 * 
	 */
	protected void execUpdateLower() {
		getGeoExecutor().lower();
	}

	private void flushVisibilityValue() {
		setVisibility(getDesiredVisibility());
	}

	/**
	 * 
	 * @param event
	 * 
	 */
	protected void handleVisibilityRequestEvent(final GeoEvent event) {
		if (event.getGeoTransformableRectangle().equals(this)
				&& (getParentGeoManager() != null)) {
			getParentGeoManager().onChangeVisibilityRequest(this,
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
	public void doUpdateParent() {
		doUpdateParentValue(true);
		fireGeoEvent(handleOwnReparentNotifyEvent(newGeoEvent(GeoOperation.REPARENT)));
	}

	/**
	 * 
	 * @param execute
	 * 
	 */
	protected void doUpdateParentValue(final boolean execute) {

		// notify the old parent
		if (getParent() != null) {
			getParent().removeGeoEventHandler(this.parentNotifyHandler);

			fireEvent(new BaseGeoEvent(GeoOperation.CHILD_LEFT_NOTIFY, this,
					toGeoTransformation()));
		}
		// actual parent update
		if (execute) {
			execUpdateParent();
		}
		flushParentValue();

		// notify parent that it received a new child
		getParent().notifyChildAdded(this);

		// listen to from_parent events.
		getParent().addGeoEventHandler(this.parentNotifyHandler);

		// Make sure we have the same size
		// and place in our new parent
		// as in our old parent.
		this.doUpdateSizePlace();
	}

	protected GeoEvent handleOwnReparentNotifyEvent(final GeoEvent event) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			geoManager.onChangeParentNotify(
					event.getGeoTransformableRectangle(),
					event.getGeoTransformation());
		}
		return event;
	}

	/**
	 * 
	 * 
	 */
	protected void execUpdateParent() {
		getGeoExecutor().updateParent(getDesiredParent());
	}

	private void flushParentValue() {
		this.parent = getDesiredParent();
	}

	/**
	 * 
	 * @param event
	 * 
	 */
	protected void handleOwnReparentRequestEvent(final GeoEvent event) {
		if (getParentGeoManager() != null) {
			getParentGeoManager().onChangeParentRequest(this,
					event.getGeoTransformation());
		} else {
			doUpdateParent();
		}

	}

	/**
	 * 
	 */
	@Override
	public void cancelPendingMove() {
		setWidth(getWidth());
		setHeight(getHeight());
	}

	/**
	 * 
	 */
	@Override
	public void cancelPendingResize() {
		setX(getX());
		setY(getY());
	}

	/**
	 * 
	 * @param operation
	 */
	protected void fireGeoEvent(GeoEvent geoEvent) {
		fireEvent(geoEvent);
		final GeoTransformation geoTransformation = toGeoTransformation();
		final GeoEvent fromParentGeoEvent = new BaseGeoEvent(
				GeoOperation.PARENT_NOTIFY, this, geoTransformation);
		fireEvent(fromParentGeoEvent);
	}

	/**
	 * 
	 * @param geoEventFromParent
	 */
	protected void handleParentNotifyEvent(final GeoEvent geoEventFromParent) {
		fireEvent(geoEventFromParent);
	}

	@Override
	public void addGeoEventHandler(final GeoEventHandler geoEventHandler) {
		addTypedEventHandler(geoEventHandler);
	}

	protected void handlechildLeftNotifyEvent(final GeoEvent event) {
		final GeoTransformableRectangle child = event
				.getGeoTransformableRectangle();
		child.removeGeoEventHandler(this.childLeftNotifyHandler);
		this.children.remove(child);
	}

	@Override
	public void notifyChildAdded(final GeoTransformableRectangle newChild) {
		newChild.addGeoEventHandler(this.childLeftNotifyHandler);
		this.children.add(newChild);
	}

	@Override
	public void removeGeoEventHandler(final GeoEventHandler geoEventHandler) {
		removeTypedEventHandler(geoEventHandler);
	}

	@Override
	public GeoManager getGeoManager() {
		return this.geoManager;
	}

	@Override
	public void setGeoManager(final GeoManager geoManager) {
		this.geoManager = geoManager;
	}

	protected int getDesiredHeight() {
		return desiredHeight;
	}

	protected int getDesiredWidth() {
		return desiredWidth;
	}

	protected int getDesiredX() {
		return desiredX;
	}

	protected int getDesiredY() {
		return desiredY;
	}

	protected boolean getDesiredVisibility() {
		return desiredVisibility;
	}

	protected GeoTransformableRectangle getDesiredParent() {
		return desiredParent;
	}
}
