/*
 * This file is part of Fusion-X11.
 * 
 * Fusion-X11 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.x11.xTestEnvironment;

import java.io.File;
import java.io.IOException;

import org.fusion.x11.nativeHelpers.FusionNativeLibLoader;
import org.hydrogen.utils.ClasspathResourceLoader;
import org.hydrogen.utils.ProcessMonitor;

/**
 * Test framework code. Currently unused.
 * <p>
 * 
 * @author Erik De Rijcke
 * 
 */
public final class XTestEnvironment {
	private static boolean nativeLibsLoaded = false;

	public static void loadNativeLibs() throws IOException {
		if (!XTestEnvironment.nativeLibsLoaded) {
			FusionNativeLibLoader.loadNativeFusionXcb();
		}
	}

	private static final String shellPath = "/bin/bash";
	private static final String startSciptName = "startXTestEnvironment.sh";
	private static final String stopScriptName = "stopXTestEnvironment.sh";

	private final String displayName;

	public XTestEnvironment(final String displayName) throws IOException {
		XTestEnvironment.loadNativeLibs();
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public ProcessMonitor setUpXTestEnvironment() throws IOException {
		return startShellScipt(XTestEnvironment.startSciptName);
	}

	private ProcessMonitor startShellScipt(final String scriptName)
			throws IOException {
		final ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader(
				"trinity_test_resources");
		final File file = classpathResourceLoader.fromClassPath(scriptName);

		final ProcessBuilder processBuilder = new ProcessBuilder(
				XTestEnvironment.shellPath, file.getAbsolutePath());

		processBuilder.environment().put("DISPLAY", this.displayName);
		final ProcessMonitor monitoredProcess = new ProcessMonitor(
				processBuilder.start(), true, true);
		monitoredProcess.startProcessMonitor();

		return monitoredProcess;
	}

	public ProcessMonitor tearDownXTestEnvironment() throws Exception {
		return startShellScipt(XTestEnvironment.stopScriptName);
	}
}
