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
package org.trinity.shell.geo.impl;

import org.trinity.shell.api.geo.AbstractShellNode;
import org.trinity.shell.api.geo.ShellNode;
import org.trinity.shell.api.geo.ShellNodeExecutor;
import org.trinity.shell.api.geo.manager.ShellLayoutManager;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
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
@Bind(value = @Named("ShellGeoVNode"), to = @To(value = Type.CUSTOM, customs = ShellNode.class))
public class ShellGeoVNode extends AbstractShellNode {
	private final ShellNodeExecutor shellNodeExecutor;
	private ShellLayoutManager shellLayoutManager;

	@Inject
	protected ShellGeoVNode(final EventBus eventBus,
							@Named("ShellGeoVExecutor") final ShellNodeExecutor shellNodeExecutor) {
		super(eventBus);
		this.shellNodeExecutor = shellNodeExecutor;
	}

	@Override
	public ShellNodeExecutor getNodeExecutor() {
		return this.shellNodeExecutor;
	}

	@Override
	public ShellLayoutManager getLayoutManager() {
		return this.shellLayoutManager;
	}

	/**
	 * @param shellLayoutManager
	 */
	@Override
	public void setLayoutManager(final ShellLayoutManager shellLayoutManager) {
		this.shellLayoutManager = shellLayoutManager;
	}
}
