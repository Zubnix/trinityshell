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
package org.trinity.foundation.render.qt.impl.eventconverters;

import org.trinity.foundation.api.display.input.Momentum;
import org.trinity.foundation.api.render.binding.BindingDiscovery;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;

@Bind(multiple = true)
@To(To.Type.INTERFACES)
@Singleton
public class QJKeyPressedConversion extends AbstractQJKeyConversion {

	@Inject
	QJKeyPressedConversion(final BindingDiscovery bindingDiscovery) {
		super(bindingDiscovery);
	}

	@Override
	public Type getQEventType() {
		return QEvent.Type.KeyPress;
	}

	@Override
	public Momentum getMomemtum() {
		return Momentum.STARTED;
	}
}