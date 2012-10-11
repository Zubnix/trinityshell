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
package org.trinity.shellplugin.widget.impl;

import org.trinity.foundation.display.api.event.ButtonNotifyEvent;
import org.trinity.foundation.input.api.Momentum;
import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shell.api.widget.BaseShellWidget;
import org.trinity.shell.api.widget.ShellWidgetView;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

// TODO create abstract push button class
/**
 * A <code>HideButton</code> can hide (unmap) another target
 * <code>ShellNode</code>. The target is specified by a call to
 * {@link ShellHideButton#setTargetRenderArea(ShellNode)}. A hide is initiated
 * when a (any) mouse button is pressed on the <code>HideButton</code> . This
 * behaviour can be changed by overriding the
 * {@link ShellHideButton#onMouseInput(BaseMouseInput)} method.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
public class ShellHideButton extends BaseShellWidget {

	private ShellNode client;

	/*****************************************
	 * @param painterFactory
	 * @param view
	 ****************************************/
	@Inject
	protected ShellHideButton(	final EventBus eventBus,
								final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
								final PainterFactory painterFactory,
								@Named("ShellButtonView") final ShellWidgetView view) {
		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory,
				view);
	}

	@Subscribe
	public void onMouseButtonPressed(final ButtonNotifyEvent input) {
		if (input.getInput().getMomentum() == Momentum.STARTED) {
			hideClient();
		}
	}

	public ShellNode getClient() {
		return this.client;
	}

	public void setClient(final ShellNode client) {
		this.client = client;
	}

	public void hideClient() {
		getClient().requestHide();
	}
}