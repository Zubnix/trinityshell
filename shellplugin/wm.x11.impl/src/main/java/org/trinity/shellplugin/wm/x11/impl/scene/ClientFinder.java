package org.trinity.shellplugin.wm.x11.impl.scene;

import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplayServer;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.OwnerThread;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.ShellSurfaceFactory;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

import static com.google.common.util.concurrent.Futures.addCallback;

@Bind(to = @To(value = Type.IMPLEMENTATION))
@Singleton
@OwnerThread("WindowManager")
@ThreadSafe
public class ClientFinder {

	private static final Logger logger = LoggerFactory.getLogger(ClientFinder.class);

	private final DisplayServer displayServer;
	private final ListeningExecutorService wmExecutor;
	private final SceneManager sceneManager;
	private final ShellSurfaceFactory shellSurfaceFactory;

	@Inject
	ClientFinder(	final DisplayServer displayServer,
					final ShellSurfaceFactory shellSurfaceFactory,
					final SceneManager sceneManager,
					@Named("WindowManager") final ListeningExecutorService wmExecutor) {
		this.displayServer = displayServer;
		this.shellSurfaceFactory = shellSurfaceFactory;
		this.sceneManager = sceneManager;
		this.wmExecutor = wmExecutor;
	}

	public void find() {
		final ListenableFuture<DisplaySurface[]> clientDisplaySurfaces = this.displayServer.getClientDisplaySurfaces();
		addCallback(clientDisplaySurfaces,
					new FutureCallback<DisplaySurface[]>() {
						@Override
						public void onSuccess(final DisplaySurface[] displaySurfaces) {
							for (final DisplaySurface displaySurface : displaySurfaces) {
								manageClient(displaySurface);
							}
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Unable to query for existing display surfaces.",
											t);

						}
					},
					this.wmExecutor);
	}

	private void manageClient(final DisplaySurface client) {
		final ListenableFuture<ShellSurface> clientShellSurfaceFuture = this.shellSurfaceFactory
				.createShellClientSurface(client);
		addCallback(clientShellSurfaceFuture,
					new FutureCallback<ShellSurface>() {
						@Override
						public void onSuccess(final ShellSurface clientShellSurface) {
							ClientFinder.this.sceneManager.manageNewClient(clientShellSurface);
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Failed to create client shell surface.",
											t);
						}
					});
	}
}
