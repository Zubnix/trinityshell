package org.hyperdrive.api.widget;

import org.hyperdrive.api.core.RenderArea;
import org.hyperdrive.api.geo.GeoOperation;

public interface ClientManager extends Widget {

	public interface View extends Widget.View {
		PaintInstruction<Void> onManageClient(RenderArea client);

		PaintInstruction<Void> onUnmanageClient(RenderArea client);

		PaintInstruction<Void> onClientStateChanged(RenderArea client,
				GeoOperation geoOperation);
	}

	@Override
	public View getView();

	void unmanageClient(RenderArea client);

	void manageClient(final RenderArea client);

	RenderArea[] getManagedClients();
}
