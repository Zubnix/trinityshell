/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.widget.api;

import org.trinity.core.display.api.event.DisplayEventSource;
import org.trinity.core.geometry.api.Rectangle;
import org.trinity.core.input.api.KeyboardInput;
import org.trinity.core.input.api.MouseInput;
import org.trinity.core.render.api.PaintConstruction;
import org.trinity.core.render.api.PaintInstruction;
import org.trinity.core.render.api.Paintable;
import org.trinity.shell.foundation.api.RenderArea;

public interface Widget extends Paintable, DisplayEventSource, RenderArea {

	public interface View {
		/**
		 * @param args
		 *            First arg is of type {@link Widget} and is the closest
		 *            {@link Paintable} parent of the <code>Paintable</code>
		 *            that needs to be created.
		 * @return
		 */
		PaintConstruction<?> doCreate(	Rectangle form,
										boolean visible,
										Paintable parentPaintable);

		PaintInstruction<?> doDestroy();
	}

	void onMouseButtonPressed(final MouseInput input);

	void onMouseButtonReleased(final MouseInput input);

	void onKeyboardPressed(final KeyboardInput input);

	void onKeyboardReleased(final KeyboardInput input);

	/*****************************************
	 * @return
	 ****************************************/
	View getView();
}
