/*
 * This file is part of Fusion-qtjambi.
 * 
 * Fusion-qtjambi is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Fusion-qtjambi is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-qtjambi. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.qt.paintengine;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.fusion.qt.error.DisplayNotSetError;
import org.fusion.x11.core.XDisplay;
import org.hydrogen.displayinterface.Display;
import org.hydrogen.displayinterface.EventProducer;
import org.hydrogen.displayinterface.event.DisplayEvent;
import org.hydrogen.displayinterface.event.DisplayEventSource;

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
	private static final QFusionDestroyConverter DESTROY_CONVERTER = new QFusionDestroyConverter();
	private static final QFusionFocusInConverter FOCUS_IN_CONVERTER = new QFusionFocusInConverter();
	private static final QFusionFocusOutConverter FOCUS_OUT_CONVERTER = new QFusionFocusOutConverter();
	private static final QFusionKeyPressConverter KEY_PRESS_CONVERTER = new QFusionKeyPressConverter();
	private static final QFusionKeyReleaseConverter KEY_RELEASE_CONVERTER = new QFusionKeyReleaseConverter();
	private static final QFusionMouseButtonPressConverter MOUSE_BUTTON_PRESS_CONVERTER = new QFusionMouseButtonPressConverter();
	private static final QFusionMouseButtonReleaseConverter MOUSE_BUTTON_RELEASE_CONVERTER = new QFusionMouseButtonReleaseConverter();
	private static final QFusionMouseEnterEventConverter MOUSE_ENTER_EVENT_CONVERTER = new QFusionMouseEnterEventConverter();
	private static final QFusionMouseLeaveEventConverter MOUSE_LEAVE_EVENT_CONVERTER = new QFusionMouseLeaveEventConverter();

	private static final Map<QEvent.Type, QFusionEventConverter<? extends QEvent>> CONVERSION_MAP = new HashMap<QEvent.Type, QFusionEventConverter<? extends QEvent>>();

	static {
		QFusionEventProducer.CONVERSION_MAP.put(QEvent.Type.KeyPress,
				QFusionEventProducer.KEY_PRESS_CONVERTER);
		QFusionEventProducer.CONVERSION_MAP.put(QEvent.Type.KeyRelease,
				QFusionEventProducer.KEY_RELEASE_CONVERTER);
		QFusionEventProducer.CONVERSION_MAP.put(QEvent.Type.MouseButtonPress,
				QFusionEventProducer.MOUSE_BUTTON_PRESS_CONVERTER);
		QFusionEventProducer.CONVERSION_MAP.put(QEvent.Type.MouseButtonRelease,
				QFusionEventProducer.MOUSE_BUTTON_RELEASE_CONVERTER);
		QFusionEventProducer.CONVERSION_MAP.put(QEvent.Type.Destroy,
				QFusionEventProducer.DESTROY_CONVERTER);
		QFusionEventProducer.CONVERSION_MAP.put(QEvent.Type.Enter,
				QFusionEventProducer.MOUSE_ENTER_EVENT_CONVERTER);
		QFusionEventProducer.CONVERSION_MAP.put(QEvent.Type.Leave,
				QFusionEventProducer.MOUSE_LEAVE_EVENT_CONVERTER);
		QFusionEventProducer.CONVERSION_MAP.put(QEvent.Type.FocusIn,
				QFusionEventProducer.FOCUS_IN_CONVERTER);
		QFusionEventProducer.CONVERSION_MAP.put(QEvent.Type.FocusOut,
				QFusionEventProducer.FOCUS_OUT_CONVERTER);
	}

	private static final Logger LOGGER = Logger
			.getLogger(QFusionEventProducer.class);
	private static final String EVENT_CONVERSION_LOGMESSAGE = "Converted qfusion event: '%s' to display event: '%s'";
	private static final String DISPLAY_NOT_SET_LOGMESSAGE = "Could not convert event '%s'. Display not set for this event producer.";
	private static final String TIMEOUT_PUT_EVENT_LOGMESSAGE = "Time out. Could not add qfusion event '%s' to queue.";
	private static final String INTERRUPTED_PUT_EVENT_LOGMESSAGE = "Interrupted while waiting for free queue space. Could not add qfusion event '%s' to queue";

	// TODO we don't want to depend on xdisplay to find the
	// event source. Either QFusionrenderengine or Display
	// should suffice.
	private final XDisplay display;
	private final ArrayBlockingQueue<DisplayEvent> qEventQueue;

	private static final int TIME_OUT = 60;
	private static final int DEFAULT_EVENT_QUEUE_SIZE = 128;

	/**
	 * 
	 * @param display
	 */
	public QFusionEventProducer(final Display display) {
		// TODO we don't want to depend on xcoredisplay to find the
		// event source. Either QFusionrenderengine or Display
		// should suffice.
		this.display = (XDisplay) display;
		this.qEventQueue = new ArrayBlockingQueue<DisplayEvent>(
				QFusionEventProducer.DEFAULT_EVENT_QUEUE_SIZE, true);
	}

	/**
	 * 
	 * @param eventSource
	 * @param event
	 */
	public void promote(final DisplayEventSource eventSource, final QEvent event) {
		if ((event == null) || (eventSource == null)) {
			return;
		}

		@SuppressWarnings("unchecked")
		final QFusionEventConverter<QEvent> eventConverter = (QFusionEventConverter<QEvent>) QFusionEventProducer.CONVERSION_MAP
				.get(event.type());

		if (eventConverter != null) {
			if (QFusionEventProducer.this.display == null) {
				final DisplayNotSetError ex = new DisplayNotSetError();

				QFusionEventProducer.LOGGER
						.fatal(String
								.format(QFusionEventProducer.DISPLAY_NOT_SET_LOGMESSAGE,
										event), ex);

				throw ex;
			} else {
				DisplayEvent convertedEvent;

				convertedEvent = eventConverter.sinkEvent(eventSource, event);

				QFusionEventProducer.LOGGER.debug(String.format(
						QFusionEventProducer.EVENT_CONVERSION_LOGMESSAGE,
						event, convertedEvent));

				try {
					if ((convertedEvent != null)
							&& !this.qEventQueue.offer(convertedEvent,
									QFusionEventProducer.TIME_OUT,
									TimeUnit.SECONDS)) {

						QFusionEventProducer.LOGGER
								.error(String
										.format(QFusionEventProducer.TIMEOUT_PUT_EVENT_LOGMESSAGE,
												event));

						// TODO seperate exception
						throw new RuntimeException(String.format("Time out.\n"
								+ "Could not add qfusion event %s to queue",
								event));
					}
				} catch (final InterruptedException e) {

					QFusionEventProducer.LOGGER
							.error(String
									.format(QFusionEventProducer.INTERRUPTED_PUT_EVENT_LOGMESSAGE,
											event));

					// TODO seperate exception
					throw new RuntimeException(
							String.format(
									"Interrupted while waiting for free queue space.\n"
											+ "Could not add qfusion event %s to queue",
									event), e);
				}
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
