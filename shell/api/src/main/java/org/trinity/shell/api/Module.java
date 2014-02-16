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

package org.trinity.shell.api;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.onami.autobind.annotations.GuiceModule;
import org.trinity.foundation.api.shared.Listenable;
import org.trinity.foundation.api.shared.ListenableEventBus;
import org.trinity.shell.api.bindingkey.ShellScene;
import org.trinity.shell.api.plugin.ShellPlugin;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.AbstractModule;
import com.google.inject.name.Named;

/***************************************
 * Registers useful objects in the Guice injection framework. The following
 * objects are registered:
 * <p>
 * <ul>
 * <li>An {@link EventBus} which is {@link Named} "ShellEventBus". This eventbus
 * is the primary means of communication between independent {@link ShellPlugin}
 * s and informs any subscribed listener to changes of the shell. This eventbus
 * is driven by a single shell thread, subscribers should thus not block their
 * handling of notifications.
 * </ul>
 *
 ***************************************
 */
@GuiceModule
class Module extends AbstractModule {

	@Override
	protected void configure() {
		ListeningExecutorService shellExecutor = MoreExecutors.listeningDecorator(Executors
				.newSingleThreadExecutor(new ThreadFactory() {
					@Override
					public Thread newThread(final Runnable r) {
						return new Thread(	r,
											"shell-executor");
					}
				}));
		bind(ListeningExecutorService.class).annotatedWith(ShellExecutor.class).toInstance(shellExecutor);
		bind(Listenable.class).annotatedWith(ShellScene.class)
				.toInstance(new ListenableEventBus(shellExecutor));

	}
}
