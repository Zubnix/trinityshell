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
package org.fusion.paintengine.impl;

import org.fusion.paintengine.impl.eventconverters.QFButtonPressedConverterImpl;
import org.fusion.paintengine.impl.eventconverters.QFButtonReleasedConverterImpl;
import org.fusion.paintengine.impl.eventconverters.QFDestroyConverterImpl;
import org.fusion.paintengine.impl.eventconverters.QFFocusGainConverterImpl;
import org.fusion.paintengine.impl.eventconverters.QFFocusLostConverterImpl;
import org.fusion.paintengine.impl.eventconverters.QFKeyPressedConverterImpl;
import org.fusion.paintengine.impl.eventconverters.QFKeyReleasedConverterImpl;
import org.fusion.paintengine.impl.eventconverters.QFMouseEnterConverterImpl;
import org.fusion.paintengine.impl.eventconverters.QFMouseLeaveConverterImpl;
import org.hydrogen.display.api.EventProducer;
import org.hydrogen.paint.api.RenderEventConverter;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class QFPaintEngineModule extends AbstractModule {

	/*
	 * (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		@SuppressWarnings("rawtypes")
		// add event conversion bindings
		final Multibinder<RenderEventConverter> eventConvertMultibinder = Multibinder
				.newSetBinder(binder(), RenderEventConverter.class);
		eventConvertMultibinder.addBinding()
				.to(QFButtonPressedConverterImpl.class);
		eventConvertMultibinder.addBinding()
				.to(QFButtonReleasedConverterImpl.class);
		eventConvertMultibinder.addBinding().to(QFDestroyConverterImpl.class);
		eventConvertMultibinder.addBinding()
				.to(QFFocusGainConverterImpl.class);
		eventConvertMultibinder.addBinding()
				.to(QFFocusLostConverterImpl.class);
		eventConvertMultibinder.addBinding()
				.to(QFKeyPressedConverterImpl.class);
		eventConvertMultibinder.addBinding()
				.to(QFKeyReleasedConverterImpl.class);
		eventConvertMultibinder.addBinding()
				.to(QFMouseEnterConverterImpl.class);
		eventConvertMultibinder.addBinding()
				.to(QFMouseLeaveConverterImpl.class);

		// add event producer binding
		final Multibinder<EventProducer> eventProcuderMultibinder = Multibinder
				.newSetBinder(binder(), EventProducer.class);
		eventProcuderMultibinder.addBinding()
				.to(QFRenderEventBridgeImpl.class);

	}
}
