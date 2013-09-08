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

import static com.google.common.collect.Iterables.removeAll;
import static com.google.common.util.concurrent.Futures.addCallback;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;

import org.apache.onami.autobind.annotations.Bind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.Display;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.DisplaySurfaceCreationNotify;
import org.trinity.foundation.api.render.ViewReference;
import org.trinity.foundation.api.render.binding.Binder;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Margins;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.scene.event.ShellNodeDestroyedEvent;
import org.trinity.shell.api.scene.event.ShellNodeMoveResizeRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeShowRequestEvent;
import org.trinity.shell.api.scene.manager.ShellLayoutManagerFactory;
import org.trinity.shell.api.scene.manager.ShellLayoutManagerLine;
import org.trinity.shell.api.scene.manager.ShellLayoutPropertyLine;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.ShellSurfaceFactory;
import org.trinity.shellplugin.wm.api.Shell;
import org.trinity.shellplugin.wm.api.viewreferencekey.DesktopViewReference;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Singleton;

// TODO split up this class
@Bind
@Singleton
@ExecutionContext(ShellExecutor.class)
@NotThreadSafe
public class ShellImpl implements Shell {

	private static final Logger LOG = LoggerFactory.getLogger(ShellImpl.class);

	// (view)listenable lists
	private final EventList<Object> notificationsBar = new BasicEventList<>();
	private final EventList<Object> clientsBar = new BasicEventList<>();
	private final EventList<Object> bottomBar = new BasicEventList<>();
	private final Display display;
	private final ListeningExecutorService shellExecutor;
	private final Binder binder;
	private final ShellSurfaceFactory shellSurfaceFactory;
	private final ShellLayoutManagerFactory shellLayoutManagerFactory;
	private final ClientBarElementFactory clientBarElementFactory;
	private final ListenableFuture<ViewReference> desktopViewFuture;

	private final Set<DisplaySurface> nonClientDisplaySurfaces = Sets.newHashSet();

	@Inject
	ShellImpl(	final Display display,
				@ShellExecutor final ListeningExecutorService shellExecutor,
				final Binder binder,
				final ShellSurfaceFactory shellSurfaceFactory,
				final ShellLayoutManagerFactory shellLayoutManagerFactory,
				final ClientBarElementFactory clientBarElementFactory,
				@DesktopViewReference final ListenableFuture<ViewReference> desktopViewFuture) {
		this.display = display;
		this.shellExecutor = shellExecutor;
		this.binder = binder;
		this.shellSurfaceFactory = shellSurfaceFactory;
		this.shellLayoutManagerFactory = shellLayoutManagerFactory;
		this.clientBarElementFactory = clientBarElementFactory;
		this.desktopViewFuture = desktopViewFuture;
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
	public ListenableFuture<Void> addStatusElement(final Object element) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				ShellImpl.this.notificationsBar.add(element);
				return null;
			}
		});
	}

	@Override
	public ListenableFuture<Void> removeStatusElement(final Object element) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				ShellImpl.this.notificationsBar.remove(element);
				return null;
			}
		});
	}

	@Override
	public void start() {
		addCallback(desktopViewFuture,
					new FutureCallback<ViewReference>() {
						@Override
						public void onSuccess(final ViewReference viewReference) {
							ShellImpl.this.binder.bind(	shellExecutor,
														ShellImpl.this,
														viewReference.getView());

							// FIXME we can still miss display events here.
							final DisplaySurface viewDisplaySurface = viewReference.getViewDisplaySurface();
							ShellImpl.this.nonClientDisplaySurfaces.add(viewDisplaySurface);
							final ShellSurface desktopShellSurface = ShellImpl.this.shellSurfaceFactory
									.createShellSurface(viewDisplaySurface);
							configureDesktopShellSurfaceBehavior(desktopShellSurface);
							handleDesktopShellSurface(desktopShellSurface);
						}

						@Override
						public void onFailure(final Throwable t) {
							LOG.error(	"Failed to get ViewReference.",
										t);
						}
					});
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

	// called by display executor
	private void find(final ShellLayoutManagerLine shellLayoutManagerLine) {
		final ListenableFuture<List<DisplaySurface>> clientDisplaySurfaces = this.display.getDisplaySurfaces();
		// called by display thread
		addCallback(clientDisplaySurfaces,
					new FutureCallback<List<DisplaySurface>>() {
						@Override
						public void onSuccess(final List<DisplaySurface> displaySurfaces) {
							removeAll(	displaySurfaces,
										ShellImpl.this.nonClientDisplaySurfaces);
							for (final DisplaySurface displaySurface : displaySurfaces) {
								final ShellSurface clientShellSurface = ShellImpl.this.shellSurfaceFactory
										.createShellSurface(displaySurface);
								handleClientShellSurface(	clientShellSurface,
															shellLayoutManagerLine);
							}
						}

						@Override
						public void onFailure(final Throwable t) {
							LOG.error(	"Unable to query for existing display surfaces.",
										t);
						}
					});
	}

	private void handleClientShellSurface(	final ShellSurface clientShellSurface,
											final ShellLayoutManagerLine shellLayoutManagerLine) {
		this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				shellLayoutManagerLine.addChildNode(clientShellSurface,
													new ShellLayoutPropertyLine(1,
																				new Margins(2,
																							2,
																							25,
																							25)));
				final ClientBarElement clientTopBarItem = clientBarElementFactory
						.createClientTopBarItem(clientShellSurface);
				clientShellSurface.register(new Object() {
					@Subscribe
					public void handleDestoryed(final ShellNodeDestroyedEvent destroyedEvent) {
						clientsBar.remove(clientTopBarItem);
					}
				});
				clientsBar.add(clientTopBarItem);
				return null;
			}
		});
	}
}
