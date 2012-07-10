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
package org.trinity.render.paintengine.qt.impl.painter;

import org.trinity.foundation.render.api.PaintCalculation;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.Painter;
import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.render.paintengine.qt.api.painter.QFPaintCalculationFactory;
import org.trinity.render.paintengine.qt.api.painter.QFPaintInstructionFactory;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFMoveInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFMoveResizeInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFResizeInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFSetParentInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFTranslateCoordinatesCalculation;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

import de.devsurf.injection.guice.annotations.GuiceModule;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
@GuiceModule
public class QFPainterModule extends AbstractModule {

	/*
	 * (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		// start bind painter instructions factory
		install(new FactoryModuleBuilder()
				.implement(	PaintInstruction.class,
							Names.named("QFMove"),
							QFMoveInstruction.class)
				.implement(	PaintInstruction.class,
							Names.named("QFMoveResize"),
							QFMoveResizeInstruction.class)
				.implement(	PaintInstruction.class,
							Names.named("QFResize"),
							QFResizeInstruction.class)
				.implement(	PaintInstruction.class,
							Names.named("QFSetParent"),
							QFSetParentInstruction.class)
				.build(QFPaintInstructionFactory.class));
		// end bind painter instructions factory

		// start bind painter calculations factory
		install(new FactoryModuleBuilder()
				.implement(	PaintCalculation.class,
							Names.named("QFTranslateCoordinates"),
							QFTranslateCoordinatesCalculation.class)
				.build(QFPaintCalculationFactory.class));
		// end bind painter calculations factory

		// start bind painter factory
		install(new FactoryModuleBuilder().implement(	Painter.class,
														QFPainter.class)
				.build(PainterFactory.class));
		// end bind painter factory
	}
}