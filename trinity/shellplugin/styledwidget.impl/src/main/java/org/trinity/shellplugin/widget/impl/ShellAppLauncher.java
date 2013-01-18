package org.trinity.shellplugin.widget.impl;
// /*
// * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
// * This program is free software: you can redistribute it and/or modify it
// under
// * the terms of the GNU General Public License as published by the Free
// Software
// * Foundation, either version 3 of the License, or (at your option) any later
// * version. This program is distributed in the hope that it will be useful,
// but
// * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
// or
// * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more
// * details. You should have received a copy of the GNU General Public License
// * along with this program. If not, see <http://www.gnu.org/licenses/>.
// */
// package org.trinity.shellplugin.widget.impl;
//
// import java.io.File;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;
// import java.util.StringTokenizer;
//
// import org.trinity.foundation.input.api.Keyboard;
// import org.trinity.foundation.render.api.PainterFactory;
// import org.trinity.shell.api.input.ShellKeyInputStringBuilder;
// import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
// import org.trinity.shell.api.surface.ShellSurface;
// import org.trinity.shell.api.widget.ShellWidgetView;
//
// import com.google.common.eventbus.EventBus;
// import com.google.inject.Inject;
// import com.google.inject.name.Named;
//
// import de.devsurf.injection.guice.annotations.Bind;
//
// // TODO documentation
// /**
// * A <code>ShellAppLauncher</code> is a {@link AbstractShellKeyDrivenMenu} for
// * launching applications.
// *
// * @author Erik De Rijcke
// * @since 1.0
// */
// @Bind
// public class ShellAppLauncher extends AbstractShellKeyDrivenMenu {
//
// @Inject
// protected ShellAppLauncher( @Named("shellEventBus") final EventBus
// shellEventBus,
// final EventBus nodeEventBus,
// final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
// final PainterFactory painterFactory,
// final Keyboard keyboard,
// @Named("ShellRootSurface") final ShellSurface root,
// final ShellKeyInputStringBuilder keyInputStringBuilder,
// final ShellWidgetView view) {
// super( shellEventBus,
// nodeEventBus,
// shellDisplayEventDispatcher,
// painterFactory,
// keyboard,
// root,
// keyInputStringBuilder,
// view);
// }
//
// private static final String PATH = "PATH";
//
// /**
// * @param pathName
// * @return
// */
// private List<String> getAppNamesFromPathName(final String pathName) {
// final File path = new File(pathName);
// final File[] appPaths = path.listFiles();
// final List<String> appNames = new ArrayList<String>(appPaths.length);
// for (final File appPath : appPaths) {
// final String appName = appPath.getName();
// appNames.add(appName);
// }
// return appNames;
// }
//
// @Override
// public List<String> getAllChoices() {
// final List<String> apps = new ArrayList<String>(250);
// final String pathValue = System.getenv().get(ShellAppLauncher.PATH);
// final StringTokenizer pathParser = new StringTokenizer( pathValue,
// ":");
//
// while (pathParser.hasMoreTokens()) {
// final String pathName = pathParser.nextToken();
// apps.addAll(getAppNamesFromPathName(pathName));
// }
// Collections.sort(apps);
//
// return apps;
// }
//
// @Override
// public void choiceConfirmed(final String choice) {
// try {
// Runtime.getRuntime().exec(choice);
// } catch (final IOException e) {
// throw new IllegalArgumentException(e);
// }
// }
//
// @Override
// public String getConfirmChoiceKey() {
// return Keyboard.ENTER;
// }
//
// @Override
// public String getCancelKey() {
// return Keyboard.ESCAPE;
// }
//
// }