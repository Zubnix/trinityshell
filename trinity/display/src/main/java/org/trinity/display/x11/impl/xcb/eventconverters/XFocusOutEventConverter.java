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
import org.trinity.display.x11.api.XProtocolConstants;
import org.trinity.display.x11.api.XResourceFactory;
import org.trinity.display.x11.api.XWindow;
import org.trinity.display.x11.api.event.XEvent;
import org.trinity.display.x11.api.event.XFocusEvent;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
@Singleton
public class XFocusOutEventConverter extends AbstractXFocusEventConverter {

	private final DisplayEventFactory displayEventFactory;

	/*****************************************
	 * @param eventCode
	 * @param xResourceFactory
	 ****************************************/
	@Inject
	public XFocusOutEventConverter(	final XResourceFactory xResourceFactory,
									final DisplayEventFactory displayEventFactory) {
		super(XProtocolConstants.FOCUS_OUT, xResourceFactory);
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
		final XFocusEvent event = (XFocusEvent) xEvent;
		final XWindow window = event.getEvent();
		return this.displayEventFactory.createFocusLost(window);
	}
}