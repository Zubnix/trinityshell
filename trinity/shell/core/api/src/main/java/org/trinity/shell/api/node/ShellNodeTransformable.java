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
package org.trinity.shell.api.node;

/**
 * A <code>ShellNodeTransformable</code> can have it's geometry changed over
 * time.
 * <p>
 * To access the desired transformation of a <code>ShellNodeTransformable</code>
 * , implementing classes must provide a <code>ShellNodeTransformation</code>
 * describing the current and new geometric values of the
 * <code>ShellNodeTransformable</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface ShellNodeTransformable {
	/**
	 * The <code>ShellNodeTransformation</code> of this
	 * <code>ShellNodeTransformable</code> at the time of the call.
	 * <p>
	 * A <code>ShellNodeTransformation</code> describes how this
	 * <code>ShellNodeTransformable</code> wishes to be transformed.
	 * 
	 * @return A {@link ShellNodeTransformation}.
	 */
	ShellNodeTransformation toGeoTransformation();
}
