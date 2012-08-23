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

import org.trinity.foundation.display.api.event.ButtonNotifyEvent;
import org.trinity.foundation.input.api.Momentum;
import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.foundation.shared.geometry.api.Rectangle;
import org.trinity.shell.core.api.ShellDisplayEventDispatcher;
import org.trinity.shell.geo.api.ShellNode;
import org.trinity.shell.geo.api.ShellNodeExecutor;
import org.trinity.shell.widget.api.view.ShellWidgetView;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;

// TODO create push button superclass
/**
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ShellMaximizeButton extends ShellButton {

	private ShellNode client;
	private Rectangle maximize;

	private int oldRelX;
	private int oldRelY;
	private int oldWidth;
	private int oldHeight;
	private boolean maximized = false;

	@Inject
	protected ShellMaximizeButton(	final EventBus eventBus,
									final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
									final PainterFactory painterFactory,
									@Named("shellWidgetGeoExecutor") final ShellNodeExecutor shellNodeExecutor,
									final ShellWidgetView view) {
		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory,
				shellNodeExecutor,
				view);
	}

	@Subscribe
	public void onMouseButtonPressed(final ButtonNotifyEvent input) {
		if (input.getInput().getMomentum() == Momentum.STARTED) {
			if (this.maximized) {
				restore();
			} else {
				maximize();
			}
		}
	}

	public ShellNode getClient() {
		return this.client;
	}

	public void maximize() {
		final int newRelX = getMaximize().getX();
		final int newRelY = getMaximize().getY();
		final int newWidth = getMaximize().getWidth();
		final int newHeight = getMaximize().getHeight();

		this.oldRelX = getClient().getX();
		this.oldRelY = getClient().getY();
		this.oldWidth = getClient().getWidth();
		this.oldHeight = getClient().getHeight();

		getClient().setX(newRelX);
		getClient().setY(newRelY);
		getClient().setWidth(newWidth);
		getClient().setHeight(newHeight);

		this.maximized = true;

	}

	public void restore() {
		getClient().setX(this.oldRelX);
		getClient().setY(this.oldRelY);
		getClient().setWidth(this.oldWidth);
		getClient().setHeight(this.oldHeight);

		this.maximized = false;
	}

	public void setClient(final ShellNode client) {
		this.client = client;
	}

	public void setMaximize(final Rectangle rectangle) {
		this.maximize = rectangle;

	}

	public Rectangle getMaximize() {
		return this.maximize;
	}
}
