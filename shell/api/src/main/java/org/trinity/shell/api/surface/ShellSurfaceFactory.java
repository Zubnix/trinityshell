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
package org.trinity.shell.api.surface;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.ExecutionContext;

import com.google.common.util.concurrent.ListenableFuture;
import org.trinity.shell.api.bindingkey.ShellExecutor;

import javax.annotation.Nonnull;

/***************************************
 * Creates shell surfaces from a display surface and provides access to the root
 * shell surface.
 ***************************************
 */
@ExecutionContext(ShellExecutor.class)
public interface ShellSurfaceFactory {
	/***************************************
	 * Create a new shell surface that is backed by the given display surface.
	 * The new shell surface will have the root shell surface as its parent.
	 *
	 * @param displaySurface
	 *            a {@link DisplaySurface}
	 * @return a new future {@link ShellSurface}.
	 ***************************************
	 */
	ListenableFuture<ShellSurface> createShellClientSurface(@Nonnull DisplaySurface displaySurface);
}