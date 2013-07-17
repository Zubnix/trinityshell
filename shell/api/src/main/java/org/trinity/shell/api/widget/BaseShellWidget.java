/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.api.widget;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.render.Painter;
import org.trinity.foundation.api.render.PainterFactory;
import org.trinity.foundation.api.render.binding.model.ViewReference;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.bindingkey.ShellRootNode;
import org.trinity.shell.api.bindingkey.ShellScene;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shell.api.surface.AbstractShellSurface;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * ************************************
 * An abstract base {@link ShellWidget} implementation.
 * <p/>
 * A <code>BaseShellWidget</code> is manipulated through a {@link Painter} for
 * it's basic geometry operations and through the binding framework for more
 * fine grained visual operations. This is done by injecting the widget with a
 * view object and manually call {@link Painter#bindView()}.
 *
 * @see org.trinity.foundation.api.render.binding
 *      **************************************
 */
@ExecutionContext(ShellExecutor.class)
public abstract class BaseShellWidget extends AbstractShellSurface implements ShellWidget {

    private static final Logger LOG = LoggerFactory.getLogger(BaseShellWidget.class);
    private final Painter painter;
    private final Object view;
    private final BaseShellWidgetGeometryDelegate shellNodeGeometryDelegate = new BaseShellWidgetGeometryDelegate(this);

    protected BaseShellWidget(@Nullable @ShellRootNode final ShellNodeParent shellRootNode,
                              @Nonnull @ShellScene final AsyncListenable shellScene,
                              @Nonnull @ShellExecutor final ListeningExecutorService shellExecutor,
                              @Nonnull final PainterFactory painterFactory,
                              @Nonnull final Object view) {
        super(  shellRootNode,
                shellScene,
                shellExecutor);
        this.view = view;
        this.painter = painterFactory.createPainter(this);
    }

    @ViewReference
    public final Object getView() {
        return this.view;
    }

    @Override
    public Painter getPainter() {
        return this.painter;
    }

    @Override
    public BaseShellWidgetGeometryDelegate getShellNodeGeometryDelegate() {
        return this.shellNodeGeometryDelegate;
    }

    @Override
    public DisplaySurface getDisplaySurfaceImpl() {
        final ListenableFuture<DisplaySurface> dsFuture = this.painter.getDislaySurface();

        DisplaySurface ds = null;
        try {
            ds = dsFuture.get();
        } catch (final InterruptedException e) {
            LOG.error("Interrupted while waiting for display surface from painter.",
                    e);
        } catch (final ExecutionException e) {
            LOG.error("Error while waiting for display surface from painter.",
                    e);
        }
        return ds;
    }
}
