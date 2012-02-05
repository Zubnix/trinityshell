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
package org.hydrogen.paintinterface;

/**
 * A <code>PainterFactory</code> a central place where a <code>Painter</code>
 * implementation can be constructed.It constructs new <code>Painter</code>s for
 * a given <code>Paintable</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface PainterFactory {

	/**
	 * Construct a new <code>Painter</code> that will manage the given
	 * <code>Paintable</code>.
	 * 
	 * @param paintable
	 * @return
	 */
	Painter getNewPainter(Paintable paintable);
}
