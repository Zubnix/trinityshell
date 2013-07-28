/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

package org.trinity.foundation.api.render.binding;

import org.apache.onami.autobind.annotations.GuiceModule;
import org.trinity.foundation.api.render.binding.model.PropertyChanged;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

@GuiceModule
class Module extends AbstractModule {

	@Override
	protected void configure() {
		final PropertyChangedSignalDispatcher viewSignalDispatcher = new PropertyChangedSignalDispatcher();
		requestInjection(viewSignalDispatcher);
		bindInterceptor(Matchers.any(),
						Matchers.annotatedWith(PropertyChanged.class),
						viewSignalDispatcher);
	}
}
