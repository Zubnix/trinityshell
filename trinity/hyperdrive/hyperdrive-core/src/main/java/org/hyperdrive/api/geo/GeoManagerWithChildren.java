package org.hyperdrive.api.geo;

public interface GeoManagerWithChildren<T extends LayoutProperty> extends
		GeoManager {
	void setContainer(GeoTransformableRectangle container);

	GeoTransformableRectangle getContainer();

	void addManagedChild(GeoTransformableRectangle child);

	void addManagedChild(final GeoTransformableRectangle child,
			final T layoutProperty);

	T getLayoutProperty(final GeoTransformableRectangle child);

	GeoTransformableRectangle getManagedChild(final int index);

	GeoTransformableRectangle[] getManagedChildren();

	void removeManagedChild(final GeoTransformableRectangle child);

	void removeManagedChild(final int index);
}
