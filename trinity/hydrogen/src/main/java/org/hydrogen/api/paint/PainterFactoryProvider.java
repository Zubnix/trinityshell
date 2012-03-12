/*
 * This file is part of Hydrogen.
 * 
 * Hydrogen is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hydrogen is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hydrogen. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.api.paint;

import org.hydrogen.api.display.Display;

/**
 * A <code>PainterFactoryProvider</code> is responsible for creating a
 * <code>PainterFactory</code> for a <code>Display</code> instance. This
 * <code>PainterFactory</code> should provide <code>Painter</code>s for
 * <code>Paintable</code>s that are created on the given <code>Display</code>
 * instance.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface PainterFactoryProvider {
	/**
	 * Construct a new <code>PainterFactory</code> for the given
	 * <code>Display</code>.
	 * 
	 * @param display
	 * @return
	 */
	PainterFactory newPainterFactory(Display display);

}
