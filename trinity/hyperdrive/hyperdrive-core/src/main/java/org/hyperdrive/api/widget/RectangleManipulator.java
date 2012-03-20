package org.hyperdrive.api.widget;

import org.hyperdrive.api.core.RenderArea;
import org.hyperdrive.api.geo.GeoTransformableRectangle;

@HasView(RectangleManipulator.View.class)
public interface RectangleManipulator extends Widget {

	public interface View extends Label.View {
		PaintInstruction<Void> rectangleBound(final RenderArea targetWindow);

		PaintInstruction<Void> rectangleUnbound(final RenderArea targetWindow);
	}

	@Override
	public View getView();

	GeoTransformableRectangle getBoundRectangle();
}
