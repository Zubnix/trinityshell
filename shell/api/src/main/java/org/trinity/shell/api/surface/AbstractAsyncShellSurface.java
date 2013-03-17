package org.trinity.shell.api.surface;

import java.util.concurrent.Callable;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.scene.AbstractShellNodeParent;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

// TODO from boilerplate code generator
public abstract class AbstractAsyncShellSurface extends AbstractShellNodeParent implements ShellSurface {

	private final ListeningExecutorService shellExecutor;

	protected AbstractAsyncShellSurface(final ListeningExecutorService shellExecutor) {
		super(shellExecutor);
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

	public abstract Void setWidthIncrementImpl(int widthIncrement);

	@Override
	public final ListenableFuture<Void> syncGeoToDisplaySurface() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return syncGeoToDisplaySurfaceImpl();
			}
		});
	}

	public abstract Void syncGeoToDisplaySurfaceImpl();

}
