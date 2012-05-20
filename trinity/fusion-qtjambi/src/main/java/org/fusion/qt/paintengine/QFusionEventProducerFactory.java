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

import org.hydrogen.display.api.Display;
import org.hydrogen.display.api.EventProducer;
import org.hydrogen.display.api.EventProducerFactory;

// TODO documentation
/**
 * A <code>QFusionEventPromotorFactory</code> constructs an
 * <code>EventProducer</code> implementation for a given <code>Display</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class QFusionEventProducerFactory implements EventProducerFactory {

	private static final Map<Display, QFusionEventProducer> EVENT_PROMOTOR_MAP = new HashMap<Display, QFusionEventProducer>();

	@Override
	public EventProducer getNewEventProducer(final Display display) {

		final QFusionEventProducer eventProducer = new QFusionEventProducer(
				display);

		// TODO this is kinda fishy... fix this.

		// We link the event promotor and the display. This way we can retrieve
		// the promotor when the qfusion render engine is initialized.
		QFusionEventProducerFactory.EVENT_PROMOTOR_MAP.put(display,
				eventProducer);

		return eventProducer;
	}

	/**
	 * 
	 * @param display
	 * @return
	 */
	public static QFusionEventProducer getCreatedEventPromotor(
			final Display display) {
		return QFusionEventProducerFactory.EVENT_PROMOTOR_MAP.get(display);
	}
}
