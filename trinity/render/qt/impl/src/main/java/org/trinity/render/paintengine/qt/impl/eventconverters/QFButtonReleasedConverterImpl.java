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
package org.trinity.render.paintengine.qt.impl.eventconverters;

import org.trinity.core.display.api.event.ButtonNotifyEvent;
import org.trinity.core.display.api.event.DisplayEventFactory;
import org.trinity.core.display.api.event.DisplayEventSource;
import org.trinity.core.input.api.Button;
import org.trinity.core.input.api.ButtonFactory;
import org.trinity.core.input.api.InputModifiers;
import org.trinity.core.input.api.InputModifiersFactory;
import org.trinity.core.input.api.MouseInput;
import org.trinity.core.input.api.MouseInputFactory;
import org.trinity.render.paintengine.qt.api.QFRenderEventConverter;

import com.google.inject.Inject;
import com.google.inject.Singleton;
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
@Singleton
public final class QFButtonReleasedConverterImpl implements
		QFRenderEventConverter {

	private final ButtonFactory buttonFactory;
	private final InputModifiersFactory inputModifiersFactory;
	private final DisplayEventFactory displayEventFactory;
	private final MouseInputFactory mouseInputFactory;
	private final QEvent.Type qType;

	@Inject
	protected QFButtonReleasedConverterImpl(@Named("MouseButtonRelease") final QEvent.Type qType,
											final DisplayEventFactory displayEventFactory,
											final ButtonFactory buttonFactory,
											final InputModifiersFactory inputModifiersFactory,
											final MouseInputFactory mouseInputFactory) {
		this.buttonFactory = buttonFactory;
		this.inputModifiersFactory = inputModifiersFactory;
		this.mouseInputFactory = mouseInputFactory;
		this.displayEventFactory = displayEventFactory;
		this.qType = qType;
	}

	@Override
	public ButtonNotifyEvent convertEvent(	final DisplayEventSource source,
											final QEvent qEvent) {
		qEvent.accept();

		final QMouseEvent qMouseEvent = (QMouseEvent) qEvent;

		final int buttonCode = qMouseEvent.button().value();
		final int state = qMouseEvent.modifiers().value();

		final int rootX = qMouseEvent.globalX();
		final int rootY = qMouseEvent.globalY();
		final int relativeX = qMouseEvent.x();
		final int relativeY = qMouseEvent.y();

		final Button button = this.buttonFactory.createButton(buttonCode);
		final InputModifiers inputModifiers = this.inputModifiersFactory
				.createInputModifiers(state);
		final MouseInput mouseInput = this.mouseInputFactory
				.createMouseInputStopped(	button,
											inputModifiers,
											rootX,
											rootY,
											relativeX,
											relativeY);

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