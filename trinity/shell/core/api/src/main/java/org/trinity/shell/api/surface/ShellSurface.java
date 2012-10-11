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

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.shell.api.node.ShellNode;

public interface ShellSurface extends ShellNode {
	@Override
	ShellSurfaceExecutor getShellNodeExecutor();

	int getHeightIncrement();

	int getMaxHeight();

	int getMaxWidth();

	int getMinHeight();

	int getMinWidth();

	DisplaySurface getDisplaySurface();

	int getWidthIncrement();

	boolean isMovable();

	boolean isResizable();

	void setHeightIncrement(final int heightIncrement);

	void setMaxHeight(final int maxHeight);

	void setMaxWidth(final int maxWidth);

	void setMinHeight(final int minHeight);

	void setMinWidth(final int minWidth);

	void setMovable(final boolean movable);

	void setResizable(final boolean isResizable);

	void setWidthIncrement(final int widthIncrement);

	void syncGeoToDisplaySurface();

	void setInputFocus();
}
