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

import org.trinity.shell.api.scene.AbstractShellNodeParent;
import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellNodeExecutor;
import org.trinity.shell.api.scene.ShellNodeParent;

import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

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
@Bind(value = @Named("ShellVirtualNode"), to = @To(value = Type.CUSTOM, customs = { ShellNode.class,
		ShellNodeParent.class }))
public class ShellVirtualNode extends AbstractShellNodeParent {
	private final ShellNodeExecutor shellNodeExecutor = new ShellVirtualNodeExecutor(this);

	@Override
	public ShellNodeExecutor getShellNodeExecutor() {
		return this.shellNodeExecutor;
	}
}
