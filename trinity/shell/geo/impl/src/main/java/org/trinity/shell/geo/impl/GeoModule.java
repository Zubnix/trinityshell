/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.geo.impl;

import org.trinity.shell.geo.api.GeoEvent;
import org.trinity.shell.geo.api.GeoEventFactory;
import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.manager.GeoManager;
import org.trinity.shell.geo.api.manager.GeoManagerFactory;
import org.trinity.shell.geo.api.manager.GeoManagerWithChildren;
import org.trinity.shell.geo.api.manager.LayoutPropertyLine;
import org.trinity.shell.geo.impl.manager.GeoManagerDirect;
import org.trinity.shell.geo.impl.manager.GeoManagerLine;
import org.trinity.shell.geo.impl.manager.LayoutPropertyLineImpl;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class GeoModule extends AbstractModule {

	/*
	 * (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		// start geo bindings
		bind(GeoExecutor.class).annotatedWith(Names.named("GeoVirt"))
				.to(GeoVirtGeoExecutor.class);
		bind(GeoTransformableRectangle.class).to(GeoVirtRectangle.class);
		bind(GeoManager.class).annotatedWith(Names.named("Direct"))
				.to(GeoManagerDirect.class);
		// end geo bindings

		// start geo factory bindings
		install(new FactoryModuleBuilder().implement(	GeoEvent.class,
														GeoEventImpl.class)
				.build(GeoEventFactory.class));
		install(new FactoryModuleBuilder()
				.implement(	GeoManager.class,
							Names.named("Direct"),
							GeoManagerDirect.class)
				.implement(	GeoManagerWithChildren.class,
							Names.named("Line"),
							GeoManagerLine.class)
				.implement(	LayoutPropertyLine.class,
							LayoutPropertyLineImpl.class)
				.build(GeoManagerFactory.class));
		// end geo factory bindings

	}
}
