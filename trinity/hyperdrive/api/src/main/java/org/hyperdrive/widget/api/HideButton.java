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
package org.hyperdrive.widget.api;

import org.hydrogen.paint.api.PaintCall;
import org.hyperdrive.foundation.api.RenderArea;

public interface HideButton extends Button, RectangleManipulator {

	public interface View extends Button.View, RectangleManipulator.View {
		PaintCall<Void, ?> clientWindowHidden(final RenderArea targetWindow);
	}

	@Override
	public View getView();

	void hideClientWindow();
}
