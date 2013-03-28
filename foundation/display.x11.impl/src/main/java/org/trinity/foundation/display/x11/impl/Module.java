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
package org.trinity.foundation.display.x11.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

import de.devsurf.injection.guice.annotations.GuiceModule;

@GuiceModule
public class Module extends AbstractModule {

	@Override
	protected void configure() {
		bind(EventBus.class).annotatedWith(Names.named("XEventBus")).toInstance(new EventBus());
		bind(ListeningExecutorService.class).annotatedWith(Names.named("XExecutor"))
				.toInstance(MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor(new ThreadFactory() {
					@Override
					public Thread newThread(final Runnable arg0) {
						return new Thread(	arg0,
											"x-executor");
					}
				})));

		install(new FactoryModuleBuilder().implement(	DisplaySurface.class,
														XWindow.class).build(DisplaySurfaceFactory.class));
	}
}