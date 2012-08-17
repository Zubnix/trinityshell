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

import org.trinity.shell.geo.api.ShellGeoExecutor;
import org.trinity.shell.geo.api.manager.ShellLayoutManager;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
/**
 * A <code>ShellGeoVNode</code> is a 'virtual', invisible rectangle with a size,
 * place and visibility and has no visual representation on-screen. A
 * <code>ShellGeoVNode</code> has the same behavior as a
 * <code>ShellGeoNode</code>. This means that when a <code>ShellGeoVNode</code>
 * moves, all it's children will move with the same offset. When a
 * <code>ShellGeoVNode</code> visibility changes, all it's children's visibility
 * will change as well. A <code>ShellGeoVNode</code> can also have an optional
 * <code>ShellLayoutManager</code> which means it can manage it's children's
 * geometry requests if necessary.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
@javax.inject.Named("GeoVirt")
public class ShellGeoVNode extends AbstractShellGeoNode {
	private final ShellGeoExecutor shellGeoExecutor;
	private ShellLayoutManager shellLayoutManager;

	@Inject
	protected ShellGeoVNode(final EventBus eventBus,
							@Named("GeoVirt") final ShellGeoExecutor shellGeoExecutor) {
		super(eventBus);
		this.shellGeoExecutor = shellGeoExecutor;
	}

	@Override
	public ShellGeoExecutor getGeoExecutor() {
		return this.shellGeoExecutor;
	}

	@Override
	public ShellLayoutManager getGeoManager() {
		return this.shellLayoutManager;
	}

	/**
	 * @param shellLayoutManager
	 */
	@Override
	public void setGeoManager(final ShellLayoutManager shellLayoutManager) {
		this.shellLayoutManager = shellLayoutManager;
	}
}
