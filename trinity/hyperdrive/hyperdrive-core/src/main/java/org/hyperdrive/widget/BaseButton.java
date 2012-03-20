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

import org.hydrogen.api.display.event.ButtonNotifyEvent;
import org.hyperdrive.api.core.ManagedDisplay;
import org.hyperdrive.api.core.event.MouseButtonReleasedHandler;
import org.hyperdrive.api.widget.Button;
import org.hyperdrive.api.widget.HasView;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
@HasView(Button.View.class)
public class BaseButton extends BaseWidget implements Button {

	@Override
	public Button.View getView() {
		return (org.hyperdrive.api.widget.Button.View) super.getView();
	}

	@Override
	protected void setManagedDisplay(final ManagedDisplay managedDisplay) {
		super.setManagedDisplay(managedDisplay);
		getManagedDisplay().addDisplayEventHandler(
				new MouseButtonReleasedHandler() {
					@Override
					public void handleEvent(final ButtonNotifyEvent event) {
						getView().mouseButtonReleased(event.getInput());
						BaseButton.this.onMouseButtonReleased(event.getInput());

					}
				});
	}
}
