/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.display.x11.impl.xcb.eventconverters;

import org.trinity.core.display.api.event.DisplayEvent;
import org.trinity.core.display.api.event.DisplayEventFactory;
import org.trinity.core.input.api.Button;
import org.trinity.core.input.api.ButtonFactory;
import org.trinity.core.input.api.InputModifiers;
import org.trinity.core.input.api.InputModifiersFactory;
import org.trinity.core.input.api.PointerInput;
import org.trinity.core.input.api.PointerInputFactory;
import org.trinity.display.x11.api.XProtocolConstants;
import org.trinity.display.x11.api.XResourceFactory;
import org.trinity.display.x11.api.XDisplayServer;
import org.trinity.display.x11.api.XWindow;
import org.trinity.display.x11.api.event.XButtonEvent;
import org.trinity.display.x11.api.event.XEvent;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
@Singleton
public class XButtonPressedEventConverter extends AbstractXButtonEventConverter {

	private final DisplayEventFactory displayEventFactory;
	private final ButtonFactory buttonFactory;
	private final InputModifiersFactory inputModifiersFactory;
	private final PointerInputFactory pointerInputFactor;

	/*****************************************
	 * @param eventCode
	 * @param xResourceFactory
	 * @param display
	 * @param displayEventFactory
	 * @param buttonFactory
	 * @param inputModifiersFactory
	 * @param pointerInputFactory
	 ****************************************/
	@Inject
	public XButtonPressedEventConverter(final XResourceFactory xResourceFactory,
										final XDisplayServer display,
										final DisplayEventFactory displayEventFactory,
										final ButtonFactory buttonFactory,
										final InputModifiersFactory inputModifiersFactory,
										final PointerInputFactory pointerInputFactory) {
		super(XProtocolConstants.BUTTON_PRESS, xResourceFactory, display);
		this.displayEventFactory = displayEventFactory;
		this.buttonFactory = buttonFactory;
		this.inputModifiersFactory = inputModifiersFactory;
		this.pointerInputFactor = pointerInputFactory;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.XEventConverter#convertEvent(org.trinity.
	 * display.x11.api.event.XEvent)
	 */
	@Override
	public DisplayEvent convertEvent(final XEvent xEvent) {
		final XButtonEvent event = (XButtonEvent) xEvent;
		final int buttonCode = event.getButton();
		final XWindow window = event.getWindow();
		final int state = event.getState();
		final int rootX = event.getXRoot();
		final int rootY = event.getYRoot();
		final int relativeX = event.getX();
		final int relativeY = event.getY();

		final Button button = this.buttonFactory.createButton(buttonCode);
		final InputModifiers inputModifiers = this.inputModifiersFactory
				.createInputModifiers(state);
		final PointerInput input = this.pointerInputFactor
				.createPointerInputStarted(	button,
											inputModifiers,
											rootX,
											rootY,
											relativeX,
											relativeY);
		return this.displayEventFactory.createButtonPressed(window, input);
	}
}
