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
package org.trinity.shell.geo.api;

import java.util.HashSet;
import java.util.Set;

import org.trinity.shell.geo.api.event.ShellNodeChildAddedEvent;
import org.trinity.shell.geo.api.event.ShellNodeChildLeftEvent;
import org.trinity.shell.geo.api.event.ShellNodeDestroyEvent;
import org.trinity.shell.geo.api.event.ShellNodeEvent;
import org.trinity.shell.geo.api.event.ShellNodeHideEvent;
import org.trinity.shell.geo.api.event.ShellNodeHideRequestEvent;
import org.trinity.shell.geo.api.event.ShellNodeLowerEvent;
import org.trinity.shell.geo.api.event.ShellNodeLowerRequestEvent;
import org.trinity.shell.geo.api.event.ShellNodeMoveEvent;
import org.trinity.shell.geo.api.event.ShellNodeMoveRequestEvent;
import org.trinity.shell.geo.api.event.ShellNodeMoveResizeEvent;
import org.trinity.shell.geo.api.event.ShellNodeMoveResizeRequestEvent;
import org.trinity.shell.geo.api.event.ShellNodeRaiseEvent;
import org.trinity.shell.geo.api.event.ShellNodeRaiseRequestEvent;
import org.trinity.shell.geo.api.event.ShellNodeReparentEvent;
import org.trinity.shell.geo.api.event.ShellNodeReparentRequestEvent;
import org.trinity.shell.geo.api.event.ShellNodeResizeEvent;
import org.trinity.shell.geo.api.event.ShellNodeResizeRequestEvent;
import org.trinity.shell.geo.api.event.ShellNodeShowEvent;
import org.trinity.shell.geo.api.event.ShellNodeShowRequestEvent;
import org.trinity.shell.geo.api.manager.ShellLayoutManager;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

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

	private ShellNode parent;
	private ShellNode desiredParent;

	private boolean destroyed;

	private final Set<ShellNode> children = new HashSet<ShellNode>();

	private ShellLayoutManager shellLayoutManager;

	private final EventBus nodeEventBus;

	protected AbstractShellNode(final EventBus nodeEventBus) {
		this.nodeEventBus = nodeEventBus;
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
	public abstract ShellNodeExecutor getNodeExecutor();

	@Override
	public boolean isDestroyed() {
		return this.destroyed;
	}

	@Override
	public ShellNode getParent() {
		return this.parent;
	}

	@Override
	public void setParent(final ShellNode parent) {
		this.desiredParent = parent;
	}

	@Override
	public void requestReparent() {
		// update parent to new parent
		final ShellNodeEvent event = new ShellNodeReparentRequestEvent(	this,
																		toGeoTransformation());
		this.nodeEventBus.post(event);
		final ShellLayoutManager shellLayoutManager = getParentGeoManager();
		if (shellLayoutManager == null) {
			doReparent();
		}
	}

	@Override
	public ShellNode[] getChildren() {
		return this.children.toArray(new ShellNode[this.children.size()]);
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
		if ((getParent() != null) && (getParent() != this)) {
			parentVisible = getParent().isVisible();
		}
		return this.visible && parentVisible;
	}

	@Override
	public ShellLayoutManager getParentGeoManager() {
		final ShellNode parent = getParent();
		if (parent == null) {
			return null;
		}

		final ShellLayoutManager parentGeoManager = parent.getLayoutManager();
		if (parentGeoManager == null) {
			return parent.getParentGeoManager();
		}

		return parentGeoManager;
	}

	@Override
	public void requestMove() {
		final ShellNodeMoveRequestEvent event = new ShellNodeMoveRequestEvent(	this,
																				toGeoTransformation());
		this.nodeEventBus.post(event);
		final ShellLayoutManager shellLayoutManager = getParentGeoManager();
		if (shellLayoutManager == null) {
			doMove();
		}
	}

	@Override
	public void requestResize() {
		final ShellNodeEvent event = new ShellNodeResizeRequestEvent(	this,
																		toGeoTransformation());
		this.nodeEventBus.post(event);
		final ShellLayoutManager shellLayoutManager = getParentGeoManager();
		if (shellLayoutManager == null) {
			doResize();
		}
	}

	@Override
	public void requestMoveResize() {
		final ShellNodeMoveResizeRequestEvent event = new ShellNodeMoveResizeRequestEvent(	this,
																							toGeoTransformation());
		this.nodeEventBus.post(event);
		final ShellLayoutManager shellLayoutManager = getParentGeoManager();
		if (shellLayoutManager == null) {
			doMoveResize();
		}
	}

	@Override
	public void requestRaise() {
		final ShellNodeEvent event = new ShellNodeRaiseRequestEvent(this,
																	toGeoTransformation());
		this.nodeEventBus.post(event);
		final ShellLayoutManager shellLayoutManager = getParentGeoManager();
		if (shellLayoutManager == null) {
			doRaise();
		}
	}

	@Override
	public void requestLower() {
		final ShellNodeLowerRequestEvent event = new ShellNodeLowerRequestEvent(this,
																				toGeoTransformation());
		this.nodeEventBus.post(event);
		if (getParentGeoManager() == null) {
			doLower();
		}
	}

	@Override
	public void doMove() {
		doMove(true);
		final ShellNodeMoveEvent geoEvent = new ShellNodeMoveEvent(	this,
																	toGeoTransformation());
		this.nodeEventBus.post(geoEvent);
	}

	protected void doMove(final boolean execute) {
		if (execute) {
			execMove();
		}
		flushPlaceValues();
		updateChildrenPosition();
	}

	protected void updateChildrenPosition() {
		for (final ShellNode child : getChildren()) {
			child.getNodeExecutor().move(	this,
											child.getX(),
											child.getY());
		}
	}

	protected void execMove() {
		getNodeExecutor().move(	this,
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
		final ShellNodeResizeEvent geoEvent = new ShellNodeResizeEvent(	this,
																		toGeoTransformation());
		this.nodeEventBus.post(geoEvent);
	}

	protected void doResize(final boolean execute) {
		if (execute) {
			execResize();
		}
		flushSizeValues();
	}

	protected void execResize() {
		getNodeExecutor().resize(	this,
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
		final ShellNodeMoveResizeEvent geoEvent = new ShellNodeMoveResizeEvent(	this,
																				toGeoTransformation());
		this.nodeEventBus.post(geoEvent);
	}

	protected void doMoveResize(final boolean execute) {
		if (execute) {
			execMoveResize();
		}
		flushSizePlaceValues();
		updateChildrenPosition();

	}

	protected void execMoveResize() {
		final ShellNodeExecutor shellNodeExecutor = getNodeExecutor();
		shellNodeExecutor.moveResize(	this,
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
		final ShellNodeDestroyEvent geoEvent = new ShellNodeDestroyEvent(	this,
																			toGeoTransformation());
		this.nodeEventBus.post(geoEvent);
	}

	protected void doDestroy(final boolean execute) {
		if (execute) {
			execDestroy();
		}
		this.destroyed = true;
	}

	protected void execDestroy() {
		getNodeExecutor().destroy(this);
	}

	@Override
	public void doRaise() {
		doRaise(true);
		final ShellNodeRaiseEvent geoEvent = new ShellNodeRaiseEvent(	this,
																		toGeoTransformation());
		this.nodeEventBus.post(geoEvent);
	}

	protected void doRaise(final boolean execute) {
		if (execute) {
			execRaise();
		}
	}

	protected void execRaise() {
		getNodeExecutor().raise(this);
	}

	@Override
	public void doLower() {
		doLower(true);
		final ShellNodeLowerEvent geoEvent = new ShellNodeLowerEvent(	this,
																		toGeoTransformation());
		this.nodeEventBus.post(geoEvent);
	}

	protected void doLower(final boolean execute) {
		if (execute) {
			execLower();
		}
	}

	protected void execLower() {
		getNodeExecutor().lower(this);
	}

	@Override
	public void doReparent() {
		doReparent(true);
		final ShellNodeReparentEvent geoEvent = new ShellNodeReparentEvent(	this,
																			toGeoTransformation());
		this.nodeEventBus.post(geoEvent);
	}

	protected void doReparent(final boolean execute) {
		if (execute) {
			execReparent();
		}
		flushParentValue();
		// make sure the new parent gets notified
		addShellNodeEventHandler(getParent());
		// Make sure we have the same size
		// and place in our new parent
		// as in our old parent.
		doMoveResize();
	}

	@Subscribe
	public void handleChildReparentEvent(final ShellNodeReparentEvent shellNodeReparentEvent) {
		final ShellNode child = shellNodeReparentEvent.getSource();
		ShellNodeEvent shellNodeEvent;
		if (this.children.contains(child)) {
			this.children.remove(child);
			child.removeShellNodeEventHandler(this);
			shellNodeEvent = new ShellNodeChildLeftEvent(	this,
															toGeoTransformation());
		} else {
			this.children.add(child);
			shellNodeEvent = new ShellNodeChildAddedEvent(	this,
															toGeoTransformation());
		}
		this.nodeEventBus.post(shellNodeEvent);
	}

	protected void execReparent() {
		getNodeExecutor().reparent(	this,
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
	public void addShellNodeEventHandler(final Object geoEventHandler) {
		this.nodeEventBus.register(geoEventHandler);
	}

	@Override
	public void removeShellNodeEventHandler(final Object geoEventHandler) {
		this.nodeEventBus.unregister(geoEventHandler);
	}

	@Override
	public ShellLayoutManager getLayoutManager() {
		return this.shellLayoutManager;
	}

	@Override
	public void setLayoutManager(final ShellLayoutManager shellLayoutManager) {
		this.shellLayoutManager = shellLayoutManager;
		this.nodeEventBus.register(shellLayoutManager);
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

	protected ShellNode getDesiredParent() {
		return this.desiredParent;
	}

	@Override
	public void doShow() {
		doShow(true);
		final ShellNodeShowEvent geoEvent = new ShellNodeShowEvent(	this,
																	toGeoTransformation());
		this.nodeEventBus.post(geoEvent);
	}

	protected void doShow(final boolean execute) {
		if (execute) {
			execShow();
		}
		this.visible = true;
	}

	protected void execShow() {
		getNodeExecutor().show(this);
	}

	@Override
	public void doHide() {
		doHide(true);
		final ShellNodeHideEvent geoEvent = new ShellNodeHideEvent(	this,
																	toGeoTransformation());
		this.nodeEventBus.post(geoEvent);
	}

	protected void doHide(final boolean execute) {
		if (execute) {
			execHide();
		}
		this.visible = false;
	}

	protected void execHide() {
		getNodeExecutor().hide(this);
	}

	@Override
	public void requestShow() {
		final ShellNodeShowRequestEvent event = new ShellNodeShowRequestEvent(	this,
																				toGeoTransformation());
		this.nodeEventBus.post(event);
		if (getParentGeoManager() == null) {
			doShow();
		}
	}

	@Override
	public void requestHide() {
		final ShellNodeHideRequestEvent event = new ShellNodeHideRequestEvent(	this,
																				toGeoTransformation());
		this.nodeEventBus.post(event);
		if (getParentGeoManager() == null) {
			doHide();
		}
	}
}