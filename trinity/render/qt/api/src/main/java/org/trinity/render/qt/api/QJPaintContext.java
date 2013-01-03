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
package org.trinity.render.qt.api;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintContext;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableSurfaceNode;

import com.trolltech.qt.gui.QWidget;

/***************************************
 * Exposes QtJambi paint back-end specific paint operations for use with a
 * {@link PaintInstruction}.
 * 
 *************************************** 
 */
public interface QJPaintContext extends PaintContext {

	/***************************************
	 * Bind the given visual so it represents the visual of
	 * {@link #getPaintableSurfaceNode()}
	 * 
	 * @param qWidget
	 *            a {@link QWidget}
	 *************************************** 
	 */
	void setVisual(QWidget visual);

	/***************************************
	 * Unbind and destroy a bound visual.
	 *************************************** 
	 */
	void disposeVisual();

	/***************************************
	 * Query the bound visual for the given {@link PaintableSurfaceNode}.
	 * 
	 * @param paintableSurfaceNode
	 *            a {@link PaintableSurfaceNode}.
	 * @return a {@link QWidget}
	 *************************************** 
	 */
	QWidget getVisual(PaintableSurfaceNode paintableSurfaceNode);

	/***************************************
	 * Get the {@link DisplaySurface} that is used by the given {@link QWidget}
	 * to do it's rendering in.
	 * 
	 * @param visual
	 *            a {@link QWidget}
	 * @return a {@link DisplaySurface}
	 *************************************** 
	 */
	DisplaySurface getDisplaySurface(QWidget visual);

	/***************************************
	 * Make the given visual's geometry match that of
	 * {@link #getPaintableSurfaceNode()}.
	 * 
	 * @param visual
	 *************************************** 
	 */
	void syncVisualGeometryToSurfaceNode(QWidget visual);
}
