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
	private static final Logger LOGGER = Logger
			.getLogger(QFusionEventProducer.class);
	private static final String EVENT_CONVERSION_LOGMESSAGE = "Converted qfusion event: '%s' to display event: '%s'";
	private static final String DISPLAY_NOT_SET_LOGMESSAGE = "Could not convert event '%s'. Display not set for this event producer.";
	private static final String TIMEOUT_PUT_EVENT_LOGMESSAGE = "Time out. Could not add qfusion event '%s' to queue.";
	private static final String INTERRUPTED_PUT_EVENT_LOGMESSAGE = "Interrupted while waiting for free queue space. Could not add qfusion event '%s' to queue";

	private final Map<QEvent.Type, QFusionEventConverter<? extends QEvent>> conversionMap;

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
		this.conversionMap = new HashMap<QEvent.Type, QFusionEventConverter<? extends QEvent>>();
		this.qEventQueue = new ArrayBlockingQueue<DisplayEvent>(
				DEFAULT_EVENT_QUEUE_SIZE, true);
		initConversionMap();
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
		final QFusionEventConverter<QEvent> eventConverter = (QFusionEventConverter<QEvent>) this.conversionMap
				.get(event.type());

		if (eventConverter != null) {
			if (QFusionEventProducer.this.display == null) {
				DisplayNotSetError ex = new DisplayNotSetError();

				LOGGER.fatal(String.format(DISPLAY_NOT_SET_LOGMESSAGE, event),
						ex);

				throw ex;
			} else {
				DisplayEvent convertedEvent;

				convertedEvent = eventConverter.sinkEvent(eventSource, event);

				LOGGER.debug(String.format(EVENT_CONVERSION_LOGMESSAGE, event,
						convertedEvent));

				try {
					if (convertedEvent != null
							&& !this.qEventQueue.offer(convertedEvent,
									QFusionEventProducer.TIME_OUT,
									TimeUnit.SECONDS)) {

						LOGGER.error(String.format(
								TIMEOUT_PUT_EVENT_LOGMESSAGE, event));

						// TODO seperate exception
						throw new RuntimeException(String.format("Time out.\n"
								+ "Could not add qfusion event %s to queue",
								event));
					}
				} catch (final InterruptedException e) {

					LOGGER.error(String.format(
							INTERRUPTED_PUT_EVENT_LOGMESSAGE, event));

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

	/**
	 * A <code>Map</code> defining how a <code>QEvent</code> should be
	 * translated based on it's <code>QEvent.Type</code>.
	 * 
	 * @return A <code>Map</code> with a {@link QEvent.Type} as a key and a
	 *         {@link QFusionEventConverter} as the corresponding value.
	 */
	protected Map<QEvent.Type, QFusionEventConverter<? extends QEvent>> getConversionMap() {
		return this.conversionMap;
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

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Initializes the map that holds the key-value entries used for converting
	 * a <code>QEvent</code> to an <code>DisplayEvent</code>.
	 * <p>
	 * The key values are mapped to a <code>QEvent.Type</code> while the
	 * corresponding value is mapped to a <code>QFusionEventConverter</code>.
	 * The <code>QFusionEventConverter</code> implements the conversion through
	 * it's <code>convert(QWidget,QEvent)</convert> method.
	 */
	protected void initConversionMap() {
		getConversionMap().put(QEvent.Type.KeyPress,
				new QFusionKeyPressConverter());
		getConversionMap().put(QEvent.Type.KeyRelease,
				new QFusionKeyReleaseConverter());
		getConversionMap().put(QEvent.Type.MouseButtonPress,
				new QFusionMouseButtonPressConverter());
		getConversionMap().put(QEvent.Type.MouseButtonRelease,
				new QFusionMouseButtonReleaseConverter());
		getConversionMap().put(QEvent.Type.Destroy,
				new QFusionDestroyConverter());
		getConversionMap().put(QEvent.Type.Enter,
				new QFusionMouseEnterEventConverter());
		getConversionMap().put(QEvent.Type.Leave,
				new QFusionMouseLeaveEventConverter());
		getConversionMap().put(QEvent.Type.FocusIn,
				new QFusionFocusInConverter());
		getConversionMap().put(QEvent.Type.FocusOut,
				new QFusionFocusOutConverter());
	}
}
