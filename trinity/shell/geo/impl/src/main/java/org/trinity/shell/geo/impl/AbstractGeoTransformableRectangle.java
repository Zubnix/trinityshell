/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.geo.impl;

import java.util.ArrayList;
import java.util.List;

import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.GeoTransformation;
import org.trinity.shell.geo.api.event.GeoChildAddedEvent;
import org.trinity.shell.geo.api.event.GeoChildLeftEvent;
import org.trinity.shell.geo.api.event.GeoDestroyEvent;
import org.trinity.shell.geo.api.event.GeoEvent;
import org.trinity.shell.geo.api.event.GeoEventFactory;
import org.trinity.shell.geo.api.event.GeoHideEvent;
import org.trinity.shell.geo.api.event.GeoHideRequestEvent;
import org.trinity.shell.geo.api.event.GeoLowerEvent;
import org.trinity.shell.geo.api.event.GeoLowerRequestEvent;
import org.trinity.shell.geo.api.event.GeoMoveEvent;
import org.trinity.shell.geo.api.event.GeoMoveRequestEvent;
import org.trinity.shell.geo.api.event.GeoMoveResizeEvent;
import org.trinity.shell.geo.api.event.GeoMoveResizeRequestEvent;
import org.trinity.shell.geo.api.event.GeoRaiseEvent;
import org.trinity.shell.geo.api.event.GeoReparentEvent;
import org.trinity.shell.geo.api.event.GeoResizeEvent;
import org.trinity.shell.geo.api.event.GeoShowEvent;
import org.trinity.shell.geo.api.event.GeoShowRequestEvent;
import org.trinity.shell.geo.api.manager.GeoManager;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

// TODO Let geo events travel downwards to children to notify them that one of
// their parents has changed
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
 */
public abstract class AbstractGeoTransformableRectangle implements
		GeoTransformableRectangle {

	private int x;
	private int desiredX;

	private int y;
	private int desiredY;

	private int width;
	private int desiredWidth;

	private int height;
	private int desiredHeight;

	private boolean visible;
	private boolean desiredVisibility;

	private GeoTransformableRectangle parent;
	private GeoTransformableRectangle desiredParent;

	private boolean destroyed;

	private final List<GeoTransformableRectangle> children;

	private GeoManager geoManager;

	private final EventBus eventBus;
	private final GeoEventFactory geoEventFactory;

	public AbstractGeoTransformableRectangle(	final EventBus eventBus,
												final GeoEventFactory geoEventFactory) {
		this.children = new ArrayList<GeoTransformableRectangle>();
		this.eventBus = eventBus;
		this.geoEventFactory = geoEventFactory;
	}

	@Override
	public GeoTransformation toGeoTransformation() {
		return new GeoTransformationImpl(	getX(),
											getY(),
											getWidth(),
											getHeight(),
											isVisible(),
											getParent(),
											getDesiredX(),
											getDesiredY(),
											getDesiredWidth(),
											getDesiredHeight(),
											getDesiredVisibility(),
											getDesiredParent());
	}

	@Override
	public abstract GeoExecutor getGeoExecutor();

	@Override
	public boolean isDestroyed() {
		return this.destroyed;
	}

	@Override
	public GeoTransformableRectangle getParent() {
		return this.parent;
	}

	@Override
	public void setParent(final GeoTransformableRectangle parent) {
		this.desiredParent = parent;
	}

	@Override
	public void requestReparent() {
		// update parent to new parent
		final GeoEvent event = this.geoEventFactory
				.createGeoReparentRequestEvent(	this,
												toGeoTransformation());
		this.eventBus.post(event);
		handleOwnReparentRequestEvent(event);
	}

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

	@Override
	public void setX(final int x) {
		this.desiredX = x;
	}

	@Override
	public void setY(final int y) {
		this.desiredY = y;
	}

	@Override
	public void setWidth(final int width) {
		this.desiredWidth = width;
	}

	@Override
	public void setHeight(final int height) {
		this.desiredHeight = height;
	}

	@Override
	public boolean isVisible() {
		// If our parent is invisible, so are we.
		boolean parentVisible = true;
		if (getParent() != null && getParent() != this) {
			parentVisible = getParent().isVisible();
		}
		return this.visible && parentVisible;
	}

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

	@Override
	public void requestMove() {
		final GeoMoveRequestEvent event = this.geoEventFactory
				.createGeoMoveRequestEvent(	this,
											toGeoTransformation());
		this.eventBus.post(event);
		handleMoveRequestEvent(event);
	}

	@Override
	public void requestResize() {
		final GeoEvent event = this.geoEventFactory
				.createGeoResizeRequestEvent(	this,
												toGeoTransformation());
		this.eventBus.post(event);
		handleResizeRequestEvent(event);
	}

	@Override
	public void requestMoveResize() {
		final GeoMoveResizeRequestEvent event = this.geoEventFactory
				.createGeoMoveResizeRequestEvent(	this,
													toGeoTransformation());
		this.eventBus.post(event);
		handleMoveResizeRequestEvent(event);
	}

	@Override
	public void requestRaise() {
		final GeoEvent event = this.geoEventFactory
				.createGeoRaiseRequestEvent(this,
											toGeoTransformation());
		this.eventBus.post(event);
		handleRaiseRequestEvent(event);
	}

	@Override
	public void requestLower() {
		final GeoLowerRequestEvent event = this.geoEventFactory
				.createGeoLowerRequestEvent(this,
											toGeoTransformation());
		this.eventBus.post(event);
		handleLowerRequestEvent(event);
	}

	protected void handleLowerRequestEvent(final GeoLowerRequestEvent event) {
		final GeoManager geoManager = getParentGeoManager();
		if (geoManager != null) {
			geoManager.onLowerRequest(event);
		} else {
			doLower();
		}
	}

	protected void handleRaiseRequestEvent(final GeoEvent event) {
		final GeoManager geoManager = getParentGeoManager();
		if (geoManager != null) {
			geoManager.onRaiseRequest(event);
		} else {
			doRaise();
		}
	}

	@Override
	public void doUpdatePlace() {
		doUpdatePlaceValue(true);
		final GeoMoveEvent geoEvent = this.geoEventFactory
				.createGeoMoveEvent(this,
									toGeoTransformation());
		handleMoveNotifyEvent(geoEvent);
		this.eventBus.post(geoEvent);
	}

	protected void doUpdatePlaceValue(final boolean execute) {
		if (execute) {
			execUpdatePlace();
		}
		flushPlaceValues();
		updateChildrenPosition();
	}

	protected void handleMoveNotifyEvent(final GeoMoveEvent event) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			geoManager.onMoveNotify(event);
		}
	}

	protected void updateChildrenPosition() {
		for (final GeoTransformableRectangle child : getChildren()) {
			child.getGeoExecutor().updatePlace(	this,
												child.getX(),
												child.getY());
		}
	}

	protected void execUpdatePlace() {
		getGeoExecutor().updatePlace(	this,
										getDesiredX(),
										getDesiredY());
	}

	private void flushPlaceValues() {
		this.x = getDesiredX();
		this.y = getDesiredY();
	}

	protected void handleMoveRequestEvent(final GeoMoveRequestEvent event) {
		final GeoManager geoManager = getParentGeoManager();
		if (geoManager != null) {
			geoManager.onMoveRequest(event);
		} else {
			doUpdatePlace();
		}
	}

	@Override
	public void doUpdateSize() {
		this.doUpdateSize(true);
		final GeoResizeEvent geoEvent = this.geoEventFactory
				.createGeoResizeEvent(	this,
										toGeoTransformation());
		handleResizeNotifyEvent(geoEvent);
		this.eventBus.post(geoEvent);
	}

	protected void handleResizeNotifyEvent(final GeoResizeEvent event) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			geoManager.onResizeNotify(event);
		}
	}

	protected void doUpdateSize(final boolean execute) {
		if (execute) {
			execUpdateSize();
		}
		flushSizeValues();
	}

	protected void execUpdateSize() {
		getGeoExecutor().updateSize(this,
									getDesiredWidth(),
									getDesiredHeight());
	}

	private void flushSizeValues() {
		this.width = getDesiredWidth();
		this.height = getDesiredHeight();
	}

	protected void handleResizeRequestEvent(final GeoEvent event) {
		final GeoManager geoManager = getParentGeoManager();
		if (geoManager != null) {
			geoManager.onResizeRequest(event);
		} else {
			this.doUpdateSize();
		}
	}

	@Override
	public void doUpdateSizePlace() {
		doUpdateSizePlaceValue(true);
		final GeoMoveResizeEvent geoEvent = this.geoEventFactory
				.createGeoMoveResizeEvent(	this,
											toGeoTransformation());
		handleMoveResizeNotifyEvent(geoEvent);
		this.eventBus.post(geoEvent);
	}

	protected void doUpdateSizePlaceValue(final boolean execute) {
		if (execute) {
			execUpdateSizePlace();
		}
		flushSizePlaceValues();
		updateChildrenPosition();

	}

	protected void handleMoveResizeNotifyEvent(final GeoMoveResizeEvent event) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			geoManager.onMoveResizeNotify(event);
		}
	}

	protected void execUpdateSizePlace() {
		final GeoExecutor geoExecutor = getGeoExecutor();
		geoExecutor.updateSizePlace(this,
									getDesiredX(),
									getDesiredY(),
									getDesiredWidth(),
									getDesiredHeight());
	}

	private void flushSizePlaceValues() {
		flushPlaceValues();
		flushSizeValues();
	}

	protected void
			handleMoveResizeRequestEvent(final GeoMoveResizeRequestEvent event) {
		final GeoManager geoManager = getParentGeoManager();
		if (geoManager != null) {
			geoManager.onMoveResizeRequest(event);
		} else {
			doUpdateSizePlace();
		}
	}

	@Override
	public void doDestroy() {
		this.doDestroy(true);
		final GeoDestroyEvent geoEvent = this.geoEventFactory
				.createGeoDestroyEvent(	this,
										toGeoTransformation());
		handleDestroyEvent(geoEvent);
		this.eventBus.post(geoEvent);
	}

	protected void handleDestroyEvent(final GeoDestroyEvent geoEvent) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			getGeoManager().onDestroyNotify(geoEvent);
		}
	}

	protected void doDestroy(final boolean execute) {
		if (execute) {
			execDestroy();
		}
		this.destroyed = true;
	}

	protected void execDestroy() {
		getGeoExecutor().destroy(this);
	}

	@Override
	public void doRaise() {
		doRaise(true);
		final GeoRaiseEvent geoEvent = this.geoEventFactory
				.createGeoRaiseEvent(	this,
										toGeoTransformation());
		handleRaiseNotifyEvent(geoEvent);
		this.eventBus.post(geoEvent);
	}

	protected void doRaise(final boolean execute) {
		if (execute) {
			execUpdateRaise();
		}
	}

	protected void handleRaiseNotifyEvent(final GeoRaiseEvent event) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			geoManager.onRaiseNotify(event);
		}
	}

	protected void execUpdateRaise() {
		getGeoExecutor().raise(this);
	}

	@Override
	public void doLower() {
		doLower(true);
		final GeoLowerEvent geoEvent = this.geoEventFactory
				.createGeoLowerEvent(	this,
										toGeoTransformation());
		handleLowerNotifyEvent(geoEvent);
		this.eventBus.post(geoEvent);
	}

	protected void doLower(final boolean execute) {
		if (execute) {
			execUpdateLower();
		}
	}

	protected void handleLowerNotifyEvent(final GeoLowerEvent event) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			geoManager.onLowerNotify(event);
		}
	}

	protected void execUpdateLower() {
		getGeoExecutor().lower(this);
	}

	@Override
	public void doUpdateParent() {
		doUpdateParentValue(true);
		final GeoReparentEvent geoEvent = this.geoEventFactory
				.createGeoReparentEvent(this,
										toGeoTransformation());
		handleOwnReparentNotifyEvent(geoEvent);
		this.eventBus.post(geoEvent);
	}

	protected void doUpdateParentValue(final boolean execute) {

		// notify the old parent
		if (getParent() != null) {
			final GeoEvent leftEvent = this.geoEventFactory
					.createGeoChildLeftEvent(	this,
												toGeoTransformation());
			this.eventBus.post(leftEvent);
			removeGeoEventHandler(getParent());
		}
		// actual parent update
		if (execute) {
			execUpdateParent();
		}
		flushParentValue();

		// notify the new parent
		final GeoEvent addedEvent = this.geoEventFactory
				.createGeoChildAddedEvent(	this,
											toGeoTransformation());
		addGeoEventHandler(getParent());
		this.eventBus.post(addedEvent);

		// Make sure we have the same size
		// and place in our new parent
		// as in our old parent.
		doUpdateSizePlace();
	}

	protected void handleOwnReparentNotifyEvent(final GeoEvent event) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			geoManager.onChangeParentNotify(event);
		}
	}

	protected void execUpdateParent() {
		getGeoExecutor().updateParent(	this,
										getDesiredParent());
	}

	private void flushParentValue() {
		this.parent = getDesiredParent();
	}

	protected void handleOwnReparentRequestEvent(final GeoEvent event) {
		final GeoManager geoManager = getParentGeoManager();
		if (geoManager != null) {
			geoManager.onChangeParentRequest(event);
		} else {
			doUpdateParent();
		}

	}

	@Override
	public void cancelPendingMove() {
		setWidth(getWidth());
		setHeight(getHeight());
	}

	@Override
	public void cancelPendingResize() {
		setX(getX());
		setY(getY());
	}

	@Override
	public void addGeoEventHandler(final Object geoEventHandler) {
		this.eventBus.register(geoEventHandler);
	}

	@Subscribe
	public void handleChildLeftNotifyEvent(final GeoChildLeftEvent event) {
		final GeoTransformableRectangle child = event.getSource();
		this.children.remove(child);
	}

	@Subscribe
	public void handleChildAddedNotifyEvent(final GeoChildAddedEvent event) {
		final GeoTransformableRectangle child = event.getSource();
		this.children.add(child);
	}

	@Override
	public void removeGeoEventHandler(final Object geoEventHandler) {
		this.eventBus.unregister(geoEventHandler);
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
		return this.desiredHeight;
	}

	protected int getDesiredWidth() {
		return this.desiredWidth;
	}

	protected int getDesiredX() {
		return this.desiredX;
	}

	protected int getDesiredY() {
		return this.desiredY;
	}

	protected boolean getDesiredVisibility() {
		return this.desiredVisibility;
	}

	protected GeoTransformableRectangle getDesiredParent() {
		return this.desiredParent;
	}

	@Override
	public void doShow() {
		doShow(true);
		final GeoShowEvent geoEvent = this.geoEventFactory
				.createGeoShowEvent(this,
									toGeoTransformation());
		handleShowNotifyEvent(geoEvent);
		this.eventBus.post(geoEvent);
	}

	protected void doShow(final boolean execute) {
		if (execute) {
			execShow();
		}
		this.visible = true;
	}

	protected void handleShowNotifyEvent(final GeoShowEvent geoEvent) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			geoManager.onShowNotify(geoEvent);
		}
	}

	protected void execShow() {
		getGeoExecutor().show(this);
	}

	@Override
	public void doHide() {
		doHide(true);
		final GeoHideEvent geoEvent = this.geoEventFactory
				.createGeoHideEvent(this,
									toGeoTransformation());
		handleHideEvent(geoEvent);
		this.eventBus.post(geoEvent);
	}

	protected void handleHideEvent(final GeoHideEvent geoEvent) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			geoManager.onHideNotify(geoEvent);
		}
	}

	protected void doHide(final boolean execute) {
		if (execute) {
			execHide();
		}
		this.visible = false;
	}

	protected void execHide() {
		getGeoExecutor().hide(this);
	}

	@Override
	public void requestShow() {
		final GeoShowRequestEvent event = this.geoEventFactory
				.createGeoShowRequestEvent(	this,
											toGeoTransformation());
		this.eventBus.post(event);
		handleShowRequestEvent(event);
	}

	protected void handleShowRequestEvent(final GeoShowRequestEvent event) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			geoManager.onShowRequest(event);
		}
	}

	@Override
	public void requestHide() {
		final GeoHideRequestEvent event = this.geoEventFactory
				.createGeoHideRequestEvent(	this,
											toGeoTransformation());
		this.eventBus.post(event);
		handleHideRequestEvent(event);
	}

	protected void handleHideRequestEvent(final GeoHideRequestEvent event) {
		final GeoManager geoManager = getGeoManager();
		if (geoManager != null) {
			geoManager.onHideRequest(event);
		}
	}
}
