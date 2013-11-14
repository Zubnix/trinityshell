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

package org.trinity.shell.api.surface;

import java.util.concurrent.Callable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.bindingkey.ShellScene;
import org.trinity.shell.api.scene.AbstractShellNodeParent;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

// TODO from boilerplate code generator
/***************************************
 * Abstract asynchronous base implementation of a {@link ShellSurface}. Method
 * calls are placed on the injected shell executor. Subclasses must implement
 * any concrete internal node manipulation.
 *
 ***************************************
 */
@NotThreadSafe
@ExecutionContext(ShellExecutor.class)
public abstract class AbstractAsyncShellSurface extends AbstractShellNodeParent implements ShellSurface {

	private final ListeningExecutorService shellExecutor;
	private final DisplaySurface displaySurface;

	protected AbstractAsyncShellSurface(@Nonnull DisplaySurface displaySurface,
										@Nonnull @ShellScene final AsyncListenable shellScene,
										@Nonnull @ShellExecutor final ListeningExecutorService shellExecutor) {
		super(	shellScene,
				shellExecutor);
		this.shellExecutor = shellExecutor;
		this.displaySurface = displaySurface;
	}

	@Override
	public DisplaySurface getDisplaySurface() {
		return displaySurface;
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
	public final ListenableFuture<Void> setHeightIncrement(@Nonnegative final int heightIncrement) {
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
	 * @param heightIncrement The absolute incrementation used when adjusting the height of this surface, usually in pixels.
	 * @return null
	 * @see #setHeightIncrement(int)
	 ***************************************
	 */
	public abstract Void setHeightIncrementImpl(@Nonnegative int heightIncrement);

	@Override
	public final ListenableFuture<Void> setMaxSize(@Nonnull final Size maxSize) {
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
     * @param maxSize The maximum size of this shell surface, usually in pixels.
	 * @return null
	 * @see #setMaxSize(Size)
	 ***************************************
	 */
	public abstract Void setMaxSizeImpl(Size maxSize);

	@Override
	public final ListenableFuture<Void> setMinSize(@Nonnull final Size minSize) {
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
     * @param maxSize The minimum size of this shell surface, usually in pixels.
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
     * @param movable True if this shell surface should be movable, false if not.
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
     * @param resizable True if this shell surface should be resizable, false if not.
	 * @return null
	 * @see #setResizable(boolean)
	 ***************************************
	 */
	public abstract Void setResizableImpl(boolean resizable);

	@Override
	public final ListenableFuture<Void> setWidthIncrement(@Nonnegative final int widthIncrement) {
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
     * @param widthIncrement The absolute incrementation used when adjusting the width of this surface, usually in pixels.
	 * @return null
	 * @see #setWidthIncrement(int)
	 ***************************************
	 */
	public abstract Void setWidthIncrementImpl(@Nonnegative int widthIncrement);
}
