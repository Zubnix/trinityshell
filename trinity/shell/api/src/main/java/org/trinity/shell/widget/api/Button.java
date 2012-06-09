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

import org.trinity.core.input.api.PointerInput;
import org.trinity.core.render.api.PaintInstruction;

public interface Button extends Widget {

	public interface View extends Widget.View {
		PaintInstruction<?> mouseButtonPressed(final PointerInput input);

		PaintInstruction<?> mouseButtonReleased(final PointerInput input);
	}

	@Override
	Button.View getView();
}
