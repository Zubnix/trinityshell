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
package org.trinity.shell.scene.impl;

import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellNodeGeometryDelegate;
import org.trinity.shell.api.scene.ShellNodeTransformation;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.nativewindow.util.Dimension;
import javax.media.nativewindow.util.Point;
import javax.media.nativewindow.util.Rectangle;

// TODO documentation

/**
 * A <code>ShellGeoVExecutor</code> executes the actual geometry changes for a
 * {@link ShellVirtualSurface}.
 *
 */
@NotThreadSafe
public class ShellVirtualSurfaceExecutor implements ShellNodeGeometryDelegate {

    private final ShellVirtualSurface shellVirtualSurface;

    public ShellVirtualSurfaceExecutor(@Nonnull final ShellVirtualSurface shellVirtualSurface) {
        this.shellVirtualSurface = shellVirtualSurface;
    }

    @Override
    public ShellVirtualSurface getShellNode() {
        return this.shellVirtualSurface;
    }

    @Override
    public void move(@Nonnull final Point position) {
        final ShellVirtualSurface shellNode = getShellNode();
        final ShellNodeTransformation shellNodeTransformation = shellNode.toGeoTransformation();
        final Rectangle rect0 = shellNodeTransformation.getRect0();
        final Rectangle rect1 = shellNodeTransformation.getRect1();

        final int dX = rect1.getX()-rect0.getX();
        final int dY = rect1.getY()-rect0.getY();

        final Point deltaPosition = new Point(dX,dY);

        for (final ShellNode child : shellNode.getChildren()) {
            final Point oldRelPosition = child.getPosition();

            final Point newRelPosition = oldRelPosition.translate(deltaPosition);

            // directly manipulated underlying platform specific geometry of the
            // child
            child.getShellNodeGeometryDelegate().move(newRelPosition);
        }
    }

    @Override
    public void resize(@Nonnull final Dimension size) {
        // do nothing
    }
}
