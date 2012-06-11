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
package org.trinity.display.x11.impl.xcb.eventconverters;

import org.trinity.display.x11.api.XEventConverter;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class XcbEventModule extends AbstractModule {

	/*
	 * (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		@SuppressWarnings("rawtypes")
		final Multibinder<XEventConverter> eventConvertMultibinder = Multibinder
				.newSetBinder(binder(), XEventConverter.class);
	}
}
