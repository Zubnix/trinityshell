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

import static com.google.common.util.concurrent.Futures.addCallback;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import javax.inject.Named;

import org.trinity.foundation.api.display.Display;
import org.trinity.foundation.api.display.Screen;
import org.trinity.foundation.api.render.binding.Binder;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.bindingkey.ShellScene;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shell.api.scene.manager.ShellLayoutManagerLine;
import org.trinity.shellplugin.wm.api.Desktop;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

// bound in the module as autobind currently doesnt pick up the annotation key
// @Bind
// @Singleton
// @AnnotatedWith
// @ShellRootNode
// @To(value = CUSTOM, customs = { ShellNodeParent.class, Desktop.class })
@ExecutionContext(ShellExecutor.class)
@NotThreadSafe
public class DesktopImpl implements Desktop {

	private final EventList<Object> notificationsBar = new BasicEventList<>();
	private final EventList<Object> clientsBar = new BasicEventList<>();
	private final EventList<Object> bottomBar = new BasicEventList<>();

	@Inject
	DesktopImpl(final Display display,
	            @ShellScene final AsyncListenable shellScene,
	            @ShellExecutor final ListeningExecutorService shellExecutor,
	            final Binder binder,
	            @Named("RootView") final Object view,
	            final ShellLayoutManagerLine shellLayoutManagerLine) {

		//TODO get view & bind it to this model.
		//TODO get display surface from view & create shellsurface from display surface & resize to correct size

		// find correct size
		final ListenableFuture<Screen> screenFuture = display.getScreen();
		// set correct size
		addCallback(screenFuture,
					new FutureCallback<Screen>() {
						// called by display executor
						@Override
						public void onSuccess(final Screen result) {
							setSize(result.getSize());
							doResize();
						}

						@Override
						public void onFailure(final Throwable t) {
							// TODO Auto-generated method stub
							t.printStackTrace();
						}
					});
	}

	@Override
	public EventList<Object> getNotificationsBar() {
		return this.notificationsBar;
	}

	@Override
	public EventList<Object> getClientsBar() {
		return this.clientsBar;
	}

	@Override
	public EventList<Object> getBottomBar() {
		return this.bottomBar;
	}
}
