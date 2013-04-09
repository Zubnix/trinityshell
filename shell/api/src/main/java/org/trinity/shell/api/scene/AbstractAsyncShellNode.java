package org.trinity.shell.api.scene;

import java.util.concurrent.Callable;

import javax.inject.Named;

import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.foundation.api.shared.Size;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;

// TODO auto generate this boiler plate code
// TODO documentation
/***************************************
 * Asynchronous abstract implementation of a {@link ShellNode}. Method calls are
 * placed on the shell executor queue. Subclasses must implement any concrete
 * internal node manipulation.
 * 
 *************************************** 
 */
public abstract class AbstractAsyncShellNode implements ShellNode {

	private final ListeningExecutorService shellExecutor;

	@Override
	public ListenableFuture<Coordinate> getPosition() {
		return this.shellExecutor.submit(new Callable<Coordinate>() {
			@Override
			public Coordinate call() throws Exception {
				return getPositionImpl();
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #getPosition()}. This method should
	 * only be invoked by the Shell thread.
	 * 
	 * @return a {@link Coordinate}, depicting this node's position.
	 * @see #getPosition()
	 *************************************** 
	 */
	public abstract Coordinate getPositionImpl();

	@Override
	public ListenableFuture<Size> getSize() {
		return this.shellExecutor.submit(new Callable<Size>() {
			@Override
			public Size call() throws Exception {
				return getSizeImpl();
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #getSize()}. This method should only be
	 * invoked by the Shell thread.
	 * 
	 * @return a {@link Size}, depicting this node's size.
	 * @see #getSize()
	 *************************************** 
	 */
	public abstract Size getSizeImpl();

	@Override
	public ListenableFuture<Void> setPosition(final Coordinate position) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return setPositionImpl(position);
			}
		});
	}

	public abstract Void setPositionImpl(final Coordinate position);

	@Override
	public ListenableFuture<Void> setSize(final Size size) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return setSizeImpl(size);
			}
		});
	}

	public abstract Void setSizeImpl(final Size size);

	@Inject
	protected AbstractAsyncShellNode(@Named("Shell") final ListeningExecutorService shellExecutor) {
		this.shellExecutor = shellExecutor;
	}

	@Override
	public final ListenableFuture<Rectangle> getGeometry() {
		return this.shellExecutor.submit(new Callable<Rectangle>() {
			@Override
			public Rectangle call() throws Exception {
				return getGeometryImpl();
			}
		});
	}

	public abstract Rectangle getGeometryImpl();

	@Override
	public final ListenableFuture<Boolean> isVisible() {
		return this.shellExecutor.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() {
				return isVisibleImpl();
			}
		});
	}

	public abstract Boolean isVisibleImpl();

	@Override
	public final ListenableFuture<Void> cancelPendingMove() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return cancelPendingMoveImpl();
			}
		});
	}

	public abstract Void cancelPendingMoveImpl();

	@Override
	public final ListenableFuture<Void> cancelPendingResize() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return cancelPendingResizeImpl();
			}
		});
	}

	public abstract Void cancelPendingResizeImpl();

	@Override
	public final ListenableFuture<Void> doDestroy() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return doDestroyImpl();
			}
		});
	}

	public abstract Void doDestroyImpl();

	@Override
	public final ListenableFuture<Void> doLower() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return doLowerImpl();
			}
		});
	}

	public abstract Void doLowerImpl();

	@Override
	public final ListenableFuture<Void> doReparent() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return doReparentImpl();
			}
		});
	}

	public abstract Void doReparentImpl();

	@Override
	public final ListenableFuture<Void> doMove() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return doMoveImpl();
			}
		});
	}

	public abstract Void doMoveImpl();

	@Override
	public final ListenableFuture<Void> doRaise() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return doRaiseImpl();
			}
		});
	}

	public abstract Void doRaiseImpl();

	@Override
	public final ListenableFuture<Void> doMoveResize() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return doMoveResizeImpl();
			}
		});
	}

	public abstract Void doMoveResizeImpl();

	@Override
	public final ListenableFuture<Void> doResize() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return doResizeImpl();
			}
		});
	}

	public abstract Void doResizeImpl();

	@Override
	public final ListenableFuture<Void> doShow() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return doShowImpl();
			}
		});
	}

	public abstract Void doShowImpl();

	@Override
	public final ListenableFuture<Void> doHide() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return doHideImpl();
			}
		});
	}

	public abstract Void doHideImpl();

	@Override
	public final ListenableFuture<Boolean> isDestroyed() {
		return this.shellExecutor.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() {
				return isDestroyedImpl();
			}
		});
	}

	public abstract Boolean isDestroyedImpl();

	@Override
	public final ListenableFuture<Void> requestLower() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return requestLowerImpl();
			}
		});
	}

	public abstract Void requestLowerImpl();

	@Override
	public final ListenableFuture<Void> requestMove() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return requestMoveImpl();
			}
		});
	}

	public abstract Void requestMoveImpl();

	@Override
	public final ListenableFuture<Void> requestMoveResize() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return requestMoveResizeImpl();
			}
		});
	}

	public abstract Void requestMoveResizeImpl();

	@Override
	public final ListenableFuture<Void> requestRaise() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return requestRaiseImpl();
			}
		});
	}

	public abstract Void requestRaiseImpl();

	@Override
	public final ListenableFuture<Void> requestReparent() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return requestReparentImpl();
			}
		});
	}

	public abstract Void requestReparentImpl();

	@Override
	public final ListenableFuture<Void> requestResize() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return requestResizeImpl();
			}
		});
	}

	public abstract Void requestResizeImpl();

	@Override
	public final ListenableFuture<Void> requestShow() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return requestShowImpl();
			}
		});
	}

	public abstract Void requestShowImpl();

	@Override
	public final ListenableFuture<Void> requestHide() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return requestHideImpl();
			}
		});
	}

	public abstract Void requestHideImpl();

	@Override
	public final ListenableFuture<Void> setSize(final int width,
												final int height) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return setSizeImpl(	width,
									height);
			}
		});
	}

	public abstract Void setSizeImpl(	final int width,
										final int height);

	@Override
	public final ListenableFuture<Void> setPosition(final int x,
													final int y) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return setPositionImpl(	x,
										y);
			}
		});
	}

	public abstract Void setPositionImpl(	int x,
											int y);

	@Override
	public final ListenableFuture<Void> setParent(final ShellNodeParent parent) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return setParentImpl(parent);
			}
		});
	}

	public abstract Void setParentImpl(ShellNodeParent parent);

	@Override
	public final ListenableFuture<ShellNodeParent> getParent() {
		return this.shellExecutor.submit(new Callable<ShellNodeParent>() {
			@Override
			public ShellNodeParent call() {
				return getParentImpl();
			}
		});
	}

	public abstract ShellNodeParent getParentImpl();

	@Override
	public final ListenableFuture<ShellNodeTransformation> toGeoTransformation() {
		return this.shellExecutor.submit(new Callable<ShellNodeTransformation>() {
			@Override
			public ShellNodeTransformation call() {
				return toGeoTransformationImpl();
			}
		});
	}

	public abstract ShellNodeTransformation toGeoTransformationImpl();
}