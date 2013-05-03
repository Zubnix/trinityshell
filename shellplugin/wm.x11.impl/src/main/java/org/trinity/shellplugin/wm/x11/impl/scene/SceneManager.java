package org.trinity.shellplugin.wm.x11.impl.scene;

import java.util.concurrent.Callable;

import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.Margins;
import org.trinity.shell.api.scene.event.ShellNodeDestroyedEvent;
import org.trinity.shell.api.scene.manager.ShellLayoutManager;
import org.trinity.shell.api.scene.manager.ShellLayoutManagerLine;
import org.trinity.shell.api.scene.manager.ShellLayoutPropertyLine;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shellplugin.wm.x11.impl.protocol.XWindowProcol;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

import static com.google.common.util.concurrent.Futures.addCallback;

@Bind(to = @To(value = Type.IMPLEMENTATION))
@ThreadSafe
public class SceneManager {

	private static final Logger logger = LoggerFactory.getLogger(SceneManager.class);

	private final ClientTopBarItemFactory clientTopBarItemFactory;
	private final ShellLayoutManager rootLayoutManager;
	private final ShellRootWidget shellRootWidget;
	private final XWindowProcol xWindowProcol;
	private final ListeningExecutorService wmExecutor;

	@Inject
	SceneManager(	@Named("WindowManager") final ListeningExecutorService wmExecutor,
					final ClientTopBarItemFactory clientTopBarItemFactory,
					final XWindowProcol xWindowProcol,
					final ShellRootWidget shellRootWidget,
					final ShellLayoutManagerLine shellLayoutManagerLine) {
		this.wmExecutor = wmExecutor;
		this.clientTopBarItemFactory = clientTopBarItemFactory;
		this.xWindowProcol = xWindowProcol;
		this.shellRootWidget = shellRootWidget;
		this.rootLayoutManager = shellLayoutManagerLine;

		this.shellRootWidget.setLayoutManager(this.rootLayoutManager);
		this.shellRootWidget.doShow();

		registerXWindow(shellRootWidget);
	}

	public void manageClient(final ShellSurface client) {
		this.wmExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				registerXWindow(client);

				addClientTopBarItem(client);
				layoutClient(client);

				return null;
			}
		});
	}

	private void registerXWindow(final ShellSurface xShellSurface) {
		addCallback(xShellSurface.getDisplaySurface(),
					new FutureCallback<DisplaySurface>() {
						@Override
						public void onSuccess(final DisplaySurface xWindow) {
							SceneManager.this.xWindowProcol.register(xWindow);
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Failed to find display surface from client shell surface",
											t);
						}
					},
					this.wmExecutor);
	}

	private void addClientTopBarItem(final ShellSurface client) {
		final ClientTopBarItem clientTopBarItem = this.clientTopBarItemFactory.createClientTopBarItem(client);
		SceneManager.this.shellRootWidget.getTopBar().add(clientTopBarItem);
		client.register(new Object() {
							@Subscribe
							public void onClientDestroyed(final ShellNodeDestroyedEvent destroyedEvent) {
								removeClientTopBarItem(clientTopBarItem);
							}
						},
						this.wmExecutor);

		// check if we missed any destroy events. Corner case we remove the
		// object twice but that's not a problem.
		final ListenableFuture<Boolean> destroyedFuture = client.isDestroyed();
		addCallback(destroyedFuture,
					new FutureCallback<Boolean>() {
						@Override
						public void onSuccess(final Boolean destroyed) {
							if (destroyed) {
								removeClientTopBarItem(clientTopBarItem);
							}
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Failed to query destroyed state from client.",
											t);
						}
					},
					this.wmExecutor);
	}

	private void removeClientTopBarItem(final ClientTopBarItem clientTopBarItem) {
		this.shellRootWidget.getTopBar().remove(clientTopBarItem);
	}

	private void layoutClient(final ShellSurface client) {
		client.setParent(this.shellRootWidget);
		this.rootLayoutManager.addChildNode(client,
											new ShellLayoutPropertyLine(1,
																		new Margins(0,
																					20)));
		this.shellRootWidget.layout();
		client.doReparent();
		client.doShow();
	}
}