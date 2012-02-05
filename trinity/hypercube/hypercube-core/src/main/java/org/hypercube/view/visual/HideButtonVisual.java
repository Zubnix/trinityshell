/*
 * This file is part of Hypercube.
 * 
 * Hypercube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hypercube is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hypercube. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hypercube.view.visual;

import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class HideButtonVisual extends QLabel implements StyledVisual {

	private final static String NAME = "HideButton";

	/**
	 * 
	 * @param parentVisual
	 */
	public HideButtonVisual(final QWidget parentVisual) {
		super(parentVisual);
		setObjectName(getStyleName());
	}

	@Override
	public String getStyleName() {
		return HideButtonVisual.NAME;
	}

}
