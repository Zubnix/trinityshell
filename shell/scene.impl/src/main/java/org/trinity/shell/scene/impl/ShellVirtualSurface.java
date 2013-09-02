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

import static org.apache.onami.autobind.annotations.To.Type.CUSTOM;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.bindingkey.ShellScene;
import org.trinity.shell.api.bindingkey.ShellVirtualNode;
import org.trinity.shell.api.scene.AbstractShellNode;
import org.trinity.shell.api.scene.AbstractShellNodeParent;
import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellNodeGeometryDelegate;
import org.trinity.shell.api.scene.ShellNodeParent;

import com.google.common.util.concurrent.ListeningExecutorService;

// TODO documentation
/**
 * A <code>ShellGeoVNode</code> is a 'virtual', invisible rectangle with a size,
 * place and visibility and has no visual representation on-screen. A
 * <code>ShellGeoVNode</code> has the same behavior as a <code>ShellNode</code>.
 * This means that when a <code>ShellGeoVNode</code> moves, all it's children
 * will move with the same offset. When a <code>ShellGeoVNode</code> visibility
 * changes, all it's children's visibility will change as well. A
 * <code>ShellGeoVNode</code> can also have an optional
 * <code>ShellLayoutManager</code> which means it can manage it's children's
 * geometry requests if necessary.
 *
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind(to = @To(value = CUSTOM, customs = { ShellNode.class, ShellNodeParent.class }))
@ShellVirtualNode
@ExecutionContext(ShellExecutor.class)
public class ShellVirtualSurface extends AbstractShellNodeParent {

	private final ShellNodeGeometryDelegate shellNodeGeometryDelegate = new ShellVirtualSurfaceExecutor(this);

	@Inject
	protected ShellVirtualSurface(	@Nonnull @ShellScene final AsyncListenable shellScene,
									@Nonnull @ShellExecutor final ListeningExecutorService shellExecutor) {
		super(	shellScene,
				shellExecutor);
	}

	@Override
	public Void doDestroyImpl() {
		for (final AbstractShellNode child : getChildrenImpl()) {
			if (!child.isDestroyedImpl()) {
				child.doDestroy();
			}
		}
		return super.doDestroyImpl();
	}

	@Override
	public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
		return this.shellNodeGeometryDelegate;
	}
}
