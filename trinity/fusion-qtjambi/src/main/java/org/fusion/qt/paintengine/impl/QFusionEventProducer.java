/*
 * This file is part of Fusion-qtjambi. Fusion-qtjambi is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Fusion-qtjambi is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with Fusion-qtjambi. If not,
 * see <http://www.gnu.org/licenses/>.
 */
package org.fusion.qt.paintengine.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.fusion.qt.paintengine.api.QFusionEventConverter;
import org.hydrogen.display.api.EventProducer;
import org.hydrogen.display.api.event.DisplayEvent;
import org.hydrogen.display.api.event.DisplayEventSource;

import com.google.inject.Inject;
import com.trolltech.qt.core.QEvent;

// TODO documentation
/**
 * Promotes <code>QEvent</code>s to <code>DisplayEvent</code>s so they can be
 * processed by an <code>BaseEventProducingDisplay</code>. Promoted
 * <code>QEvent</code>s are translated to an {@link DisplayEvent} and placed on
 * a thread safe queue. This queue can be read with a call to
 * <code>getNextEvent()</code>.
 * <p>
 * Every <code>QEvent</code> that is promoted is read only. Not every
 * <code>QEvent</code> will be translated. Only those <code>QEvent</code>s that
 * have a corresponding <code>DisplayEvent</code> are translated and placed on a
 * queue.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class QFusionEventProducer implements EventProducer {

	private static final Logger LOGGER = Logger
			.getLogger(QFusionEventProducer.class);

	private static final String EVENT_CONVERSION_LOGMESSAGE = "Converted qfusion event: '%s' to display event: '%s'";
	private static final String TIMEOUT_PUT_EVENT_LOGMESSAGE = "Time out. Could not add qfusion event '%s' to queue.";
	private static final String INTERRUPTED_PUT_EVENT_LOGMESSAGE = "Interrupted while waiting for free queue space. Could not add qfusion event '%s' to queue";

	private final Map<QEvent.Type, QFusionEventConverter<? extends QEvent>> converterByQEventType = new HashMap<QEvent.Type, QFusionEventConverter<? extends QEvent>>();

	private final ArrayBlockingQueue<DisplayEvent> qEventQueue = new ArrayBlockingQueue<DisplayEvent>(	QFusionEventProducer.DEFAULT_EVENT_QUEUE_SIZE,
																										true);

	private static final int TIME_OUT = 60;
	private static final int DEFAULT_EVENT_QUEUE_SIZE = 128;

	@Inject
	protected QFusionEventProducer(final Set<QFusionEventConverter<? extends QEvent>> qfusionEventConverters) {
		for (final QFusionEventConverter<? extends QEvent> eventConverter : qfusionEventConverters) {
			this.converterByQEventType.put(	eventConverter.getFromType(),
											eventConverter);
		}
	}

	/**
	 * @param eventSource
	 * @param event
	 */
	public void promote(final DisplayEventSource eventSource, final QEvent event) {
		if ((event == null) || (eventSource == null)) {
			return;
		}

		@SuppressWarnings("unchecked")
		final QFusionEventConverter<QEvent> eventConverter = (QFusionEventConverter<QEvent>) this.converterByQEventType
				.get(event.type());

		if (eventConverter != null) {
			DisplayEvent convertedEvent;

			convertedEvent = eventConverter.sinkEvent(eventSource, event);

			QFusionEventProducer.LOGGER.debug(String
					.format(QFusionEventProducer.EVENT_CONVERSION_LOGMESSAGE,
							event,
							convertedEvent));

			try {
				if ((convertedEvent != null)
						&& !this.qEventQueue
								.offer(	convertedEvent,
										QFusionEventProducer.TIME_OUT,
										TimeUnit.SECONDS)) {

					QFusionEventProducer.LOGGER
							.error(String
									.format(QFusionEventProducer.TIMEOUT_PUT_EVENT_LOGMESSAGE,
											event));

					// TODO seperate exception
					throw new RuntimeException(String.format("Time out.\n"
							+ "Could not add qfusion event %s to queue", event));
				}
			} catch (final InterruptedException e) {

				QFusionEventProducer.LOGGER
						.error(String
								.format(QFusionEventProducer.INTERRUPTED_PUT_EVENT_LOGMESSAGE,
										event));

				// TODO seperate exception
				throw new RuntimeException(	String.format(	"Interrupted while waiting for free queue space.\n"
																	+ "Could not add qfusion event %s to queue",
															event),
											e);
			}
		}
	}

	@Override
	public DisplayEvent getNextEvent() {
		try {
			final DisplayEvent returnEvent = this.qEventQueue.take();
			return returnEvent;
		} catch (final InterruptedException e) {
			// TODO log
			// TODO seperate exception
			throw new RuntimeException(e);
		}
	}
}
