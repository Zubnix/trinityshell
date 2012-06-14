package org.trinity.shell.geo.api.manager;

import org.trinity.shell.geo.api.GeoTransformableRectangle;

public interface GeoManagerWithChildren<T extends LayoutProperty> extends
		GeoManager {
	void setContainer(GeoTransformableRectangle container);

	GeoTransformableRectangle getContainer();

	T addManagedChild(GeoTransformableRectangle child);

	T addManagedChild(final GeoTransformableRectangle child,
			final T layoutProperty);

	T getLayoutProperty(final GeoTransformableRectangle child);

	GeoTransformableRectangle getManagedChild(final int index);

	GeoTransformableRectangle[] getManagedChildren();

	void removeManagedChild(final GeoTransformableRectangle child);

	void removeManagedChild(final int index);
}
