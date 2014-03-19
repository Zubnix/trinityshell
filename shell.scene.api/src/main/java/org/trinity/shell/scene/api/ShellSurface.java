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
package org.trinity.shell.scene.api;

import org.trinity.common.Listenable;
import org.trinity.shell.scene.api.event.*;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.media.nativewindow.util.Dimension;
import javax.media.nativewindow.util.Point;
import javax.media.nativewindow.util.PointImmutable;

/**
 * ************************************
 * The super interface of all nodes that live in the shell scene.
 * **************************************
 */
public interface ShellSurface extends Listenable, HasSize<ShellSpace> {

    @Nonnull
    ShellSurface accept(@Nonnull ShellSurfaceConfiguration shellSurfaceConfiguration);

    @Nonnull
    PointImmutable getPosition();

    /**
     * ************************************
     * The underlying, {@code DisplaySurface} that this shell surface will use
     * to display it's contents. A display surface can be 'shared', so it is
     * possible that multiple shell surface's use the same display surface.
     *
     * @return a platform display buffer.
     * **************************************
     */
    @Nonnull
    HasSize<BufferSpace> getBuffer();

    /**
     * *************************************
     * Indicates if this node is visible. This is implementation dependent. A
     * <code>PaintSurfaceNode</code> is usually only visible if it's parent is
     * visible. So even though this method may return true, the
     * <code>PaintSurfaceNode</code> will only be physically visible if all it's
     * parents are physically visible as well.
     *
     * @return future true if visible, future false if not
     * **************************************
     */
    @Nonnull
    Boolean isVisible();

    /**
     * ************************************
     * Signals if this node is destroyed. A destroyed node should not be able to
     * process any geometry changes and should be discarded.
     *
     * @return a true if destroyed, false if not.
     * **************************************
     */
    @Nonnull
    Boolean isDestroyed();

    /**
     * ************************************
     * Request that this node is reparented. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellSurfaceReparentRequest}.
     * <p>
     * **************************************
     */
    @Nonnull
    default ShellSurface requestReparent(@Nonnull final ShellSurface parent) {
        post(new ShellSurfaceReparentRequest(this,
                                             parent));
        return this;
    }

    /**
     * ************************************
     * Request that this node is moved. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellSurfaceMoveRequest}.
     * <p>
     * **************************************
     */
    @Nonnull
    default ShellSurface requestMove(final int x,
                                     final int y) {
        post(new ShellSurfaceMoveRequest(this,
                                         new Point(x,
                                                   y)));
        return this;
    }

    /**
     * ************************************
     * Request that this node is resized. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellSurfaceResizeRequest}.
     * <p>
     * **************************************
     */
    @Nonnull
    default ShellSurface requestResize(@Nonnegative final int width,
                                       @Nonnegative final int height) {
        post(new ShellSurfaceResizeRequest(this,
                                           new Dimension(width,
                                                         height)));
        return this;
    }

    /**
     * ************************************
     * Request that this node is raised. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellSurfaceRaiseRequest}.
     * <p>
     * **************************************
     */
    @Nonnull
    default ShellSurface requestRaise() {
        post(new ShellSurfaceRaiseRequest(this));
        return this;
    }

    /**
     * ************************************
     * Request that this node is lowered. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellSurfaceLowerRequest}.
     * <p>
     * **************************************
     */
    @Nonnull
    default ShellSurface requestLower() {
        post(new ShellSurfaceLowerRequest(this));
        return this;
    }

    /**
     * ************************************
     * Request that this node is shown. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellSurfaceShowRequest}.
     * <p>
     * **************************************
     */
    @Nonnull
    default ShellSurface requestShow() {
        post(new ShellSurfaceShowRequest(this));
        return this;
    }

    /**
     * ************************************
     * Request that this node is hidden. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellSurfaceHidden}.
     * <p>
     * **************************************
     */
    @Nonnull
    default ShellSurface requestHide() {
        post(new ShellSurfaceHideRequest(this));
        return this;
    }

    /**
     * ************************************
     * The shell parent of this node.
     *
     * @return a {@link ShellSurface}.
     * **************************************
     */
    @Nonnull
    ShellSurface getParent();
}