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

import javax.media.nativewindow.util.PointImmutable;

/**
 * ************************************
 * The super interface of all nodes that live in the shell scene.
 * **************************************
 */
public interface ShellSurface extends Listenable, HasSize<ShellSpace> {

    void accept(ShellSurfaceConfiguration shellSurfaceConfiguration);

    PointImmutable getPosition();

    /***************************************
     * The underlying, {@code DisplaySurface} that this shell surface will use
     * to display it's contents. A display surface can be 'shared', so it is
     * possible that multiple shell surface's use the same display surface.
     *
     * @return a platform display buffer.
     ***************************************
     */
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
    Boolean isVisible();

    /**
     * ************************************
     * Signals if this node is destroyed. A destroyed node should not be able to
     * process any geometry changes and should be discarded.
     *
     * @return a true if destroyed, false if not.
     * **************************************
     */
    Boolean isDestroyed();

    /**
     * ************************************
     * Request that this node is lowered. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellSurfaceLowerRequest}.
     * <p/>
     * **************************************
     */
    void requestLower();

    /**
     * ************************************
     * Request that this node is moved. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellSurfaceMoveRequest}.
     * <p/>
     * **************************************
     */
    void requestMove(int x,
                     int y);

    /**
     * ************************************
     * Request that this node is raised. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellSurfaceRaiseRequest}.
     * <p/>
     * **************************************
     */
    void requestRaise();

    /**
     * ************************************
     * Request that this node is reparented. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellSurfaceReparentRequest}.
     * <p/>
     * **************************************
     */
    void requestReparent(final ShellSurface parent);

    /**
     * ************************************
     * Request that this node is resized. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellSurfaceResizeRequest}.
     * <p/>
     * **************************************
     */
    void requestResize(int width,
                       int height);

    /**
     * ************************************
     * Request that this node is shown. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellSurfaceShowRequest}.
     * <p/>
     * **************************************
     */
    void requestShow();

    /**
     * ************************************
     * Request that this node is hidden. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellSurfaceHidden}.
     * <p/>
     * **************************************
     */
    void requestHide();

    /**
     * ************************************
     * The shell parent of this node.
     *
     * @return a {@link ShellSurface}.
     * **************************************
     */
    ShellSurface getParent();
}