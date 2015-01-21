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

package org.trinity.foundation.api.binding.binding;

import dagger.Module;
import dagger.Provides;
import org.trinity.binding.api.ViewBinder;

import javax.inject.Singleton;

@Module(
        injects = {
                ViewBinder.class,

                CollectionBindingFactory.class,
                EventBindingFactory.class,
                PropertyBindingFactory.class,
                SignalFactory.class,
                ViewBindingsTraverser.class
        },
        library = true,
        complete = false
)
public final class BindingDefaultModule {

    @Provides
    @Singleton
    ViewBinder provideViewBinder(final ViewBinderDefault viewBinderDefault) {
        return viewBinderDefault;
    }

}
