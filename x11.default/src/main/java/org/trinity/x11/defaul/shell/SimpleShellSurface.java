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
package org.trinity.x11.defaul.shell;

import com.google.auto.factory.AutoFactory;
import com.google.common.eventbus.EventBus;
import org.trinity.common.Listenable;
import org.trinity.shell.scene.api.BufferSpace;
import org.trinity.shell.scene.api.HasSize;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.ShellSurfaceConfigurable;
import org.trinity.shell.scene.api.ShellSurfaceConfiguration;
import org.trinity.shell.scene.api.event.ShellSurfaceDestroyed;
import org.trinity.shell.scene.api.event.ShellSurfaceHidden;
import org.trinity.shell.scene.api.event.ShellSurfaceMoved;
import org.trinity.shell.scene.api.event.ShellSurfaceReparented;
import org.trinity.shell.scene.api.event.ShellSurfaceShowed;
import org.trinity.shell.scene.api.event.ShellSurfaceVisibilityEvent;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.nativewindow.util.DimensionImmutable;
import javax.media.nativewindow.util.Point;
import javax.media.nativewindow.util.PointImmutable;


@NotThreadSafe
@AutoFactory
public class SimpleShellSurface extends EventBus implements ShellSurface, ShellSurfaceConfigurable, Listenable {

    @Nonnull
    private PointImmutable position= new Point(0,0);
    @Nonnull
    private Boolean visible = Boolean.FALSE;
    @Nonnull
    private Boolean destroyed= Boolean.FALSE;
    @Nonnull
    private ShellSurface parent;
    @Nonnull
    private HasSize<BufferSpace> buffer;

    SimpleShellSurface(@Nonnull final ShellSurface parent,
                       @Nonnull final HasSize<BufferSpace> buffer) {
        this.parent = parent;
        this.buffer = buffer;
    }

    @Nonnull
    @Override
    public ShellSurface accept(@Nonnull final ShellSurfaceConfiguration shellSurfaceConfiguration) {
        shellSurfaceConfiguration.configure(this);
        return this;
    }

    @Nonnull
    @Override
    public Boolean isDestroyed() {
        return this.destroyed;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable markDestroyed() {
        this.destroyed = true;
        post(new ShellSurfaceDestroyed(this));
        return this;
    }

    @Nonnull
    @Override
    public ShellSurface getParent() {
        return this.parent;
    }

    @Nonnull
    public ShellSurfaceConfigurable setParent(@Nonnull final ShellSurface parent) {
        this.parent = parent;
        post(new ShellSurfaceReparented(this,
                parent));
        return this;
    }

    @Nonnull
    @Override
    public PointImmutable getPosition() {
        return this.position;
    }

    @Nonnull
    @Override
    public HasSize<BufferSpace> getBuffer() {
        return this.buffer;
    }

    @Nonnull
    @Override
    public Boolean isVisible() {
        return this.visible;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable setVisible(@Nonnull final Boolean visible) {
        this.visible = visible;
        final ShellSurfaceVisibilityEvent shellSurfaceVisibilityEvent = this.visible ? new ShellSurfaceShowed(this):new ShellSurfaceHidden(this);
        post(shellSurfaceVisibilityEvent);
        return this;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable commit() {
        //TODO implement
        return this;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable setPosition(@Nonnull final PointImmutable pointImmutable) {
        this.position = new Point(pointImmutable.getX(),
                                  pointImmutable.getY());
        post(new ShellSurfaceMoved(this,
                                   this.position));
        return this;
    }

    @Nonnull
    @Override
    public DimensionImmutable getSize() {
        return this.buffer.getSize();
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable attachBuffer(@Nonnull final HasSize<BufferSpace> buffer) {
        this.buffer = buffer;
        return this;
    }
}