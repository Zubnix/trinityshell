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
import org.trinity.shell.scene.api.Buffer;
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
import java.util.Optional;

@NotThreadSafe
@AutoFactory
public class SimpleShellSurface extends EventBus implements ShellSurface, ShellSurfaceConfigurable {

	@Nonnull
	private PointImmutable position  = new Point(0,
												 0);
	@Nonnull
	private Boolean        destroyed = Boolean.FALSE;

    @Nonnull
    private Optional<RectangleImmutable> pendingInputRegion = Optional.empty();
    @Nonnull
    private Optional<RectangleImmutable> inputRegion = Optional.empty();
    @Nonnull
    private Optional<RectangleImmutable> pendingDamage = Optional.empty();
    @Nonnull
    private Optional<RectangleImmutable> damage        = Optional.empty();

    @Nonnull
    private Optional<Buffer> pendingBuffer;
    @Nonnull
    private Optional<Buffer> buffer;

    SimpleShellSurface(@Nonnull final Buffer buffer) {
        this.buffer = Optional.of(buffer);
    }

    @Override
    public void accept(@Nonnull final ShellSurfaceConfiguration shellSurfaceConfiguration) {
        shellSurfaceConfiguration.visit(this);
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
		post(new Destroyed(this));
		return this;
	}

    @Nonnull
    @Override
    public ShellSurfaceConfigurable markDamaged(@Nonnull final RectangleImmutable damage) {
        final RectangleImmutable damageUnion = this.damage.orElse(damage)
                                                          .union(damage);
        this.pendingDamage = Optional.of(damageUnion);
        return this;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable attachBuffer(@Nonnull final Buffer buffer,
                                                 @Nonnull final Integer relX,
                                                 @Nonnull final Integer relY) {
        this.pendingBuffer = Optional.of(buffer);
        this.position = new Point(this.position.getX() + relX,
                                  this.position.getY() + relY);
        return this;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable detachBuffer() {
        this.pendingBuffer = Optional.empty();
        return this;
    }

    @Nonnull
    @Override
    public PointImmutable getPosition() {
        return this.position;
    }

    @Nonnull
    @Override
    public Optional<RectangleImmutable> getInputRegion() {
        return this.inputRegion;
    }

    @Nonnull
    @Override
    public Optional<RectangleImmutable> getDamage() {
        return this.damage;
    }

    @Nonnull
    @Override
    public Optional<Buffer> getBuffer() {
        return this.buffer;
    }

    @Nonnull
	@Override
	public ShellSurfaceConfigurable commit() {
        this.buffer = this.pendingBuffer;
        this.pendingBuffer = Optional.empty();
        this.damage = this.pendingDamage;
        this.pendingDamage = Optional.empty();
        this.inputRegion = this.pendingInputRegion;

        post(new Committed(this));
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
    public ShellSurfaceConfigurable setInputRegion(@Nonnull final RectangleImmutable inputRegion) {
        this.pendingInputRegion = Optional.of(inputRegion);
        return this;
    }

    @Nonnull
    @Override
    public ShellSurfaceConfigurable setPosition(@Nonnull final PointImmutable position) {
        this.position = new Point(position.getX(),
                                  position.getY());
        post(new Moved(this,
                       this.position));
        return this;
    }
}