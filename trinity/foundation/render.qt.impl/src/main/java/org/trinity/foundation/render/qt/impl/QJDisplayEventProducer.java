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
package org.trinity.foundation.render.qt.impl;

import org.trinity.foundation.api.display.DisplayEventProducer;

import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.gui.QApplication;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;

@Bind(multiple = true, to = @To(value = To.Type.CUSTOM, customs = { DisplayEventProducer.class }))
public class QJDisplayEventProducer implements DisplayEventProducer, Runnable {

	private Thread renderThread;

	@Override
	public void startDisplayEventProduction() {
		this.renderThread = new Thread(	this,
										"Qt Jambi Render Thread");
		this.renderThread.start();
	}

	@Override
	public void stopDisplayEventProduction() {
		QCoreApplication.invokeLater(new Runnable() {
			@Override
			public void run() {
				QCoreApplication.quit();
			}
		});
	}

	@Override
	public void run() {
		QApplication.initialize(new String[] {});
		QApplication.setQuitOnLastWindowClosed(false);
		final int r = QApplication.exec();
		if (r != 0) {
			throw new RuntimeException("Qt Jambi exited with error: " + r);
		}
	}

}
