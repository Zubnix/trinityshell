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
package org.trinity.display.x11.core.impl;

import org.trinity.foundation.display.api.DisplayEventProducer;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class DisplayEventProducerImpl implements DisplayEventProducer {

	private final Thread eventPumpThread;

	/*****************************************
	 * 
	 ****************************************/
	@Inject
	public DisplayEventProducerImpl(@Named("XEventPump") final Runnable eventPump) {
		this.eventPumpThread = new Thread(eventPump);
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.core.display.api.DisplayEventProducer#start()
	 */
	@Override
	public void start() {
		this.eventPumpThread.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.core.display.api.DisplayEventProducer#stop()
	 */
	@Override
	public void stop() {
		this.eventPumpThread.interrupt();
	}
}