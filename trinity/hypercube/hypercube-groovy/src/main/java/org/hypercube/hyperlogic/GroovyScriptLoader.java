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

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.apache.log4j.Logger;
import org.hypercube.error.ConfigurationScriptInitialziationError;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class GroovyScriptLoader {
	private final Logger logger = Logger.getLogger(GroovyScriptLoader.class);
	private static final String SCRIPT_INIT_LOGMESSAGE = "Error while trying to load script.";

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 */
	public enum ScriptDefinition {
		CLASSPATH, FILEPATH
	}

	private final GroovyClassLoader loader;
	private final String script;
	private final ScriptDefinition scriptType;

	/**
	 * 
	 * @param script
	 * @param scriptType
	 */
	public GroovyScriptLoader(final String script,
			final ScriptDefinition scriptType) {
		this.scriptType = scriptType;
		this.script = script;
		final ClassLoader parentClassLoader = this.getClass().getClassLoader();
		this.loader = AccessController
				.doPrivileged(new PrivilegedAction<GroovyClassLoader>() {
					@Override
					public GroovyClassLoader run() {

						final GroovyClassLoader returnGroovyClassLoader = new GroovyClassLoader(
								parentClassLoader);

						return returnGroovyClassLoader;
					}
				});
	}

	/**
	 * 
	 * @return
	 * @throws HypercubeException
	 */
	public GroovyObject loadScript() {
		try {
			GroovyObject scriptObject = null;

			Class<?> groovyClass = null;
			if (this.scriptType == ScriptDefinition.FILEPATH) {
				final File logicFile = new File(this.script);
				if (logicFile.exists()) {
					groovyClass = this.loader.parseClass(logicFile);
				}
			} else if (this.scriptType == ScriptDefinition.CLASSPATH) {
				groovyClass = this.loader.loadClass(this.script);
			}
			if (groovyClass == null) {
				ConfigurationScriptInitialziationError ex = new ConfigurationScriptInitialziationError(
						String.format(
								"No suitable groovy class was found. Looked in %s: %s",
								this.scriptType.toString(), this.script));
				logger.fatal(SCRIPT_INIT_LOGMESSAGE, ex);

				// TODO sytem exit instead of throw?
				throw ex;
			}
			scriptObject = (GroovyObject) groovyClass.newInstance();

			if (scriptObject == null) {
				ConfigurationScriptInitialziationError ex = new ConfigurationScriptInitialziationError(
						"Created instance from the found groovy class is null!");

				logger.fatal(SCRIPT_INIT_LOGMESSAGE, ex);

				// TODO sytem exit instead of throw?
				throw ex;
			}
			return scriptObject;
		} catch (IOException e) {
			throw new ConfigurationScriptInitialziationError(e);
		} catch (ClassNotFoundException e) {
			throw new ConfigurationScriptInitialziationError(e);
		} catch (InstantiationException e) {
			throw new ConfigurationScriptInitialziationError(e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationScriptInitialziationError(e);
		}
	}
}
