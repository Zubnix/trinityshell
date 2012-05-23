/*
 * This file is part of Hydrogen. Hydrogen is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Hydrogen is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Hydrogen. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.core.display.impl;

import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.trinity.core.display.api.Display;
import org.trinity.core.display.api.DisplayPlatform;
import org.trinity.core.display.api.EventProducer;
import org.trinity.core.display.api.event.DisplayEvent;
import org.trinity.core.event.api.EventBus;

import com.google.inject.Inject;

// TODO documentation
/**
 * An <code>BaseEventProducingDisplay</code> provides a basic implementation for
 * a <code>Display</code>. A thread save blocking <code>DisplayEvent</code>
 * queue with the necessary worker threads for the added
 * <code>EventProducer</code>s are implemented.
 * <p>
 * It is recommended to subclass <code>BaseEventProducingDisplay</code> if a
 * programmer wants to implements a custom <code>Display</code> instead of
 * directly implementing the <code>Display</code> interface from scratch.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public abstract class BaseDisplay extends EventBus implements Display {

	private static final Logger LOGGER = Logger.getLogger(BaseDisplay.class);
	private static final String EVENT_QUEUE_ERROR_LOGMESSAGE = "Error while interacting with the event queue.";

	/**
	 * An <code>EventFetcher</code> is a <code>Runnable</code> worker class
	 * which sole purpose is to pull events from a source and put them on the
	 * <code>BaseEventProducingDisplay</code>'s main event queue.
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 */
	private class EventFetcher implements Runnable {
		private static final String THREAD_NAME = "DisplayEvent Pump.";
		private final EventProducer eventProducer;
		private boolean nameNotSet;
		private volatile boolean running;

		/**
		 * Create a new <code>EventFetcher</code> with the given
		 * <code>EventProducer</code> as this <code>EventFetcher</code>'s event
		 * source.
		 * 
		 * @param eventProducer
		 *            An {@link EventProducer} who's events will be read and
		 *            placed on the <code>BaseEventProducingDisplay</code> main
		 *            event queue.
		 */
		private EventFetcher(final EventProducer eventProducer) {
			this.eventProducer = eventProducer;
			this.nameNotSet = true;
			this.running = true;

		}

		@Override
		public void run() {
			if (this.nameNotSet) {
				Thread.currentThread().setName(EventFetcher.THREAD_NAME);
				this.nameNotSet = false;
			}
			while (this.running) {

				final DisplayEvent displayEvent = this.eventProducer
						.getNextEvent();

				addEventToMasterQueue(displayEvent);
			}
		}

		/**
		 * Shut down this <code>EventFetcher</code>. Calling this method will
		 * result in a gracefully termination of the thread running this
		 * <code>EventFetcher</code>
		 */
		public void shutDown() {
			this.running = false;
		}
	}

	private final ExecutorService eventFetchingService;
	private final ArrayBlockingQueue<DisplayEvent> masterQueue;

	private final Set<EventFetcher> eventFetchers;

	private static final int EVENT_QUEUE_SIZE = 256;
	private static final int EVENT_OFFER_TIMEOUT = 30;
	private static final TimeUnit EVENT_OFFER_TIMEOUT_UNIT = TimeUnit.SECONDS;

	/**
	 * Create a new <code>BaseEventProducingDisplay</code> which will be managed
	 * by the given <code>DisplayPlatform</code>.
	 * 
	 * @param displayPlatform
	 *            The {@link DisplayPlatform} where this
	 *            <code>BaseEventProducingDisplay</code> will live.
	 * @param displayName
	 *            The native display identifier.
	 * @param screenNr
	 *            The screen of the native display that should be used. @ Thrown
	 *            when an error related to the underlying native display occurs.
	 */
	@Inject
	protected BaseDisplay(final Set<EventFetcher> eventFetchers) {
		this.eventFetchers = eventFetchers;
		this.masterQueue = new ArrayBlockingQueue<DisplayEvent>(BaseDisplay.EVENT_QUEUE_SIZE,
																true);
		this.eventFetchingService = Executors.newCachedThreadPool();

	}

	/**
	 * @param displayEvent
	 */
	@Override
	public void addEventToMasterQueue(final DisplayEvent displayEvent) {
		try {
			if (!this.masterQueue.offer(displayEvent,
										BaseDisplay.EVENT_OFFER_TIMEOUT,
										BaseDisplay.EVENT_OFFER_TIMEOUT_UNIT)) {
				throw new RuntimeException(String.format("Time Out.\nCould not add event to queue."));
			}
		} catch (final InterruptedException e) {
			LOGGER.error(EVENT_QUEUE_ERROR_LOGMESSAGE);
		}
	}

	@Override
	public void addEventProducer(final EventProducer eventProducer) {
		final EventFetcher eventFetcher = new EventFetcher(eventProducer);
		this.eventFetchers.add(eventFetcher);
		this.eventFetchingService.execute(eventFetcher);

	}

	/**
	 * @return
	 */
	protected ArrayBlockingQueue<DisplayEvent> getMasterQueue() {
		return this.masterQueue;
	}

	@Override
	public DisplayEvent getEventFromMasterQueue() {
		try {
			return this.masterQueue.take();
		} catch (final InterruptedException e) {
			LOGGER.error(EVENT_QUEUE_ERROR_LOGMESSAGE);
			return null;
		}
	}

	@Override
	public void shutDown() {
		this.eventFetchingService.shutdown();
		for (final EventFetcher eventFetcher : this.eventFetchers) {
			eventFetcher.shutDown();
		}
		this.eventFetchers.clear();
	}

	@Override
	public boolean isMasterQueueEmpty() {
		return this.masterQueue.isEmpty();
	}
}
