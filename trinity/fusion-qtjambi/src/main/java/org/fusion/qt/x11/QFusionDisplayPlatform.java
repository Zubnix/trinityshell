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

import java.util.Map;

import org.fusion.qt.paintengine.impl.QFusionEventProducerFactory;
import org.fusion.qt.painter.QFusionPainterFactoryProvider;
import org.fusion.x11.core.XCoreInterfaceProvider;
import org.fusion.x11.core.XDisplayPlatform;

/**
 * A <code>QFusionDisplayPlatform</code> provides a
 * <code>QFusionPainterFactoryProvider</code>. This allows for a separate
 * QtJambi based toolkit to construct <code>Painter</code>s for
 * <code>Paintable</code>s that where constructed using a
 * <code>QFusionDisplayPlatform</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class QFusionDisplayPlatform extends XDisplayPlatform {

	/**
	 * 
	 * @param backEndProperties
	 * @param xCoreInterfaceProvider
	 */
	public QFusionDisplayPlatform(final Map<String, String> backEndProperties,
			final XCoreInterfaceProvider xCoreInterfaceProvider) {
		super(xCoreInterfaceProvider);
		addEventProducerFactory(new QFusionEventProducerFactory());
	}
}
