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
import org.hydrogen.api.display.event.DisplayEventType;
import org.hydrogen.api.event.EventHandler;
import org.hyperdrive.api.widget.Button;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class BaseButton extends BaseWidget implements Button {

	@Override
	public Button.View getView() {
		return (org.hyperdrive.api.widget.Button.View) super.getView();
	}

	@Override
	protected void init(final BaseWidget indirectParent) {
		super.init(indirectParent);
		this.addEventHandler(new EventHandler<ButtonNotifyEvent>() {
			@Override
			public void handleEvent(final ButtonNotifyEvent event) {
				getView().mouseButtonPressed(event.getInput());
				BaseButton.this.onMouseButtonPressed(event.getInput());
			}
		}, DisplayEventType.BUTTON_PRESSED);

		// TODO
		getManagedDisplay().addEventHandler(
				new EventHandler<ButtonNotifyEvent>() {
					@Override
					public void handleEvent(final ButtonNotifyEvent event) {
						getView().mouseButtonReleased(event.getInput());
						BaseButton.this.onMouseButtonReleased(event.getInput());
					}
				}, DisplayEventType.BUTTON_RELEASED);
	}
}
