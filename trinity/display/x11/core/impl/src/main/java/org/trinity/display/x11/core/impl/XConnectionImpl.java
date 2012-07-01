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

import org.trinity.display.x11.core.api.XCall;
import org.trinity.display.x11.core.api.XCaller;
import org.trinity.display.x11.core.api.XConnection;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class XConnectionImpl implements XConnection<Long> {

	private final XCaller xCaller;
	private final XCall<Long, Void, Integer> openConnection;
	private final XCall<Void, Long, Void> closeConnection;
	private final Integer screen;
	private final Integer display;

	private Long connectionReference;

	@Inject
	public XConnectionImpl(	final XCaller xCaller,
							@Named("openConnection") final XCall<Long, Void, Integer> openConnection,
							@Named("closeConnection") final XCall<Void, Long, Void> closeConnection,
							@Assisted final Integer screen,
							@Assisted final Integer display) {
		this.xCaller = xCaller;
		this.openConnection = openConnection;
		this.closeConnection = closeConnection;
		this.screen = screen;
		this.display = display;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.XConnection#open()
	 */
	@Override
	public void open() {
		this.connectionReference = this.xCaller.doCall(	this.openConnection,
														null,
														this.screen,
														this.display);
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.XConnection#close()
	 */
	@Override
	public void close() {
		this.xCaller.doCall(this.closeConnection, this.connectionReference);
	}

	/*****************************************
	 * @return the connectionReference
	 ****************************************/
	@Override
	public Long getConnectionReference() {
		return this.connectionReference;
	}
}
