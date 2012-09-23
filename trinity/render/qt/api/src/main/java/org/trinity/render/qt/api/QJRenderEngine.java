package org.trinity.render.qt.api;

import java.util.concurrent.Future;

import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableSurfaceNode;

public interface QJRenderEngine {

	<R> Future<R> invoke(	PaintableSurfaceNode paintableSurfaceNode,
							PaintInstruction<R, QJPaintContext> paintInstruction);

}
