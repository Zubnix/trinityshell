package org.trinity.shell.scene.impl;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import de.devsurf.injection.guice.annotations.Bind;
import org.trinity.foundation.api.shared.AsyncListenableEventBus;
import org.trinity.foundation.api.shared.OwnerThread;
import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellScene;
import org.trinity.shell.api.scene.event.ShellNodeEvent;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.ExecutorService;

/**
 *
 */
@Bind
@Singleton
@OwnerThread("Shell")
@ThreadSafe
public class ShellSceneImpl implements ShellScene {

    private final AsyncListenableEventBus muxer;

    @Inject
    ShellSceneImpl(@Named("Shell") final ListeningExecutorService executor) {
        muxer = new AsyncListenableEventBus(executor);
    }

    @Override
    public void addShellNode(final ShellNode shellNode) {
        shellNode.register(this);
    }

    @Override
    public void removeShellNode(final ShellNode shellNode) {
        shellNode.unregister(this);
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
    public void register(final Object listener, final ExecutorService executor) {
        muxer.register(listener, executor);
    }

    @Override
    public void unregister(final Object listener) {
        muxer.unregister(listener);
    }

    @Override
    public void post(final Object event) {
        muxer.post(event);
    }
}
