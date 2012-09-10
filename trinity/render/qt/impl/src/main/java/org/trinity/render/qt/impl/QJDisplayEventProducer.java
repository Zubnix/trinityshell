package org.trinity.render.qt.impl;

import org.trinity.foundation.display.api.DisplayEventProducer;

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
