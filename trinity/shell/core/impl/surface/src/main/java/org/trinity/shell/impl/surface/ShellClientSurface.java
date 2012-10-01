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
package org.trinity.shell.impl.surface;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.event.DestroyNotifyEvent;
import org.trinity.shell.api.node.ShellNodeExecutor;
import org.trinity.shell.api.surface.AbstractShellSurface;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shell.api.surface.ShellSurface;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

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
public class ShellClientSurface extends AbstractShellSurface {

	private final ShellNodeExecutor renderAreaGeoExecutor;
	private final DisplaySurface displaySurface;
	private final ShellDisplayEventDispatcher shellDisplayEventDispatcher;

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
	ShellClientSurface(	final EventBus nodeEventBus,
						final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
						@Named("ShellRootSurface") final ShellSurface root,
						@Named("shellSurfaceGeoExecutor") final ShellNodeExecutor shellNodeExecutor,
						@Assisted final DisplaySurface displaySurface) {
		super(nodeEventBus);
		this.shellDisplayEventDispatcher = shellDisplayEventDispatcher;
		this.displaySurface = displaySurface;
		this.renderAreaGeoExecutor = shellNodeExecutor;

		shellDisplayEventDispatcher.registerDisplayEventSourceListener(	nodeEventBus,
																		displaySurface);

		setParent(root);
		doReparent(false);
		syncGeoToDisplaySurface();
	}

	@Override
	public void handleDestroyNotifyEvent(final DestroyNotifyEvent destroyNotifyEvent) {
		super.handleDestroyNotifyEvent(destroyNotifyEvent);
		this.shellDisplayEventDispatcher.unregisterAllDisplayEventSourceListeners(getDisplaySurface());
	}

	@Override
	public ShellNodeExecutor getNodeExecutor() {
		return this.renderAreaGeoExecutor;
	}

	@Override
	public DisplaySurface getDisplaySurface() {
		return this.displaySurface;
	}

}
