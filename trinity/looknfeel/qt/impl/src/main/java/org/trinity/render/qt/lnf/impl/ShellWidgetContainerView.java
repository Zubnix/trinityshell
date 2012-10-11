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
package org.trinity.render.qt.lnf.impl;

import javax.inject.Named;

import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QGraphicsDropShadowEffect;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Named("ShellWidgetContainerView")
public class ShellWidgetContainerView extends AbstractShellWidgetView {

	@Override
	protected QWidget createVisual(final QWidget parentVisual) {
		final QFrame visual = new QFrame(parentVisual);
		final QGraphicsDropShadowEffect effect = new QGraphicsDropShadowEffect();
		effect.setBlurRadius(10);
		effect.setOffset(	0,
							5);
		effect.setColor(QColor.darkGray);

		visual.setGraphicsEffect(effect);
		return visual;
	}

}
