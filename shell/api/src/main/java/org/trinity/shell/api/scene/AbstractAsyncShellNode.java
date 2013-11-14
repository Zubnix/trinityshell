/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

package org.trinity.shell.api.scene;

import java.util.concurrent.Callable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.bindingkey.ShellExecutor;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

// TODO auto generate this boiler plate code?
// TODO documentation
/***************************************
 * Asynchronous abstract implementation of a {@link ShellNode}. Method calls are
 * are delegated to the shell executor. Subclasses must implement any concrete
 * internal node manipulation.
 ***************************************
 */
@ThreadSafe
@ExecutionContext(ShellExecutor.class)
public abstract class AbstractAsyncShellNode implements ShellNode {

	private final ListeningExecutorService shellExecutor;

	protected AbstractAsyncShellNode(@Nonnull @ShellExecutor final ListeningExecutorService shellExecutor) {
		this.shellExecutor = shellExecutor;
	}

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
	 * Concrete implementation of {@link #getPosition()}. This method is invoked
	 * by the Shell thread.
	 *
	 * @return a {@link Coordinate}, depicting this node's shell position.
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
	 * Concrete implementation of {@link #getSize()}. This method is invoked by
	 * the Shell thread.
	 *
	 * @return a {@link Size}, depicting this node's shell size.
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

	/***************************************
	 * Concrete implementation of {@link #setPosition(Coordinate)}. This method
	 * is invoked by the Shell thread.
	 *
	 * @param position
	 *            a {@link Coordinate}.
	 * @return null
	 * @see #setPosition(Coordinate)
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #setSize(Size)}. This method is invoked
	 * by the Shell thread.
	 *
	 * @param size
	 *            a {@link Size}
	 * @return null
	 * @see #setSize(Size)
	 ***************************************
	 */
	public abstract Void setSizeImpl(final Size size);

	@Override
	public final ListenableFuture<Rectangle> getGeometry() {
		return this.shellExecutor.submit(new Callable<Rectangle>() {
			@Override
			public Rectangle call() throws Exception {
				return getGeometryImpl();
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #getGeometry()}. This method is invoked
	 * by the Shell thread.
	 *
	 * @return a {@link Rectangle}
	 * @see #getGeometryImpl()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #isVisible()}. This method is invoked
	 * by the Shell thread.
	 *
	 * @return a {@link Boolean}
	 * @see #isVisible()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #cancelPendingMove()}. This method is
	 * invoked by the Shell thread.
	 *
	 * @return null
	 * @see #cancelPendingMove()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #cancelPendingResize()}. This method is
	 * invoked by the Shell thread.
	 *
	 * @return null
	 * @see #cancelPendingResize()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #doDestroy()}. This method should only
	 * be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #doDestroy()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #doLower()}. This method is invoked by
	 * the Shell thread.
	 *
	 * @return null
	 * @see #doLower()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #doReparent()}. This method should only
	 * be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #doReparent()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #doMove()}. This method is invoked by
	 * the Shell thread.
	 *
	 * @return null
	 * @see #doMove()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #doRaise()}. This method is invoked by
	 * the Shell thread.
	 *
	 * @return null
	 * @see #doRaise()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #doMoveResize()}. This method should
	 * only be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #doMoveResize()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #doResize()}. This method should only
	 * be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #doResize()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #doShow()}. This method is invoked by
	 * the Shell thread.
	 *
	 * @return null
	 * @see #doShow()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #doHide()}. This method is invoked by
	 * the Shell thread.
	 *
	 * @return null
	 * @see #doHide()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #isDestroyed()}. This method should
	 * only be invoked by the Shell thread.
	 *
	 * @return a {@link Boolean}
	 * @see #isDestroyed()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #requestLower()}. This method should
	 * only be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #requestLower()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #requestMove()}. This method should
	 * only be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #requestMove()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #requestMoveResize()}. This method is
	 * invoked by the Shell thread.
	 *
	 * @return null
	 * @see #requestMoveResize()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #requestRaise()}. This method should
	 * only be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #requestRaise()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #requestReparent()}. This method should
	 * only be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #requestReparent()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #requestResize()}. This method should
	 * only be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #requestResize()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #requestShow()}. This method should
	 * only be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #requestShow()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #requestHide()}. This method should
	 * only be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #requestHide()
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #setSize(int, int)}. This method should
	 * only be invoked by the Shell thread.
	 *
     * @param width The desired width, usually in pixels
     * @param height The desired height, usually in pixels.
	 * @return null
	 * @see #setSize(int, int)
	 ***************************************
	 */
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

	/***************************************
	 * Concrete implementation of {@link #setPosition(int, int)}. This method is
	 * invoked by the Shell thread.
	 *
     * @param x The desired horizontal position, usually in pixels
     * @param y The desired vertical position, usually in pixels.
	 * @return null
	 * @see #setPosition(int, int)
	 ***************************************
	 */
	public abstract Void setPositionImpl(	int x,
											int y);

	@Override
	public final ListenableFuture<Void> setParent(final Optional<? extends ShellNodeParent> parent) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				return setParentImpl(parent);
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #setParent(Optional)}. This method is
	 * invoked by the Shell thread.
	 *
     * @param parent An optional parent. An absent parent indicates the removal of this node's parent.
	 * @return null
	 * @see #setParent(Optional)
	 ***************************************
	 */
	public abstract Void setParentImpl(Optional<? extends ShellNodeParent> parent);

	@Override
	public final ListenableFuture<Optional<ShellNodeParent>> getParent() {
		return this.shellExecutor.submit(new Callable<Optional<ShellNodeParent>>() {
			@Override
			public Optional<ShellNodeParent> call() {
				return getParentImpl();
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #getParent()}. This method should only
	 * be invoked by the Shell thread.
	 *
	 * @return a {@link ShellNodeParent}.
	 * @see #getParent()
	 ***************************************
	 */
	public abstract Optional<ShellNodeParent> getParentImpl();

	@Override
	public final ListenableFuture<ShellNodeTransformation> toGeoTransformation() {
		return this.shellExecutor.submit(new Callable<ShellNodeTransformation>() {
			@Override
			public ShellNodeTransformation call() {
				return toGeoTransformationImpl();
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #toGeoTransformation()}. This method is
	 * invoked by the Shell thread.
	 *
	 * @return a {@link ShellNodeTransformation}
	 * @see #toGeoTransformation()
	 ***************************************
	 */
	public abstract ShellNodeTransformation toGeoTransformationImpl();
}
