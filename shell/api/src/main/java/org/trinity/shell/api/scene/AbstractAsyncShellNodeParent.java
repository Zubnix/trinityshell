package org.trinity.shell.api.scene;

import java.util.concurrent.Callable;

import org.trinity.shell.api.scene.manager.ShellLayoutManager;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

public abstract class AbstractAsyncShellNodeParent extends AbstractShellNode implements ShellNodeParent {

	private final ListeningExecutorService shellExecutor;

	protected AbstractAsyncShellNodeParent(final ListeningExecutorService shellExecutor) {
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

	public abstract Optional<ShellLayoutManager> getLayoutManagerImpl();

	@Override
	public final ListenableFuture<Void> layout() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return layoutImpl();
			}
		});
	}

	public abstract Void layoutImpl();

	@Override
	public final ListenableFuture<Void> setLayoutManager(final ShellLayoutManager shellLayoutManager) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return setLayoutManagerImpl(shellLayoutManager);
			}
		});
	}

	public abstract Void setLayoutManagerImpl(ShellLayoutManager shellLayoutManager);

	@Override
	public final ListenableFuture<ShellNode[]> getChildren() {
		return this.shellExecutor.submit(new Callable<ShellNode[]>() {
			@Override
			public ShellNode[] call() throws Exception {
				return getChildrenImpl();
			}
		});
	}

	public abstract ShellNode[] getChildrenImpl();

	@Override
	public final ListenableFuture<Void> handleChildReparentEvent(final ShellNode child) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return handleChildReparentEventImpl(child);
			}
		});
	}

	public abstract Void handleChildReparentEventImpl(final ShellNode child);

}
