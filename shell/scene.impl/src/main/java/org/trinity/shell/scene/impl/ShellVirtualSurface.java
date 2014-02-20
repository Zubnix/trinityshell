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

import org.trinity.foundation.api.shared.Listenable;
import org.trinity.shell.api.bindingkey.ShellScene;
import org.trinity.shell.api.scene.AbstractShellNodeParent;
import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellNodeGeometryDelegate;

import javax.annotation.Nonnull;
import javax.inject.Inject;

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
 */
public class ShellVirtualSurface extends AbstractShellNodeParent {

	private final ShellNodeGeometryDelegate shellNodeGeometryDelegate = new ShellVirtualSurfaceExecutor(this);

	@Inject
	protected ShellVirtualSurface(	@Nonnull @ShellScene final Listenable shellScene) {
		super(	shellScene);
	}

	@Override
	public void doDestroy() {
		for (final ShellNode child : getChildren()) {
			if (!child.isDestroyed()) {
				child.doDestroy();
			}
		}
		super.doDestroy();
	}

	@Override
	public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
		return this.shellNodeGeometryDelegate;
	}
}
