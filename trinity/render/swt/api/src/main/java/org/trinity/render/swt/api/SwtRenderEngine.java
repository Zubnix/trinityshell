package org.trinity.render.swt.api;

import java.util.concurrent.Future;

import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableSurfaceNode;

public interface SwtRenderEngine {
	<R> Future<R> invoke(PaintableSurfaceNode paintableSurfaceNode,
			PaintInstruction<R, SwtPaintContext> paintInstruction);
}
