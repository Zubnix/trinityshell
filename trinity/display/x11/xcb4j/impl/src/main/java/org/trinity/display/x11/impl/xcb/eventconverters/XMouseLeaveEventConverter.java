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
import org.trinity.display.x11.core.api.event.XPointerVisitationEvent;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.DisplayEventFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
@Bind(multiple = true)
@Singleton
public class XMouseLeaveEventConverter extends
		AbstractXPointerVisitationConverter {

	private final DisplayEventFactory displayEventFactory;

	/*****************************************
	 * @param eventCode
	 * @param xResourceFactory
	 * @param display
	 * @param xResourceFactory2
	 ****************************************/
	@Inject
	public XMouseLeaveEventConverter(	final XDisplayResourceFactory xDisplayResourceFactory,
										final XEventFactory xEventFactory,
										final XResourceHandleFactory xResourceHandleFactory,
										final XDisplayServer display,
										final DisplayEventFactory displayEventFactory) {
		super(	XProtocolConstants.LEAVE_NOTIFY,
				xEventFactory,
				xDisplayResourceFactory,
				xResourceHandleFactory,
				display);
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
		final XPointerVisitationEvent event = (XPointerVisitationEvent) xEvent;
		final XWindow window = event.getEvent();
		return this.displayEventFactory.createMouseLeave(window);
	}
}
