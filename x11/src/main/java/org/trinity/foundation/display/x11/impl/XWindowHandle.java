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
package org.trinity.foundation.display.x11.impl;

import com.google.auto.value.AutoValue;
import org.trinity.display.api.DisplaySurfaceHandle;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
@AutoValue(cacheHashCode = true)
public abstract class XWindowHandle implements DisplaySurfaceHandle {

    public static DisplaySurfaceHandle create(@Nonnull final Integer nativeHandle) {
        return new AutoValue_XWindowHandle(nativeHandle);
    }

    @Override
    public abstract Integer getNativeHandle();
}