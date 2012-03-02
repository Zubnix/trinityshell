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

import org.hydrogen.displayinterface.input.MouseInput;

// TODO create super push button class
// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ResizeButton extends DragButton {

	@ViewDefinition
	public interface View extends Button.View {

	}

	private int desiredWidth;
	private int desiredHeight;

	@Override
	public void startDrag(final MouseInput input) {
		this.desiredWidth = getTargetRenderArea().getWidth();
		this.desiredHeight = getTargetRenderArea().getHeight();
		super.startDrag(input);
	}

	@Override
	protected void mutate(final int vectX, final int vectY) {
		this.desiredWidth += vectX;
		this.desiredHeight += vectY;

		getTargetRenderArea().setWidth(this.desiredWidth);
		getTargetRenderArea().setHeight(this.desiredHeight);

		// use delay to sync with client as specified by ewmh
		// _NET_WM_SYNC_REQUEST. implement this delay in the fusion-x11 package.

		getTargetRenderArea().requestResize();
	}
}
