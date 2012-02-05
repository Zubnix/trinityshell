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

import com.trolltech.qt.gui.QWidget;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ClientManagerLabelVisual extends LabelVisual {

	public static final String NAME = "ClientManagerLabel";

	public ClientManagerLabelVisual(final QWidget parentVisual) {
		super(parentVisual);
		setObjectName(getStyleName());
	}

	@Override
	public String getStyleName() {
		return ClientManagerLabelVisual.NAME;
	}

	/**
	 * 
	 */
	public void activate() {
		setProperty("active", true);
		forceStyleUpdate();
	}

	/**
	 * 
	 */
	public void deactivate() {
		setProperty("active", false);
		forceStyleUpdate();
	}

	/**
	 * 
	 */
	private void forceStyleUpdate() {
		style().unpolish(this);
		style().polish(this);
	}

}
