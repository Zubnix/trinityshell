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

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.bindingkey.ShellExecutor;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.Callable;

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
public interface DefaultShellNode extends ShellNode {

    ListeningExecutorService getShellExecutor();

	@Override
	public default ListenableFuture<Coordinate> getPosition() {
        return getShellExecutor().submit((Callable<Coordinate>) this::getPositionImpl);
    }

    /***************************************
     * Concrete implementation of {@link #getPosition()}. This method is invoked
     * by the Shell thread.
     *
     * @return a {@link Coordinate}, depicting this node's shell position.
	 * @see #getPosition()
	 ***************************************
	 */
	Coordinate getPositionImpl();

	@Override
	public default ListenableFuture<Size> getSize() {
        return getShellExecutor().submit((Callable<Size>) this::getSizeImpl);
    }

    /***************************************
     * Concrete implementation of {@link #getSize()}. This method is invoked by
     * the Shell thread.
     *
     * @return a {@link Size}, depicting this node's shell size.
     * @see #getSize()
	 ***************************************
	 */
	Size getSizeImpl();

	@Override
	public default ListenableFuture<Void> setPosition(final Coordinate position) {
        return getShellExecutor().submit((Callable<Void>) () -> setPositionImpl(position));
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
	Void setPositionImpl(final Coordinate position);

	@Override
	public default ListenableFuture<Void> setSize(final Size size) {
        return getShellExecutor().submit((Callable<Void>) () -> setSizeImpl(size));
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
	Void setSizeImpl(final Size size);

	@Override
	public default ListenableFuture<Rectangle> getGeometry() {
        return getShellExecutor().submit((Callable<Rectangle>) this::getGeometryImpl);
    }

    /***************************************
     * Concrete implementation of {@link #getGeometry()}. This method is invoked
     * by the Shell thread.
	 *
	 * @return a {@link Rectangle}
	 * @see #getGeometryImpl()
	 ***************************************
	 */
	Rectangle getGeometryImpl();

	@Override
	public default ListenableFuture<Boolean> isVisible() {
        return getShellExecutor().submit((Callable<Boolean>) this::isVisibleImpl);
    }

    /***************************************
     * Concrete implementation of {@link #isVisible()}. This method is invoked
     * by the Shell thread.
	 *
	 * @return a {@link Boolean}
	 * @see #isVisible()
	 ***************************************
	 */
	Boolean isVisibleImpl();

	@Override
	public default ListenableFuture<Void> cancelPendingMove() {
        return getShellExecutor().submit((Callable<Void>) this::cancelPendingMoveImpl);
    }

    /***************************************
     * Concrete implementation of {@link #cancelPendingMove()}. This method is
     * invoked by the Shell thread.
	 *
	 * @return null
	 * @see #cancelPendingMove()
	 ***************************************
	 */
	Void cancelPendingMoveImpl();

	@Override
	public default ListenableFuture<Void> cancelPendingResize() {
        return getShellExecutor().submit((Callable<Void>) this::cancelPendingResizeImpl);
    }

    /***************************************
     * Concrete implementation of {@link #cancelPendingResize()}. This method is
     * invoked by the Shell thread.
	 *
	 * @return null
	 * @see #cancelPendingResize()
	 ***************************************
	 */
	Void cancelPendingResizeImpl();

	@Override
	public default ListenableFuture<Void> doDestroy() {
        return getShellExecutor().submit((Callable<Void>) this::doDestroyImpl);
    }

    /***************************************
     * Concrete implementation of {@link #doDestroy()}. This method should only
     * be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #doDestroy()
	 ***************************************
	 */
	Void doDestroyImpl();

	@Override
	public default ListenableFuture<Void> doLower() {
        return getShellExecutor().submit((Callable<Void>) this::doLowerImpl);
    }

    /***************************************
     * Concrete implementation of {@link #doLower()}. This method is invoked by
     * the Shell thread.
	 *
	 * @return null
	 * @see #doLower()
	 ***************************************
	 */
	Void doLowerImpl();

	@Override
	public default ListenableFuture<Void> doReparent() {
        return getShellExecutor().submit((Callable<Void>) this::doReparentImpl);
    }

    /***************************************
     * Concrete implementation of {@link #doReparent()}. This method should only
     * be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #doReparent()
	 ***************************************
	 */
	Void doReparentImpl();

	@Override
	public default ListenableFuture<Void> doMove() {
        return getShellExecutor().submit((Callable<Void>) this::doMoveImpl);
    }

    /***************************************
     * Concrete implementation of {@link #doMove()}. This method is invoked by
     * the Shell thread.
	 *
	 * @return null
	 * @see #doMove()
	 ***************************************
	 */
	Void doMoveImpl();

	@Override
	public default ListenableFuture<Void> doRaise() {
        return getShellExecutor().submit((Callable<Void>) this::doRaiseImpl);
    }

    /***************************************
     * Concrete implementation of {@link #doRaise()}. This method is invoked by
     * the Shell thread.
	 *
	 * @return null
	 * @see #doRaise()
	 ***************************************
	 */
	Void doRaiseImpl();

	@Override
	public default ListenableFuture<Void> doMoveResize() {
        return getShellExecutor().submit((Callable<Void>) this::doMoveResizeImpl);
    }

    /***************************************
     * Concrete implementation of {@link #doMoveResize()}. This method should
     * only be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #doMoveResize()
	 ***************************************
	 */
	Void doMoveResizeImpl();

	@Override
	public default ListenableFuture<Void> doResize() {
        return getShellExecutor().submit((Callable<Void>) this::doResizeImpl);
    }

    /***************************************
     * Concrete implementation of {@link #doResize()}. This method should only
     * be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #doResize()
	 ***************************************
	 */
	Void doResizeImpl();

	@Override
	public default ListenableFuture<Void> doShow() {
        return getShellExecutor().submit((Callable<Void>) this::doShowImpl);
    }

    /***************************************
     * Concrete implementation of {@link #doShow()}. This method is invoked by
     * the Shell thread.
	 *
	 * @return null
	 * @see #doShow()
	 ***************************************
	 */
	Void doShowImpl();

	@Override
	public default ListenableFuture<Void> doHide() {
        return getShellExecutor().submit((Callable<Void>) this::doHideImpl);
    }

    /***************************************
     * Concrete implementation of {@link #doHide()}. This method is invoked by
     * the Shell thread.
	 *
	 * @return null
	 * @see #doHide()
	 ***************************************
	 */
	Void doHideImpl();

	@Override
	public default ListenableFuture<Boolean> isDestroyed() {
        return getShellExecutor().submit((Callable<Boolean>) this::isDestroyedImpl);
    }

    /***************************************
     * Concrete implementation of {@link #isDestroyed()}. This method should
     * only be invoked by the Shell thread.
	 *
	 * @return a {@link Boolean}
	 * @see #isDestroyed()
	 ***************************************
	 */
	Boolean isDestroyedImpl();

	@Override
	public default ListenableFuture<Void> requestLower() {
        return getShellExecutor().submit((Callable<Void>) this::requestLowerImpl);
    }

    /***************************************
     * Concrete implementation of {@link #requestLower()}. This method should
     * only be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #requestLower()
	 ***************************************
	 */
	Void requestLowerImpl();

	@Override
	public default ListenableFuture<Void> requestMove() {
        return getShellExecutor().submit((Callable<Void>) this::requestMoveImpl);
    }

    /***************************************
     * Concrete implementation of {@link #requestMove()}. This method should
     * only be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #requestMove()
	 ***************************************
	 */
	Void requestMoveImpl();

	@Override
	public default ListenableFuture<Void> requestMoveResize() {
        return getShellExecutor().submit((Callable<Void>) this::requestMoveResizeImpl);
    }

    /***************************************
     * Concrete implementation of {@link #requestMoveResize()}. This method is
     * invoked by the Shell thread.
	 *
	 * @return null
	 * @see #requestMoveResize()
	 ***************************************
	 */
	Void requestMoveResizeImpl();

	@Override
	public default ListenableFuture<Void> requestRaise() {
        return getShellExecutor().submit((Callable<Void>) this::requestRaiseImpl);
    }

    /***************************************
     * Concrete implementation of {@link #requestRaise()}. This method should
     * only be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #requestRaise()
	 ***************************************
	 */
	Void requestRaiseImpl();

	@Override
	public default ListenableFuture<Void> requestReparent() {
        return getShellExecutor().submit((Callable<Void>) this::requestReparentImpl);
    }

    /***************************************
     * Concrete implementation of {@link #requestReparent()}. This method should
     * only be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #requestReparent()
	 ***************************************
	 */
	Void requestReparentImpl();

	@Override
	public default ListenableFuture<Void> requestResize() {
        return getShellExecutor().submit((Callable<Void>) this::requestResizeImpl);
    }

    /***************************************
     * Concrete implementation of {@link #requestResize()}. This method should
     * only be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #requestResize()
	 ***************************************
	 */
	Void requestResizeImpl();

	@Override
	public default ListenableFuture<Void> requestShow() {
        return getShellExecutor().submit((Callable<Void>) this::requestShowImpl);
    }

    /***************************************
     * Concrete implementation of {@link #requestShow()}. This method should
     * only be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #requestShow()
	 ***************************************
	 */
	Void requestShowImpl();

	@Override
	public default ListenableFuture<Void> requestHide() {
        return getShellExecutor().submit((Callable<Void>) this::requestHideImpl);
    }

    /***************************************
     * Concrete implementation of {@link #requestHide()}. This method should
     * only be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #requestHide()
	 ***************************************
	 */
	Void requestHideImpl();

	@Override
	public default ListenableFuture<Void> setSize(final int width,
												final int height) {
        return getShellExecutor().submit((Callable<Void>) () -> setSizeImpl(width,
                                                                            height));
    }

    /***************************************
     * Concrete implementation of {@link #setSize(int, int)}. This method should
     * only be invoked by the Shell thread.
	 *
	 * @return null
	 * @see #setSize(int, int)
	 ***************************************
	 */
	Void setSizeImpl(	final int width,
										final int height);

	@Override
	public default ListenableFuture<Void> setPosition(final int x,
													final int y) {
        return getShellExecutor().submit((Callable<Void>) () -> setPositionImpl(x,
                                                                                y));
    }

    /***************************************
     * Concrete implementation of {@link #setPosition(int, int)}. This method is
	 * invoked by the Shell thread.
	 *
	 * @return null
	 * @see #setPosition(int, int)
	 ***************************************
	 */
	Void setPositionImpl(	int x,
											int y);

	@Override
	public default ListenableFuture<Void> setParent(final Optional<? extends ShellNodeParent> parent) {
        return getShellExecutor().submit((Callable<Void>) () -> setParentImpl(parent));
    }

    /***************************************
	 * Concrete implementation of {@link #setParent(Optional)}. This method is
	 * invoked by the Shell thread.
	 *
	 * @return null
	 * @see #setParent(Optional)
	 ***************************************
	 */
	Void setParentImpl(Optional<? extends ShellNodeParent> parent);

	@Override
	public default ListenableFuture<Optional<ShellNodeParent>> getParent() {
        return getShellExecutor().submit((Callable<Optional<ShellNodeParent>>) this::getParentImpl);
    }

    /***************************************
	 * Concrete implementation of {@link #getParent()}. This method should only
	 * be invoked by the Shell thread.
	 *
	 * @return a {@link ShellNodeParent}.
	 * @see #getParent()
	 ***************************************
	 */
	Optional<ShellNodeParent> getParentImpl();

	@Override
	public default ListenableFuture<ShellNodeTransformation> toGeoTransformation() {
        return getShellExecutor().submit((Callable<ShellNodeTransformation>) this::toGeoTransformationImpl);
    }

    /***************************************
	 * Concrete implementation of {@link #toGeoTransformation()}. This method is
	 * invoked by the Shell thread.
	 *
	 * @return a {@link ShellNodeTransformation}
	 * @see #toGeoTransformation()
	 ***************************************
	 */
	ShellNodeTransformation toGeoTransformationImpl();
}