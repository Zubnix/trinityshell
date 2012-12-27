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
package org.trinity.foundation.render.qt.impl.binding;

import java.lang.reflect.Method;

import org.trinity.foundation.api.render.binding.ViewProperty;
import org.trinity.foundation.api.render.binding.ViewSlotInvocationHandler;
import org.trinity.foundation.render.qt.api.QJRenderEngine;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class ViewSlotInvocationHandlerImpl implements ViewSlotInvocationHandler {

	private final QJRenderEngine qjRenderEngine;

	@Inject
	ViewSlotInvocationHandlerImpl(final QJRenderEngine qjRenderEngine) {
		this.qjRenderEngine = qjRenderEngine;
	}

	@Override
	public void invokeSlot(	final Object dataContext,
							final ViewProperty viewProperty,
							final Object view,
							final Method viewSlot,
							final Object argument) {
		this.qjRenderEngine.invoke(	dataContext,
									new InvokeSlotRoutine(	viewProperty,
															view,
															viewSlot,
															argument));
	}
}