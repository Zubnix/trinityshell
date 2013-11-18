/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.shell.api.scene;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.bindingkey.ShellScene;
import org.trinity.shell.api.scene.event.ShellNodeChildAddedEvent;
import org.trinity.shell.api.scene.event.ShellNodeChildLeftEvent;
import org.trinity.shell.api.scene.event.ShellNodeEvent;
import org.trinity.shell.api.scene.manager.ShellLayoutManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * An abstract base implementation of a {@link ShellNodeParent}.
 * **************************************
 */
@ExecutionContext(ShellExecutor.class)
public abstract class AbstractShellNodeParent extends AbstractAsyncShellNodeParent {

	private final LinkedList<AbstractShellNode> children = new LinkedList<>();
	private Optional<ShellLayoutManager> optionalLayoutManager = Optional.absent();

	protected AbstractShellNodeParent(	@Nonnull @ShellScene final AsyncListenable shellScene,
										@Nonnull @ShellExecutor final ListeningExecutorService shellExecutor) {
		super(	shellScene,
				shellExecutor);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The returned array is a copy of the internal array.
	 */
	@Override
	public List<AbstractShellNode> getChildrenImpl() {
		return new ArrayList<>(this.children);
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
	public Void setLayoutManagerImpl(@Nullable final ShellLayoutManager shellLayoutManager) {
		this.optionalLayoutManager = Optional.of(shellLayoutManager);
		if (this.optionalLayoutManager.isPresent()) {
			register(shellLayoutManager);
		}
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

	protected void handleChildReparent(@Nonnull final ShellNode child) {
		checkArgument(child instanceof AbstractShellNode);

		final ShellNodeEvent shellNodeEvent;
		if (this.children.remove(child)) {
			shellNodeEvent = new ShellNodeChildLeftEvent(	this,
															toGeoTransformationImpl());
		} else {
			this.children.addLast((AbstractShellNode) child);
			shellNodeEvent = new ShellNodeChildAddedEvent(	this,
															toGeoTransformationImpl());
		}
		post(shellNodeEvent);
	}

	protected void handleChildStacking(	@Nonnull final ShellNode child,
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
	 * <p>
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
