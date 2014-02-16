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
package org.trinity.shell.api.plugin;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Service;

/*****************************************
 * General marker interface for every shell plugin. Every shell plugin has a
 * {@code start()} and {@code stop()} method that is called by the
 * shell when a plugin is started and stopped respectively. Calls to
 * {@code start()} and {@code stop} should be non-blocking and should not spawn
 * any additional threads unless absolutely necessary. Instead a shell plugin
 * should hook into the shell thread itself. Should a shell plugin start a new
 * thread then this thread should not call any object internals that live
 * outside it's own shell plugin implementation, instead use the
 * {@link ListenableFuture}s provided throughout the shell api. This keeps for a
 * more thread safe and more predictable behavior of shell plugins.
 *
 *
 ****************************************/
public interface ShellPlugin extends Service {
}
