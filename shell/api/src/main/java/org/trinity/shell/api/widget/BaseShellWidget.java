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

import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.render.Painter;
import org.trinity.foundation.api.render.PainterFactory;
import org.trinity.foundation.api.render.binding.model.ViewReference;
import org.trinity.shell.api.surface.AbstractShellSurfaceParent;
import org.trinity.shell.api.surface.ShellSurfaceParent;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.name.Named;

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
@NotThreadSafe
public class BaseShellWidget extends AbstractShellSurfaceParent implements ShellWidget {

	// private class DestroyCallback {
	// // method is used by the widget's eventbus
	// @SuppressWarnings("unused")
	// @Subscribe
	// public void handleDestroy(final ShellNodeDestroyedEvent destroyEvent) {
	// BaseShellWidget.this.shellDisplayEventDispatcher.unregisterAllDisplayEventTarget(getDisplaySurface());
	// BaseShellWidget.this.shellDisplayEventDispatcher.unregisterAllDisplayEventTarget(BaseShellWidget.this);
	// }
	// }

	// private final DestroyCallback destroyCallback = new DestroyCallback();

	private final Painter painter;
	private final BaseShellWidgetExecutor shellNodeExecutor;

	/**
	 * Create an uninitialized <code>BaseShellWidget</code>.A
	 * <code>BaseShellWidget</code> will only be fully functional when it is
	 * assigned to an initialized parent <code>BaseShellWidget</code>.
	 */
	protected BaseShellWidget(	@Named("ShellExecutor") final ListeningExecutorService shellExecutor,
								final PainterFactory painterProxyFactory) {
		super(shellExecutor);
		this.shellNodeExecutor = new BaseShellWidgetExecutor(this);
		this.painter = painterProxyFactory.createPainter(this);
	}

	/***************************************
	 * Initializes the widget and marks is as a child of the given root
	 * {@link ShellSurfaceParent}. This method is automatically called after
	 * construction of the widget and should generally not be called manually.
	 * 
	 * @param shellRootSurface
	 *            The root node of shell scene.
	 *************************************** 
	 */
	@Inject
	protected void init(@Named("ShellRootSurface") final ShellSurfaceParent shellRootSurface) {
		// init will be called with the injected instance immediately after our
		// widget is constructed.

		setParent(shellRootSurface);
		doReparent(false);

		// this.shellDisplayEventDispatcher.registerDisplayEventTarget(getNodeEventBus(),
		// this);
		// this.shellDisplayEventDispatcher.registerDisplayEventTarget(getNodeEventBus(),
		// getDisplaySurface());
		// searches for a @ViewReference annotated getter and binds the
		// resulting view to this widget.
		this.painter.bindView();
		// addListener(this.destroyCallback);
	}

	@Override
	public Painter getPainter() {
		return this.painter;
	}

	@Override
	public BaseShellWidgetExecutor getShellNodeExecutor() {
		return this.shellNodeExecutor;
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