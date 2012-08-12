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
package org.trinity.render.paintengine.qt.impl.painter.instructions;

import org.trinity.foundation.render.api.PaintCalculation;
import org.trinity.foundation.render.api.Paintable;
import org.trinity.foundation.shared.geometry.api.Coordinate;
import org.trinity.render.paintengine.qt.api.QFRenderEngine;

import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.gui.QWidget;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class QFTranslateCoordinatesCalculation implements
		PaintCalculation<Coordinate, QFRenderEngine> {

	private final Paintable source;
	private final int sourceX;
	private final int sourceY;

	public QFTranslateCoordinatesCalculation(	final Paintable source,
												final int sourceX,
												final int sourceY) {

		this.source = source;
		this.sourceX = sourceX;
		this.sourceY = sourceY;
	}

	@Override
	public Coordinate calculate(final Paintable paintable,
								final QFRenderEngine renderEngine) {
		final QWidget sourcePaintPeer = renderEngine.getVisual(this.source);
		final QWidget targetPaintPeer = renderEngine.getVisual(paintable);

		final QPoint translatedPoint = sourcePaintPeer
				.mapTo(targetPaintPeer, new QPoint(this.sourceX, this.sourceY));
		final Coordinate coordinate = new Coordinate(	translatedPoint.x(),
														translatedPoint.y());
		return coordinate;
	}

}
