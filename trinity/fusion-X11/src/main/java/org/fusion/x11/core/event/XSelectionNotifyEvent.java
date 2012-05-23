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

import org.fusion.x11.core.XAtom;
import org.fusion.x11.core.XWindow;
import org.trinity.core.display.api.event.DisplayEventType;
import org.trinity.core.display.impl.event.BaseDisplayEvent;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XSelectionNotifyEvent extends BaseDisplayEvent {

	public static final DisplayEventType TYPE = new DisplayEventType();

	private final XAtom selection;
	private final XAtom target;
	private final XAtom property;

	/**
	 * 
	 * @param requestor
	 * @param selection
	 * @param target
	 * @param property
	 */
	public XSelectionNotifyEvent(final XWindow requestor,
			final XAtom selection, final XAtom target, final XAtom property) {
		super(XSelectionNotifyEvent.TYPE, requestor);
		this.selection = selection;
		this.target = target;
		this.property = property;
	}

	/**
	 * 
	 * @return
	 */
	public XAtom getSelection() {
		return this.selection;
	}

	/**
	 * 
	 * @return
	 */
	public XAtom getTarget() {
		return this.target;
	}

	/**
	 * 
	 * @return
	 */
	public XAtom getProperty() {
		return this.property;
	}
}