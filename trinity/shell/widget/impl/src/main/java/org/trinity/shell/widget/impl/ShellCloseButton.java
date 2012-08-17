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
package org.trinity.shell.widget.impl;

import org.trinity.foundation.display.api.DisplayProtocol;
import org.trinity.foundation.display.api.DisplayProtocols;
import org.trinity.foundation.display.api.event.ButtonNotifyEvent;
import org.trinity.foundation.input.api.Momentum;
import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.core.api.ShellDisplayEventDispatcher;
import org.trinity.shell.core.api.ShellRenderArea;
import org.trinity.shell.geo.api.ShellGeoExecutor;
import org.trinity.shell.widget.api.view.ShellButtonView;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

/**
 * A <code>CloseButton</code> can terminate another
 * <code>AbstractShellRenderArea</code> that is defined by the target
 * <code>AbstractShellRenderArea</code>.
 * <p>
 * In case that the target <code>AbstractShellRenderArea</code> is a
 * <code>ShellClient</code>, the <code>CloseButton</code> will ask the
 * <code>ShellClient</code> to shut down itself. This does not guaranty that the
 * <code>ShellClient</code> will actually shut down since most
 * <code>ShellClient</code>s will present the user with a dialog to confirm if
 * the <code>ShellClient</code> should shut down.
 * <p>
 * If the <code>AbstractShellRenderArea</code> is a <code>ShellWidget</code>,
 * the <code>AbstractShellRenderArea</code> will simply be destroyed or other as
 * defined by the <code>ShellLayoutManager</code> of the
 * <code>ShellWidget</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
public class ShellCloseButton extends ShellButton {

	private ShellRenderArea client;
	private final DisplayProtocols desktopProtocol;

	@Inject
	protected ShellCloseButton(	final EventBus eventBus,
								final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
								final PainterFactory painterFactory,
								@Named("ShellWidget") final ShellGeoExecutor shellGeoExecutor,
								final DisplayProtocols desktopProtocol,
								final ShellButtonView view) {
		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory,
				shellGeoExecutor,
				view);
		this.desktopProtocol = desktopProtocol;
	}

	public void setClient(final ShellRenderArea client) {
		this.client = client;
	}

	public ShellRenderArea getClient() {
		return this.client;
	}

	@Subscribe
	public void onMouseButtonPressed(final ButtonNotifyEvent input) {
		if (input.getInput().getMomentum() == Momentum.STARTED) {
			closeClient();
		}
	}

	public void closeClient() {
		this.desktopProtocol.queryProtocol(	getDisplayRenderArea(),
											DisplayProtocol.CLOSE_REQUEST);
	}
}