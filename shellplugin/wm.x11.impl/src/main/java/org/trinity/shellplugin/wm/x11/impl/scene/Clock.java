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

package org.trinity.shellplugin.wm.x11.impl.scene;


import org.trinity.foundation.api.render.binding.model.PropertyChanged;
import org.trinity.shellplugin.wm.api.HasText;
import org.trinity.shellplugin.wm.api.Shell;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Singleton
public class Clock implements HasText, Runnable {

	private final ScheduledExecutorService clockExecutor = Executors.newScheduledThreadPool(1);
	private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	private String text;

	@Inject
	Clock(final Shell desktop) {
		desktop.addStatusElement(this);
		this.clockExecutor.scheduleAtFixedRate(	this,
												0,
												1,
												TimeUnit.MINUTES);
	}

	@Override
	public String getText() {
		return this.text;
	}

	@PropertyChanged(value = "text")
	public void setText(final String text) {
		this.text = text;
	}

	@Override
	public void run() {
		setText(this.sdf.format(new Date()));
	}
}
