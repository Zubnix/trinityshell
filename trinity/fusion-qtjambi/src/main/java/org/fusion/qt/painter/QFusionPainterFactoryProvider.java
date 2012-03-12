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
package org.fusion.qt.painter;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.fusion.qt.error.InterruptedPaintResourceInitialization;
import org.fusion.qt.paintengine.QFusionRenderEngine;
import org.fusion.qt.paintengine.RenderEngineInitializer;
import org.hydrogen.api.display.Display;
import org.hydrogen.api.paint.PainterFactoryProvider;

/**
 * A <code>QFusionPainterFactoryProvider</code> is responsible for creating a
 * <code>QFusionPainterFactory</code>. A
 * <code>QFusionPainterFactoryProvider</code> is used by a
 * <code>QFusionDisplayPlatform</code> to provide a visual implementation.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class QFusionPainterFactoryProvider implements PainterFactoryProvider {

	private static final Logger LOGGER = Logger
			.getLogger(QFusionPainterFactoryProvider.class);
	private static final String PAINTERFACTORY_ERROR_MESSAGE = "Error while initializing painter factory resource.";

	private final ExecutorService renderEnginesExecutor;

	private final Map<String, String> backEndProperties;

	/**
	 * 
	 * @param backEndProperties
	 */
	public QFusionPainterFactoryProvider(
			final Map<String, String> backEndProperties) {
		this.renderEnginesExecutor = Executors.newCachedThreadPool();
		this.backEndProperties = backEndProperties;
	}

	/**
	 * The <code>ExecutorService</code> used by this
	 * <code>QFusionPainterFactoryProvider</code>. The returned
	 * <code>ExecutorService</code> will launch and run the GUI
	 * <code>Thread</code> used by a new <code>QFusionPainterFactory</code> that
	 * was created by this <code>QFusionPainterFactoryProvider</code>.
	 * 
	 * @return An <code>ExecutorService</code>.
	 */
	protected ExecutorService getRenderEnginesExecutor() {
		return this.renderEnginesExecutor;
	}

	@Override
	public QFusionPainterFactory newPainterFactory(final Display display) {
		final RenderEngineInitializer renderEngineInitializer = new RenderEngineInitializer(
				display, this.backEndProperties);
		getRenderEnginesExecutor().submit(renderEngineInitializer);

		QFusionRenderEngine renderEngine = null;
		while (renderEngine == null) {
			try {
				renderEngine = renderEngineInitializer.getRenderEngine();
			} catch (InterruptedException e) {
				InterruptedPaintResourceInitialization ex = new InterruptedPaintResourceInitialization(
						e);

				LOGGER.error(PAINTERFACTORY_ERROR_MESSAGE, ex);

				throw ex;
			}
		}

		final QFusionPainterFactory returnQFusionPainterFactory = new QFusionPainterFactory(
				renderEngine);

		return returnQFusionPainterFactory;
	}
}
