package org.trinity.shell.api.surface;

import java.util.concurrent.Callable;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.shell.api.scene.AbstractShellNode;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

// TODO from boilerplate code generator
public abstract class AbstractAsyncShellSurface extends AbstractShellNode implements ShellSurface {

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
	public final ListenableFuture<Integer> getMaxHeight() {
		return this.shellExecutor.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return getMaxHeightImpl();
			}
		});
	}

	public abstract Integer getMaxHeightImpl();

	@Override
	public final ListenableFuture<Integer> getMaxWidth() {
		return this.shellExecutor.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return getMaxWidthImpl();
			}
		});
	}

	public abstract Integer getMaxWidthImpl();

	@Override
	public final ListenableFuture<Integer> getMinHeight() {
		return this.shellExecutor.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return getMinHeightImpl();
			}
		});
	}

	public abstract Integer getMinHeightImpl();

	@Override
	public final ListenableFuture<Integer> getMinWidth() {
		return this.shellExecutor.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return getMinWidthImpl();
			}
		});
	}

	public abstract Integer getMinWidthImpl();

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
	public final ListenableFuture<Void> setMaxHeight(final int maxHeight) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return setMaxHeightImpl(maxHeight);
			}
		});
	}

	public abstract Void setMaxHeightImpl(int maxHeight);

	@Override
	public final ListenableFuture<Void> setMaxWidth(final int maxWidth) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return setMaxWidthImpl(maxWidth);
			}
		});
	}

	public abstract Void setMaxWidthImpl(int maxWidth);

	@Override
	public final ListenableFuture<Void> setMinHeight(final int minHeight) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return setMinHeightImpl(minHeight);
			}
		});
	}

	public abstract Void setMinHeightImpl(int minHeight);

	@Override
	public final ListenableFuture<Void> setMinWidth(final int minWidth) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return setMinWidthImpl(minWidth);
			}
		});
	}

	public abstract Void setMinWidthImpl(int minWidth);

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
