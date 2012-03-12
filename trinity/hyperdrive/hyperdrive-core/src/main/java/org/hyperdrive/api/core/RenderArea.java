package org.hyperdrive.api.core;

import org.hydrogen.api.display.PlatformRenderArea;
import org.hyperdrive.api.geo.GeoTransformableRectangle;

public interface RenderArea extends GeoTransformableRectangle {
	int getHeightIncrement();

	ManagedDisplay getManagedDisplay();

	int getMaxHeight();

	int getMaxWidth();

	int getMinHeight();

	int getMinWidth();

	PlatformRenderArea getPlatformRenderArea();

	int getWidthIncrement();

	boolean isMovable();

	boolean isResizable();

	void setHeightIncrement(final int heightIncrement);

	void setMaxHeight(final int maxHeight);

	void setMaxWidth(final int maxWidth);

	void setMinHeight(final int minHeight);

	void setMinWidth(final int minWidth);

	void setMovable(final boolean movable);

	void setResizable(final boolean isResizable);

	void setWidthIncrement(final int widthIncrement);

	void syncGeoToPlatformRenderAreaGeo();

	void setInputFocus();

	boolean hasInputFocus();
}
