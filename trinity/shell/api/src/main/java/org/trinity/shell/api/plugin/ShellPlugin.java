/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.api.plugin;

import org.trinity.shell.api.Module;

/*****************************************
 * General interface for every shell plugin. Every shell plugin has a
 * <code>start()</code> and <code>stop()</code> method that is called by the
 * shell when a plugin is started and stopped respectively. Calls to
 * {@code start()} and {@code stop} should be non-blocking and should not spawn
 * any additional threads unless absolutely necessary. Instead a shell plugin
 * should hook into the shell thread itself. Should a shell plugin start a new
 * thread then this thread should not call any objects that live outside it's
 * own shell plugin. This keeps for a more thread safe and more predictable
 * behavior of shell plugins.
 * 
 * @see {@link Module}.
 * 
 ****************************************/
public interface ShellPlugin {
	/***************************************
	 * Start the plugin. This method should not block.
	 *************************************** 
	 */
	void start();

	/***************************************
	 * Stop the plugin. This method should not block.
	 *************************************** 
	 */
	void stop();
}
