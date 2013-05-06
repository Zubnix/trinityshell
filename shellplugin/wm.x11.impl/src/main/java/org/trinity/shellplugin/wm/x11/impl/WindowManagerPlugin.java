package org.trinity.shellplugin.wm.x11.impl;

import static com.google.common.util.concurrent.Futures.addCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplayServer;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.CreationNotify;
import org.trinity.shell.api.plugin.ShellPlugin;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.ShellSurfaceFactory;
import org.trinity.shellplugin.wm.x11.impl.scene.SceneManager;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.Inject;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
public class WindowManagerPlugin extends AbstractIdleService implements ShellPlugin {

	private static final Logger logger = LoggerFactory.getLogger(WindowManagerPlugin.class);

	private final DisplayServer displayServer;
	private final SceneManager sceneManager;
	private final ShellSurfaceFactory shellSurfaceFactory;

	@Inject
	WindowManagerPlugin(final SceneManager sceneManager,
						final ShellSurfaceFactory shellSurfaceFactory,
						final DisplayServer displayServer) {
		this.displayServer = displayServer;
		this.sceneManager = sceneManager;
		this.shellSurfaceFactory = shellSurfaceFactory;
	}

	@Override
	protected void startUp() throws Exception {

		final ListenableFuture<DisplaySurface[]> clientDisplaySurfacesFuture = this.displayServer.getClientDisplaySurfaces();
		// callback will be called by "Display" thread.
		addCallback(clientDisplaySurfacesFuture,
					new FutureCallback<DisplaySurface[]>() {
						@Override
						public void onSuccess(final DisplaySurface[] displaySurfaces) {
							// iterate over existing non-managed client display
							// surfaces
							for (final DisplaySurface clientDisplaySurface : displaySurfaces) {
								// we have to check if it's an "onscreen" client
								// display surface as the slighty retarded icccm
								// & ewmh protocols specifiy a bunch of windows
								// that are not supposed to be shown (or at
								// least not as the application's main
								// window...).
								if (isClientMainWindow(clientDisplaySurface)) {
									handleClientDisplaySurface(clientDisplaySurface);
								}
							}

							// We register without specifying an executor. This
							// means our listener (@Subscribe method) will be
							// called by the "Display" thread.
							WindowManagerPlugin.this.displayServer.register(WindowManagerPlugin.this);
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Failed to query non-manged client display surfaces",
											t);
						}
					});

	}

	private boolean isClientMainWindow(final DisplaySurface clientDisplaySurface) {
		// TODO check for icon window
		// TODO check for 'parent main window'
		// TODO check for more nonsens windows?
		return false;
	}

	@Override
	protected void shutDown() throws Exception {
		this.displayServer.unregister(this);
	}

	@Subscribe
	public void handleCreationNotify(final CreationNotify creationNotify) {
		final DisplaySurface displaySurface = creationNotify.getDisplaySurface();
		handleClientDisplaySurface(displaySurface);
	}

	// called by "Display" thread.
	private void handleClientDisplaySurface(final DisplaySurface displaySurface) {
		final ListenableFuture<ShellSurface> shellSurfaceFuture = this.shellSurfaceFactory.createShellClientSurface(displaySurface);
		// callback will be called by "Shell" thread.
		addCallback(shellSurfaceFuture,
					new FutureCallback<ShellSurface>() {
						@Override
						public void onSuccess(final ShellSurface shellSurface) {
							WindowManagerPlugin.this.sceneManager.manageClient(shellSurface);
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Failed to create a shellsurface",
											t);
						}
					});
	}
}