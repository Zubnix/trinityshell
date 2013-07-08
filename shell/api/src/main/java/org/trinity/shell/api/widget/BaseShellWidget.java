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
import org.trinity.shell.api.surface.AbstractShellSurface;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

/***************************************
 * An abstract base {@link ShellWidget} implementation.
 * <p>
 * A <code>BaseShellWidget</code> is manipulated through a {@link Painter} for
 * it's basic geometry operations and through the binding framework for more
 * fine grained visual operations. This is done by injecting the widget with a
 * view object and manually call {@link Painter#bindView()}.
 *
 * @see org.trinity.foundation.api.render.binding
 ***************************************
 */
public abstract class BaseShellWidget extends AbstractShellSurface implements ShellWidget {

	private static final Logger logger = LoggerFactory.getLogger(BaseShellWidget.class);

	private final Painter painter;
	private final Object view;
	private final BaseShellWidgetGeometryDelegate shellNodeGeometryDelegate = new BaseShellWidgetGeometryDelegate(this);

	protected BaseShellWidget(	final AsyncListenable shellScene,
								final ListeningExecutorService shellExecutor,
								final PainterFactory painterFactory,
								final Object view) {
		super(shellScene,shellExecutor);
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
			logger.error(	"Interrupted while waiting for display surface from painter.",
							e);
		} catch (final ExecutionException e) {
			logger.error(	"Error while waiting for display surface from painter.",
							e);
		}
		return ds;
	}
}