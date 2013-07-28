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
import org.trinity.shell.api.scene.AbstractShellNode;
import org.trinity.shell.api.scene.ShellNodeGeometryDelegate;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shell.api.scene.ShellNodeTransformation;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.List;

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
    public void lower() {
        for (final AbstractShellNode child : getShellNode().getChildrenImpl()) {
            child.getShellNodeGeometryDelegate().lower();
        }
    }

    @Override
    public void raise() {
        final List<AbstractShellNode> children = getShellNode().getChildrenImpl();
        for (final AbstractShellNode child : children) {
            child.getShellNodeGeometryDelegate().raise();
        }
    }

    @Override
    public void move(@Nonnull final Coordinate position) {
        final ShellVirtualSurface shellNode = getShellNode();
        final ShellNodeTransformation shellNodeTransformation = shellNode.toGeoTransformationImpl();

        final Coordinate deltaPosition = shellNodeTransformation.getDeltaRect().getPosition();

        for (final AbstractShellNode child : shellNode.getChildrenImpl()) {
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

    @Override
    public void show() {
        for (final AbstractShellNode child : getShellNode().getChildrenImpl()) {
            updateChildVisibility(child,
                    true);
        }
    }

    @Override
    public void hide() {
        for (final AbstractShellNode child : getShellNode().getChildrenImpl()) {
            updateChildVisibility(child,
                    false);
        }
    }

    /**
     * @param child
     * @param visible
     */
    private void updateChildVisibility(@Nonnull final AbstractShellNode child,
                                       final boolean parentVisible) {
        final boolean childVisible = child.isVisibleImpl();
        // directly update underlying platform specific visibility of
        // the child
        if (childVisible & parentVisible) {
            child.getShellNodeGeometryDelegate().show();
        } else {
            child.getShellNodeGeometryDelegate().hide();
        }
    }

    @Override
    public void reparent(@Nonnull final ShellNodeParent parent) {
        final ShellVirtualSurface shellNode = getShellNode();

        final boolean nodeVisible = shellNode.isVisibleImpl();
        for (final AbstractShellNode child : shellNode.getChildrenImpl()) {
            // reparent children to ourself, this will trigger an update of
            // these children who will search for a compatible
            // grand-parent for them to be a child of in the underlying
            // relation.
            child.getShellNodeGeometryDelegate().reparent(shellNode);

            updateChildVisibility(child,
                    nodeVisible);
        }
    }

    @Override
    public void destroy() {
        for (final AbstractShellNode child : getShellNode().getChildrenImpl()) {
            if (!child.isDestroyedImpl()) {
                child.doDestroy();
            }
        }
    }
}
