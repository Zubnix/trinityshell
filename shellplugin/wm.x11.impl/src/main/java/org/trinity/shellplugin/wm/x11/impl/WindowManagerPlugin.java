package org.trinity.shellplugin.wm.x11.impl;

import static com.google.common.util.concurrent.Futures.addCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplayServer;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.CreationNotify;
import org.trinity.foundation.api.shared.OwnerThread;
import org.trinity.shell.api.plugin.ShellPlugin;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.ShellSurfaceFactory;
import org.trinity.shellplugin.wm.x11.impl.scene.ClientFinder;
import org.trinity.shellplugin.wm.x11.impl.scene.SceneManager;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.Inject;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@OwnerThread("WindowManager")
public class WindowManagerPlugin extends AbstractIdleService implements ShellPlugin {

	private static final Logger logger = LoggerFactory.getLogger(WindowManagerPlugin.class);

	private final DisplayServer displayServer;
	private final SceneManager sceneManager;
	private final ShellSurfaceFactory shellSurfaceFactory;

	private final ClientFinder clientFinder;

	@Inject
	WindowManagerPlugin(final SceneManager sceneManager,
						final ShellSurfaceFactory shellSurfaceFactory,
						final DisplayServer displayServer,
						final ClientFinder clientFinder) {

		this.displayServer = displayServer;
		this.sceneManager = sceneManager;
		this.shellSurfaceFactory = shellSurfaceFactory;
		this.clientFinder = clientFinder;
	}

	// called by "Window Manager" thread.
	@Override
	protected void startUp() throws Exception {
		// We register without specifying an executor. This
		// means our listener (@Subscribe method) will be
		// called by the "Display" thread.
		WindowManagerPlugin.this.displayServer.register(WindowManagerPlugin.this);
		// search & manage for existing clients on the display server.
		this.clientFinder.find();
	}

	// called by "Window Manager" thread.
	@Override
	protected void shutDown() throws Exception {
		this.displayServer.unregister(this);
	}

	// called by "Display" thread.
	@Subscribe
	public void handleCreationNotify(final CreationNotify creationNotify) {
		final DisplaySurface displaySurface = creationNotify.getDisplaySurface();
		handleClientDisplaySurface(displaySurface);
	}

	// Called by "Display" thread for new display surfaces.
	private void handleClientDisplaySurface(final DisplaySurface displaySurface) {
		final ListenableFuture<ShellSurface> shellSurfaceFuture = this.shellSurfaceFactory.createShellClientSurface(displaySurface);
		// callback will be called by "Shell" thread.
		addCallback(shellSurfaceFuture,
					new FutureCallback<ShellSurface>() {
						@Override
						public void onSuccess(final ShellSurface shellSurface) {
							WindowManagerPlugin.this.sceneManager.manageNewClient(shellSurface);
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Failed to create a shellsurface",
											t);
						}
					});
	}
}