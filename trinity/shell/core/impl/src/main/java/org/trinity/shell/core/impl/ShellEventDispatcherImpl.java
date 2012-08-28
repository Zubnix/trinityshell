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

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.display.api.DisplayServer;
import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.shell.core.api.ShellDisplayEventDispatcher;
import org.trinity.shell.core.api.ShellSurface;
import org.trinity.shell.core.api.event.ShellSurfaceCreatedEvent;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

/**
 * An <code>EventDispatcher</code> fetches and dispatches
 * <code>DisplayEvent</code>s for a {@link ShellDisplay}. An
 * <code>EventDispatcher</code> fetches <code>DisplayEvent</code>s from the
 * <code>Display</code> that is wrapped by a <code>ManagedDisplay</code>. After
 * an event is fetched, it is delivered at corresponding the
 * <code>EventBus</code> objects that were registered with the
 * <code>ManagedDisplay</code>. (see
 * {@link ShellDisplay#registerEventBus(DisplayEventSource, EventBus)})
 * <p>
 * The <code>EventDispatcher</code> implements the main hyperdrive even loop in
 * it's run method. This method is activated by the <code>ManagedDisplay</code>
 * that created the <code>EventDispatcher</code>. See
 * {@link ShellDisplay#startDisplayEventProduction()}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
@Singleton
public class ShellEventDispatcherImpl implements ShellDisplayEventDispatcher {

	// TODO this is basically the same mechanism as used in EventBus. Find a way
	// to seperate and uniform this mechanism.
	private final Cache<DisplayEventSource, Cache<EventBus, EventBus>> eventRecipients = CacheBuilder.newBuilder()
			.concurrencyLevel(4).weakKeys().build();

	private final DisplayServer display;
	private final ShellClientSurfaceFactory shellClientSurfaceFactory;
	private final EventBus shellEventBus;

	@Inject
	ShellEventDispatcherImpl(	@Named("shellEventBus") final EventBus shellEventBus,
								final ShellClientSurfaceFactory shellClientSurfaceFactory,
								final DisplayServer display) {
		this.shellEventBus = shellEventBus;
		this.shellClientSurfaceFactory = shellClientSurfaceFactory;
		this.display = display;
		this.shellEventBus.register(this);
	}

	/**
	 * Fetch and dispatch the next <code>DisplayEvent</code>.
	 * 
	 * @param block
	 *            True to wait for a new event to become available. False to
	 *            continue without dispatching an event.
	 */
	@Override
	public void dispatchDisplayEvent(final boolean block) {
		if (!block && this.display.hasNextDisplayEvent()) {
			return;
		}

		final DisplayEvent displayEvent = this.display.getNextDisplayEvent();

		if (displayEvent == null) {
			return;
		}

		newShellSurfaceClientIfNeeded(displayEvent);
		this.shellEventBus.post(displayEvent);

		final Cache<EventBus, EventBus> eventBusses = this.eventRecipients.getIfPresent(displayEvent.getEventSource());
		if (eventBusses == null) {
			return;
		}

		final Set<Entry<EventBus, EventBus>> eventBussesEntries = eventBusses.asMap().entrySet();
		for (final Entry<EventBus, EventBus> eventBusEntry : eventBussesEntries) {
			final EventBus eventBus = eventBusEntry.getKey();
			eventBus.post(displayEvent);
		}
	}

	private void newShellSurfaceClientIfNeeded(final DisplayEvent event) {
		final Cache<EventBus, EventBus> eventBusses = this.eventRecipients.getIfPresent(event.getEventSource());

		final DisplayEventSource eventSource = event.getEventSource();

		if (((eventBusses == null) || (eventBusses.size() == 0)) && (eventSource instanceof DisplaySurface)) {
			createClientShellSurface((DisplaySurface) eventSource);
		}
	}

	private void createClientShellSurface(final DisplaySurface clientDisplaySurface) {
		final ShellSurface clientShellSurface = this.shellClientSurfaceFactory
				.createShellClientSurface(clientDisplaySurface);
		this.shellEventBus.post(new ShellSurfaceCreatedEvent(clientShellSurface));
	}

	@Override
	public void registerDisplayEventSource(	final EventBus nodeEventBus,
											final DisplayEventSource displayEventSource) {

		try {
			final Cache<EventBus, EventBus> nodeEventBusses = this.eventRecipients
					.get(	displayEventSource,
							new Callable<Cache<EventBus, EventBus>>() {
								@Override
								public Cache<EventBus, EventBus> call() throws Exception {
									return CacheBuilder.newBuilder().weakKeys().build();
								}
							});
			nodeEventBusses.put(nodeEventBus,
								nodeEventBus);
		} catch (final ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void unregisterDisplayEventSource(	final EventBus nodeEventBus,
												final DisplayEventSource displayEventSource) {
		final Cache<EventBus, EventBus> nodeEventBusses = this.eventRecipients.getIfPresent(displayEventSource);
		if (nodeEventBusses != null) {
			nodeEventBusses.invalidate(nodeEventBus);
		}
	}
}