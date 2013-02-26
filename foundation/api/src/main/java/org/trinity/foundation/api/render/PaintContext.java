/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.api.render;

/***************************************
 * The context in which a paint operation takes place. Optionally, a
 * {@code PaintContext} can be extended by a specific paint back-end API to
 * provide additional paint operations.
 * 
 *************************************** 
 */
public interface PaintContext {

	/***************************************
	 * The object that issues the painting. This is the caller context
	 * {@code Object} argument as passed in to
	 * {@link PaintRenderer#invoke(Object, PaintRoutine)}
	 * 
	 * @return a caller context {@code Object}.
	 *************************************** 
	 */
	Object getCaller();
}