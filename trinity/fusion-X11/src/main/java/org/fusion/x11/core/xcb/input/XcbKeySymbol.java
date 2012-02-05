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

package org.fusion.x11.core.xcb.input;

import org.fusion.x11.core.input.XKeySymbol;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XcbKeySymbol implements XKeySymbol {
	private final Long   keySymbolCode;
	private final String stringValue;

	/**
	 * 
	 * @param keySymbolCode
	 * @param stringValue
	 * @param keySymbolRegistry
	 */
	public XcbKeySymbol(final long keySymbolCode,
	                    final String stringValue,
	                    final XcbKeySymbolRegistry keySymbolRegistry) {
		this.keySymbolCode = Long.valueOf(keySymbolCode);
		this.stringValue = stringValue;
		keySymbolRegistry.addXKeysymbol(this);
	}

	@Override
	public Long getSymbolCode() {
		return this.keySymbolCode;
	}

	@Override
	public String stringValue() {
		return this.stringValue;
	}

}
