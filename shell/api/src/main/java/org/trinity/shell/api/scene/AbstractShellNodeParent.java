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
import org.trinity.foundation.api.shared.Listenable;
import org.trinity.foundation.api.shared.Coordinate;
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
public abstract class AbstractShellNodeParent extends AbstractShellNode implements ShellNodeParent {

	private final LinkedList<ShellNode> children = new LinkedList<>();
	private Optional<ShellLayoutManager> optionalLayoutManager = Optional.absent();

	protected AbstractShellNodeParent(	@Nonnull @ShellScene final Listenable shellScene) {
		super(	shellScene);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The returned array is a copy of the internal array.
	 */
	@Override
	public List<ShellNode> getChildren() {
		return new ArrayList<>(this.children);
	}

	/**
	 * Refresh the on-screen position of this node's children.
	 */
	protected void updateChildrenPosition() {
		for (final ShellNode child : getChildren()) {
			final Coordinate childPosition = child.getPosition();
			child.getShellNodeGeometryDelegate().move(childPosition);
		}
	}

	@Override
	public Optional<ShellLayoutManager> getLayoutManager() {
		return this.optionalLayoutManager;
	}

	@Override
	public void setLayoutManager(@Nullable final ShellLayoutManager shellLayoutManager) {
		this.optionalLayoutManager = Optional.of(shellLayoutManager);
		if (this.optionalLayoutManager.isPresent()) {
			register(shellLayoutManager);
		}
	}

	@Override
	public void doMove() {
		super.doMove();
		updateChildrenPosition();
	}

	@Override
	public void doMoveResize() {
		super.doMoveResize();
		updateChildrenPosition();
		layout();
	}

	@Override
	public void doResize() {
		super.doResize();
		layout();
	}

	protected void handleChildReparent(@Nonnull final ShellNode child) {
		checkArgument(child instanceof AbstractShellNode);

		final ShellNodeEvent shellNodeEvent;
		if (this.children.remove(child)) {
			shellNodeEvent = new ShellNodeChildLeftEvent(	this,
															toGeoTransformation());
		} else {
			this.children.addLast((AbstractShellNode) child);
			shellNodeEvent = new ShellNodeChildAddedEvent(	this,
															toGeoTransformation());
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
	public void layout() {
		final Optional<ShellLayoutManager> optionalLayoutManager = getLayoutManager();
		if (optionalLayoutManager.isPresent()) {
			optionalLayoutManager.get().layout(this);
		}
	}
}
