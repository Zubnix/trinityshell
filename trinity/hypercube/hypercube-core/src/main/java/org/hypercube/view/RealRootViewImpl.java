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
package org.hypercube.view;

import org.hydrogen.paintinterface.Paintable;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QDesktopWidget;
import com.trolltech.qt.gui.QWidget;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class RealRootViewImpl extends QFusionViewImpl {
	@Override
	protected QWidget initVisual(QWidget parentVisual,
	                             Paintable paintable,
	                             Object... args) {
		final QDesktopWidget desktopWidget = QApplication.desktop();
		desktopWidget.setFixedSize(desktopWidget.frameSize());
		return desktopWidget;
	}
}
