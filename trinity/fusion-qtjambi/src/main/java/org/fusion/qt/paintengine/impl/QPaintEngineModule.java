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
package org.fusion.qt.paintengine.impl;

import org.fusion.qt.paintengine.api.QFusionEventConverter;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class QPaintEngineModule extends AbstractModule {

	/*
	 * (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		@SuppressWarnings("rawtypes")
		final Multibinder<QFusionEventConverter> eventConvertMultibinder = Multibinder
				.newSetBinder(binder(), QFusionEventConverter.class);
		eventConvertMultibinder.addBinding()
				.to(QFusionButtonPressedConverter.class);
		eventConvertMultibinder.addBinding()
				.to(QFusionButtonReleasedConverter.class);
		eventConvertMultibinder.addBinding().to(QFusionDestroyConverter.class);
		eventConvertMultibinder.addBinding()
				.to(QFusionFocusGainConverter.class);
		eventConvertMultibinder.addBinding()
				.to(QFusionFocusLostConverter.class);
		eventConvertMultibinder.addBinding()
				.to(QFusionKeyPressedConverter.class);
		eventConvertMultibinder.addBinding()
				.to(QFusionKeyReleasedConverter.class);
		eventConvertMultibinder.addBinding()
				.to(QFusionMouseEnterConverter.class);
		eventConvertMultibinder.addBinding()
				.to(QFusionMouseLeaveConverter.class);

	}
}
