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

import java.util.HashSet;
import java.util.Set;

import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.event.ShellNodeChildAddedEvent;
import org.trinity.shell.api.scene.event.ShellNodeChildLeftEvent;
import org.trinity.shell.api.scene.event.ShellNodeEvent;
import org.trinity.shell.api.scene.manager.ShellLayoutManager;

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;

public abstract class AbstractShellSurfaceParent extends AbstractShellSurface
		implements ShellSurfaceParent {

	private final EventBus nodeEventBus;

	private final Set<ShellNode> children = new HashSet<ShellNode>();

	private Optional<ShellLayoutManager> shellLayoutManager = Optional.absent();

	public AbstractShellSurfaceParent(final EventBus nodeEventBus) {
		super(nodeEventBus);
		this.nodeEventBus = nodeEventBus;
	}

	@Override
	public ShellNode[] getChildren() {
		return this.children.toArray(new ShellNode[this.children.size()]);
	}

	protected void updateChildrenPosition() {
		for (final ShellNode child : getChildren()) {
			final int childX = child.getX();
			final int childY = child.getY();
			child.getShellNodeExecutor().move(	childX,
												childY);
		}
	}

	@Override
	public Optional<ShellLayoutManager> getLayoutManager() {
		return this.shellLayoutManager;
	}

	@Override
	public void setLayoutManager(final ShellLayoutManager shellLayoutManager) {
		this.shellLayoutManager = Optional.of(shellLayoutManager);
		this.nodeEventBus.register(shellLayoutManager);
	}

	@Override
	protected void doMove(final boolean execute) {
		super.doMove(execute);
		updateChildrenPosition();
	}

	@Override
	protected void doMoveResize(final boolean execute) {
		super.doMoveResize(execute);
		updateChildrenPosition();
	}

	@Override
	public void handleChildReparentEvent(final ShellNode child) {
		ShellNodeEvent shellNodeEvent;
		if (this.children.contains(child)) {
			this.children.remove(child);
			// child.removeShellNodeEventHandler(this);
			shellNodeEvent = new ShellNodeChildLeftEvent(	this,
															toGeoTransformation());
			this.nodeEventBus.post(shellNodeEvent);
		} else {
			this.children.add(child);
			shellNodeEvent = new ShellNodeChildAddedEvent(	this,
															toGeoTransformation());
			this.nodeEventBus.post(shellNodeEvent);
		}
	}

	@Override
	public void layout() {
		final Optional<ShellLayoutManager> layoutManager = getLayoutManager();
		if (layoutManager.isPresent()) {
			layoutManager.get().layout(this);
		}
	}
}