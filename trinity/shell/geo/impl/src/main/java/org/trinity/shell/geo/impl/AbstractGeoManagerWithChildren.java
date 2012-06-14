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
package org.trinity.shell.geo.impl;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.trinity.shell.geo.api.GeoEvent;
import org.trinity.shell.geo.api.GeoEventHandler;
import org.trinity.shell.geo.api.GeoOperation;
import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.GeoTransformation;
import org.trinity.shell.geo.api.manager.GeoManagerWithChildren;
import org.trinity.shell.geo.api.manager.LayoutProperty;
import org.trinity.shell.geo.impl.manager.GeoManagerDirect;

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
public abstract class AbstractGeoManagerWithChildren<T extends LayoutProperty>
		extends GeoManagerDirect implements GeoManagerWithChildren<T> {

	private final Map<GeoTransformableRectangle, T> childrenWithLayoutProperty;
	private GeoTransformableRectangle container;

	/**
	 * 
	 */
	public AbstractGeoManagerWithChildren(final GeoTransformableRectangle container) {
		setContainer(container);
		this.childrenWithLayoutProperty = new LinkedHashMap<GeoTransformableRectangle, T>();
	}

	/**
	 * @return
	 */
	@Override
	public GeoTransformableRectangle getContainer() {
		return this.container;
	}

	/**
	 * @param container
	 */
	@Override
	public void setContainer(final GeoTransformableRectangle container) {
		this.container = container;
	}

	/**
	 * @param child
	 * @param layoutProperty
	 */
	@Override
	public T addManagedChild(	final GeoTransformableRectangle child,
								final T layoutProperty) {
		this.childrenWithLayoutProperty.put(child, layoutProperty);
		return layoutProperty;
	}

	@Override
	public T addManagedChild(final GeoTransformableRectangle child) {
		return addManagedChild(child, newDefaultLayoutProperty());
	}

	protected abstract T newDefaultLayoutProperty();

	/**
	 * @param child
	 */
	protected void listenForChildReparent(final GeoTransformableRectangle child) {
		child.addGeoEventHandler(new GeoEventHandler() {
			@Override
			public void handleEvent(final GeoEvent event) {
				final GeoTransformableRectangle square = event
						.getGeoTransformableRectangle();
				removeManagedChild(square);
			}

			@Override
			public GeoOperation getType() {
				return GeoOperation.REPARENT;
			}
		});
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

		for (int i = 0; it.hasNext(); i++, it.next()) {
			if (i == index) {
				it.remove();
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

	@Override
	public void onChangeParentRequest(	final GeoTransformableRectangle geoTransformable,
										final GeoTransformation transformation) {
		if (this.childrenWithLayoutProperty.containsKey(geoTransformable)) {
			onChildChangeParentRequest(geoTransformable, transformation);
		} else {
			super.onChangeParentRequest(geoTransformable, transformation);
		}
	}

	protected abstract void onChildChangeParentRequest(	GeoTransformableRectangle child,
														GeoTransformation transformation);

	@Override
	public void onChangeVisibilityRequest(	final GeoTransformableRectangle geoTransformable,
											final GeoTransformation transformation) {
		if (this.childrenWithLayoutProperty.containsKey(geoTransformable)) {
			onChildChangeVisibilityRequest(geoTransformable, transformation);
		} else {
			super.onChangeVisibilityRequest(geoTransformable, transformation);
		}
	}

	protected abstract void onChildChangeVisibilityRequest(	GeoTransformableRectangle child,
															GeoTransformation transformation);

	@Override
	public void onLowerRequest(	final GeoTransformableRectangle geoTransformable,
								final GeoTransformation transformation) {
		if (this.childrenWithLayoutProperty.containsKey(geoTransformable)) {
			onChildLowerRequest(geoTransformable, transformation);
		} else {
			super.onLowerRequest(geoTransformable, transformation);
		}
	}

	protected abstract void onChildLowerRequest(GeoTransformableRectangle child,
												GeoTransformation transformation);

	@Override
	public void onMoveRequest(	final GeoTransformableRectangle geoTransformable,
								final GeoTransformation transformation) {
		if (this.childrenWithLayoutProperty.containsKey(geoTransformable)) {
			onChildMoveRequest(geoTransformable, transformation);
		} else {
			super.onMoveRequest(geoTransformable, transformation);
		}
	}

	protected abstract void onChildMoveRequest(	GeoTransformableRectangle child,
												GeoTransformation transformation);

	@Override
	public void onMoveResizeRequest(final GeoTransformableRectangle geoTransformable,
									final GeoTransformation transformation) {
		if (this.childrenWithLayoutProperty.containsKey(geoTransformable)) {
			onChildMoveResizeRequest(geoTransformable, transformation);
		} else {
			super.onMoveResizeRequest(geoTransformable, transformation);
		}
	}

	protected abstract void onChildMoveResizeRequest(	GeoTransformableRectangle child,
														GeoTransformation transformation);

	@Override
	public void onRaiseRequest(	final GeoTransformableRectangle geoTransformable,
								final GeoTransformation transformation) {
		if (this.childrenWithLayoutProperty.containsKey(geoTransformable)) {
			onChildRaiseRequest(geoTransformable, transformation);
		} else {
			super.onRaiseRequest(geoTransformable, transformation);
		}
	}

	protected abstract void onChildRaiseRequest(GeoTransformableRectangle child,
												GeoTransformation transformation);

	@Override
	public void onResizeRequest(final GeoTransformableRectangle geoTransformable,
								final GeoTransformation transformation) {
		if (this.childrenWithLayoutProperty.containsKey(geoTransformable)) {
			onChildResizeRequest(geoTransformable, transformation);
		} else {
			super.onResizeRequest(geoTransformable, transformation);
		}
	}

	protected abstract void onChildResizeRequest(	GeoTransformableRectangle child,
													GeoTransformation transformation);

	@Override
	public void onChangeParentNotify(	final GeoTransformableRectangle geoTransformable,
										final GeoTransformation transformation) {
		if (getContainer().equals(geoTransformable)) {
			handleContainerChanged(geoTransformable, transformation);
		} else {
			super.onChangeParentNotify(geoTransformable, transformation);
		}
	}

	protected abstract void handleContainerChanged(	final GeoTransformableRectangle container,
													final GeoTransformation transformation);

	@Override
	public void onChangeVisibilityNotify(	final GeoTransformableRectangle geoTransformable,
											final GeoTransformation transformation) {
		if (getContainer().equals(geoTransformable)) {
			handleContainerChanged(geoTransformable, transformation);
		} else {
			super.onChangeVisibilityNotify(geoTransformable, transformation);
		}
	}

	@Override
	public void onDestroyNotify(final GeoTransformableRectangle geoTransformable,
								final GeoTransformation transformation) {
		if (getContainer().equals(geoTransformable)) {
			handleContainerChanged(geoTransformable, transformation);
		} else {
			super.onDestroyNotify(geoTransformable, transformation);
		}
	}

	@Override
	public void onLowerNotify(	final GeoTransformableRectangle geoTransformable,
								final GeoTransformation transformation) {
		if (getContainer().equals(geoTransformable)) {
			handleContainerChanged(geoTransformable, transformation);
		} else {
			super.onLowerNotify(geoTransformable, transformation);
		}
	}

	@Override
	public void onMoveNotify(	final GeoTransformableRectangle geoTransformable,
								final GeoTransformation transformation) {
		if (getContainer().equals(geoTransformable)) {
			handleContainerChanged(geoTransformable, transformation);
		} else {
			super.onMoveNotify(geoTransformable, transformation);
		}
	}

	@Override
	public void onMoveResizeNotify(	final GeoTransformableRectangle geoTransformable,
									final GeoTransformation transformation) {
		if (getContainer().equals(geoTransformable)) {
			handleContainerChanged(geoTransformable, transformation);
		} else {
			super.onMoveResizeNotify(geoTransformable, transformation);
		}
	}

	@Override
	public void onRaiseNotify(	final GeoTransformableRectangle geoTransformable,
								final GeoTransformation transformation) {
		if (getContainer().equals(geoTransformable)) {
			handleContainerChanged(geoTransformable, transformation);
		} else {
			super.onRaiseNotify(geoTransformable, transformation);
		}
	}

	@Override
	public void onResizeNotify(	final GeoTransformableRectangle geoTransformable,
								final GeoTransformation transformation) {
		if (getContainer().equals(geoTransformable)) {
			handleContainerChanged(geoTransformable, transformation);
		} else {
			super.onResizeNotify(geoTransformable, transformation);
		}
	}
}
