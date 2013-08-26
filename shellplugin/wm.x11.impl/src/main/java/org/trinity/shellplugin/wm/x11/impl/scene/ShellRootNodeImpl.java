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

package org.trinity.shellplugin.wm.x11.impl.scene;

import javax.inject.Named;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.render.ViewDisplayServiceFinder;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.bindingkey.ShellRootNode;
import org.trinity.shell.api.bindingkey.ShellScene;
import org.trinity.shell.api.scene.ShellNodeGeometryDelegate;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shell.api.surface.AbstractShellSurface;
import org.trinity.shell.api.surface.ShellSurfaceGeometryDelegate;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.concurrent.ExecutionException;

import static com.google.common.util.concurrent.Futures.addCallback;
import static org.apache.onami.autobind.annotations.To.Type.INTERFACES;

@Bind
@Singleton
@ShellRootNode
public class ShellRootNodeImpl extends AbstractShellSurface implements ShellNodeParent {
	private  DisplaySurface displaySurface;
	private final ShellNodeGeometryDelegate shellNodeGeometryDelegate = new ShellSurfaceGeometryDelegate(this);

	@Inject
	ShellRootNodeImpl(	final ViewDisplayServiceFinder viewDisplayServiceFinder,
						@Named("DesktopView") final ListenableFuture desktopView,
						@ShellScene final AsyncListenable shellScene,
						@ShellExecutor final ListeningExecutorService shellExecutor) {
		super(null,
				shellScene,
				shellExecutor);

		addCallback(desktopView,new FutureCallback() {
			@Override
			public void onSuccess(final Object result) {
				displaySurface = viewDisplayServiceFinder.find(result);
			}

			@Override
			public void onFailure(final Throwable t) {
				//To change body of implemented methods use File | Settings | File Templates.
			}
		});

	}

	@Override
	public DisplaySurface getDisplaySurface() {
		return this.displaySurface;
	}

	@Override
	public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
		return this.shellNodeGeometryDelegate;
	}
}
