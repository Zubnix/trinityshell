/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.widget;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.hydrogen.displayinterface.input.SpecialKeyName;

// TODO documentation
/**
 * A <code>KeyDrivenAppLauncher</code> is a {@link KeyDrivenMenu} for launching
 * applications.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class KeyDrivenAppLauncher extends KeyDrivenMenu {

	@ViewDefinition
	public interface View extends KeyDrivenMenu.View {

	}

	private static final String PATH = "PATH";

	/**
	 * 
	 * @param pathName
	 * @return
	 */
	private List<String> getAppNamesFromPathName(final String pathName) {
		final File path = new File(pathName);
		final File[] appPaths = path.listFiles();
		final List<String> appNames = new ArrayList<String>(appPaths.length);
		for (final File appPath : appPaths) {
			final String appName = appPath.getName();
			appNames.add(appName);
		}
		return appNames;
	}

	@Override
	public List<String> getChoices() {
		final List<String> apps = new ArrayList<String>(250);
		final String pathValue = System.getenv().get(KeyDrivenAppLauncher.PATH);
		final StringTokenizer pathParser = new StringTokenizer(pathValue, ":");

		while (pathParser.hasMoreTokens()) {
			final String pathName = pathParser.nextToken();
			apps.addAll(getAppNamesFromPathName(pathName));
		}
		Collections.sort(apps);

		return apps;
	}

	@Override
	public void choiceConfirmed(final String choice) {
		try {
			Runtime.getRuntime().exec(choice);
		} catch (final IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public String getConfirmChoiceKeyName() {
		return SpecialKeyName.ENTER.name();
	}

	@Override
	public String getCancelKeyName() {
		return SpecialKeyName.ESCAPE.name();
	}

}