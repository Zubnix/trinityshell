/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.shell.plugin.impl;

import org.trinity.shell.api.plugin.ShellPlugin;
import org.trinity.shell.api.plugin.ShellPluginsRunner;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Singleton
public class ShellPluginsRunnerImpl implements ShellPluginsRunner {

    private final List<ShellPlugin> shellPlugins;

    @Inject
    ShellPluginsRunnerImpl(final Set<ShellPlugin> shellPlugins) {
        this.shellPlugins = new ArrayList<>(shellPlugins);
    }

    @Override
    public void startAll() {
        for (final ShellPlugin shellPlugin : this.shellPlugins) {
            shellPlugin.startAsync();
        }
    }

    @Override
    public void stopAll() {
        for (final ShellPlugin shellPlugin : this.shellPlugins) {
            shellPlugin.stopAsync();
        }
    }
}
