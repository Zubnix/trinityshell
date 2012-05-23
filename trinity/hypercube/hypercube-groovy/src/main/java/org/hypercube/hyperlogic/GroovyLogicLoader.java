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
package org.hypercube.hyperlogic;

import groovy.lang.GroovyObject;

import java.io.File;

import org.hypercube.LogicLoader;
import org.hypercube.hyperlogic.GroovyScriptLoader.ScriptDefinition;
import org.hyperdrive.core.api.ManagedDisplay;
import org.trinity.core.config.api.DisplayConfiguration;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class GroovyLogicLoader implements LogicLoader {
	private GroovyScriptLoader customGroovyScriptLoader;
	private GroovyScriptLoader defaultGroovyScriptLoader;
	private GroovyScriptLoader userGroovyScriptLoader;
	private GroovyScriptLoader buildInGroovyScriptLoader;

	private static final String LOGIC_FILENAME = "HypercubeLogic.groovy";
	private static final String DEFAULT_LOGIC_DIR = "/usr/share/hypercube";
	private static final String USER_CONFIG_DIR = ".config/hypercube";
	private static final String BUILDIN_CLASSPATH = "HypercubeLogic";

	private GroovyObject hypercubeLogic;

	public GroovyLogicLoader() {
		this(null);
	}

	/**
	 * 
	 */
	public GroovyLogicLoader(final String customLogicPath) {
		init(customLogicPath);
	}

	/**
	 * 
	 * @param customLogicPath
	 */
	private void init(final String customLogicPath) {
		if (customLogicPath != null) {
			if (customLogicPath.length() != 0) {
				this.customGroovyScriptLoader = new GroovyScriptLoader(
						customLogicPath, ScriptDefinition.FILEPATH);
			}
		} else {
			final File userScriptFile = new File(String.format("%s/%s/%s",
					System.getProperty("user.home"),
					GroovyLogicLoader.USER_CONFIG_DIR,
					GroovyLogicLoader.LOGIC_FILENAME));
			if (userScriptFile.exists()) {
				this.userGroovyScriptLoader = new GroovyScriptLoader(
						userScriptFile.getAbsolutePath(),
						ScriptDefinition.FILEPATH);
				return;
			}

			final File defaultScriptFile = new File(String.format("%s/%s",
					GroovyLogicLoader.DEFAULT_LOGIC_DIR,
					GroovyLogicLoader.LOGIC_FILENAME));
			if (defaultScriptFile.exists()) {
				this.defaultGroovyScriptLoader = new GroovyScriptLoader(
						defaultScriptFile.getAbsolutePath(),
						ScriptDefinition.FILEPATH);
				return;
			}

			this.buildInGroovyScriptLoader = new GroovyScriptLoader(
					GroovyLogicLoader.BUILDIN_CLASSPATH,
					ScriptDefinition.CLASSPATH);
		}
	}

	@Override
	public void loadLogic() {
		if (this.customGroovyScriptLoader != null) {
			this.hypercubeLogic = this.customGroovyScriptLoader.loadScript();

		} else if (this.userGroovyScriptLoader != null) {
			this.hypercubeLogic = this.userGroovyScriptLoader.loadScript();

		} else if (this.defaultGroovyScriptLoader != null) {
			this.hypercubeLogic = this.defaultGroovyScriptLoader.loadScript();

		} else {
			this.hypercubeLogic = this.buildInGroovyScriptLoader.loadScript();

		}
	}

	@Override
	public void postInit(final ManagedDisplay managedDisplay) {
		final Object[] args = { managedDisplay };
		this.hypercubeLogic.invokeMethod("postInit", args);
	}

	@Override
	public void preInit(final DisplayConfiguration displayConfiguration) {
		final Object[] args = { displayConfiguration };
		this.hypercubeLogic.invokeMethod("preInit", args);
	}
}
