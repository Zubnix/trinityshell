/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.core.impl;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.trinity.foundation.display.api.DisplayServer;
import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.shell.core.api.ManagedDisplayService;
import org.trinity.shell.core.api.RenderArea;
import org.trinity.shell.core.api.RenderAreaFactory;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class ManagedDisplayServiceImpl implements ManagedDisplayService {

	private final DisplayServer display;
	private final DisplayEventDispatcher displayEventDispatcher;
	private final Thread displayEventDispatcherThread;
	private final Executor managedDisplayEventExecutor;
	private final RenderArea root;

	@Inject
	protected ManagedDisplayServiceImpl(final DisplayServer display,
										@Named("display") final EventBus eventBus,
										@Named("root") final RenderArea root,
										final RenderAreaFactory clientFactory) {
		this.display = display;
		this.managedDisplayEventExecutor = Executors.newSingleThreadExecutor();
		this.displayEventDispatcher = new DisplayEventDispatcher(	eventBus,
																	display,
																	clientFactory);
		this.displayEventDispatcherThread = new Thread(this.displayEventDispatcherThread);
		this.root = root;
	}

	@Override
	public void start() {
		// TODO this is more an X specific thing, create a more platform
		// neutral mechanism/interface and hide any reference to root window. ->
		// use DisplayProtocol itf?
		// this.root
		// .getPlatformRenderArea()
		// .selectEvent(DisplayEventSelector.REDIRECT_CHILD_WINDOW_GEOMETRY_CHANGES);
		this.managedDisplayEventExecutor.execute(getEventDispatcher());
	}

	protected DisplayEventDispatcher getEventDispatcher() {
		return this.displayEventDispatcher;
	}

	@Override
	public void stop() {
		this.display.shutDown();
	}

	@Override
	public String toString() {
		return String.format("Display: %s", this.display);
	}

	@Override
	public void postNextDisplayEvent(final boolean block) {
		this.displayEventDispatcher.postNextEvent(block);
	}

	@Override
	public void registerEventBusForSource(	final EventBus eventBus,
											final DisplayEventSource forDisplayEventSource) {
		this.displayEventDispatcher
				.addEventManagerForDisplayEventSource(	eventBus,
														forDisplayEventSource);
	}
}
