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
package org.fusion.paintengine.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.fusion.paintengine.api.QFRenderEventBridge;
import org.hydrogen.display.api.event.DisplayEvent;
import org.hydrogen.display.api.event.DisplayEventSource;
import org.hydrogen.paint.api.RenderEventConverter;

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
public class QFRenderEventBridgeImpl implements QFRenderEventBridge {

	private static final Logger LOGGER = Logger
			.getLogger(QFRenderEventBridgeImpl.class);

	private static final String EVENT_CONVERSION_LOGMESSAGE = "Converted qfusion event: '%s' to display event: '%s'";
	private static final String TIMEOUT_PUT_EVENT_LOGMESSAGE = "Time out. Could not add qfusion event '%s' to queue.";
	private static final String INTERRUPTED_PUT_EVENT_LOGMESSAGE = "Interrupted while waiting for free queue space. Could not add qfusion event '%s' to queue";

	private final Map<QEvent.Type, RenderEventConverter<? extends QEvent, QEvent.Type>> converterByQEventType = new HashMap<QEvent.Type, RenderEventConverter<? extends QEvent, QEvent.Type>>();

	private final ArrayBlockingQueue<DisplayEvent> qEventQueue = new ArrayBlockingQueue<DisplayEvent>(	QFRenderEventBridgeImpl.DEFAULT_EVENT_QUEUE_SIZE,
																										true);

	private static final int TIME_OUT = 60;
	private static final int DEFAULT_EVENT_QUEUE_SIZE = 128;

	@Inject
	protected QFRenderEventBridgeImpl(final Set<RenderEventConverter<? extends QEvent, QEvent.Type>> qfusionEventConverters) {
		for (final RenderEventConverter<? extends QEvent, QEvent.Type> eventConverter : qfusionEventConverters) {
			this.converterByQEventType.put(	eventConverter.getFromType(),
											eventConverter);
		}
	}

	/**
	 * @param eventSource
	 * @param event
	 */
	@Override
	public void queueRenderEvent(	final DisplayEventSource eventSource,
									final QEvent event) {
		if ((event == null) || (eventSource == null)) {
			return;
		}

		@SuppressWarnings("unchecked")
		final RenderEventConverter<QEvent, QEvent.Type> eventConverter = (RenderEventConverter<QEvent, QEvent.Type>) this.converterByQEventType
				.get(event.type());

		if (eventConverter != null) {
			DisplayEvent convertedEvent;

			convertedEvent = eventConverter.convertEvent(eventSource, event);

			QFRenderEventBridgeImpl.LOGGER
					.debug(String
							.format(QFRenderEventBridgeImpl.EVENT_CONVERSION_LOGMESSAGE,
									event,
									convertedEvent));

			try {
				if ((convertedEvent != null)
						&& !this.qEventQueue
								.offer(	convertedEvent,
										QFRenderEventBridgeImpl.TIME_OUT,
										TimeUnit.SECONDS)) {

					QFRenderEventBridgeImpl.LOGGER
							.error(String
									.format(QFRenderEventBridgeImpl.TIMEOUT_PUT_EVENT_LOGMESSAGE,
											event));

					// TODO seperate exception
					throw new RuntimeException(String.format("Time out.\n"
							+ "Could not add qfusion event %s to queue", event));
				}
			} catch (final InterruptedException e) {

				QFRenderEventBridgeImpl.LOGGER
						.error(String
								.format(QFRenderEventBridgeImpl.INTERRUPTED_PUT_EVENT_LOGMESSAGE,
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
