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
package org.trinity;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import org.trinity.shell.scene.api.Region;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.ShellSurfaceConfigurable;
import org.trinity.shell.scene.api.ShellSurfaceConfiguration;
import org.trinity.shell.scene.api.event.Committed;
import org.trinity.shell.scene.api.event.Destroyed;
import org.trinity.shell.scene.api.event.Moved;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.nativewindow.util.Point;
import javax.media.nativewindow.util.PointImmutable;
import javax.media.nativewindow.util.RectangleImmutable;
import java.util.List;
import java.util.Optional;
import java.util.function.IntConsumer;

@NotThreadSafe
@AutoFactory(className = "SimpleShellSurfaceFactory")
public class SimpleShellSurface extends EventBus implements ShellSurface, ShellSurfaceConfigurable {

    private final PixmanRegionFactory pixmanRegionFactory;

    //pending states
    @Nonnull
    private List<IntConsumer>  pendingCallbacks    = Lists.newLinkedList();
    @Nonnull
    private Optional<Region>   pendingOpaqueRegion = Optional.empty();
    @Nonnull
    private Optional<Region>   pendingInputRegion  = Optional.empty();
    @Nonnull
    private Optional<Region>   pendingDamage       = Optional.empty();
    @Nonnull
    private Optional<Object>   pendingBuffer       = Optional.empty();
    @Nonnull
    private float[] pendingTransform    = new float[]{1, 0, 0,
                                                      0, 1, 0,
                                                      0, 0, 1};
    @Nonnull
    private PointImmutable     pendingPosition     = new Point(0,
                                                               0);

    //committed states
    @Nonnull
    private List<IntConsumer>  callbacks    = Lists.newLinkedList();
    @Nonnull
    private Optional<Region>   opaqueRegion = Optional.empty();
    @Nonnull
    private Optional<Region>   inputRegion  = Optional.empty();
    @Nonnull
    private Optional<Region>   damage       = Optional.empty();
    @Nonnull
    private Optional<Object>   buffer       = Optional.empty();
    @Nonnull
    private float[] transform    = new float[]{1, 0, 0,
                                               0, 1, 0,
                                               0, 0, 1};
    @Nonnull
    private PointImmutable     position     = new Point(0,
                                                        0);
    //additional server side states
    @Nonnull
    private Boolean            destroyed    = Boolean.FALSE;

    SimpleShellSurface(@Provided final PixmanRegionFactory pixmanRegionFactory,
                       @Nonnull final Optional<Object> optionalBuffer) {
        this.pixmanRegionFactory = pixmanRegionFactory;
        this.buffer = optionalBuffer;
    }

    @Override
    public void accept(@Nonnull final ShellSurfaceConfiguration shellSurfaceConfiguration) {
        shellSurfaceConfiguration.visit(this);
    }

    @Nonnull
    @Override
    public List<IntConsumer> getPaintCallbacks() {
        return this.callbacks;
    }

    @Nonnull
    @Override
    public Boolean isDestroyed() {
        return this.destroyed;
    }

    @Nonnull
    @Override
    public float[] getTransform() {
        return this.transform;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable markDestroyed() {
        this.destroyed = true;
        post(new Destroyed(this));
        return this;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable markDamaged(@Nonnull final RectangleImmutable damage) {
        this.pendingDamage = Optional.of(this.pendingDamage.orElse(this.pixmanRegionFactory.create())
                                                           .add(damage));
        return this;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable attachBuffer(@Nonnull final Object  buffer,
                                                 @Nonnull final Integer relX,
                                                 @Nonnull final Integer relY) {
        this.pendingBuffer = Optional.of(buffer);
        this.pendingPosition = new Point(this.position.getX() + relX,
                                         this.position.getY() + relY);
        return this;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable setTransform(final float[] transform) {
        this.pendingTransform = transform;
        return this;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable removeTransform() {
        this.pendingTransform = new float[]{1, 0, 0,
                                            0, 1, 0,
                                            0, 0, 1};
        return this;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable detachBuffer() {
        this.pendingBuffer = Optional.empty();
        this.pendingDamage = Optional.empty();
        this.pendingPosition = new Point(0,
                                         0);
        return this;
    }

    @Nonnull
    @Override
    public PointImmutable getPosition() {
        return this.position;
    }

    @Nonnull
    @Override
    public Optional<Region> getInputRegion() {
        return this.inputRegion;
    }

    @Nonnull
    @Override
    public Optional<Region> getDamage() {
        return this.damage;
    }

    @Nonnull
    @Override
    public Optional<Region> getOpaqueRegion() {
        return this.opaqueRegion;
    }

    @Nonnull
    @Override
    public Optional<Object> getBuffer() {
        return this.buffer;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable commit() {
        //flush
        this.transform    = this.pendingTransform;
        this.buffer       = this.pendingBuffer;
        this.position     = this.pendingPosition;
        this.damage       = this.pendingDamage;
        this.inputRegion  = this.pendingInputRegion;
        this.opaqueRegion = this.pendingOpaqueRegion;
        this.callbacks    = this.pendingCallbacks;
        //reset
        detachBuffer();
        this.pendingCallbacks = Lists.newLinkedList();
        //notify
        post(new Committed(this));
        return this;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable addCallback(final IntConsumer callback) {
        this.pendingCallbacks.add(callback);
        return this;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable removeOpaqueRegion() {
        this.pendingOpaqueRegion = Optional.empty();
        return this;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable setOpaqueRegion(@Nonnull final Region opaqueRegion) {
        this.pendingOpaqueRegion = Optional.of(opaqueRegion);
        return this;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable removeInputRegion() {
        this.pendingInputRegion = Optional.empty();
        return this;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable setInputRegion(@Nonnull final Region inputRegion) {
        this.pendingInputRegion = Optional.of(inputRegion);
        return this;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable setPosition(@Nonnull final PointImmutable position) {
        this.pendingPosition = new Point(position.getX(),
                                         position.getY());
        post(new Moved(this,
                       this.pendingPosition));
        return this;
    }
}