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
package org.trinity.shell.impl.surface;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.trinity.foundation.display.api.DisplayServer;
import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.DisplayEventTarget;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.event.ShellSurfaceCreatedEvent;

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
 * {@link ShellDisplay#registerEventBus(DisplayEventTarget, EventBus)})
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
public class ShellDisplayEventDispatcherImpl implements ShellDisplayEventDispatcher {

	// TODO this is basically the same mechanism as used in EventBus. Find a way
	// to seperate and uniform this mechanism.
	private final Map<DisplayEventTarget, List<EventBus>> eventRecipients = new WeakHashMap<DisplayEventTarget, List<EventBus>>();

	private final DisplayServer displayServer;
	private final ShellClientSurfaceFactory shellClientSurfaceFactory;
	private final EventBus shellEventBus;

	@Inject
	ShellDisplayEventDispatcherImpl(@Named("shellEventBus") final EventBus shellEventBus,
									final ShellClientSurfaceFactory shellClientSurfaceFactory,
									final DisplayServer displayServer) {
		this.shellEventBus = shellEventBus;
		this.shellClientSurfaceFactory = shellClientSurfaceFactory;
		this.displayServer = displayServer;
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
		if (!block && this.displayServer.hasNextDisplayEvent()) {
			return;
		}

		final DisplayEvent displayEvent = this.displayServer.getNextDisplayEvent();

		if (displayEvent == null) {
			return;
		}

		newShellSurfaceClientIfNeeded(displayEvent);
		this.shellEventBus.post(displayEvent);

		synchronized (this.eventRecipients) {
			final List<EventBus> eventBusses = this.eventRecipients.get(displayEvent.getDisplayEventSource());
			if (eventBusses == null) {
				return;
			}

			for (final EventBus eventBus : eventBusses) {

				// TODO logging
				System.err.println(String.format(	"Dispatching: %s",
													displayEvent));

				eventBus.post(displayEvent);
			}
		}
	}

	private void newShellSurfaceClientIfNeeded(final DisplayEvent event) {
		synchronized (this.eventRecipients) {
			final DisplayEventTarget displayEventTarget = event.getDisplayEventSource();
			if (!this.eventRecipients.containsKey(displayEventTarget) && (displayEventTarget instanceof DisplaySurface)) {
				createClientShellSurface((DisplaySurface) displayEventTarget);
			}
		}
	}

	private void createClientShellSurface(final DisplaySurface clientDisplaySurface) {
		final ShellSurface clientShellSurface = this.shellClientSurfaceFactory
				.createShellClientSurface(clientDisplaySurface);
		this.shellEventBus.post(new ShellSurfaceCreatedEvent(clientShellSurface));
	}

	@Override
	public void registerDisplayEventSourceListener(	final EventBus nodeEventBus,
													final DisplayEventTarget displayEventTarget) {
		List<EventBus> nodeEventBusses;
		synchronized (this.eventRecipients) {
			nodeEventBusses = this.eventRecipients.get(displayEventTarget);

			if (nodeEventBusses == null) {
				nodeEventBusses = new CopyOnWriteArrayList<EventBus>();
				this.eventRecipients.put(	displayEventTarget,
											nodeEventBusses);
			}
			nodeEventBusses.add(nodeEventBus);
		}
	}

	@Override
	public void unregisterDisplayEventSourceListener(	final EventBus nodeEventBus,
														final DisplayEventTarget displayEventTarget) {
		synchronized (this.eventRecipients) {

			final List<EventBus> nodeEventBusses = this.eventRecipients.get(displayEventTarget);
			if (nodeEventBusses != null) {
				nodeEventBusses.remove(nodeEventBus);
			}
		}
	}

	@Override
	public void unregisterAllDisplayEventSourceListeners(final DisplayEventTarget displayEventTarget) {
		synchronized (this.eventRecipients) {
			this.eventRecipients.remove(displayEventTarget);
		}
	}
}