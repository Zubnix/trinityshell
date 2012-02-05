/*
 * This file is part of Hypercube.
 * 
 * Hypercube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hypercube is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hypercube. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hypercube;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.fusion.qt.x11.QFusionDisplayConfiguration;
import org.hydrogen.config.DisplayConfiguration;
import org.hyperdrive.core.DisplayManager;
import org.hyperdrive.core.ManagedDisplay;
import org.hyperdrive.widget.ViewFactory;

import com.martiansoftware.jsap.JSAPException;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class Hypercube {

	private final ArgumentParser argumentParser;
	private final LogicLoaderFactory logicLoaderFactory;

	// TODO read classnames from configfile
	/**
	 * Create a new <code>Hypercube</code> with the given arguments. Use static
	 * factory method <code> METHOD HERE </code>
	 * 
	 * @deprecated relies on naming class names instead of the type argument
	 * 
	 * @param logicLoaderFactoryClassName
	 * @param viewFactoryClassName
	 * @param arguments
	 *            A number of arguments denoted with a long or short argument
	 *            flag
	 * @throws JSAPException
	 *             Thrown when an illegal argument is given.
	 * @throws URISyntaxException
	 */
	@Deprecated
	public Hypercube(final String displayConfigurationClassName,
			final String logicLoaderFactoryClassName,
			final String viewFactoryClassName, final String[] arguments)
			throws JSAPException,

			InstantiationException, IllegalAccessException,
			ClassNotFoundException, URISyntaxException {

		// TODO pass class as parameter
		this.logicLoaderFactory = (LogicLoaderFactory) Class.forName(
				logicLoaderFactoryClassName).newInstance();

		this.argumentParser = new ArgumentParser(arguments);
		startLocal(displayConfigurationClassName, viewFactoryClassName);
	}

	/**
	 * Creates a new <code> HyperCube </code>
	 * 
	 * @param logicLoaderFactory
	 *            An instance of a LogicLoaderFactory
	 * @param displayConfiguration
	 *            An instance of a DisplayConfiguration
	 * @param viewFactory
	 *            An instance of ViewFactory with any type parameters excepted
	 * @param arguments
	 *            The arguments used in the HyperCube creation
	 * @throws JSAPException
	 * @throws URISyntaxException
	 */

	public Hypercube(final LogicLoaderFactory logicLoaderFactory,
			final DisplayConfiguration displayConfiguration,
			final ViewFactory<?> viewFactory, final String[] arguments)
			throws JSAPException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, URISyntaxException {

		this.logicLoaderFactory = logicLoaderFactory;

		this.argumentParser = new ArgumentParser(arguments);
		startLocal(displayConfiguration, viewFactory);
	}

	/**
	 * Starts a local Hypercube instance from the names of classes.
	 * 
	 * @throws HyperdriveException
	 *             Thrown when the Hyperdrive library encounters an exception. @
	 *             Thrown when the display back end encounters an exception.
	 * @throws HypercubeException
	 *             Thrown when Hypercube encounters an exception.
	 * @throws JSAPException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws URISyntaxException
	 */
	public static Hypercube getInstanceFromClassNames(
			final String displayConfigurationClassName,
			final String logicLoaderFactoryClassName,
			final String viewFactoryClassName, final String[] arguments)
			throws JSAPException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, URISyntaxException {

		final LogicLoaderFactory llf = (LogicLoaderFactory) Class.forName(
				displayConfigurationClassName).newInstance();
		final DisplayConfiguration dc = (DisplayConfiguration) Class.forName(
				displayConfigurationClassName).newInstance();
		final ViewFactory<?> vf = (ViewFactory<?>) Class.forName(
				viewFactoryClassName).newInstance();

		final Hypercube hc = new Hypercube(llf, dc, vf, arguments);

		return hc;
	}

	private void startLocal(final String displayConfigurationClassName,
			final String viewFactoryClassName) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, URISyntaxException {
		// Creates an object of each class name, then calls the start local for
		// object instances
		final DisplayConfiguration displayConfiguration = (DisplayConfiguration) Class
				.forName(displayConfigurationClassName).newInstance();
		final ViewFactory<?> viewFactory = (ViewFactory<?>) Class.forName(
				viewFactoryClassName).newInstance();
		startLocal(displayConfiguration, viewFactory);
	}

	/**
	 * Start a local Hypercube instance.
	 * 
	 * @param dc
	 *            An instance of a DisplayConfiguration used to configure the
	 *            Hypercube's display
	 * @param viewFactory
	 *            An instance of a ViewFactory used to construct the Hypercube's
	 *            view
	 * @throws URISyntaxException
	 * 
	 */
	private void startLocal(final DisplayConfiguration dc,
			final ViewFactory<?> viewFactory) throws URISyntaxException {

		final LogicLoader logicLoader = this.logicLoaderFactory
				.newLogicLoader();
		logicLoader.loadLogic();

		final String display = this.argumentParser.getDisplayName();

		// general properties
		dc.getBackEndProperties().put(DisplayConfiguration.DISPLAY, display);

		// qfusion specific properties
		String buildInStyleSheetPath = "";

		buildInStyleSheetPath = getBuildInStyleSheetPath();

		// TODO initialize these back-end properties in a qfusion<->hypercube
		// decoupled way.
		dc.getBackEndProperties().put(QFusionDisplayConfiguration.STYLE_SHEET,
				buildInStyleSheetPath);
		dc.getBackEndProperties().put(QFusionDisplayConfiguration.DISPLAY,
				display);

		logicLoader.preInit(dc);

		final ManagedDisplay managedDisplay = DisplayManager.instance()
				.getNewManagedDisplay(dc, viewFactory);

		logicLoader.postInit(managedDisplay);
	}

	/**
	 * Used to get the location of this HyperCube's style sheet
	 * 
	 * @return the absolute path of the HyperCube's style sheet
	 * @throws URISyntaxException
	 */
	private String getBuildInStyleSheetPath() throws URISyntaxException {

		final URL styleUrl = this.getClass().getClassLoader()
				.getResource("theme/style.qss");
		if (styleUrl == null) {
			return "";
		}
		final File styleFile = new File(styleUrl.toURI());

		return styleFile.getAbsolutePath();
	}
}
