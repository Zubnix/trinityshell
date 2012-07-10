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

import org.trinity.shell.geo.api.event.GeoEvent;
import org.trinity.shell.geo.api.event.GeoEventFactory;
import org.trinity.shell.geo.api.manager.ConfigurableGeoManager;
import org.trinity.shell.geo.api.manager.GeoManager;
import org.trinity.shell.geo.api.manager.GeoManagerFactory;
import org.trinity.shell.geo.api.manager.LayoutPropertyLine;
import org.trinity.shell.geo.impl.event.GeoEventImpl;
import org.trinity.shell.geo.impl.manager.AbstractAbsoluteGeoManager;
import org.trinity.shell.geo.impl.manager.GeoManagerLine;
import org.trinity.shell.geo.impl.manager.LayoutPropertyLineImpl;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

import de.devsurf.injection.guice.annotations.GuiceModule;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
@GuiceModule
public class GeoModule extends AbstractModule {

	@Override
	protected void configure() {
		// TODO more geovents
		install(new FactoryModuleBuilder().implement(	GeoEvent.class,
														GeoEventImpl.class)
				.build(GeoEventFactory.class));
		install(new FactoryModuleBuilder()
				.implement(	GeoManager.class,
							Names.named("Direct"),
							AbstractAbsoluteGeoManager.class)
				.implement(	ConfigurableGeoManager.class,
							Names.named("Line"),
							GeoManagerLine.class)
				.implement(	LayoutPropertyLine.class,
							LayoutPropertyLineImpl.class)
				.build(GeoManagerFactory.class));
	}
}
