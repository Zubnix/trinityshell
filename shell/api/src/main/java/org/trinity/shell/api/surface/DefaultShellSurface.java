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

import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.bindingkey.ShellExecutor;

import com.google.common.util.concurrent.ListenableFuture;
import org.trinity.shell.api.scene.DefaultShellNodeParent;

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
public interface DefaultShellSurface extends DefaultShellNodeParent,ShellSurface {

	@Override
	public default ListenableFuture<Integer> getHeightIncrement() {
		return getShellExecutor().submit((Callable<Integer>) this::getHeightIncrementImpl);
	}

	/***************************************
	 * Concrete implementation of {@link #getHeightIncrement()}. This method is
	 * invoked by the Shell thread.
	 *
	 * @return an {@link Integer}
	 * @see #getHeightIncrement()
	 ***************************************
	 */
	Integer getHeightIncrementImpl();

	@Override
	public default ListenableFuture<Size> getMaxSize() {
		return getShellExecutor().submit((Callable<Size>) this::getMaxSizeImpl);
	}

	/***************************************
	 * Concrete implementation of {@link #getMaxSize()}. This method is invoked
	 * by the Shell thread.
	 *
	 * @return a {@link Size}
	 * @see #getMaxSize()
	 ***************************************
	 */
	Size getMaxSizeImpl();

	@Override
	public default ListenableFuture<Size> getMinSize() {
		return getShellExecutor().submit((Callable<Size>) this::getMinSizeImpl);
	}

	/***************************************
	 * Concrete implementation of {@link #getMinSize()}. This method is invoked
	 * by the Shell thread.
	 *
	 * @return a {@link Size}
	 * @see #getMinSize()
	 ***************************************
	 */
	Size getMinSizeImpl();

	@Override
	public default ListenableFuture<Integer> getWidthIncrement() {
		return getShellExecutor().submit((Callable<Integer>) this::getWidthIncrementImpl);
	}

	/***************************************
	 * Concrete implementation of {@link #getWidthIncrement()}. This method is
	 * invoked by the Shell thread.
	 *
	 * @return an {@link Integer}
	 * @see #getWidthIncrement()
	 ***************************************
	 */
	Integer getWidthIncrementImpl();

	@Override
	public default ListenableFuture<Boolean> isMovable() {
		return getShellExecutor().submit((Callable<Boolean>) this::isMovableImpl);
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
	public default ListenableFuture<Boolean> isResizable() {
		return getShellExecutor().submit((Callable<Boolean>) this::isResizableImpl);
	}

	/***************************************
	 * Concrete implementation of {@link #isResizable()}. This method is invoked
	 * by the Shell thread.
	 *
	 * @return a {@link Boolean}
	 * @see #isResizable()
	 ***************************************
	 */
	Boolean isResizableImpl();

	@Override
	public default ListenableFuture<Void> setHeightIncrement(@Nonnegative final int heightIncrement) {
		return getShellExecutor().submit((Callable<Void>) () -> setHeightIncrementImpl(heightIncrement));
	}

	/***************************************
	 * Concrete implementation of {@link #setHeightIncrement(int)}. This method
	 * is invoked by the Shell thread.
	 *
	 * @return null
	 * @see #setHeightIncrement(int)
	 ***************************************
	 */
	Void setHeightIncrementImpl(@Nonnegative int heightIncrement);

	@Override
	public default ListenableFuture<Void> setMaxSize(@Nonnull final Size maxSize) {
		return getShellExecutor().submit((Callable<Void>) () -> setMaxSizeImpl(maxSize));
	}

	/***************************************
	 * Concrete implementation of {@link #setMaxSize(Size)}. This method is
	 * invoked by the Shell thread.
	 *
	 * @return null
	 * @see #setMaxSize(Size)
	 ***************************************
	 */
	Void setMaxSizeImpl(Size maxSize);

	@Override
	public default ListenableFuture<Void> setMinSize(@Nonnull final Size minSize) {
		return getShellExecutor().submit((Callable<Void>) () -> setMinSizeImpl(minSize));
	}

	/***************************************
	 * Concrete implementation of {@link #setMinSize(Size)}. This method is
	 * invoked by the Shell thread.
	 *
	 * @return null
	 * @see #setMinSize(Size)
	 ***************************************
	 */
	Void setMinSizeImpl(Size maxSize);

	@Override
	public default ListenableFuture<Void> setMovable(final boolean movable) {
		return getShellExecutor().submit((Callable<Void>) () -> setMovableImpl(movable));
	}

	/***************************************
	 * Concrete implementation of {@link #setMovable(boolean)}. This method is
	 * invoked by the Shell thread.
	 *
	 * @return null
	 * @see #setMovable(boolean)
	 ***************************************
	 */
	Void setMovableImpl(boolean movable);

	@Override
	public default ListenableFuture<Void> setResizable(final boolean resizable) {
		return getShellExecutor().submit((Callable<Void>) () -> setResizableImpl(resizable));
	}

	/***************************************
	 * Concrete implementation of {@link #setResizable(boolean)}. This method is
	 * invoked by the Shell thread.
	 *
	 * @return null
	 * @see #setResizable(boolean)
	 ***************************************
	 */
	Void setResizableImpl(boolean resizable);

	@Override
	public default ListenableFuture<Void> setWidthIncrement(@Nonnegative final int widthIncrement) {
		return getShellExecutor().submit((Callable<Void>) () -> setWidthIncrementImpl(widthIncrement));
	}

	/***************************************
	 * Concrete implementation of {@link #setWidthIncrement(int)}. This method
	 * is invoked by the Shell thread.
	 *
	 * @return null
	 * @see #setWidthIncrement(int)
	 ***************************************
	 */
	Void setWidthIncrementImpl(@Nonnegative int widthIncrement);
}