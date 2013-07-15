package org.trinity.shellplugin.wm.x11.impl.scene;

import static com.google.common.util.concurrent.Futures.addCallback;
import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;

import java.util.List;

import javax.annotation.concurrent.ThreadSafe;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.Display;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.ShellSurfaceFactory;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Bind
@Singleton
@To(IMPLEMENTATION)
@ExecutionContext(DisplayExecutor.class)
@ThreadSafe
public class ClientFinder {

	private static final Logger LOG = LoggerFactory.getLogger(ClientFinder.class);
	private final Display display;
	private final SceneManager sceneManager;
	private final ShellSurfaceFactory shellSurfaceFactory;

	@Inject
	ClientFinder(	final Display display,
					final ShellSurfaceFactory shellSurfaceFactory,
					final SceneManager sceneManager) {
		this.display = display;
		this.shellSurfaceFactory = shellSurfaceFactory;
		this.sceneManager = sceneManager;
	}

	public void find() {
		final ListenableFuture<List<DisplaySurface>> clientDisplaySurfaces = this.display.getClientDisplaySurfaces();
		addCallback(clientDisplaySurfaces,
					new FutureCallback<List<DisplaySurface>>() {
						@Override
						public void onSuccess(final List<DisplaySurface> displaySurfaces) {
							for (final DisplaySurface displaySurface : displaySurfaces) {
								manageClient(displaySurface);
							}
						}

						@Override
						public void onFailure(final Throwable t) {
							LOG.error("Unable to query for existing display surfaces.",
									t);

						}
					});
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
							LOG.error("Failed to create client shell surface.",
									t);
						}
					});
	}
}
