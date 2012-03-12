/*
 * This file is part of Fusion-qtjambi.
 * 
 * Fusion-qtjambi is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Fusion-qtjambi is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-qtjambi. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.qt.x11;

import org.fusion.x11.core.xcb.XcbCoreInterfaceProvider;
import org.hydrogen.api.display.DisplayPlatform;
import org.hydrogen.config.BaseDisplayConfiguration;

/**
 * A <code>QFusionDisplayConfiguration</code> defines the configuration needed
 * by a Q Fusion paint implementation. It defines a
 * <code>QFusionDisplayPlatform</code> and a given unique X display name.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class QFusionDisplayConfiguration extends BaseDisplayConfiguration {

	/**
	 * Valid values are: "software", "raster", "opengl", "opengl2".
	 * <p>
	 * Default is "raster".
	 */
	public static final String GRAPHICS_SYSTEM = "qfusion.graphicssystem";
	public static final String DISPLAY = "qfusion.display";
	public static final String STYLE_SHEET = "qfusion.stylesheet";

	/**
     *
     */
	public QFusionDisplayConfiguration() {
		getBackEndProperties().put(QFusionDisplayConfiguration.GRAPHICS_SYSTEM,
				"raster");
	}

	@Override
	public DisplayPlatform initNewDisplayPlatform() {
		for (final Runnable configPerform : getConfigPerforms()) {
			configPerform.run();
		}
		return new QFusionDisplayPlatform(getBackEndProperties(),
				new XcbCoreInterfaceProvider());

	}

}
