package org.trinity.shellplugin.wm.x11.impl.protocol.icccm;

import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;

public interface ProtocolListener<P> {

	@Subscribe
	void onProtocolChanged(Optional<P> protocol);
}
