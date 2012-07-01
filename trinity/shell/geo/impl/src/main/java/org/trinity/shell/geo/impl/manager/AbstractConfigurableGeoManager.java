/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.geo.impl.manager;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.manager.ConfigurableGeoManager;
import org.trinity.shell.geo.api.manager.LayoutProperty;

// TODO documentation
/**
 * A <code>GeoManagerWithChildren</code> is an abstract base class for
 * <code>GeoManager</code>s that have to manage children's geometry relative to
 * other children's geometry.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * @param <T>
 */
public abstract class AbstractConfigurableGeoManager<T extends LayoutProperty>
		extends AbstractAbsoluteGeoManager implements ConfigurableGeoManager<T> {

	private final Map<GeoTransformableRectangle, T> childrenWithLayoutProperty = new LinkedHashMap<GeoTransformableRectangle, T>();
	private final GeoTransformableRectangle container;

	/**
	 * 
	 */
	public AbstractConfigurableGeoManager(final GeoTransformableRectangle container) {
		this.container = container;
	}

	/**
	 * @return
	 */
	@Override
	public GeoTransformableRectangle getContainer() {
		return this.container;
	}

	/**
	 * @param child
	 * @param layoutProperty
	 */
	@Override
	public T addManagedChild(	final GeoTransformableRectangle child,
								final T layoutProperty) {
		this.childrenWithLayoutProperty.put(child,
											layoutProperty);
		return layoutProperty;
	}

	@Override
	public T addManagedChild(final GeoTransformableRectangle child) {
		return addManagedChild(	child,
								createLayoutProperty());
	}

	/**
	 * @param child
	 */
	@Override
	public void removeManagedChild(final GeoTransformableRectangle child) {
		this.childrenWithLayoutProperty.remove(child);
	}

	/**
	 * @param index
	 */
	@Override
	public void removeManagedChild(final int index) {

		final Iterator<Entry<GeoTransformableRectangle, T>> it = this.childrenWithLayoutProperty
				.entrySet().iterator();

		final int i = 0;
		while (it.hasNext()) {
			final Entry<GeoTransformableRectangle, T> entry = it.next();
			if (i == index) {
				removeManagedChild(entry.getKey());
				break;
			}
		}
	}

	/**
	 * @return
	 */
	@Override
	public GeoTransformableRectangle[] getManagedChildren() {
		return this.childrenWithLayoutProperty.keySet()
				.toArray(new GeoTransformableRectangle[] {});
	}

	/**
	 * @param index
	 * @return
	 */
	@Override
	public GeoTransformableRectangle getManagedChild(final int index) {
		final Iterator<Entry<GeoTransformableRectangle, T>> it = this.childrenWithLayoutProperty
				.entrySet().iterator();

		GeoTransformableRectangle child = null;
		for (int i = 0; it.hasNext(); i++, child = it.next().getKey()) {
			if (i == index) {
				break;
			}
		}
		return child;
	}

	/**
	 * @param child
	 * @return
	 */
	@Override
	public T getLayoutProperty(final GeoTransformableRectangle child) {
		return this.childrenWithLayoutProperty.get(child);
	}
}