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
package org.hydrogen.api.display;

/**
 * <code>PlatformRenderAreaAttributes</code> groups number of different
 * attributes related to a <code>PlatformRenderArea</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface PlatformRenderAreaAttributes {

	/**
	 * Indicates if the <code>PlatformRenderArea</code> has it's override
	 * redirect attribute set.
	 * 
	 * @return True if the override redirect attribute is set, false if not.
	 * @see {@link PlatformRenderArea#overrideRedirect(boolean)}
	 */
	boolean isOverrideRedirect();

	/**
	 * Indicates if the <code>PlatformRenderArea</code> is unmapped. An unmapped
	 * <code>PlatformRenderArea</code> is not visible on screen.
	 * 
	 * @return True if the <code>PlatformRenderArea</code> is invisible, false
	 *         if not.
	 */
	boolean isUnmapped();

	/**
	 * Indicates if the <code>PlatformRenderArea</code> is viewable yet one of
	 * it's direct or indirect ancestors is unmapped.
	 * 
	 * @return True if the <code>PlatformRenderArea</code> is unviewable. False
	 *         if not.
	 */
	boolean isUnviewable();

	/**
	 * Indicates if the <code>PlatformRenderArea</code> is mapped. A mapped
	 * <code>PlatformRenderArea</code> is visible on screen.
	 * <p>
	 * Note that a <code>PlatformRenderArea</code> can be mapped, yet appear
	 * invisible if it is obscured by another <code>PlatformRenderArea</code>.
	 * 
	 * @return
	 */
	boolean isViewable();
}
