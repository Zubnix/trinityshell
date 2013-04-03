package org.trinity.shellplugin.wm.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplayServer;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.CreationNotify;
import org.trinity.shell.api.plugin.ShellPlugin;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.ShellSurfaceFactory;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.Inject;

import de.devsurf.injection.guice.annotations.Bind;

import static com.google.common.util.concurrent.Futures.addCallback;

@Bind(multiple = true)
public class WindowManagerPlugin extends AbstractIdleService implements ShellPlugin {

	private static final Logger logger = LoggerFactory.getLogger(WindowManagerPlugin.class);

	private final DisplayServer displayServer;
	private final Scene scene;
	private final ShellSurfaceFactory shellSurfaceFactory;

	@Inject
	WindowManagerPlugin(final Scene scene,
						final ShellSurfaceFactory shellSurfaceFactory,
						final DisplayServer displayServer) {
		this.displayServer = displayServer;
		this.scene = scene;
		this.shellSurfaceFactory = shellSurfaceFactory;
	}

	@Override
	protected void startUp() throws Exception {
		// We register without specifying an executor. This means our listener
		// (@Subscribe) will be called by the display thread itself.
		this.displayServer.register(this);
	}

	@Override
	protected void shutDown() throws Exception {
		this.displayServer.unregister(this);
	}

	@Subscribe
	public void handleCreationNotify(final CreationNotify creationNotify) {
		final DisplaySurface displaySurface = creationNotify.getDisplaySurface();
		final ListenableFuture<ShellSurface> shellSurfaceFuture = this.shellSurfaceFactory
				.createShellClientSurface(displaySurface);
		// callback will be called by the shell thread.
		addCallback(shellSurfaceFuture,
					new FutureCallback<ShellSurface>() {
						@Override
						public void onSuccess(final ShellSurface result) {
							handleShellSurface(result);
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Failed to create a shellsurface",
											t);
						}
					});
	}

	public void handleShellSurface(final ShellSurface shellSurface) {
		this.scene.addClient(shellSurface);
	}
}
