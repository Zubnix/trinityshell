package org.fusion.swing.painter;

import org.fusion.swing.paintengine.SwingPaintEngine;
import org.hydrogen.displayinterface.Display;
import org.hydrogen.error.HydrogenPaintInterfaceException;
import org.hydrogen.paintinterface.PainterFactory;
import org.hydrogen.paintinterface.PainterFactoryProvider;

public class SwingFusionPainterFactoryProvider implements
                PainterFactoryProvider {

	@Override
	public PainterFactory newPainterFactory(final Display display)
	                throws HydrogenPaintInterfaceException {
		return new SwingFusionPainterFactory(new SwingPaintEngine());
	}
}
