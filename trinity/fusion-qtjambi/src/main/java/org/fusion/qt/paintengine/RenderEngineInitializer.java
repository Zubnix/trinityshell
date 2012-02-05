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
package org.fusion.qt.paintengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.fusion.qt.error.RenderEngineQuitError;
import org.fusion.qt.error.RenderEngineTimeOutError;
import org.hydrogen.displayinterface.Display;

import com.trolltech.qt.gui.QApplication;

/**
 * A <code>RenderEngineInitializer</code> initializes a
 * <code>QFusionRenderEngine</code>. After a <code>QFusionRenderEngine</code> is
 * started, it is made available through a thread-safe get method
 * <code>getRenderEngine</code>. This getter method will block until the
 * <code>QFusionRenderEngine</code> is started.
 * <p>
 * The <code>QFusionRenderEngine</code> will be constructed when the
 * <code>run()</code> method is called on the
 * <code>RenderEngineInitializer</code>. The thread calling the run method will
 * be the thread that runs the gui loop. This thread will thus only return from
 * the <code>run()</code> call after the gui engine has been shut down.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class RenderEngineInitializer implements Runnable {
	private static final Logger LOGGER = Logger
			.getLogger(RenderEngineInitializer.class);
	private static final String RENDERENGINE_TIMEOUT_LOGMESSAGE = "Timeout while initializing render engine.";
	private static final String RENDERENGINE_NONZERO_EXIT_LOGMESSAGE = "Render engine terminated with a non-zero exit status.";
	private static final String RENDERENGINE_CRASH_LOGMESSAGE = "The render engine threw an unknown exception. Exiting JVM.";

	private static final long INIT_TIMEOUT = 120;
	private static final String GUI_THREAD_NAME = "GUI Thread";
	private static int threadNr = 0;

	private final Display display;
	private QFusionRenderEngine renderEngine;
	private final BlockingQueue<QFusionRenderEngine> renderEngineContainer;
	private final Map<String, String> backEndProperties;

	/**
	 * Create a new runnable <code>RenderEngineInitializer</code> for the given
	 * <code>Display</code>. The back properties will be used by the
	 * <code>QFusionRenderEngine</code> upon initialization.
	 * 
	 * @param display
	 * @param backEndProperties
	 */
	public RenderEngineInitializer(final Display display,
			final Map<String, String> backEndProperties) {
		this.backEndProperties = backEndProperties;
		this.display = display;
		this.renderEngineContainer = new ArrayBlockingQueue<QFusionRenderEngine>(
				1);
	}

	/**
	 * Return the initialized <code>QFusionRenderEngine</code>. This method will
	 * block until the engine is available.
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public QFusionRenderEngine getRenderEngine() throws InterruptedException {
		if (this.renderEngine == null) {
			this.renderEngine = this.renderEngineContainer.poll(
					RenderEngineInitializer.INIT_TIMEOUT, TimeUnit.SECONDS);
		}

		if (this.renderEngine == null) {
			RenderEngineTimeOutError ex = new RenderEngineTimeOutError(
					RenderEngineInitializer.INIT_TIMEOUT, TimeUnit.SECONDS);

			LOGGER.fatal(RENDERENGINE_TIMEOUT_LOGMESSAGE, ex);

			throw ex;
		}

		return this.renderEngine;
	}

	/**
	 * Create a new <code>QFusionRenderEngine</code>. The
	 * <code>QFusionRenderEngine</code> will be constructed with the arguments
	 * passed to this <code>RenderEngineInitializer</code> constructor.
	 * 
	 * @return
	 */
	protected QFusionRenderEngine newQFusionRenderEngine() {
		// construct new String[] args from back-end properties.
		return new QFusionRenderEngine(this.display, constructArgs());
	}

	private String[] constructArgs() {
		final List<String> argsList = new ArrayList<String>();
		final String validArgKey = "qfusion";
		// flag indicator. eg. the "-" from the "-display" flag.
		final String flagIndicator = "-";
		for (final Entry<String, String> property : this.backEndProperties
				.entrySet()) {
			final String[] argKey = property.getKey().split("\\.");

			if (argKey[0].equals(validArgKey)) {
				final String flag = argKey[1];
				final String arg = property.getValue();
				argsList.add(flagIndicator + flag);
				argsList.add(arg);
			}
		}
		final String[] args = argsList.toArray(new String[argsList.size()]);
		return args;
	}

	@Override
	public void run() {
		try {
			Thread.currentThread().setName(
					String.format("%s %d",
							RenderEngineInitializer.GUI_THREAD_NAME,
							RenderEngineInitializer.threadNr++));

			this.renderEngineContainer.add(newQFusionRenderEngine());
			QApplication.setQuitOnLastWindowClosed(false);
			final int exitCode = QApplication.exec();

			if (exitCode != 0) {
				RenderEngineQuitError ex = new RenderEngineQuitError(exitCode);

				LOGGER.fatal(RENDERENGINE_NONZERO_EXIT_LOGMESSAGE, ex);

				throw ex;
			}
		} catch (final Throwable e) {
			LOGGER.fatal(RENDERENGINE_CRASH_LOGMESSAGE, e);
			System.exit(1);
		}
	}
}
