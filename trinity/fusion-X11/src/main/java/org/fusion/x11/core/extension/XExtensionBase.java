/*
 * This file is part of Fusion-X11.
 * 
 * Fusion-X11 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.x11.core.extension;

//currently unused
//TODO documentation
/**
 * Currently unused.
 * 
 * @author Erik De Rijcke
 * @since 1.1
 */
public abstract class XExtensionBase implements XExtension {
	private final int majorVersion;
	private final int minorVersion;
	private final String extensionName;

	public XExtensionBase(final String extensionNameDesc,
			final int majorVersion, final int minorVersion) {
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.extensionName = extensionNameDesc;
	}

	@Override
	public int getMajorVersion() {
		return this.majorVersion;
	}

	@Override
	public int getMinorVersion() {
		return this.minorVersion;
	}

	@Override
	public String getName() {
		return this.extensionName;
	}

}
