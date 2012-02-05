/*
 * This file is part of Fusion-X11.
 * 
 * Fusion-X11 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * This file is part of Fusion-X11.
 * 
 * Fusion-X11 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.x11.core.event;

import org.fusion.x11.core.DataContainer;
import org.fusion.x11.core.XAtom;
import org.fusion.x11.core.XWindow;
import org.hydrogen.displayinterface.event.BaseClientMessageEvent;

// TODO documentation

/**
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XClientMessageEvent extends BaseClientMessageEvent {

	private final DataContainer<?> dataContainer;

	/**
	 * 
	 * @param origin
	 * @param messageType
	 * @param dataContainer
	 */
	public XClientMessageEvent(final XWindow origin,
	                             final XAtom messageType,
	                             final DataContainer<?> dataContainer) {
		super(origin,
		      messageType,
		      dataContainer.getDataFormat().getFormat(),
		      dataContainer.getAllData());
		this.dataContainer = dataContainer;
	}

	/**
	 * 
	 * @return
	 */
	public DataContainer<?> getDataContainer() {
		return this.dataContainer;
	}

	@Override
	public XAtom getMessageType() {
		return (XAtom) super.getMessageType();
	}

	@Override
	public XWindow getEventSource() {
		return (XWindow) super.getEventSource();
	}
}
