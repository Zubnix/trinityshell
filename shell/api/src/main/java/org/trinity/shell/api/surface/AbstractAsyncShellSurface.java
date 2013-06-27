package org.trinity.shell.api.surface;

import java.util.concurrent.Callable;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.scene.AbstractShellNodeParent;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.trinity.shell.api.scene.ShellScene;

// TODO from boilerplate code generator
/***************************************
 * Abstract asynchronous base implementation of a {@link ShellSurface}. Method
 * calls are placed on the injected shell executor queue. Subclasses must
 * implement any concrete internal node manipulation.
 * 
 *************************************** 
 */
public abstract class AbstractAsyncShellSurface extends AbstractShellNodeParent implements ShellSurface {

	private final ListeningExecutorService shellExecutor;

	protected AbstractAsyncShellSurface(final ShellScene shellScene,final ListeningExecutorService shellExecutor) {
		super(shellScene,shellExecutor);
		this.shellExecutor = shellExecutor;
	}

	@Override
	public final ListenableFuture<Integer> getHeightIncrement() {
		return this.shellExecutor.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return getHeightIncrementImpl();
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #getHeightIncrement()}. This method is
	 * invoked by the Shell thread.
	 * 
	 * @return an {@link Integer}
	 * @see #getHeightIncrement()
	 *************************************** 
	 */
	public abstract Integer getHeightIncrementImpl();

	@Override
	public final ListenableFuture<Size> getMaxSize() {
		return this.shellExecutor.submit(new Callable<Size>() {
			@Override
			public Size call() throws Exception {
				return getMaxSizeImpl();
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #getMaxSize()}. This method is invoked
	 * by the Shell thread.
	 * 
	 * @return a {@link Size}
	 * @see #getMaxSize()
	 *************************************** 
	 */
	public abstract Size getMaxSizeImpl();

	@Override
	public final ListenableFuture<Size> getMinSize() {
		return this.shellExecutor.submit(new Callable<Size>() {
			@Override
			public Size call() throws Exception {
				return getMinSizeImpl();
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #getMinSize()}. This method is invoked
	 * by the Shell thread.
	 * 
	 * @return a {@link Size}
	 * @see #getMinSize()
	 *************************************** 
	 */
	public abstract Size getMinSizeImpl();

	@Override
	public final ListenableFuture<DisplaySurface> getDisplaySurface() {
		return this.shellExecutor.submit(new Callable<DisplaySurface>() {
			@Override
			public DisplaySurface call() throws Exception {
				return getDisplaySurfaceImpl();
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #getDisplaySurface()}. This method is
	 * invoked by the Shell thread.
	 * 
	 * @return a {@link DisplaySurface}
	 * @see #getDisplaySurface()
	 *************************************** 
	 */
	public abstract DisplaySurface getDisplaySurfaceImpl();

	@Override
	public final ListenableFuture<Integer> getWidthIncrement() {
		return this.shellExecutor.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return getWidthIncrementImpl();
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #getWidthIncrement()}. This method is
	 * invoked by the Shell thread.
	 * 
	 * @return an {@link Integer}
	 * @see #getWidthIncrement()
	 *************************************** 
	 */
	public abstract Integer getWidthIncrementImpl();

	@Override
	public final ListenableFuture<Boolean> isMovable() {
		return this.shellExecutor.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return isMovableImpl();
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #isMovable()}. This method is invoked
	 * by the Shell thread.
	 * 
	 * @return a {@link Boolean}
	 * @see #isMovable()
	 *************************************** 
	 */
	public abstract Boolean isMovableImpl();

	@Override
	public final ListenableFuture<Boolean> isResizable() {
		return this.shellExecutor.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return isResizableImpl();
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #isResizable()}. This method is invoked
	 * by the Shell thread.
	 * 
	 * @return a {@link Boolean}
	 * @see #isResizable()
	 *************************************** 
	 */
	public abstract Boolean isResizableImpl();

	@Override
	public final ListenableFuture<Void> setHeightIncrement(final int heightIncrement) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return setHeightIncrementImpl(heightIncrement);
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #setHeightIncrement(int)}. This method
	 * is invoked by the Shell thread.
	 * 
	 * @return null
	 * @see #setHeightIncrement(int)
	 *************************************** 
	 */
	public abstract Void setHeightIncrementImpl(int heightIncrement);

	@Override
	public final ListenableFuture<Void> setMaxSize(final Size maxSize) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return setMaxSizeImpl(maxSize);
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #setMaxSize(Size)}. This method is
	 * invoked by the Shell thread.
	 * 
	 * @return null
	 * @see #setMaxSize(Size)
	 *************************************** 
	 */
	public abstract Void setMaxSizeImpl(Size maxSize);

	@Override
	public final ListenableFuture<Void> setMinSize(final Size minSize) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return setMinSizeImpl(minSize);
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #setMinSize(Size)}. This method is
	 * invoked by the Shell thread.
	 * 
	 * @return null
	 * @see #setMinSize(Size)
	 *************************************** 
	 */
	public abstract Void setMinSizeImpl(Size maxSize);

	@Override
	public final ListenableFuture<Void> setMovable(final boolean movable) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return setMovableImpl(movable);
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #setMovable(boolean)}. This method is
	 * invoked by the Shell thread.
	 * 
	 * @return null
	 * @see #setMovable(boolean)
	 *************************************** 
	 */
	public abstract Void setMovableImpl(boolean movable);

	@Override
	public final ListenableFuture<Void> setResizable(final boolean resizable) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return setResizableImpl(resizable);
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #setResizable(boolean)}. This method is
	 * invoked by the Shell thread.
	 * 
	 * @return null
	 * @see #setResizable(boolean)
	 *************************************** 
	 */
	public abstract Void setResizableImpl(boolean resizable);

	@Override
	public final ListenableFuture<Void> setWidthIncrement(final int widthIncrement) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return setWidthIncrementImpl(widthIncrement);
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #setWidthIncrement(int)}. This method
	 * is invoked by the Shell thread.
	 * 
	 * @return null
	 * @see #setWidthIncrement(int)
	 *************************************** 
	 */
	public abstract Void setWidthIncrementImpl(int widthIncrement);
}