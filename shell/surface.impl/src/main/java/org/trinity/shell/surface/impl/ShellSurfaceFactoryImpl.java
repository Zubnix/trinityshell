package org.trinity.shell.surface.impl;

import java.util.concurrent.Callable;

import javax.annotation.concurrent.ThreadSafe;

import org.trinity.foundation.api.display.DisplayServer;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.ShellSurfaceFactory;
import org.trinity.shell.api.surface.ShellSurfaceParent;

import com.google.common.base.Function;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

import static com.google.common.util.concurrent.Futures.transform;

@Bind
@ThreadSafe
public class ShellSurfaceFactoryImpl implements ShellSurfaceFactory {

	private final DisplayServer displayServer;
	private final ListeningExecutorService shellExecutor;

	private ShellSurfaceParent rootShellSurface;

	@Inject
	ShellSurfaceFactoryImpl(@Named("Shell") final ListeningExecutorService shellExecutor,
							final DisplayServer displayServer) {
		this.shellExecutor = shellExecutor;
		this.displayServer = displayServer;
	}

	@Override
	public ListenableFuture<ShellSurface> createShellClientSurface(final DisplaySurface displaySurface) {
		final ListenableFuture<ShellSurfaceParent> rootShellSurfaceFuture = getRootShellSurface();
		return transform(	rootShellSurfaceFuture,
							new Function<ShellSurfaceParent, ShellSurface>() {
								@Override
								public ShellSurface apply(final ShellSurfaceParent rootShellSurface) {
									return new ShellClientSurface(	ShellSurfaceFactoryImpl.this.shellExecutor,
																	rootShellSurface,
																	displaySurface);
								}
							});
	}

	@Override
	public ListenableFuture<ShellSurfaceParent> getRootShellSurface() {
		final ListenableFuture<Boolean> hasRootShellSurfaceFuture = this.shellExecutor.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return Boolean.valueOf(ShellSurfaceFactoryImpl.this.rootShellSurface != null);
			}
		});

		return transform(	hasRootShellSurfaceFuture,
							new AsyncFunction<Boolean, ShellSurfaceParent>() {
								@Override
								public ListenableFuture<ShellSurfaceParent> apply(final Boolean hasRootShellSurface) {
									final ListenableFuture<ShellSurfaceParent> rootShellSurfaceFuture;
									if (hasRootShellSurface.booleanValue()) {
										rootShellSurfaceFuture = getCachedRootShellSurface();
									} else {
										rootShellSurfaceFuture = createRootShellSurface();
									}
									return rootShellSurfaceFuture;
								}
							});
	}

	private ListenableFuture<ShellSurfaceParent> getCachedRootShellSurface() {
		return MoreExecutors.sameThreadExecutor().submit(new Callable<ShellSurfaceParent>() {
			@Override
			public ShellSurfaceParent call() throws Exception {
				return ShellSurfaceFactoryImpl.this.rootShellSurface;
			}
		});
	}

	private ListenableFuture<ShellSurfaceParent> createRootShellSurface() {
		final ListenableFuture<DisplaySurface> rootDisplaySurfaceFuture = this.displayServer.getRootDisplayArea();
		// Get the root display surface, which will be given to us by the
		// display thread, next we schedule a task on the shell thread to wrap
		// it in a root shell surface.
		return transform(	rootDisplaySurfaceFuture,
							new Function<DisplaySurface, ShellSurfaceParent>() {
								@Override
								public ShellSurfaceParent apply(final DisplaySurface input) {
									return new ShellRootSurface(ShellSurfaceFactoryImpl.this.shellExecutor,
																input);
								}
							},
							this.shellExecutor);
	}

}
