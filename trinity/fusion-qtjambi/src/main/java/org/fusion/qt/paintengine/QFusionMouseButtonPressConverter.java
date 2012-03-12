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

import org.hydrogen.api.display.event.ButtonNotifyEvent;
import org.hydrogen.api.display.event.DisplayEventSource;
import org.hydrogen.api.display.event.DisplayEventType;
import org.hydrogen.api.display.input.Momentum;
import org.hydrogen.display.event.BaseButtonNotifyEvent;
import org.hydrogen.display.input.BaseButton;
import org.hydrogen.display.input.BaseInputModifiers;
import org.hydrogen.display.input.BaseMouseInput;

import com.trolltech.qt.gui.QMouseEvent;

// TODO documentation
/**
 * 
 * A <code>QFusionMouseButtonPressConverter</code> takes a
 * <code>QMouseEvent</code> and it's <code>DisplayEventSource</code> as input
 * and converts it to a <code>ButtonNotifyEvent</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class QFusionMouseButtonPressConverter implements
		QFusionEventConverter<QMouseEvent> {

	@Override
	public ButtonNotifyEvent sinkEvent(final DisplayEventSource source,
			final QMouseEvent qEvent) {
		qEvent.accept();

		final QMouseEvent qMouseEvent = qEvent;

		final int detail = qMouseEvent.button().value();
		final int state = qMouseEvent.modifiers().value();

		final int rootX = qMouseEvent.globalX();
		final int rootY = qMouseEvent.globalY();
		final int eventX = qMouseEvent.x();
		final int eventY = qMouseEvent.y();

		final BaseButton baseButton = new BaseButton(detail);
		final BaseInputModifiers baseInputModifiers = new BaseInputModifiers(
				state);
		final BaseMouseInput baseMouseInput = new BaseMouseInput(
				Momentum.STARTED, baseButton, baseInputModifiers, rootX, rootY,
				eventX, eventY);

		return new BaseButtonNotifyEvent(DisplayEventType.BUTTON_PRESSED,
				source, baseMouseInput);
	}
}