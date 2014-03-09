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
package org.trinity.shell.scene.impl;

import com.google.auto.factory.AutoFactory;
import com.google.common.base.Optional;
import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shell.api.scene.event.ShellNodeChildAddedEvent;
import org.trinity.shell.api.scene.event.ShellNodeChildLeftEvent;
import org.trinity.shell.api.scene.event.ShellNodeEvent;
import org.trinity.shell.api.scene.manager.ShellLayoutManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * An abstract base implementation of a {@link org.trinity.shell.api.scene.ShellNodeParent}.
 * **************************************
 */
@NotThreadSafe
@AutoFactory
public class ShellNodeParentImpl extends ShellNodeImpl implements ShellNodeParent {

	private final LinkedList<ShellNode> children = new LinkedList<>();
	private Optional<ShellLayoutManager> optionalLayoutManager = Optional.absent();

	ShellNodeParentImpl(@Nonnull final ShellNodeParent parent) {
		super(parent);
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
    public void removeLayoutManager() {
        this.optionalLayoutManager = Optional.absent();
    }

    protected void handleChildReparent(@Nonnull final ShellNode child) {
		checkArgument(child instanceof ShellNodeImpl);

		final ShellNodeEvent shellNodeEvent;
		if (this.children.remove(child)) {
			shellNodeEvent = new ShellNodeChildLeftEvent(	this,
															child);
		} else {
			this.children.addLast(child);
			shellNodeEvent = new ShellNodeChildAddedEvent(	this,
															child);
		}
		post(shellNodeEvent);
	}

	protected void handleChildStacking(	@Nonnull final ShellNode child,
										final boolean raised) {
		checkArgument(child instanceof ShellNodeImpl);
		checkArgument(this.children.remove(child));

		if (raised) {
			this.children.addLast(child);
		} else {
			this.children.addFirst(child);
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
