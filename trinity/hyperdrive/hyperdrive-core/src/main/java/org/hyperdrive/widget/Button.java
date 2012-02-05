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

import org.hydrogen.displayinterface.event.ButtonNotifyEvent;
import org.hydrogen.displayinterface.input.MouseInput;
import org.hydrogen.eventsystem.EventHandler;
import org.hydrogen.paintinterface.PaintCall;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class Button extends Widget {

	// TODO documentation
	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static interface ButtonView extends View {
		/**
		 * 
		 * @param input
		 * @return
		 */
		PaintCall<?, ?> mouseButtonPressed(MouseInput input);

		/**
		 * 
		 * @param input
		 * @return
		 */
		PaintCall<?, ?> mouseButtonReleased(MouseInput input);
	}

	@Override
	protected void init(final Widget indirectParent) {
		super.init(indirectParent);
		this.addEventHandler(new EventHandler<ButtonNotifyEvent>() {
			@Override
			public void handleEvent(final ButtonNotifyEvent event) {
				getPainter().paint(
						getView().mouseButtonPressed(event.getInput()));
				Button.this.onMouseButtonPressed(event.getInput());
			}
		}, ButtonNotifyEvent.PRESSED_TYPE);

		getManagedDisplay().addEventHandler(
				new EventHandler<ButtonNotifyEvent>() {
					@Override
					public void handleEvent(final ButtonNotifyEvent event) {
						getPainter()
								.paint(getView().mouseButtonReleased(
										event.getInput()));
						Button.this.onMouseButtonReleased(event.getInput());
					}
				}, ButtonNotifyEvent.RELEASED_TYPE);
	}

	@Override
	public ButtonView getView() {
		return (ButtonView) super.getView();
	}

	/**
	 * 
	 * @param input
	 */
	public void onMouseButtonPressed(final MouseInput input) {

	}

	/**
	 * 
	 * @param input
	 */
	public void onMouseButtonReleased(final MouseInput input) {

	}
}
