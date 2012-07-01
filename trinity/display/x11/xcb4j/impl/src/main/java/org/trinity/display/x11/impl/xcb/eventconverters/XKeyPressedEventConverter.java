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

import org.trinity.display.x11.core.api.XDisplayResourceFactory;
import org.trinity.display.x11.core.api.XDisplayServer;
import org.trinity.display.x11.core.api.XProtocolConstants;
import org.trinity.display.x11.core.api.XResourceHandleFactory;
import org.trinity.display.x11.core.api.XWindow;
import org.trinity.display.x11.core.api.event.XEvent;
import org.trinity.display.x11.core.api.event.XEventFactory;
import org.trinity.display.x11.core.api.event.XKeyEvent;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.DisplayEventFactory;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.InputModifiersFactory;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.input.api.KeyFactory;
import org.trinity.foundation.input.api.KeyboardInput;
import org.trinity.foundation.input.api.KeyboardInputFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
@Singleton
public class XKeyPressedEventConverter extends AbstractXKeyEventConverter {

	private final KeyFactory keyFactory;
	private final InputModifiersFactory inputModifiersFactory;
	private final KeyboardInputFactory keyboardInputFactory;
	private final DisplayEventFactory displayEventFactory;

	/*****************************************
	 * @param eventCode
	 * @param xDisplayServer
	 * @param xDisplayResourceFactory
	 ****************************************/
	@Inject
	public XKeyPressedEventConverter(	final XEventFactory xEventFactory,
										final XDisplayServer xDisplayServer,
										final XDisplayResourceFactory xDisplayResourceFactory,
										final XResourceHandleFactory xResourceHandleFactory,
										final KeyFactory keyFactory,
										final InputModifiersFactory inputModifiersFactory,
										final KeyboardInputFactory keyboardInputFactory,
										final DisplayEventFactory displayEventFactory) {
		super(	XProtocolConstants.KEY_PRESS,
				xEventFactory,
				xDisplayServer,
				xDisplayResourceFactory,
				xResourceHandleFactory);
		this.keyFactory = keyFactory;
		this.inputModifiersFactory = inputModifiersFactory;
		this.keyboardInputFactory = keyboardInputFactory;
		this.displayEventFactory = displayEventFactory;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.XEventConverter#convertEvent(org.trinity.
	 * display.x11.api.event.XEvent)
	 */
	@Override
	public DisplayEvent convertEvent(final XEvent xEvent) {
		final XKeyEvent event = (XKeyEvent) xEvent;
		final int detail = event.getDetail();
		final int state = event.getState();
		final XWindow window = event.getEvent();

		final Key key = this.keyFactory.createKey(detail);
		final InputModifiers inputModifiers = this.inputModifiersFactory
				.createInputModifiers(state);

		final KeyboardInput keyInput = this.keyboardInputFactory
				.createKeyboardInputStopped(key, inputModifiers);
		return this.displayEventFactory.createKeyPressed(window, keyInput);
	}
}