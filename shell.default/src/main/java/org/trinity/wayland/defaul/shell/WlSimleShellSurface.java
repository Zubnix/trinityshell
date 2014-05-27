package org.trinity.wayland.defaul.shell;

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
import javax.inject.Inject;
import javax.media.nativewindow.util.Point;
import javax.media.nativewindow.util.PointImmutable;
import javax.media.nativewindow.util.RectangleImmutable;
import java.util.Optional;

/**
 * Created by Erik De Rijcke on 5/27/14.
 */
public class WlSimleShellSurface extends EventBus implements ShellSurface, ShellSurfaceConfigurable{
    @Nonnull
    private PointImmutable position  = new Point(0,
            0);
    @Nonnull
    private Boolean        destroyed = Boolean.FALSE;

    @Nonnull
    private Optional<RectangleImmutable> pendingInputRegion = Optional.empty();
    @Nonnull
    private Optional<RectangleImmutable> inputRegion        = Optional.empty();
    @Nonnull
    private Optional<RectangleImmutable> pendingDamage      = Optional.empty();
    @Nonnull
    private Optional<RectangleImmutable> damage             = Optional.empty();

    @Nonnull
    private Optional<Buffer> pendingBuffer  = Optional.empty();
    @Nonnull
    private Optional<Buffer> buffer         = Optional.empty();

    @Inject
    WlSimleShellSurface() {
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
        this.buffer         = this.pendingBuffer;
        this.pendingBuffer  = Optional.empty();
        this.damage         = this.pendingDamage;
        this.pendingDamage  = Optional.empty();
        this.inputRegion    = this.pendingInputRegion;

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
