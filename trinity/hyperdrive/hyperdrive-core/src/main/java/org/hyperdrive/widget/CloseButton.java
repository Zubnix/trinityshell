/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.widget;

import org.hydrogen.displayinterface.input.MouseInput;
import org.hyperdrive.core.AbstractRenderArea;
import org.hyperdrive.core.ClientWindow;
import org.hyperdrive.protocol.DesktopProtocol;

/**
 * A <code>CloseButton</code> can terminate another
 * <code>AbstractRenderArea</code> that is defined by the target
 * <code>AbstractRenderArea</code>.
 * <p>
 * In case that the target <code>AbstractRenderArea</code> is a
 * <code>ClientWindow</code>, the <code>CloseButton</code> will ask the
 * <code>ClientWindow</code> to shut down itself. This does not guaranty that
 * the <code>ClientWindow</code> will actually shut down since most
 * <code>ClientWindow</code>s will present the user with a dialog to confirm if
 * the <code>ClientWindow</code> should shut down.
 * <p>
 * If the <code>AbstractRenderArea</code> is a <code>Widget</code>, the
 * <code>AbstractRenderArea</code> will simply be destroyed or other as defined
 * by the <code>GeoManager</code> of the <code>Widget</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class CloseButton extends Button {

	@ViewDefinition
	public interface View extends Button.View {

	}

	private ClientWindow clientWindow;
	private final DesktopProtocol desktopProtocol;

	public CloseButton(final DesktopProtocol desktopProtocol) {
		this.desktopProtocol = desktopProtocol;
	}

	/**
	 * Request that the destroy process of the target
	 * <code>AbstractRenderArea</code> is executed.
	 * 
	 */
	public void closeWindow() {
		this.desktopProtocol.requestDelete(getTargetRenderArea());
	}

	/**
	 * The target <code>AbstractRenderArea</code>.
	 * <p>
	 * The target <code>AbstractRenderArea</code> is the
	 * <code>AbstractRenderArea</code> which will be asked to terminate should
	 * this <code>CloseButton</code> be triggered.
	 * 
	 * @return
	 */
	public ClientWindow getTargetRenderArea() {
		return this.clientWindow;
	}

	/**
	 * Define the target <code>AbstractRenderArea</code>.
	 * <p>
	 * The target <code>AbstractRenderArea</code> is the
	 * <code>AbstractRenderArea</code> which will be asked to terminate should
	 * this <code>CloseButton</code> be triggered.
	 * 
	 * @param targetRenderArea
	 *            A {@link AbstractRenderArea}.
	 */
	public void setTargetRenderArea(final ClientWindow targetRenderArea) {
		this.clientWindow = targetRenderArea;
	}

	@Override
	public void onMouseButtonPressed(final MouseInput input) {
		closeWindow();
	}
}
