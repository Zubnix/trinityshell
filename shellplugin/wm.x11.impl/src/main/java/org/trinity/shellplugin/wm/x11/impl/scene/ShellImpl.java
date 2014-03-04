/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

package org.trinity.shellplugin.wm.x11.impl.scene;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.Display;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.DisplaySurfaceCreationNotify;
import org.trinity.foundation.api.render.View;
import org.trinity.foundation.api.render.binding.ViewBinder;
import org.trinity.shell.api.scene.event.ShellNodeDestroyedEvent;
import org.trinity.shell.api.scene.event.ShellNodeShowRequestEvent;
import org.trinity.shell.api.scene.manager.ShellLayoutManagerFactory;
import org.trinity.shell.api.scene.manager.ShellLayoutManagerLine;
import org.trinity.shell.api.scene.manager.ShellLayoutPropertyLine;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.ShellSurfaceFactory;
import org.trinity.shellplugin.wm.api.Shell;
import org.trinity.shellplugin.wm.api.viewkey.DesktopView;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Iterables.removeAll;

// TODO split up this class
@Singleton
@NotThreadSafe
public class ShellImpl implements Shell {

	private static final Logger LOG = LoggerFactory.getLogger(ShellImpl.class);

	// (view)listenable lists
	private final EventList<Object> notificationsBar = new BasicEventList<>();
	private final EventList<Object> clientsBar = new BasicEventList<>();
	private final EventList<Object> bottomBar = new BasicEventList<>();
	private final Display display;
	private final ViewBinder viewBinder;
	private final ShellSurfaceFactory shellSurfaceFactory;
	private final ShellLayoutManagerFactory shellLayoutManagerFactory;
	private final ClientBarElementFactory clientBarElementFactory;
    private final View view;

    private final Set<DisplaySurface> nonClientDisplaySurfaces = Sets.newHashSet();

    @Inject
    ShellImpl(final Display display,
              final ViewBinder viewBinder,
              final ShellSurfaceFactory shellSurfaceFactory,
				final ShellLayoutManagerFactory shellLayoutManagerFactory,
				final ClientBarElementFactory clientBarElementFactory,
                @DesktopView final View view) {
        this.display = display;
        this.viewBinder = viewBinder;
        this.shellSurfaceFactory = shellSurfaceFactory;
        this.shellLayoutManagerFactory = shellLayoutManagerFactory;
        this.clientBarElementFactory = clientBarElementFactory;
        this.view = view;
    }

    public EventList<Object> getNotificationsBar() {
        return this.notificationsBar;
    }

    public EventList<Object> getClientsBar() {
        return this.clientsBar;
	}

	public EventList<Object> getBottomBar() {
		return this.bottomBar;
	}

	@Override
	public void addStatusElement(final Object element) {
				ShellImpl.this.notificationsBar.add(element);
	}

	@Override
	public void removeStatusElement(final Object element) {
				ShellImpl.this.notificationsBar.remove(element);
	}

	@Override
	public void start() {

                    ShellImpl.this.viewBinder.bind(
                                                   this,
                                                   this.view.getBindableView());

                    ShellImpl.this.nonClientDisplaySurfaces.add(this.view.getViewDisplaySurface());
                    final ShellSurface desktopShellSurface = ShellImpl.this.shellSurfaceFactory
                            .createShellSurface(this.view.getViewDisplaySurface());
                    configureDesktopShellSurfaceBehavior(desktopShellSurface);
                    handleDesktopShellSurface(desktopShellSurface);
    }

    // called by display thread so we avoid missing any display methods.
    private void configureDesktopShellSurfaceBehavior(final ShellSurface desktopShellSurface) {
		desktopShellSurface.register(new Object() {
			@Subscribe
			public void handleMoveResizeRequest(final ShellNodeMoveResizeRequestEvent event) {
				desktopShellSurface.doMoveResize();
			}

			@Subscribe
			public void handleShowRequest(final ShellNodeShowRequestEvent event) {
				desktopShellSurface.doShow();
			}
		});
	}

	private void handleDesktopShellSurface(final ShellSurface desktopShellSurface) {
		final ShellLayoutManagerLine clientShellLayoutManagerLine = this.shellLayoutManagerFactory
				.createShellLayoutManagerLine(desktopShellSurface);
		// We register without specifying an executor. This
		// means our listener (@Subscribe method) will be
		// called by the "Display" thread.
		this.display.register(new Object() {
			// called by display executor
			@Subscribe
			public void handleCreationNotify(final DisplaySurfaceCreationNotify displaySurfaceCreationNotify) {
				final DisplaySurface displaySurface = displaySurfaceCreationNotify.getDisplaySurface();
				final ShellSurface clientShellSurface = ShellImpl.this.shellSurfaceFactory
						.createShellSurface(displaySurface);
				handleClientShellSurface(	clientShellSurface,
											clientShellLayoutManagerLine);
			}
		});
		// search & manage existing clients on the display server.
		find(clientShellLayoutManagerLine);
	}

	// called by shell executor.
	@Override
	public void stop() {
		this.display.unregister(this);
	}

	private void find(final ShellLayoutManagerLine shellLayoutManagerLine) {
		final List<DisplaySurface> displaySurfaces = this.display.getDisplaySurfaces();

							removeAll(	displaySurfaces,
										ShellImpl.this.nonClientDisplaySurfaces);
							for (final DisplaySurface displaySurface : displaySurfaces) {
								final ShellSurface clientShellSurface = ShellImpl.this.shellSurfaceFactory
										.createShellSurface(displaySurface);
								handleClientShellSurface(	clientShellSurface,
															shellLayoutManagerLine);
							}
						}


	private void handleClientShellSurface(	final ShellSurface clientShellSurface,
											final ShellLayoutManagerLine shellLayoutManagerLine) {
        //check if we haven't missed any destroy event.
        final Boolean destroyed = clientShellSurface.isDestroyed();

                            if(!destroyed) {
                                //if not then we manage it.
                                shellLayoutManagerLine.addChildNode(clientShellSurface,
                                                                    new ShellLayoutPropertyLine(1,
                                                                                                Margins.create(2,
																											   2,
																											   35,
																											   45)));
                                final ClientBarElement clientTopBarItem = ShellImpl.this.clientBarElementFactory
                                        .createClientTopBarItem(clientShellSurface);

                                clientShellSurface.register(new Object() {
                                    @Subscribe
                                    public void handleDestroyed(final ShellNodeDestroyedEvent destroyedEvent) {
                                        ShellImpl.this.clientsBar.remove(clientTopBarItem);
                                    }
                                });
                                ShellImpl.this.clientsBar.add(clientTopBarItem);
                            }
    }
}
