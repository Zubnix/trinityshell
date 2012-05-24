/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.widget.api;

import org.trinity.core.render.api.PaintInstruction;
import org.trinity.shell.foundation.api.RenderArea;
import org.trinity.shell.geo.api.GeoOperation;

public interface ClientManager extends Widget {

	public interface View extends Widget.View {
		PaintInstruction<?> onManageClient(RenderArea client);

		PaintInstruction<?> onUnmanageClient(RenderArea client);

		PaintInstruction<?> onClientStateChanged(	RenderArea client,
													GeoOperation geoOperation);
	}

	void unmanageClient(RenderArea client);

	void manageClient(final RenderArea client);

	RenderArea[] getManagedClients();
}
