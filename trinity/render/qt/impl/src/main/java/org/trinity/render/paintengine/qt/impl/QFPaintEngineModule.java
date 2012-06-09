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
package org.trinity.render.paintengine.qt.impl;

import org.trinity.core.display.api.DisplayEventConverter;
import org.trinity.core.display.api.DisplayEventProducer;
import org.trinity.core.render.api.RenderEngine;
import org.trinity.render.paintengine.qt.api.QFRenderEventBridge;
import org.trinity.render.paintengine.qt.impl.eventconverters.QFButtonPressedConverterImpl;
import org.trinity.render.paintengine.qt.impl.eventconverters.QFButtonReleasedConverterImpl;
import org.trinity.render.paintengine.qt.impl.eventconverters.QFDestroyConverterImpl;
import org.trinity.render.paintengine.qt.impl.eventconverters.QFFocusGainConverterImpl;
import org.trinity.render.paintengine.qt.impl.eventconverters.QFFocusLostConverterImpl;
import org.trinity.render.paintengine.qt.impl.eventconverters.QFKeyPressedConverterImpl;
import org.trinity.render.paintengine.qt.impl.eventconverters.QFKeyReleasedConverterImpl;
import org.trinity.render.paintengine.qt.impl.eventconverters.QFMouseEnterConverterImpl;
import org.trinity.render.paintengine.qt.impl.eventconverters.QFMouseLeaveConverterImpl;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.trolltech.qt.core.QEvent;

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
		// start bind jambi event type instances
		bind(QEvent.Type.class).annotatedWith(Names.named("MouseButtonPress"))
				.toInstance(QEvent.Type.MouseButtonPress);
		bind(QEvent.Type.class)
				.annotatedWith(Names.named("MouseButtonRelease"))
				.toInstance(QEvent.Type.MouseButtonRelease);
		bind(QEvent.Type.class).annotatedWith(Names.named("Close"))
				.toInstance(QEvent.Type.Close);
		bind(QEvent.Type.class).annotatedWith(Names.named("FocusIn"))
				.toInstance(QEvent.Type.FocusIn);
		bind(QEvent.Type.class).annotatedWith(Names.named("FocusOut"))
				.toInstance(QEvent.Type.FocusOut);
		bind(QEvent.Type.class).annotatedWith(Names.named("KeyPress"))
				.toInstance(QEvent.Type.KeyPress);
		bind(QEvent.Type.class).annotatedWith(Names.named("KeyRelease"))
				.toInstance(QEvent.Type.KeyRelease);
		bind(QEvent.Type.class).annotatedWith(Names.named("Enter"))
				.toInstance(QEvent.Type.Enter);
		bind(QEvent.Type.class).annotatedWith(Names.named("Leave"))
				.toInstance(QEvent.Type.Leave);
		// end bind jambi event type instances

		@SuppressWarnings("rawtypes")
		// start add render event conversion bindings
		final Multibinder<DisplayEventConverter> eventConvertMultibinder = Multibinder
				.newSetBinder(binder(), DisplayEventConverter.class);
		eventConvertMultibinder.addBinding()
				.to(QFButtonPressedConverterImpl.class);
		eventConvertMultibinder.addBinding()
				.to(QFButtonReleasedConverterImpl.class);
		eventConvertMultibinder.addBinding().to(QFDestroyConverterImpl.class);
		eventConvertMultibinder.addBinding().to(QFFocusGainConverterImpl.class);
		eventConvertMultibinder.addBinding().to(QFFocusLostConverterImpl.class);
		eventConvertMultibinder.addBinding()
				.to(QFKeyPressedConverterImpl.class);
		eventConvertMultibinder.addBinding()
				.to(QFKeyReleasedConverterImpl.class);
		eventConvertMultibinder.addBinding()
				.to(QFMouseEnterConverterImpl.class);
		eventConvertMultibinder.addBinding()
				.to(QFMouseLeaveConverterImpl.class);
		// end add render event conversion bindings

		// start bindings
		bind(QFRenderEventBridge.class).to(QFRenderEventBridgeImpl.class);
		bind(RenderEngine.class).to(QFRenderEngineImpl.class);
		// end bindings

		// start add event producer binding
		final Multibinder<DisplayEventProducer> eventProcuderMultibinder = Multibinder
				.newSetBinder(binder(), DisplayEventProducer.class);
		eventProcuderMultibinder.addBinding().to(QFRenderEngineImpl.class);
		// end add event producer bindings
	}
}
