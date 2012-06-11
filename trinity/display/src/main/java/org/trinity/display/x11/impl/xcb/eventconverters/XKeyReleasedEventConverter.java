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
import org.trinity.core.input.api.InputModifiers;
import org.trinity.core.input.api.InputModifiersFactory;
import org.trinity.core.input.api.Key;
import org.trinity.core.input.api.KeyFactory;
import org.trinity.core.input.api.KeyboardInput;
import org.trinity.core.input.api.KeyboardInputFactory;
import org.trinity.display.x11.api.XDisplayServer;
import org.trinity.display.x11.api.XProtocolConstants;
import org.trinity.display.x11.api.XResourceFactory;
import org.trinity.display.x11.api.XWindow;
import org.trinity.display.x11.api.event.XEvent;
import org.trinity.display.x11.api.event.XKeyEvent;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
@Singleton
public class XKeyReleasedEventConverter extends AbstractXKeyEventConverter {

	private final KeyFactory keyFactory;
	private final InputModifiersFactory inputModifiersFactory;
	private final KeyboardInputFactory keyboardInputFactory;
	private final DisplayEventFactory displayEventFactory;

	/*****************************************
	 * @param eventCode
	 * @param xDisplayServer
	 * @param xResourceFactory
	 ****************************************/
	@Inject
	public XKeyReleasedEventConverter(	final int eventCode,
										final XDisplayServer xDisplayServer,
										final XResourceFactory xResourceFactory,
										final KeyFactory keyFactory,
										final InputModifiersFactory inputModifiersFactory,
										final KeyboardInputFactory keyboardInputFactory,
										final DisplayEventFactory displayEventFactory) {
		super(XProtocolConstants.KEY_RELEASE, xDisplayServer, xResourceFactory);
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
		return this.displayEventFactory.createKeyReleased(window, keyInput);
	}
}