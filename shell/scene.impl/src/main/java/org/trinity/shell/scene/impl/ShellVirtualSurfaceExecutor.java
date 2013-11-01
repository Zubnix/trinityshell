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

import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.scene.DefaultShellNode;
import org.trinity.shell.api.scene.ShellNodeGeometryDelegate;
import org.trinity.shell.api.scene.ShellNodeTransformation;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

// TODO documentation

/**
 * A <code>ShellGeoVExecutor</code> executes the actual geometry changes for a
 * {@link ShellVirtualSurface}.
 *
 * @author Erik De Rijcke
 * @since 1.0
 */
@NotThreadSafe
@ExecutionContext(ShellExecutor.class)
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
    public void move(@Nonnull final Coordinate position) {
        final ShellVirtualSurface shellNode = getShellNode();
        final ShellNodeTransformation shellNodeTransformation = shellNode.toGeoTransformationImpl();

        final Coordinate deltaPosition = shellNodeTransformation.getDeltaRect().getPosition();

        for (final DefaultShellNode child : shellNode.getChildrenImpl()) {
            final Coordinate oldRelPosition = child.getPositionImpl();

            final Coordinate newRelPosition = oldRelPosition.add(deltaPosition);

            // directly manipulated underlying platform specific geometry of the
            // child
            child.getShellNodeGeometryDelegate().move(newRelPosition);
        }
    }

    @Override
    public void resize(@Nonnull final Size size) {
        // do nothing
    }

    @Override
    public void moveResize(@Nonnull final Coordinate position,
                           @Nonnull final Size size) {
        move(position);
    }
}
