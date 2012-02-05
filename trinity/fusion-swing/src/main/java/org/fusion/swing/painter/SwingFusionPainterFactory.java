package org.fusion.swing.painter;

import org.fusion.swing.paintengine.SwingPaintEngine;
import org.hydrogen.error.HydrogenPaintInterfaceException;
import org.hydrogen.paintinterface.Paintable;
import org.hydrogen.paintinterface.PainterFactory;

public class SwingFusionPainterFactory implements PainterFactory {

	private final SwingPaintEngine paintEngine;

	public SwingFusionPainterFactory(final SwingPaintEngine paintEngine) {
		this.paintEngine = paintEngine;
	}

	@Override
	public SwingFusionPainter getNewPainter(final Paintable paintable)
	                throws HydrogenPaintInterfaceException {
		return new SwingFusionPainter(getPaintEngine(),
		                              paintable);
	}

	public SwingPaintEngine getPaintEngine() {
		return this.paintEngine;
	}

}
