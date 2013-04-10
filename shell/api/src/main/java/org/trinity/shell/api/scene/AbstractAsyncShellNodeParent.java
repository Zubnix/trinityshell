package org.trinity.shell.api.scene;

import java.util.concurrent.Callable;

import org.trinity.shell.api.scene.manager.ShellLayoutManager;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

/***************************************
 * Asynchronous abstract implementation of a {@link ShellNodeParent}. Method
 * calls are placed on the shell executor queue as provide in the constructor.
 * Subclasses must implement any concrete internal node manipulation.
 *************************************** 
 */
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

	/***************************************
	 * Concrete implementation of {@link #getLayoutManager()}. This method
	 * should only be invoked by the Shell thread.
	 * 
	 * @return an {@link Optional} {@link ShellLayoutManager}.
	 * @see #getLayoutManager()
	 *************************************** 
	 */
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

	/***************************************
	 * Concrete implementation of {@link #layout()}. This method should only be
	 * invoked by the Shell thread.
	 * 
	 * @return null
	 * @see #layout()
	 *************************************** 
	 */
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

	/***************************************
	 * Concrete implementation of {@link #setLayoutManager(ShellLayoutManager)}.
	 * This method should only be invoked by the Shell thread.
	 * 
	 * @return null
	 * @see #setLayoutManager(ShellLayoutManager)
	 *************************************** 
	 */
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

	/***************************************
	 * Concrete implementation of {@link #getChildren()}. This method should
	 * only be invoked by the Shell thread.
	 * 
	 * @return an array of {@link ShellNode}s
	 * @see #getChildren()
	 *************************************** 
	 */
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

	/***************************************
	 * Concrete implementation of {@link #handleChildReparentEvent(ShellNode)}.
	 * This method should only be invoked by the Shell thread.
	 * 
	 * @return null
	 * @see #handleChildReparentEvent(ShellNode)
	 *************************************** 
	 */
	public abstract Void handleChildReparentEventImpl(final ShellNode child);

}
