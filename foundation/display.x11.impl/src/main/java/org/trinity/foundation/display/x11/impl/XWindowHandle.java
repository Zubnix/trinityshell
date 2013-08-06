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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import static com.google.common.base.Preconditions.checkNotNull;

@ExecutionContext(DisplayExecutor.class)
@Immutable
public class XWindowHandle implements DisplaySurfaceHandle {

    private final Integer nativeHandle;

    @Inject
    XWindowHandle(@Nonnull @Assisted final Object nativeHandle) {
        checkNotNull(nativeHandle);

        if (nativeHandle instanceof Integer) {
            this.nativeHandle = (Integer) nativeHandle;
        } else {
            throw new Error("Can only handle handle native X window handle of type 'Integer'. Got native handle of type: "
                    + nativeHandle.getClass().getName());
        }
    }

    @Override
    public Integer getNativeHandle() {
        return this.nativeHandle;
    }

    @Override
    public boolean equals(final Object obj) {
        if(obj == null){
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
        return getNativeHandle().hashCode();
    }
}
