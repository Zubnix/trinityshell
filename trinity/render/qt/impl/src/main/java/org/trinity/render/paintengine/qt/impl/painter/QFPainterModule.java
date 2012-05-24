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

import org.trinity.core.render.api.PaintCalculation;
import org.trinity.core.render.api.PaintInstruction;
import org.trinity.core.render.api.Painter;
import org.trinity.core.render.api.PainterFactory;
import org.trinity.render.paintengine.qt.api.painter.QFPaintCalculationFactory;
import org.trinity.render.paintengine.qt.api.painter.QFPaintInstructionFactory;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFDestroyInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFGiveFocusInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFGrabKeyboardInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFGrabMouse;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFHideInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFLowerInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFMoveInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFMoveResizeInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFRaiseInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFReleaseKeyboardInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFReleaseMouseInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFResizeInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFSetParentInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFShowInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFTranslateCoordinatesCalculation;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class QFPainterModule extends AbstractModule {

	/*
	 * (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		// start bind painter instructions
		bind(PaintInstruction.class).annotatedWith(Names.named("QFDestroy"))
				.to(QFDestroyInstruction.class);
		bind(PaintInstruction.class).annotatedWith(Names.named("QFGiveFocus"))
				.to(QFGiveFocusInstruction.class);
		bind(PaintInstruction.class)
				.annotatedWith(Names.named("QFGrabKeyboard"))
				.to(QFGrabKeyboardInstruction.class);
		bind(PaintInstruction.class).annotatedWith(Names.named("QFGrabMouse"))
				.to(QFGrabMouse.class);
		bind(PaintInstruction.class).annotatedWith(Names.named("QFHide"))
				.to(QFHideInstruction.class);
		bind(PaintInstruction.class).annotatedWith(Names.named("QFLower"))
				.to(QFLowerInstruction.class);
		bind(PaintInstruction.class).annotatedWith(Names.named("QFRaise"))
				.to(QFRaiseInstruction.class);
		bind(PaintInstruction.class)
				.annotatedWith(Names.named("QFReleaseKeyboard"))
				.to(QFReleaseKeyboardInstruction.class);
		bind(PaintInstruction.class)
				.annotatedWith(Names.named("QFReleaseMouse"))
				.to(QFReleaseMouseInstruction.class);
		bind(PaintInstruction.class).annotatedWith(Names.named("QFShow"))
				.to(QFShowInstruction.class);
		// end bind painter instructions

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