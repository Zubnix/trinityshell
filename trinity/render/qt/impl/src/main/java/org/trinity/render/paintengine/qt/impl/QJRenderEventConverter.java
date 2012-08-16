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
import com.google.inject.Singleton;
import com.trolltech.qt.core.QEvent;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

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
@Bind(to = @To(Type.IMPLEMENTATION))
@Singleton
public class QJRenderEventConverter {

	private final Map<QEvent.Type, QJRenderEventConversion> converterByQEventType = new HashMap<QEvent.Type, QJRenderEventConversion>();

	@Inject
	QJRenderEventConverter(final Set<QJRenderEventConversion> qfusionEventConversions) {
		for (final QJRenderEventConversion eventConverter : qfusionEventConversions) {
			this.converterByQEventType.put(	eventConverter.getQEventType(),
											eventConverter);
		}
	}

	public DisplayEvent convertRenderEvent(	final DisplayEventSource eventSource,
											final QEvent event) {

		if ((event != null) && (eventSource != null)) {
			final QJRenderEventConversion eventConverter = this.converterByQEventType
					.get(event.type());
			if (eventConverter != null) {
				final DisplayEvent convertedEvent = eventConverter
						.convertEvent(eventSource, event);

				return convertedEvent;
			}
		}
		return null;
	}
}