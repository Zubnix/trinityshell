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

import org.fusion.qt.paintengine.api.QFusionEventConverter;
import org.hydrogen.display.api.event.ButtonNotifyEvent;
import org.hydrogen.display.api.event.DisplayEventSource;
import org.hydrogen.display.api.event.base.DisplayEventFactory;
import org.hydrogen.display.api.input.Button;
import org.hydrogen.display.api.input.InputModifiers;
import org.hydrogen.display.api.input.Momentum;
import org.hydrogen.display.api.input.MouseInput;
import org.hydrogen.display.api.input.base.BaseButton;
import org.hydrogen.display.api.input.base.BaseInputModifiers;
import org.hydrogen.display.api.input.base.BaseMouseInput;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;
import com.trolltech.qt.gui.QMouseEvent;

// TODO documentation
/**
 * A <code>QFusionMouseButtonReleaseConverter</code> takes a
 * <code>QMouseEvent</code> and it's <code>DisplayEventSource</code> as input
 * and converts it to a <code>ButtonNotifyEvent</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class QFusionButtonReleasedConverter implements
		QFusionEventConverter<QMouseEvent> {

	private final DisplayEventFactory displayEventFactory;
	private final QEvent.Type qType;

	@Inject
	protected QFusionButtonReleasedConverter(	@Named("QButtonRelease") final QEvent.Type qType,
												final DisplayEventFactory displayEventFactory) {
		this.displayEventFactory = displayEventFactory;
		this.qType = qType;
	}

	@Override
	public ButtonNotifyEvent sinkEvent(	final DisplayEventSource source,
										final QMouseEvent qEvent) {
		qEvent.accept();

		final QMouseEvent qMouseEvent = qEvent;

		final int detail = qMouseEvent.button().value();
		final int state = qMouseEvent.modifiers().value();

		final int rootX = qMouseEvent.globalX();
		final int rootY = qMouseEvent.globalY();
		final int eventX = qMouseEvent.x();
		final int eventY = qMouseEvent.y();

		final Button button = new BaseButton(detail);
		final InputModifiers inputModifiers = new BaseInputModifiers(state);
		final MouseInput mouseInput = new BaseMouseInput(	Momentum.STOPPED,
															button,
															inputModifiers,
															rootX,
															rootY,
															eventX,
															eventY);

		return this.displayEventFactory
				.createButtonReleased(source, mouseInput);
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.qt.paintengine.QFusionEventConverter#getFromType()
	 */
	@Override
	public Type getFromType() {
		return this.qType;
	}
}