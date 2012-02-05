package org.fusion.swing.x11;

import org.fusion.swing.painter.SwingFusionPainterFactoryProvider;
import org.fusion.x11.core.XDisplayPlatform;
import org.fusion.x11.core.xcb.XcbCoreInterfaceProvider;
import org.hydrogen.config.BaseDisplayConfiguration;
import org.hydrogen.displayinterface.DisplayPlatform;

public class SwingFusionDisplayConfiguration extends BaseDisplayConfiguration {

	@Override
	public DisplayPlatform initNewDisplayPlatform() {
		return new XDisplayPlatform(new SwingFusionPainterFactoryProvider(),
		                            new XcbCoreInterfaceProvider());
	}

}
