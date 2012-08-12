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
package org.trinity.render.paintengine.qt.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.DisplayEventSource;

import com.google.inject.Inject;
import com.trolltech.qt.core.QEvent;

import de.devsurf.injection.guice.annotations.Bind;

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
@Bind
public class QFRenderEventBridge {

	private final Map<QEvent.Type, QFRenderEventConverter> converterByQEventType = new HashMap<QEvent.Type, QFRenderEventConverter>();

	@Inject
	private final DisplayEventQueue displayEventQueue;

	@Inject
	public void setEventConverters(final Set<QFRenderEventConverter> qfusionEventConverters) {
		for (final QFRenderEventConverter eventConverter : qfusionEventConverters) {
			this.converterByQEventType.put(	eventConverter.getQEventType(),
											eventConverter);
		}
	}

	public void queueRenderEvent(	final DisplayEventSource eventSource,
									final QEvent event) {
		if ((event == null) || (eventSource == null)) {
			return;
		}

		final QFRenderEventConverter eventConverter = this.converterByQEventType
				.get(event.type());

		if (eventConverter != null) {
			final DisplayEvent convertedEvent = eventConverter
					.convertEvent(eventSource, event);

			if (convertedEvent != null) {
				this.displayEventQueue.queueDisplayEvent(convertedEvent);
			}
		}
	}
}