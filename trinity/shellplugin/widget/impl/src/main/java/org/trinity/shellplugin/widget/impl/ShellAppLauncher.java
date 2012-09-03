/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shellplugin.widget.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.trinity.foundation.input.api.Keyboard;
import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.api.geo.ShellNodeExecutor;
import org.trinity.shell.api.input.KeyInputStringBuilder;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.widget.ShellWidgetView;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
/**
 * A <code>ShellAppLauncher</code> is a {@link AbstractShellKeyDrivenMenu} for
 * launching applications.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
public class ShellAppLauncher extends AbstractShellKeyDrivenMenu {

	/*****************************************
	 * @param painterFactory
	 * @param shellNodeExecutor
	 * @param managedKeyboard
	 * @param keyInputStringBuilder
	 ****************************************/
	@Inject
	protected ShellAppLauncher(	@Named("shellEventBus") final EventBus shellEventBus,
								final EventBus nodeEventBus,
								final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
								final PainterFactory painterFactory,
								@Named("shellWidgetGeoExecutor") final ShellNodeExecutor shellNodeExecutor,
								final Keyboard keyboard,
								@Named("ShellRootSurface") final ShellSurface root,
								final KeyInputStringBuilder keyInputStringBuilder,
								final ShellWidgetView view) {
		super(	shellEventBus,
				nodeEventBus,
				shellDisplayEventDispatcher,
				painterFactory,
				shellNodeExecutor,
				keyboard,
				root,
				keyInputStringBuilder,
				view);
	}

	private static final String PATH = "PATH";

	/**
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
	public List<String> getAllChoices() {
		final List<String> apps = new ArrayList<String>(250);
		final String pathValue = System.getenv().get(ShellAppLauncher.PATH);
		final StringTokenizer pathParser = new StringTokenizer(	pathValue,
																":");

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
	public String getConfirmChoiceKey() {
		return Keyboard.ENTER;
	}

	@Override
	public String getCancelKey() {
		return Keyboard.ESCAPE;
	}

}