package org.trinity.shellplugin.wm.x11.impl.scene;

import static com.google.common.util.concurrent.Futures.addCallback;
import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;

import javax.annotation.concurrent.NotThreadSafe;

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
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Bind
@To(IMPLEMENTATION)
@NotThreadSafe
@Singleton
@ExecutionContext(ShellExecutor.class)
public class SceneManager {

	private static final Logger LOG = LoggerFactory.getLogger(SceneManager.class);
	private final ClientBarElementFactory clientBarElementFactory;
	private final ShellLayoutManager rootLayoutManager;
	private final ShellRootWidget shellRootWidget;
	private final XWindowProtocol xWindowProtocol;

	@Inject
	SceneManager(	final ClientBarElementFactory clientBarElementFactory,
					final XWindowProtocol xWindowProtocol,
					final ShellRootWidget shellRootWidget,
					final ShellLayoutManagerLine shellLayoutManagerLine) {
		this.clientBarElementFactory = clientBarElementFactory;
		this.xWindowProtocol = xWindowProtocol;
		this.shellRootWidget = shellRootWidget;
		this.rootLayoutManager = shellLayoutManagerLine;

		this.shellRootWidget.setLayoutManager(this.rootLayoutManager);
		this.shellRootWidget.doShow();
	}

	// called by shell executor
	public void manageNewClient(final DisplaySurface displaySurface,
								final ShellSurface client) {
		this.xWindowProtocol.register(displaySurface);
		addClientTopBarItem(displaySurface,
							client);
		layoutClient(client);
	}

	// called by shell executor
	private void addClientTopBarItem(	final DisplaySurface displaySurface,
										final ShellSurface client) {
		final ClientBarElement clientBarElement = this.clientBarElementFactory.createClientTopBarItem(displaySurface);
		this.shellRootWidget.getClientsBar().add(clientBarElement);
		client.register(new Object() {
			@Subscribe
			public void onClientDestroyed(final ShellNodeDestroyedEvent destroyedEvent) {
				removeClientTopBarItem(clientBarElement);
			}
		});
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
							LOG.error(	"Failed to query destroyed state from client.",
										t);
						}
					});
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
