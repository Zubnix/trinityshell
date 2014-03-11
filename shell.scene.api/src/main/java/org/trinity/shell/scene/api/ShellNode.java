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

import javax.media.nativewindow.util.DimensionImmutable;
import javax.media.nativewindow.util.PointImmutable;
import javax.media.nativewindow.util.RectangleImmutable;

/**
 * ************************************
 * The super interface of all nodes that live in the shell scene.
 * **************************************
 */
public interface ShellNode extends Listenable {

    void accept(ShellNodeConfiguration shellNodeConfiguration);

    /**
     * ************************************
     * The shell geometry of the node.
     *
     * @return a {@link RectangleImmutable} shape.
     * **************************************
     */
    RectangleImmutable getShape();

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
     * @return a future true if destroyed, a future false if not.
     * **************************************
     */
    Boolean isDestroyed();

    /**
     * ************************************
     * Request that this node is lowered. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellNodeLowerRequestEvent}.
     * <p/>
     * **************************************
     */
    void requestLower();

    /**
     * ************************************
     * Request that this node is moved. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellNodeMoveRequestEvent}.
     * <p/>
     * **************************************
     */
    void requestMove(int x,
                     int y);

    /**
     * ************************************
     * Request that this node is raised. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellNodeRaiseRequestEvent}.
     * <p/>
     * **************************************
     */
    void requestRaise();

    /**
     * ************************************
     * Request that this node is reparented. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellNodeReparentRequestEvent}.
     * <p/>
     * **************************************
     */
    void requestReparent(final ShellNodeParent parent);

    /**
     * ************************************
     * Request that this node is resized. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellNodeResizeRequestEvent}.
     * <p/>
     * **************************************
     */
    void requestResize(int width,
                       int height);

    /**
     * ************************************
     * Request that this node is shown. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellNodeShowRequestEvent}.
     * <p/>
     * **************************************
     */
    void requestShow();

    /**
     * ************************************
     * Request that this node is hidden. This will cause any subscribed node
     * listener, e.g. a layout manager, to receive a
     * {@link org.trinity.shell.scene.api.event.ShellNodeHiddenEvent}.
     * <p/>
     * **************************************
     */
    void requestHide();

    /**
     * ************************************
     * The shell parent of this node.
     *
     * @return a future {@link ShellNodeParent}.
     * **************************************
     */
    ShellNodeParent getParent();
}