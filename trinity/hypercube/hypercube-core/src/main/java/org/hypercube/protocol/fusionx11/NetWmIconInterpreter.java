package org.hypercube.protocol.fusionx11;

import org.fusion.x11.ewmh._NetWmIconInstance;
import org.fusion.x11.ewmh._NetWmIconInstance.WmIcon;
import org.hyperdrive.core.api.RenderArea;
import org.hyperdrive.protocol.ClientIcon;
import org.hyperdrive.protocol.ClientIcon.IconFormat;

final class NetWmIconInterpreter {

	private final XDesktopProtocol xDesktopProtocol;

	NetWmIconInterpreter(final XDesktopProtocol xDesktopProtocol) {
		this.xDesktopProtocol = xDesktopProtocol;
	}

	void handleWmIcon(final RenderArea client,
			final _NetWmIconInstance wmIconInstance) {
		final WmIcon[] icons = wmIconInstance.getWmIcons();
		if (icons.length == 0) {
			return;
		}

		// find the biggest icon
		int width = 0;
		int biggestIconIdx = 0;
		for (int i = 0; i < icons.length; i++) {
			if (icons[i].getWidth() > width) {
				biggestIconIdx = i;
				width = icons[i].getWidth();
			}
		}

		final WmIcon biggestIcon = icons[biggestIconIdx];
		final byte[] iconPixels = biggestIcon.getArgbPixels();

		final int height = biggestIcon.getHeight();

		final ClientIcon newClientIcon = new ClientIcon(iconPixels, width,
				height, IconFormat.ARGB);
		this.xDesktopProtocol.updateProtocolEvent(client, newClientIcon);
	}
}
