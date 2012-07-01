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

import java.util.HashSet;
import java.util.Set;

import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.GeoTransformation;
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

	private final Set<GeoTransformableRectangle> children = new HashSet<GeoTransformableRectangle>();

	private GeoManager geoManager;

	private final EventBus eventBus;
	private final GeoEventFactory geoEventFactory;

	public AbstractGeoTransformableRectangle(	final EventBus eventBus,
												final GeoEventFactory geoEventFactory) {
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
		final GeoManager geoManager = getParentGeoManager();
		if (geoManager == null) {
			doReparent();
		}
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
		final GeoManager geoManager = getParentGeoManager();
		if (geoManager == null) {
			doMove();
		}
	}

	@Override
	public void requestResize() {
		final GeoEvent event = this.geoEventFactory
				.createGeoResizeRequestEvent(	this,
												toGeoTransformation());
		this.eventBus.post(event);
		final GeoManager geoManager = getParentGeoManager();
		if (geoManager == null) {
			doResize();
		}
	}

	@Override
	public void requestMoveResize() {
		final GeoMoveResizeRequestEvent event = this.geoEventFactory
				.createGeoMoveResizeRequestEvent(	this,
													toGeoTransformation());
		this.eventBus.post(event);
		final GeoManager geoManager = getParentGeoManager();
		if (geoManager == null) {
			doMoveResize();
		}
	}

	@Override
	public void requestRaise() {
		final GeoEvent event = this.geoEventFactory
				.createGeoRaiseRequestEvent(this,
											toGeoTransformation());
		this.eventBus.post(event);
		final GeoManager geoManager = getParentGeoManager();
		if (geoManager == null) {
			doRaise();
		}
	}

	@Override
	public void requestLower() {
		final GeoLowerRequestEvent event = this.geoEventFactory
				.createGeoLowerRequestEvent(this,
											toGeoTransformation());
		this.eventBus.post(event);
		if (getParentGeoManager() == null) {
			doLower();
		}
	}

	@Override
	public void doMove() {
		doMove(true);
		final GeoMoveEvent geoEvent = this.geoEventFactory
				.createGeoMoveEvent(this,
									toGeoTransformation());
		this.eventBus.post(geoEvent);
	}

	protected void doMove(final boolean execute) {
		if (execute) {
			execMove();
		}
		flushPlaceValues();
		updateChildrenPosition();
	}

	protected void updateChildrenPosition() {
		for (final GeoTransformableRectangle child : getChildren()) {
			child.getGeoExecutor().updatePlace(	this,
												child.getX(),
												child.getY());
		}
	}

	protected void execMove() {
		getGeoExecutor().updatePlace(	this,
										getDesiredX(),
										getDesiredY());
	}

	private void flushPlaceValues() {
		this.x = getDesiredX();
		this.y = getDesiredY();
	}

	@Override
	public void doResize() {
		doResize(true);
		final GeoResizeEvent geoEvent = this.geoEventFactory
				.createGeoResizeEvent(	this,
										toGeoTransformation());
		this.eventBus.post(geoEvent);
	}

	protected void doResize(final boolean execute) {
		if (execute) {
			execResize();
		}
		flushSizeValues();
	}

	protected void execResize() {
		getGeoExecutor().updateSize(this,
									getDesiredWidth(),
									getDesiredHeight());
	}

	private void flushSizeValues() {
		this.width = getDesiredWidth();
		this.height = getDesiredHeight();
	}

	@Override
	public void doMoveResize() {
		doMoveResize(true);
		final GeoMoveResizeEvent geoEvent = this.geoEventFactory
				.createGeoMoveResizeEvent(	this,
											toGeoTransformation());
		this.eventBus.post(geoEvent);
	}

	protected void doMoveResize(final boolean execute) {
		if (execute) {
			execMoveResize();
		}
		flushSizePlaceValues();
		updateChildrenPosition();

	}

	protected void execMoveResize() {
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

	@Override
	public void doDestroy() {
		this.doDestroy(true);
		final GeoDestroyEvent geoEvent = this.geoEventFactory
				.createGeoDestroyEvent(	this,
										toGeoTransformation());
		this.eventBus.post(geoEvent);
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
		this.eventBus.post(geoEvent);
	}

	protected void doRaise(final boolean execute) {
		if (execute) {
			execRaise();
		}
	}

	protected void execRaise() {
		getGeoExecutor().raise(this);
	}

	@Override
	public void doLower() {
		doLower(true);
		final GeoLowerEvent geoEvent = this.geoEventFactory
				.createGeoLowerEvent(	this,
										toGeoTransformation());
		this.eventBus.post(geoEvent);
	}

	protected void doLower(final boolean execute) {
		if (execute) {
			execLower();
		}
	}

	protected void execLower() {
		getGeoExecutor().lower(this);
	}

	@Override
	public void doReparent() {
		doReparent(true);
		final GeoReparentEvent geoEvent = this.geoEventFactory
				.createGeoReparentEvent(this,
										toGeoTransformation());
		this.eventBus.post(geoEvent);
	}

	protected void doReparent(final boolean execute) {
		if (execute) {
			execReparent();
		}
		flushParentValue();
		// make sure the new parent gets notified
		addGeoEventHandler(getParent());
		// Make sure we have the same size
		// and place in our new parent
		// as in our old parent.
		doMoveResize();
	}

	@Subscribe
	public void
			handleChildReparentEvent(final GeoReparentEvent geoReparentEvent) {
		final GeoTransformableRectangle child = geoReparentEvent.getSource();
		GeoEvent geoEvent;
		if (this.children.contains(child)) {
			this.children.remove(child);
			child.removeGeoEventHandler(this);
			geoEvent = this.geoEventFactory
					.createGeoChildLeftEvent(	this,
												toGeoTransformation());
		} else {
			this.children.add(child);
			geoEvent = this.geoEventFactory
					.createGeoChildAddedEvent(	this,
												toGeoTransformation());
		}
		this.eventBus.post(geoEvent);
	}

	protected void execReparent() {
		getGeoExecutor().updateParent(	this,
										getDesiredParent());
	}

	private void flushParentValue() {
		this.parent = getDesiredParent();
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
		this.eventBus.register(geoManager);
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
		this.eventBus.post(geoEvent);
	}

	protected void doShow(final boolean execute) {
		if (execute) {
			execShow();
		}
		this.visible = true;
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
		this.eventBus.post(geoEvent);
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
		if (getParentGeoManager() == null) {
			doShow();
		}
	}

	@Override
	public void requestHide() {
		final GeoHideRequestEvent event = this.geoEventFactory
				.createGeoHideRequestEvent(	this,
											toGeoTransformation());
		this.eventBus.post(event);
		if (getParentGeoManager() == null) {
			doHide();
		}
	}
}