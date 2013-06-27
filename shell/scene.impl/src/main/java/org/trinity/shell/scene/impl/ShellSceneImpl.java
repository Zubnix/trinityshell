package org.trinity.shell.scene.impl;

import java.util.concurrent.ExecutorService;

import javax.annotation.concurrent.ThreadSafe;

import org.trinity.foundation.api.shared.AsyncListenableEventBus;
import org.trinity.foundation.api.shared.OwnerThread;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shell.api.scene.ShellScene;
import org.trinity.shell.api.scene.event.ShellNodeEvent;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
@OwnerThread("Shell")
@ThreadSafe
public class ShellSceneImpl implements ShellScene {

	private final AsyncListenableEventBus muxer;
	private final ShellNodeParent rootShellNodeParent;

	@Inject
	ShellSceneImpl(@Named("Shell") final ListeningExecutorService shellExecutor) {
		muxer = new AsyncListenableEventBus(shellExecutor);

		// TODO how to configure the size?
		this.rootShellNodeParent = new ShellVirtualSurface(	this,
															shellExecutor);
	}

	@Subscribe
	public void handleShellNodeEvent(final ShellNodeEvent shellNodeEvent) {
		post(shellNodeEvent);
	}

	@Override
	public void register(final Object listener) {
		muxer.register(listener);
	}

	@Override
	public void register(	final Object listener,
							final ExecutorService executor) {
		muxer.register(	listener,
						executor);
	}

	@Override
	public void unregister(final Object listener) {
		muxer.unregister(listener);
	}

	@Override
	public void post(final Object event) {
		muxer.post(event);
	}

	@Override
	public ShellNodeParent getRootShellNodeParent() {
		return rootShellNodeParent;
	}
}
