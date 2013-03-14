package org.trinity.shell.api.surface;

import java.util.concurrent.Callable;

import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.manager.ShellLayoutManager;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

public abstract class AbstractAsyncShellSurfaceParent extends AbstractShellSurface implements ShellSurfaceParent {

	private final ListeningExecutorService shellExecutor;

	protected AbstractAsyncShellSurfaceParent(final ListeningExecutorService shellExecutor) {
		super(shellExecutor);
		this.shellExecutor = shellExecutor;
	}

	@Override
	public final ListenableFuture<Optional<ShellLayoutManager>> getLayoutManager() {
		return this.shellExecutor.submit(new Callable<Optional<ShellLayoutManager>>() {
			@Override
			public Optional<ShellLayoutManager> call() throws Exception {
				return getLayoutManagerImpl();
			}
		});
	}

	protected abstract Optional<ShellLayoutManager> getLayoutManagerImpl();

	@Override
	public final ListenableFuture<Void> layout() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return layoutImpl();
			}
		});
	}

	protected abstract Void layoutImpl();

	@Override
	public final ListenableFuture<Void> setLayoutManager(final ShellLayoutManager shellLayoutManager) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return setLayoutManagerImpl(shellLayoutManager);
			}
		});
	}

	protected abstract Void setLayoutManagerImpl(ShellLayoutManager shellLayoutManager);

	@Override
	public final ListenableFuture<ShellNode[]> getChildren() {
		return this.shellExecutor.submit(new Callable<ShellNode[]>() {
			@Override
			public ShellNode[] call() throws Exception {
				return getChildrenImpl();
			}
		});
	}

	protected abstract ShellNode[] getChildrenImpl();

	@Override
	public final ListenableFuture<Void> handleChildReparentEvent(final ShellNode child) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return handleChildReparentEventImpl(child);
			}
		});
	}

	protected abstract Void handleChildReparentEventImpl(final ShellNode child);

}
