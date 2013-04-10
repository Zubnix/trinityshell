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

import javax.annotation.concurrent.ThreadSafe;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.render.Painter;
import org.trinity.foundation.api.render.PainterFactory;
import org.trinity.foundation.api.render.binding.model.ViewReference;
import org.trinity.shell.api.surface.AbstractShellSurface;
import org.trinity.shell.api.surface.AbstractShellSurfaceParent;
import org.trinity.shell.api.surface.ShellSurfaceFactory;
import org.trinity.shell.api.surface.ShellSurfaceParent;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import static com.google.common.util.concurrent.Futures.addCallback;

/**
 * An {@link AbstractShellSurfaceParent} with a basic
 * {@link PaintableSurfaceNode} implementation.
 * <p>
 * A <code>BaseShellWidget</code> is manipulated through a {@link PainterProxy}
 * that can talk to a paint back-end. This is done by injecting the widget with
 * a {@link View} and exposing it to the {@code PainterProxy} with a
 * {@link ViewReference} annotation. This <code>View</code> object is then bound
 * to the widget through binding annotations. see
 * 
 * @see org.trinity.foundation.api.render.binding
 */
@ThreadSafe
public abstract class BaseShellWidget extends AbstractShellSurface implements ShellWidget {

	private final Painter painter;
	private final BaseShellWidgetGeometryDelegate shellNodeGeometryDelegate = new BaseShellWidgetGeometryDelegate(this);

	@Inject
	protected BaseShellWidget(	final ShellSurfaceFactory shellSurfaceFactory,
								@Named("Shell") final ListeningExecutorService shellExecutor,
								final PainterFactory painterFactory) {
		super(shellExecutor);
		this.painter = painterFactory.createPainter(this);
		final ListenableFuture<ShellSurfaceParent> rootShellSurfaceFuture = shellSurfaceFactory.getRootShellSurface();
		addCallback(rootShellSurfaceFuture,
					new FutureCallback<ShellSurfaceParent>() {
						@Override
						public void onSuccess(final ShellSurfaceParent rootShellSurface) {
							setParentImpl(rootShellSurface);
							doReparent(false);
						}

						@Override
						public void onFailure(final Throwable t) {
							// TODO Auto-generated method stub
							t.printStackTrace();
						}
					},
					shellExecutor);
	}

	@ViewReference
	public final Object getView() {
		return getViewImpl();
	}

	protected abstract Object getViewImpl();

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ds;
	}
}