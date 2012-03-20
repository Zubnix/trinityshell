/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.widget;

import org.hydrogen.api.display.input.MouseInput;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ResizeButton extends BaseDragButton {

	private int desiredWidth;
	private int desiredHeight;

	@Override
	public void startDrag(final MouseInput input) {
		this.desiredWidth = getBoundRectangle().getWidth();
		this.desiredHeight = getBoundRectangle().getHeight();
		super.startDrag(input);
	}

	@Override
	protected void mutate(final int vectX, final int vectY) {
		this.desiredWidth += vectX;
		this.desiredHeight += vectY;

		getBoundRectangle().setWidth(this.desiredWidth);
		getBoundRectangle().setHeight(this.desiredHeight);

		// TODO use desktop protocol to automate this:
		// use delay to sync with client as specified by ewmh
		// _NET_WM_SYNC_REQUEST. implement this delay in the fusion-x11 package.

		getBoundRectangle().requestResize();
	}
}
