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

import org.trinity.shell.scene.api.event.HideRequest;
import org.trinity.shell.scene.api.event.LowerRequest;
import org.trinity.shell.scene.api.event.MoveRequest;
import org.trinity.shell.scene.api.event.RaiseRequest;
import org.trinity.shell.scene.api.event.ResizeRequest;
import org.trinity.shell.scene.api.event.ShowRequest;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.media.nativewindow.util.Dimension;
import javax.media.nativewindow.util.Point;
import javax.media.nativewindow.util.PointImmutable;
import java.util.List;
import java.util.Optional;
import java.util.function.IntConsumer;

/**
 * ************************************
 * The super interface of all nodes that live in the shell scene.
 * **************************************
 */
public interface ShellSurface extends Listenable {

    void accept(@Nonnull ShellSurfaceConfiguration shellSurfaceConfiguration);
    @Nonnull
    List<IntConsumer> getPaintCallbacks();
    @Nonnull
    PointImmutable getPosition();
    @Nonnull
    Optional<Region> getInputRegion();
    @Nonnull
    Optional<Region> getDamage();
    @Nonnull
    Optional<Region> getOpaqueRegion();
    @Nonnull
    Optional<Object> getBuffer();
    @Nonnull
    Boolean isDestroyed();
    @Nonnull
    float[] getTransform();
    @Nonnull
    default ShellSurface requestMove(final int x,
                                     final int y) {
        post(new MoveRequest(this,
                              new Point(x,
                                        y)));
        return this;
    }
    @Nonnull
    default ShellSurface requestResize(@Nonnegative final int width,
                                       @Nonnegative final int height) {
        post(new ResizeRequest(this,
                               new Dimension(width,
                               height)));
        return this;
    }
    @Nonnull
    default ShellSurface requestRaise() {
        post(new RaiseRequest(this));
        return this;
    }
    @Nonnull
    default ShellSurface requestLower() {
        post(new LowerRequest(this));
        return this;
    }
    @Nonnull
    default ShellSurface requestShow() {
        post(new ShowRequest(this));
        return this;
    }
    @Nonnull
    default ShellSurface requestHide() {
        post(new HideRequest(this));
        return this;
    }
    @Nonnull
    default ShellSurface firePaintCallbacks(final int serial){
        getPaintCallbacks().forEach(paintCallback -> paintCallback.accept(serial));
        return this;
    }
}