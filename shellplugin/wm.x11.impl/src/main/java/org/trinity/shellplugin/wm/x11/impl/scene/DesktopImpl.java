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

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.onami.autobind.annotations.Bind;
import org.trinity.foundation.api.render.ViewReference;
import org.trinity.foundation.api.render.binding.Binder;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shellplugin.wm.api.Desktop;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListeningExecutorService;

@Bind
@ExecutionContext(ShellExecutor.class)
@NotThreadSafe
public class DesktopImpl implements Desktop {

	private final EventList<Object> notificationsBar = new BasicEventList<>();
	private final EventList<Object> clientsBar = new BasicEventList<>();
	private final EventList<Object> bottomBar = new BasicEventList<>();

	@Inject
	DesktopImpl(@ShellExecutor final ListeningExecutorService shellExecutor,
				final Binder binder,
				@Named("DesktopView") final ViewReference desktopView) {

		Futures.addCallback(desktopView.getView(),
							new FutureCallback<Object>() {
								@Override
								public void onSuccess(final Object view) {
									binder.bind(shellExecutor,
												this,
												view);
								}

								@Override
								public void onFailure(final Throwable t) {
									// TODO implement
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
