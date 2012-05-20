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

import org.hydrogen.display.api.event.ButtonNotifyEvent;
import org.hydrogen.display.api.event.DisplayEventSource;
import org.hydrogen.display.api.event.DisplayEventType;
import org.hydrogen.display.api.event.base.BaseButtonNotifyEvent;
import org.hydrogen.display.api.input.Momentum;
import org.hydrogen.display.api.input.base.BaseButton;
import org.hydrogen.display.api.input.base.BaseInputModifiers;
import org.hydrogen.display.api.input.base.BaseMouseInput;

import com.trolltech.qt.gui.QMouseEvent;

// TODO documentation
/**
 * 
 * A <code>QFusionMouseButtonReleaseConverter</code> takes a
 * <code>QMouseEvent</code> and it's <code>DisplayEventSource</code> as input
 * and converts it to a <code>ButtonNotifyEvent</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class QFusionMouseButtonReleaseConverter implements
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
				Momentum.STOPPED, baseButton, baseInputModifiers, rootX, rootY,
				eventX, eventY);

		return new BaseButtonNotifyEvent(DisplayEventType.BUTTON_RELEASED,
				source, baseMouseInput);
	}
}