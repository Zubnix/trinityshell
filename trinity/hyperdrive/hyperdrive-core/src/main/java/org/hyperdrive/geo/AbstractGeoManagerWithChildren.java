/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.geo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hyperdrive.api.geo.GeoEvent;
import org.hyperdrive.api.geo.GeoEventHandler;
import org.hyperdrive.api.geo.GeoManagerWithChildren;
import org.hyperdrive.api.geo.GeoOperation;
import org.hyperdrive.api.geo.GeoTransformableRectangle;
import org.hyperdrive.api.geo.GeoTransformation;
import org.hyperdrive.api.geo.LayoutProperty;

// TODO documentation
/**
 * A <code>GeoManagerWithChildren</code> is an abstract base class for
 * <code>GeoManager</code>s that have to manage children's geometry relative to
 * other children's geometry.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 * @param <T>
 */
public abstract class AbstractGeoManagerWithChildren<T extends LayoutProperty>
		extends GeoManagerDirect implements GeoManagerWithChildren<T> {

	private final List<GeoTransformableRectangle> children;
	private final Map<GeoTransformableRectangle, T> childLayoutProperty;
	private GeoTransformableRectangle container;

	/**
	 * 
	 */
	public AbstractGeoManagerWithChildren(
			final GeoTransformableRectangle container) {
		setContainer(container);
		this.children = new ArrayList<GeoTransformableRectangle>();
		this.childLayoutProperty = new HashMap<GeoTransformableRectangle, T>();
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public GeoTransformableRectangle getContainer() {
		return this.container;
	}

	/**
	 * 
	 * @param container
	 */
	@Override
	public void setContainer(final GeoTransformableRectangle container) {
		this.container = container;
	}

	/**
	 * 
	 * @param child
	 * 
	 */
	@Override
	public void addManagedChild(final GeoTransformableRectangle child) {
		this.children.add(child);
	}

	/**
	 * 
	 * @param child
	 * @param layoutProperty
	 * 
	 */
	@Override
	public void addManagedChild(final GeoTransformableRectangle child,
			final T layoutProperty) {
		this.children.add(child);
		this.childLayoutProperty.put(child, layoutProperty);
	}

	/**
	 * 
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
	 * 
	 * @param child
	 * 
	 */
	@Override
	public void removeManagedChild(final GeoTransformableRectangle child) {
		this.children.remove(child);
		this.childLayoutProperty.remove(child);
	}

	/**
	 * 
	 * @param index
	 */
	@Override
	public void removeManagedChild(final int index) {
		final GeoTransformableRectangle removedChild = this.children
				.remove(index);
		this.childLayoutProperty.remove(removedChild);
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public GeoTransformableRectangle[] getManagedChildren() {
		return this.children
				.toArray(new GeoTransformableRectangle[this.children.size()]);
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	@Override
	public GeoTransformableRectangle getManagedChild(final int index) {
		return this.children.get(index);
	}

	/**
	 * 
	 * @param child
	 * @return
	 */
	@Override
	public T getLayoutProperty(final GeoTransformableRectangle child) {
		return this.childLayoutProperty.get(child);
	}

	@Override
	public void onChangeParentRequest(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		if (this.children.contains(geoTransformable)) {
			onChildChangeParentRequest(geoTransformable, transformation);
		} else {
			super.onChangeParentRequest(geoTransformable, transformation);
		}
	}

	protected abstract void onChildChangeParentRequest(
			GeoTransformableRectangle child, GeoTransformation transformation);

	@Override
	public void onChangeVisibilityRequest(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		if (this.children.contains(geoTransformable)) {
			onChildChangeVisibilityRequest(geoTransformable, transformation);
		} else {
			super.onChangeVisibilityRequest(geoTransformable, transformation);
		}
	}

	protected abstract void onChildChangeVisibilityRequest(
			GeoTransformableRectangle child, GeoTransformation transformation);

	@Override
	public void onLowerRequest(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		if (this.children.contains(geoTransformable)) {
			onChildLowerRequest(geoTransformable, transformation);
		} else {
			super.onLowerRequest(geoTransformable, transformation);
		}
	}

	protected abstract void onChildLowerRequest(
			GeoTransformableRectangle child, GeoTransformation transformation);

	@Override
	public void onMoveRequest(final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		if (this.children.contains(geoTransformable)) {
			onChildMoveRequest(geoTransformable, transformation);
		} else {
			super.onMoveRequest(geoTransformable, transformation);
		}
	}

	protected abstract void onChildMoveRequest(GeoTransformableRectangle child,
			GeoTransformation transformation);

	@Override
	public void onMoveResizeRequest(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		if (this.children.contains(geoTransformable)) {
			onChildMoveResizeRequest(geoTransformable, transformation);
		} else {
			super.onMoveResizeRequest(geoTransformable, transformation);
		}
	}

	protected abstract void onChildMoveResizeRequest(
			GeoTransformableRectangle child, GeoTransformation transformation);

	@Override
	public void onRaiseRequest(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		if (this.children.contains(geoTransformable)) {
			onChildRaiseRequest(geoTransformable, transformation);
		} else {
			super.onRaiseRequest(geoTransformable, transformation);
		}
	}

	protected abstract void onChildRaiseRequest(
			GeoTransformableRectangle child, GeoTransformation transformation);

	@Override
	public void onResizeRequest(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		if (this.children.contains(geoTransformable)) {
			onChildResizeRequest(geoTransformable, transformation);
		} else {
			super.onResizeRequest(geoTransformable, transformation);
		}
	}

	protected abstract void onChildResizeRequest(
			GeoTransformableRectangle child, GeoTransformation transformation);

	@Override
	public void onChangeParentNotify(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		if (getContainer().equals(geoTransformable)) {
			handleContainerChanged(geoTransformable, transformation);
		} else {
			super.onChangeParentNotify(geoTransformable, transformation);
		}
	}

	protected abstract void handleContainerChanged(
			final GeoTransformableRectangle container,
			final GeoTransformation transformation);

	@Override
	public void onChangeVisibilityNotify(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		if (getContainer().equals(geoTransformable)) {
			handleContainerChanged(geoTransformable, transformation);
		} else {
			super.onChangeVisibilityNotify(geoTransformable, transformation);
		}
	}

	@Override
	public void onDestroyNotify(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		if (getContainer().equals(geoTransformable)) {
			handleContainerChanged(geoTransformable, transformation);
		} else {
			super.onDestroyNotify(geoTransformable, transformation);
		}
	}

	@Override
	public void onLowerNotify(final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		if (getContainer().equals(geoTransformable)) {
			handleContainerChanged(geoTransformable, transformation);
		} else {
			super.onLowerNotify(geoTransformable, transformation);
		}
	}

	@Override
	public void onMoveNotify(final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		if (getContainer().equals(geoTransformable)) {
			handleContainerChanged(geoTransformable, transformation);
		} else {
			super.onMoveNotify(geoTransformable, transformation);
		}
	}

	@Override
	public void onMoveResizeNotify(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		if (getContainer().equals(geoTransformable)) {
			handleContainerChanged(geoTransformable, transformation);
		} else {
			super.onMoveResizeNotify(geoTransformable, transformation);
		}
	}

	@Override
	public void onRaiseNotify(final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		if (getContainer().equals(geoTransformable)) {
			handleContainerChanged(geoTransformable, transformation);
		} else {
			super.onRaiseNotify(geoTransformable, transformation);
		}
	}

	@Override
	public void onResizeNotify(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		if (getContainer().equals(geoTransformable)) {
			handleContainerChanged(geoTransformable, transformation);
		} else {
			super.onResizeNotify(geoTransformable, transformation);
		}
	}
}
