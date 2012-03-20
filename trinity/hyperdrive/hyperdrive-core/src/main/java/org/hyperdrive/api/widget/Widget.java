package org.hyperdrive.api.widget;

import org.hydrogen.api.display.ResourceHandle;
import org.hydrogen.api.display.event.DisplayEventSource;
import org.hydrogen.api.display.input.KeyboardInput;
import org.hydrogen.api.display.input.MouseInput;
import org.hydrogen.api.geometry.Rectangle;
import org.hydrogen.api.paint.Paintable;
import org.hyperdrive.api.core.RenderArea;
import org.hyperdrive.api.geo.HasGeoManager;

public interface Widget extends Paintable, DisplayEventSource, HasGeoManager,
		RenderArea {

	public interface View {
		/**
		 * 
		 * @param args
		 *            First arg is of type {@link Widget} and is the closest
		 *            {@link Paintable} parent of the <code>Paintable</code>
		 *            that needs to be created.
		 * @return
		 */
		PaintInstruction<ResourceHandle> doCreate(Rectangle form,
				boolean visible, Paintable parentPaintable);

		PaintInstruction<Void> doDestroy();
	}

	View getView();

	void onMouseButtonPressed(final MouseInput input);

	void onMouseButtonReleased(final MouseInput input);

	void onKeyboardPressed(final KeyboardInput input);

	void onKeyboardReleased(final KeyboardInput input);
}
