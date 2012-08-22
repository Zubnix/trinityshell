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
package org.trinity.shell.core.impl;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.shell.core.api.AbstractShellSurface;
import org.trinity.shell.core.api.ShellDisplayEventDispatcher;
import org.trinity.shell.core.api.ShellSurface;
import org.trinity.shell.geo.api.ShellNodeExecutor;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

// TODO documentation
/**
 * A <code>ShellClientSurface</code> wraps a {@link DisplaySurface} that was
 * created by an independent program. As such the visual content can not be
 * directly manipulated. A <code>ShellClientSurface</code> is the owner of the
 * <code>PlatformRenderArea</code> it wraps. A <code>ShellClientSurface</code>
 * provides functionality to manage and manipulate the geometry and visibility
 * of the <code>PlatformRenderArea</code> it wraps.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind(to = @To(value = Type.IMPLEMENTATION))
public class ShellClientSurface extends AbstractShellSurface {

	private final ShellNodeExecutor renderAreaGeoExecutor;

	/**
	 * Create a new <code>ShellClientSurface</code> from a foreign
	 * <code>PlatformRenderArea</code>.
	 * 
	 * @param managedDisplay
	 *            The {@link ShellDisplay} where this
	 *            <code>ShellClientSurface</code> lives on.
	 * @param clientWindow
	 *            The foreign {@link DisplaySurface}.
	 */
	@Inject
	ShellClientSurface(	final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
						final EventBus nodeEventBus,
						@Named("shellRootRenderArea") final ShellSurface root,
						@Named("shellSurfaceGeoExecutor") final ShellNodeExecutor shellNodeExecutor) {
		super(nodeEventBus, shellDisplayEventDispatcher);
		this.renderAreaGeoExecutor = shellNodeExecutor;
		setParent(root);
		doReparent(false);
	}

	@Override
	public ShellNodeExecutor getNodeExecutor() {
		return this.renderAreaGeoExecutor;
	}

	@Override
	protected void setDisplaySurface(final DisplaySurface displaySurface) {
		super.setDisplaySurface(displaySurface);
		syncGeoToDisplayRenderArea();
	}
}
