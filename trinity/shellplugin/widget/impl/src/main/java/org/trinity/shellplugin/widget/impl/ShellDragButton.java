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
package org.trinity.shellplugin.widget.impl;

import org.trinity.foundation.display.api.event.ButtonNotifyEvent;
import org.trinity.foundation.input.api.Momentum;
import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.foundation.shared.geometry.api.Coordinate;
import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.ShellNodeExecutor;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.widget.BaseShellWidget;
import org.trinity.shell.api.widget.ShellWidgetView;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

/**
 * A <code>DragButton</code> can move another
 * <code>ShellNode</cod>, defined by the target renderarea.
 * <p>
 * A <code>DragButton</code> monitors a <code>ManagedMouse</code> when the
 * <code>DragButton</code> is activated. Should the <code>ManagedMouse</code>
 * change it's position, the <code>DragButton</code> will move the target
 * <code>ShellNode</code> accordingly. A <code>DragButton</code> will stop
 * monitoring the <code>ManagedMouse</code> as soon as it is deactivated.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
public class ShellDragButton extends BaseShellWidget {

	private final Runnable dragRun = new Runnable() {
		@Override
		public void run() {

			final Coordinate mousePos = ShellDragButton.this.root.getDisplaySurface().getPointerCoordinate();
			ShellDragButton.this.x0 = mousePos.getX();
			ShellDragButton.this.y0 = mousePos.getY();
			while (!Thread.interrupted()) {
				final Coordinate mousePosition = ShellDragButton.this.root.getDisplaySurface().getPointerCoordinate();
				final int x1 = mousePosition.getX();
				final int y1 = mousePosition.getY();

				final int deltaX = x1 - ShellDragButton.this.x0;
				final int deltaY = y1 - ShellDragButton.this.y0;

				if ((deltaX != 0) || (deltaY != 0)) {
					ShellDragButton.this.x0 = x1;
					ShellDragButton.this.y0 = y1;
					mutate(	deltaX,
							deltaY);
				}

				// Process the next event without blocking, else we will never
				// receive the mouse button released event.
				ShellDragButton.this.shellDisplayEventDispatcher.dispatchDisplayEvent(false);
			}
		}
	};

	private Thread dragThread;

	private final ShellDisplayEventDispatcher shellDisplayEventDispatcher;
	private final ShellSurface root;
	private ShellNode client;
	private int x0;
	private int y0;

	/**
	 * 
	 */
	@Inject
	protected ShellDragButton(	final EventBus eventBus,
								@Named("ShellRootSurface") final ShellSurface root,
								final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
								final PainterFactory painterFactory,
								@Named("shellWidgetGeoExecutor") final ShellNodeExecutor shellNodeExecutor,
								@Named("ShellButtonView") final ShellWidgetView view) {
		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory,
				shellNodeExecutor,
				view);
		this.shellDisplayEventDispatcher = shellDisplayEventDispatcher;
		this.root = root;
		stopDragClient();
	}

	/**
	 * Mutate the target <code>ShellNode</code> with the given delta values.
	 * <p>
	 * The given delta values indicate the movement of the
	 * <code>ManagedMouse</code>.
	 * 
	 * @param deltaX
	 *            The horizontal movement of the <code>ManagedMouse</code> in
	 *            pixels.
	 * @param deltaY
	 *            The vertical movement of the <code>ManagedMouse</code> in
	 *            pixels. @ Thrown when the target
	 *            <code>AbstractShellSurface</code> has an illegal state.
	 */
	protected void mutate(	final int deltaX,
							final int deltaY) {
		getClient().setX(getClient().getX() + deltaX);
		getClient().setY(getClient().getY() + deltaY);
		getClient().requestMove();
	}

	/**
	 * Stop the <code>ManagedMouse</code> movement monitor for this
	 * <code>DragButton</code>.
	 */
	public void stopDragClient() {
		this.dragThread.interrupt();
		this.dragThread = null;
	}

	@Subscribe
	public void onMouseButtonReleased(final ButtonNotifyEvent input) {
		if (input.getInput().getMomentum() == Momentum.STOPPED) {
			stopDragClient();
		} else {
			startDragClient();
		}
	}

	public ShellNode getClient() {
		return this.client;
	}

	public void setClient(final ShellNode client) {
		this.client = client;
	}

	public void startDragClient() {
		this.dragThread = new Thread(this.dragRun);
		this.dragThread.start();
	}
}