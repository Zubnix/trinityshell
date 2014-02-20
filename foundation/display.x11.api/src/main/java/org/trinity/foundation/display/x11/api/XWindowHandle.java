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
package org.trinity.foundation.display.x11.api;

import org.trinity.foundation.api.display.DisplaySurfaceHandle;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Preconditions.checkNotNull;

@Immutable
public class XWindowHandle implements DisplaySurfaceHandle {

    private final Integer nativeHandle;

    public XWindowHandle(@Nonnull final Integer nativeHandle) {
        this.nativeHandle = checkNotNull(nativeHandle);
    }

    @Override
    public Integer getNativeHandle() {
        return this.nativeHandle;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof DisplaySurfaceHandle) {
            final DisplaySurfaceHandle otherObj = (DisplaySurfaceHandle) obj;
            return otherObj.getNativeHandle().equals(getNativeHandle());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getNativeHandle().intValue();
    }
}
