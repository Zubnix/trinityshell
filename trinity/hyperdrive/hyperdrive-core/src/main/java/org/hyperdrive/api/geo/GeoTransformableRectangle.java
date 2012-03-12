package org.hyperdrive.api.geo;

import org.hydrogen.api.paint.HierarchicalRectangle;

public interface GeoTransformableRectangle extends HierarchicalRectangle,
		GeoTransformable {

	void cancelPendingMove();

	void cancelPendingResize();

	void doDestroy();

	void doUpdateLower();

	void doUpdateParentValue();

	void doUpdatePlaceValue();

	void doUpdateRaise();

	void doUpdateSizePlaceValue();

	void doUpdateSizeValue();

	void doUpdateVisibility();

	GeoTransformableRectangle[] getChildren();

	void notifyChildAdded(GeoTransformableRectangle newChild);

	GeoExecutor getGeoExecutor();

	boolean isDestroyed();

	void requestLower();

	void requestMove();

	void requestMoveResize();

	void requestRaise();

	void requestReparent();

	void requestResize();

	void requestVisibilityChange();

	void setHeight(final int height);

	void setParent(final GeoTransformableRectangle parent);

	void setX(final int x);

	void setY(final int y);

	void setVisibility(final boolean visible);

	void setWidth(final int width);

	GeoManager getDominantGeoManager();

	@Override
	GeoTransformableRectangle getParent();

	void addGeoEventHandler(GeoEventHandler geoEventHandler);

	void removeGeoEventHandler(GeoEventHandler geoEventHandler);
}
