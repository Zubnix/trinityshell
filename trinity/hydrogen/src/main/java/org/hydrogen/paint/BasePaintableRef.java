package org.hydrogen.paint;

import org.hydrogen.api.paint.Paintable;
import org.hydrogen.api.paint.PaintableRef;

public class BasePaintableRef implements PaintableRef {

	private final Paintable paintable;

	public BasePaintableRef(final Paintable paintable) {
		this.paintable = paintable;
	}

	// @Override
	// public Paintable getPaintable() {
	// return this.paintable;
	// }

}
