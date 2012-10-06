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
package org.trinity.shell.api.node;

import org.trinity.shell.api.node.event.ShellNodeDestroyEvent;
import org.trinity.shell.api.node.event.ShellNodeEvent;
import org.trinity.shell.api.node.event.ShellNodeHideEvent;
import org.trinity.shell.api.node.event.ShellNodeHideRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeLowerEvent;
import org.trinity.shell.api.node.event.ShellNodeLowerRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeMoveEvent;
import org.trinity.shell.api.node.event.ShellNodeMoveRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeMoveResizeEvent;
import org.trinity.shell.api.node.event.ShellNodeMoveResizeRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeRaiseEvent;
import org.trinity.shell.api.node.event.ShellNodeRaiseRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeReparentEvent;
import org.trinity.shell.api.node.event.ShellNodeReparentRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeResizeEvent;
import org.trinity.shell.api.node.event.ShellNodeResizeRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeShowEvent;
import org.trinity.shell.api.node.event.ShellNodeShowRequestEvent;
import org.trinity.shell.api.node.manager.ShellLayoutManager;

import com.google.common.eventbus.EventBus;

// TODO Let geo events travel downwards to children to notify them that one of
// their parents has changed

// TODO move to api?
public abstract class AbstractShellNode implements ShellNode {

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

	private ShellNodeParent parent;
	private ShellNodeParent desiredParent;

	private boolean destroyed;

	private final EventBus nodeEventBus;

	protected AbstractShellNode(final EventBus nodeEventBus) {
		this.nodeEventBus = nodeEventBus;
		this.nodeEventBus.register(this);
	}

	@Override
	public ShellNodeTransformation toGeoTransformation() {
		return new ShellNodeTransformation(	getX(),
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
	public abstract ShellNodeExecutor getShellNodeExecutor();

	@Override
	public boolean isDestroyed() {
		return this.destroyed;
	}

	@Override
	public ShellNodeParent getParent() {
		return this.parent;
	}

	@Override
	public void setParent(final ShellNodeParent parent) {
		this.desiredParent = parent;
	}

	@Override
	public void requestReparent() {
		// update parent to new parent
		final ShellNodeEvent event = new ShellNodeReparentRequestEvent(	this,
																		toGeoTransformation());
		this.nodeEventBus.post(event);
		final ShellLayoutManager shellLayoutManager = getParentLayoutManager(getParent());
		if (shellLayoutManager == null) {
			doReparent();
		}
	}

	@Override
	public int getAbsoluteX() {
		if ((getParent() == null) || getParent().equals(this)) {
			return getX();
		}
		return getParent().getAbsoluteX() + getX();
	}

	@Override
	public int getAbsoluteY() {
		if ((getParent() == null) || getParent().equals(this)) {
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
		if (!this.visible) {
			return false;
		}
		// we're in the visible state.

		// recursion safeguard
		if (getParent() == this) {
			return true;
		}

		// check if our parent is visible.
		final boolean parentVisible = (getParent() != null) && getParent().isVisible();
		return parentVisible;
	}

	protected ShellLayoutManager getParentLayoutManager(final ShellNodeParent parent) {
		if (parent == null) {
			return null;
		}

		final ShellLayoutManager parentLayoutManager = parent.getLayoutManager();
		if (parentLayoutManager == null) {
			return getParentLayoutManager(parent.getParent());
		}

		return parentLayoutManager;
	}

	@Override
	public void requestMove() {
		final ShellNodeMoveRequestEvent event = new ShellNodeMoveRequestEvent(	this,
																				toGeoTransformation());
		this.nodeEventBus.post(event);
		final ShellLayoutManager shellLayoutManager = getParentLayoutManager(getParent());
		if (shellLayoutManager == null) {
			doMove();
		}
	}

	@Override
	public void requestResize() {
		final ShellNodeEvent event = new ShellNodeResizeRequestEvent(	this,
																		toGeoTransformation());
		this.nodeEventBus.post(event);
		final ShellLayoutManager shellLayoutManager = getParentLayoutManager(getParent());
		if (shellLayoutManager == null) {
			doResize();
		}
	}

	@Override
	public void requestMoveResize() {
		final ShellNodeMoveResizeRequestEvent event = new ShellNodeMoveResizeRequestEvent(	this,
																							toGeoTransformation());
		this.nodeEventBus.post(event);
		final ShellLayoutManager shellLayoutManager = getParentLayoutManager(getParent());
		if (shellLayoutManager == null) {
			doMoveResize();
		}
	}

	@Override
	public void requestRaise() {
		final ShellNodeEvent event = new ShellNodeRaiseRequestEvent(this,
																	toGeoTransformation());
		this.nodeEventBus.post(event);
		final ShellLayoutManager shellLayoutManager = getParentLayoutManager(getParent());
		if (shellLayoutManager == null) {
			doRaise();
		}
	}

	@Override
	public void requestLower() {
		final ShellNodeLowerRequestEvent event = new ShellNodeLowerRequestEvent(this,
																				toGeoTransformation());
		this.nodeEventBus.post(event);
		if (getParentLayoutManager(getParent()) == null) {
			doLower();
		}
	}

	@Override
	public void doMove() {
		doMove(true);
	}

	protected void doMove(final boolean execute) {
		flushPlaceValues();
		if (execute) {
			execMove();
		}
		final ShellNodeMoveEvent geoEvent = new ShellNodeMoveEvent(	this,
																	toGeoTransformation());
		this.nodeEventBus.post(geoEvent);
	}

	protected void execMove() {
		getShellNodeExecutor().move(getDesiredX(),
									getDesiredY());
	}

	private void flushPlaceValues() {
		this.x = getDesiredX();
		this.y = getDesiredY();
	}

	@Override
	public void doResize() {
		doResize(true);

	}

	protected void doResize(final boolean execute) {
		flushSizeValues();
		if (execute) {
			execResize();
		}
		final ShellNodeResizeEvent geoEvent = new ShellNodeResizeEvent(	this,
																		toGeoTransformation());
		this.nodeEventBus.post(geoEvent);
	}

	protected void execResize() {
		getShellNodeExecutor().resize(	getDesiredWidth(),
										getDesiredHeight());
	}

	private void flushSizeValues() {
		this.width = getDesiredWidth();
		this.height = getDesiredHeight();
	}

	@Override
	public void doMoveResize() {
		doMoveResize(true);

	}

	protected void doMoveResize(final boolean execute) {
		flushSizePlaceValues();
		if (execute) {
			execMoveResize();
		}
		final ShellNodeMoveResizeEvent geoEvent = new ShellNodeMoveResizeEvent(	this,
																				toGeoTransformation());
		this.nodeEventBus.post(geoEvent);
	}

	protected void execMoveResize() {
		getShellNodeExecutor().moveResize(	getDesiredX(),
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
	}

	protected void doDestroy(final boolean execute) {
		this.destroyed = true;
		if (execute) {
			execDestroy();
		}
		final ShellNodeDestroyEvent geoEvent = new ShellNodeDestroyEvent(	this,
																			toGeoTransformation());
		this.nodeEventBus.post(geoEvent);
	}

	protected void execDestroy() {
		getShellNodeExecutor().destroy();
	}

	@Override
	public void doRaise() {
		doRaise(true);
	}

	protected void doRaise(final boolean execute) {
		if (execute) {
			execRaise();
		}
		final ShellNodeRaiseEvent geoEvent = new ShellNodeRaiseEvent(	this,
																		toGeoTransformation());
		this.nodeEventBus.post(geoEvent);
	}

	protected void execRaise() {
		getShellNodeExecutor().raise();
	}

	@Override
	public void doLower() {
		doLower(true);
	}

	protected void doLower(final boolean execute) {
		if (execute) {
			execLower();
		}
		final ShellNodeLowerEvent geoEvent = new ShellNodeLowerEvent(	this,
																		toGeoTransformation());
		this.nodeEventBus.post(geoEvent);
	}

	protected void execLower() {
		getShellNodeExecutor().lower();
	}

	@Override
	public void doReparent() {
		doReparent(true);
		getParent().handleChildReparentEvent(this);
	}

	protected void doReparent(final boolean execute) {
		flushParentValue();
		if (execute) {
			execReparent();
		}

		// Make sure we have the same size
		// and place in our new parent
		// as in our old parent.
		doMoveResize(execute);
		final ShellNodeReparentEvent geoEvent = new ShellNodeReparentEvent(	this,
																			toGeoTransformation());

		this.nodeEventBus.post(geoEvent);
	}

	protected void execReparent() {
		getShellNodeExecutor().reparent(getDesiredParent());
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
	public void addShellNodeEventHandler(final Object geoEventHandler) {
		this.nodeEventBus.register(geoEventHandler);
	}

	@Override
	public void removeShellNodeEventHandler(final Object geoEventHandler) {
		this.nodeEventBus.unregister(geoEventHandler);
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

	protected ShellNodeParent getDesiredParent() {
		return this.desiredParent;
	}

	@Override
	public void doShow() {
		doShow(true);
	}

	protected void doShow(final boolean execute) {
		this.visible = true;
		if (execute) {
			execShow();
		}
		final ShellNodeShowEvent geoEvent = new ShellNodeShowEvent(	this,
																	toGeoTransformation());
		this.nodeEventBus.post(geoEvent);
	}

	protected void execShow() {
		getShellNodeExecutor().show();
	}

	@Override
	public void doHide() {
		doHide(true);
	}

	protected void doHide(final boolean execute) {
		this.visible = false;
		if (execute) {
			execHide();
		}
		final ShellNodeHideEvent geoEvent = new ShellNodeHideEvent(	this,
																	toGeoTransformation());
		this.nodeEventBus.post(geoEvent);
	}

	protected void execHide() {
		getShellNodeExecutor().hide();
	}

	@Override
	public void requestShow() {
		final ShellNodeShowRequestEvent event = new ShellNodeShowRequestEvent(	this,
																				toGeoTransformation());
		this.nodeEventBus.post(event);
		if (getParentLayoutManager(getParent()) == null) {
			doShow();
		}
	}

	@Override
	public void requestHide() {
		final ShellNodeHideRequestEvent event = new ShellNodeHideRequestEvent(	this,
																				toGeoTransformation());
		this.nodeEventBus.post(event);
		if (getParentLayoutManager(getParent()) == null) {
			doHide();
		}
	}
}