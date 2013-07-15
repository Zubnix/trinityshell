package org.trinity.shellplugin.wm.x11.impl.scene;

import static com.google.common.util.concurrent.Futures.addCallback;
import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;

import java.util.concurrent.Callable;

import javax.annotation.concurrent.ThreadSafe;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Margins;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.scene.event.ShellNodeDestroyedEvent;
import org.trinity.shell.api.scene.manager.ShellLayoutManager;
import org.trinity.shell.api.scene.manager.ShellLayoutManagerLine;
import org.trinity.shell.api.scene.manager.ShellLayoutPropertyLine;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shellplugin.wm.x11.impl.protocol.XWindowProtocol;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Bind
@To(IMPLEMENTATION)
@ThreadSafe
@Singleton
@ExecutionContext(ShellExecutor.class)
public class SceneManager {

	private static final Logger LOG = LoggerFactory.getLogger(SceneManager.class);
	private final ClientBarElementFactory clientBarElementFactory;
	private final ShellLayoutManager rootLayoutManager;
	private final ShellRootWidget shellRootWidget;
	private final XWindowProtocol xWindowProtocol;
	private final ListeningExecutorService shellExecutor;

	@Inject
	// FIXME use shell executor
	SceneManager(	@ShellExecutor final ListeningExecutorService shellExecutor,
					final ClientBarElementFactory clientBarElementFactory,
					final XWindowProtocol xWindowProtocol,
					final ShellRootWidget shellRootWidget,
					final ShellLayoutManagerLine shellLayoutManagerLine) {
		this.shellExecutor = shellExecutor;
		this.clientBarElementFactory = clientBarElementFactory;
		this.xWindowProtocol = xWindowProtocol;
		this.shellRootWidget = shellRootWidget;
		this.rootLayoutManager = shellLayoutManagerLine;

		this.shellRootWidget.setLayoutManager(this.rootLayoutManager);
		this.shellRootWidget.doShow();
	}

	public void manageNewClient(final ShellSurface client) {
		this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
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
							SceneManager.this.xWindowProtocol.register(xWindow);
						}

						@Override
						public void onFailure(final Throwable t) {
							LOG.error("Failed to find display surface from client shell surface",
									t);
						}
					},
					this.shellExecutor);
	}

	private void addClientTopBarItem(final ShellSurface client) {
		final ClientBarElement clientBarElement = this.clientBarElementFactory.createClientTopBarItem(client);
		SceneManager.this.shellRootWidget.getClientsBar().add(clientBarElement);
		client.register(new Object() {
							@Subscribe
							public void onClientDestroyed(final ShellNodeDestroyedEvent destroyedEvent) {
								removeClientTopBarItem(clientBarElement);
							}
						},
						this.shellExecutor);

		// check if we missed any destroy events. Corner case we remove the
		// object twice but that's not a problem.
		final ListenableFuture<Boolean> destroyedFuture = client.isDestroyed();
		addCallback(destroyedFuture,
					new FutureCallback<Boolean>() {
						@Override
						public void onSuccess(final Boolean destroyed) {
							if (destroyed) {
								removeClientTopBarItem(clientBarElement);
							}
						}

						@Override
						public void onFailure(final Throwable t) {
							LOG.error("Failed to query destroyed state from client.",
									t);
						}
					},
					this.shellExecutor);
	}

	private void removeClientTopBarItem(final ClientBarElement clientBarElement) {
		this.shellRootWidget.getClientsBar().remove(clientBarElement);
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
