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
package org.trinity.shell.core.impl;

import org.trinity.foundation.display.api.DisplayServer;
import org.trinity.shell.core.api.ShellDisplayEventDispatcher;
import org.trinity.shell.core.api.ShellPlugin;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(multiple = true, to = @To(value = Type.CUSTOM, customs = ShellPlugin.class))
@Singleton
public class ShellDisplayPlugin implements ShellPlugin, Runnable {

	private final DisplayServer display;
	private final ShellDisplayEventDispatcher shellDisplayEventDispatcherImpl;
	private Thread shellThread;

	@Inject
	ShellDisplayPlugin(	final DisplayServer display,
						final ShellDisplayEventDispatcher shellDisplayEventDispatcherImpl) {
		this.display = display;
		this.shellDisplayEventDispatcherImpl = shellDisplayEventDispatcherImpl;
	}

	@Override
	public void start() {
		this.shellThread = new Thread(this);
		this.shellThread.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void stop() {
		this.shellThread.interrupt();
		try {
			this.shellThread.join(3000);
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (this.shellThread.isAlive()) {
			this.shellThread.stop();
		}
		this.display.shutDown();
	}

	@Override
	public String toString() {
		return String.format("Display: %s", this.display);
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				this.shellDisplayEventDispatcherImpl.dispatchDisplayEvent(true);
				Thread.yield();
			} catch (final Exception e) {
				e.printStackTrace();
				if (e instanceof InterruptedException) {
					break;
				}
			}
		}
	}
}