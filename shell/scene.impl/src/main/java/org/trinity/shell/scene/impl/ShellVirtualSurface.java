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
package org.trinity.shell.scene.impl;

import static org.apache.onami.autobind.annotations.To.Type.CUSTOM;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.bindingkey.ShellScene;
import org.trinity.shell.api.bindingkey.ShellVirtualNode;
import org.trinity.shell.api.scene.AbstractShellNodeParent;
import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellNodeGeometryDelegate;
import org.trinity.shell.api.scene.ShellNodeParent;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;

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
@Bind
@ShellVirtualNode
@To(value = CUSTOM, customs = { ShellNode.class, ShellNodeParent.class })
public class ShellVirtualSurface extends AbstractShellNodeParent {

	private final ShellNodeGeometryDelegate shellNodeGeometryDelegate = new ShellVirtualSurfaceExecutor(this);

	@Inject
	protected ShellVirtualSurface(	@ShellScene final AsyncListenable shellScene,
									@ShellExecutor final ListeningExecutorService shellExecutor) {
		super(	shellScene,
				shellExecutor);
	}

	@Override
	public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
		return this.shellNodeGeometryDelegate;
	}
}
