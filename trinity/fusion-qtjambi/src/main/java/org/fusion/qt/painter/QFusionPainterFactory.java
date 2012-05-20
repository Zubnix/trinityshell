/*
 * This file is part of Fusion-qtjambi.
 * 
 * Fusion-qtjambi is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Fusion-qtjambi is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-qtjambi. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.qt.painter;

import org.fusion.qt.paintengine.QFusionRenderEngine;
import org.hydrogen.paint.api.Paintable;
import org.hydrogen.paint.api.PainterFactory;

/**
 * A <code>QFusionPainterFactory</code> provides an implementations of a
 * <code>QFusionPainter</code>. This implementation is used by the
 * <code>Paintable</code> that requested it.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class QFusionPainterFactory implements PainterFactory {

	public final QFusionRenderEngine renderEngine;

	/**
	 * Create a new <code>QFusionPainterFactory</code>. The
	 * <code>QFusionPainter</code>s that are created by this
	 * <code>QFusionPainterFactory</code> will use the given
	 * <code>QFusionRenderEngine</code>.
	 * 
	 * @param renderEngine
	 *            A {@link QFusionRenderEngine}.
	 */
	public QFusionPainterFactory(final QFusionRenderEngine renderEngine) {
		this.renderEngine = renderEngine;
	}

	@Override
	public QFusionPainter getNewPainter(final Paintable paintable) {
		return new QFusionPainter(getRenderEngine(), paintable);
	}

	/**
	 * Gets the <code>QFusionRenderEngine</code> for which this
	 * <code>QFusionPainterFactory</code> provides different
	 * <code>QFusionPainter</code> implementations.
	 * 
	 * @return the <code>QFusionRenderEngine</code> to which the different
	 *         <code>QFusionPainter</code> implementations whil delegate their
	 *         public methods to.
	 */
	public QFusionRenderEngine getRenderEngine() {
		return this.renderEngine;
	}

}
