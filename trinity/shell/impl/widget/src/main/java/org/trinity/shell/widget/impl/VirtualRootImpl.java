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
package org.trinity.shell.widget.impl;

import org.trinity.core.render.api.PainterFactory;
import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.geo.impl.manager.GeoManagerDirect;
import org.trinity.shell.widget.api.Root;
import org.trinity.shell.widget.api.VirtualRoot;

import com.google.inject.Inject;
import com.google.inject.name.Named;

// TODO documentation
// TODO list events that are emitted by this widget in doc
/**
 * A <code>VirtualRoot</code> is a <code>Widget</code> that can be used as a
 * virtual desktop for a <code>ManagedDisplay</code>. Unlike the
 * <code>RealRoot<code>, there can be more than one <code>VirtualRoot</code>,
 * each referencing a different unique native window.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class VirtualRootImpl extends WidgetImpl implements VirtualRoot {

	@Inject
	private VirtualRoot.View view;

	@Override
	public VirtualRoot.View getView() {
		return this.view;
	}

	/**
	 * @param baseRoot
	 */
	@Inject
	protected VirtualRootImpl(	final PainterFactory painterFactory,
								final Root root,
								@Named("Widget") final GeoExecutor geoExecutor) {
		super(painterFactory, geoExecutor);
		setParent(root);
		setX(root.getX());
		setY(root.getY());
		setWidth(root.getWidth());
		setHeight(root.getHeight());
		setGeoManager(new GeoManagerDirect());
		requestReparent();
		requestMoveResize();
	}
}
