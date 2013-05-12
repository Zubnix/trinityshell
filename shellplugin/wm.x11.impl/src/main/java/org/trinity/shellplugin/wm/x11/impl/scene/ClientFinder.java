package org.trinity.shellplugin.wm.x11.impl.scene;

import static com.google.common.util.concurrent.Futures.addCallback;
import static com.google.common.util.concurrent.Futures.allAsList;
import static org.freedesktop.xcb.LibXcb.xcb_get_window_attributes;
import static org.freedesktop.xcb.LibXcb.xcb_get_window_attributes_reply;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.concurrent.ThreadSafe;

import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_window_attributes_cookie_t;
import org.freedesktop.xcb.xcb_get_window_attributes_reply_t;
import org.freedesktop.xcb.xcb_map_state_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplayServer;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.OwnerThread;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.ShellSurfaceFactory;
import org.trinity.shellplugin.wm.x11.impl.XConnection;
import org.trinity.shellplugin.wm.x11.impl.XcbErrorUtil;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

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
	private final XConnection xConnection;

	@Inject
	ClientFinder(	final DisplayServer displayServer,
					final ShellSurfaceFactory shellSurfaceFactory,
					final XConnection xConnection,
					final SceneManager sceneManager,
					@Named("WindowManager") final ListeningExecutorService wmExecutor) {
		this.displayServer = displayServer;
		this.shellSurfaceFactory = shellSurfaceFactory;
		this.xConnection = xConnection;
		this.sceneManager = sceneManager;
		this.wmExecutor = wmExecutor;
	}

	public void find() {
		final ListenableFuture<DisplaySurface[]> clientDisplaySurfaces = this.displayServer.getClientDisplaySurfaces();
		addCallback(clientDisplaySurfaces,
					new FutureCallback<DisplaySurface[]>() {
						@Override
						public void onSuccess(final DisplaySurface[] displaySurfaces) {
							// check what windows we have to ignore
							final List<ListenableFuture<Optional<DisplaySurface>>> surfaceAnalyzations = new ArrayList<ListenableFuture<Optional<DisplaySurface>>>();
							for (final DisplaySurface displaySurface : displaySurfaces) {
								surfaceAnalyzations.add(analyzeDisplaySurface(displaySurface));
							}

							filter(surfaceAnalyzations);
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Unable to query for existing display surfaces.",
											t);

						}
					},
					this.wmExecutor);
	}

	private void filter(final List<ListenableFuture<Optional<DisplaySurface>>> surfaceAnalyzations) {
		addCallback(allAsList(surfaceAnalyzations),
					new FutureCallback<List<Optional<DisplaySurface>>>() {
						@Override
						public void onSuccess(final List<Optional<DisplaySurface>> optionalDisplaySurfaces) {
							// now we filter out & manage the actual client
							// windows.
							for (final Optional<DisplaySurface> optionalDisplaySurface : optionalDisplaySurfaces) {
								if (optionalDisplaySurface.isPresent()) {
									manageClient(optionalDisplaySurface.get());
								}
							}
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Analyzing all found display surfaces failed.",
											t);
						}
					});
	}

	private ListenableFuture<Optional<DisplaySurface>> analyzeDisplaySurface(final DisplaySurface displaySurface) {
		final int winId = (Integer) displaySurface.getDisplaySurfaceHandle().getNativeHandle();
		final xcb_get_window_attributes_cookie_t window_attributes_cookie = xcb_get_window_attributes(	this.xConnection.getConnectionRef(),
																										winId);
		return this.wmExecutor.submit(new Callable<Optional<DisplaySurface>>() {
			@Override
			public Optional<DisplaySurface> call() throws Exception {
				final xcb_generic_error_t e = new xcb_generic_error_t();
				final xcb_get_window_attributes_reply_t window_attributes = xcb_get_window_attributes_reply(ClientFinder.this.xConnection.getConnectionRef(),
																											window_attributes_cookie,
																											e);
				if (xcb_generic_error_t.getCPtr(e) != 0) {
					final String errorMsg = XcbErrorUtil.toString(e);
					logger.error(errorMsg);
					return Optional.absent();
				}

				final int mapState = window_attributes.getMap_state();
				if (mapState == xcb_map_state_t.XCB_MAP_STATE_VIEWABLE) {
					return Optional.of(displaySurface);
				}

				return Optional.absent();
			}
		});
	}

	private void manageClient(final DisplaySurface client) {
		final ListenableFuture<ShellSurface> clientShellSurfaceFuture = this.shellSurfaceFactory.createShellClientSurface(client);
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
