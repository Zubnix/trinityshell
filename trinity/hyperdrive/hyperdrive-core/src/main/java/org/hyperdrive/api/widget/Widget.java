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
package org.hyperdrive.api.widget;

import org.hydrogen.api.display.ResourceHandle;
import org.hydrogen.api.display.event.DisplayEventSource;
import org.hydrogen.api.display.input.KeyboardInput;
import org.hydrogen.api.display.input.MouseInput;
import org.hydrogen.api.geometry.Rectangle;
import org.hydrogen.api.paint.Paintable;
import org.hyperdrive.api.core.RenderArea;

public interface Widget extends Paintable, DisplayEventSource, RenderArea {

	public interface View {
		/**
		 * 
		 * @param args
		 *            First arg is of type {@link Widget} and is the closest
		 *            {@link Paintable} parent of the <code>Paintable</code>
		 *            that needs to be created.
		 * @return
		 */
		PaintInstruction<ResourceHandle> doCreate(Rectangle form,
				boolean visible, Paintable parentPaintable);

		PaintInstruction<Void> doDestroy();
	}

	View getView();

	void onMouseButtonPressed(final MouseInput input);

	void onMouseButtonReleased(final MouseInput input);

	void onKeyboardPressed(final KeyboardInput input);

	void onKeyboardReleased(final KeyboardInput input);
}
