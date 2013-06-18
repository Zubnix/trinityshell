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
package org.trinity.shell.api.scene;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.LinkedList;

import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.OwnerThread;
import org.trinity.shell.api.scene.event.ShellNodeChildAddedEvent;
import org.trinity.shell.api.scene.event.ShellNodeChildLeftEvent;
import org.trinity.shell.api.scene.event.ShellNodeEvent;
import org.trinity.shell.api.scene.manager.ShellLayoutManager;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListeningExecutorService;

/**
 * ************************************ An abstract base implementation of a
 * {@link ShellNodeParent}.
 * <p/>
 * **************************************
 */
@OwnerThread("Shell")
public abstract class AbstractShellNodeParent extends AbstractAsyncShellNodeParent implements ShellNodeParent {

	private final LinkedList<AbstractShellNode> children = new LinkedList<AbstractShellNode>();

	private Optional<ShellLayoutManager> optionalLayoutManager = Optional.absent();

	protected AbstractShellNodeParent(	final ShellScene shellScene,
										final ListeningExecutorService shellExecutor) {
		super(	shellScene,
				shellExecutor);
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * The returned array is a copy of the internal array.
	 */
	@Override
	public AbstractShellNode[] getChildrenImpl() {
		return this.children.toArray(new AbstractShellNode[this.children.size()]);
	}

	/**
	 * Refresh the on-screen position of this node's children.
	 */
	protected void updateChildrenPosition() {
		for (final AbstractShellNode child : getChildrenImpl()) {
			final Coordinate childPosition = child.getPositionImpl();
			child.getShellNodeGeometryDelegate().move(childPosition);
		}
	}

	@Override
	public Optional<ShellLayoutManager> getLayoutManagerImpl() {
		return this.optionalLayoutManager;
	}

	@Override
	public Void setLayoutManagerImpl(final ShellLayoutManager shellLayoutManager) {
		this.optionalLayoutManager = Optional.of(shellLayoutManager);
		register(shellLayoutManager);
		return null;
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
		layout();
	}

	@Override
	protected void doResize(final boolean execute) {
		super.doResize(execute);
		layout();
	}

	protected void handleChildReparent(final ShellNode child) {
		checkArgument(child instanceof AbstractShellNode);

		final ShellNodeEvent shellNodeEvent;
		if (this.children.contains(child)) {
			this.children.remove(child);
			shellNodeEvent = new ShellNodeChildLeftEvent(	this,
															toGeoTransformationImpl());
		} else {
			this.children.addLast((AbstractShellNode) child);
			shellNodeEvent = new ShellNodeChildAddedEvent(	this,
															toGeoTransformationImpl());
		}
		post(shellNodeEvent);
	}

	protected void handleChildStacking(	final ShellNode child,
										final boolean raised) {
		checkArgument(child instanceof AbstractShellNode);
		checkArgument(this.children.remove(child));

		if (raised) {
			this.children.addLast((AbstractShellNode) child);
		} else {
			this.children.addFirst((AbstractShellNode) child);
		}
		// TODO fire a specific event?
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * This call has no effect if no {@link ShellLayoutManager} is set for this
	 * node.
	 */
	@Override
	public Void layoutImpl() {
		final Optional<ShellLayoutManager> optionalLayoutManager = getLayoutManagerImpl();
		if (optionalLayoutManager.isPresent()) {
			optionalLayoutManager.get().layout(this);
		}
		return null;
	}
}
