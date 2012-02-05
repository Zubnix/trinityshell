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

import org.hydrogen.displayinterface.Property;
import org.hydrogen.displayinterface.PropertyInstanceText;
import org.hydrogen.eventsystem.EventHandler;
import org.hydrogen.paintinterface.PaintCall;
import org.hyperdrive.core.ClientWindow;
import org.hyperdrive.core.RenderAreaPropertiesManipulator;
import org.hyperdrive.core.RenderAreaPropertyChangedEvent;

// TODO documentation
/**
 * 
 * A <code>ClientNameLabel</code> shows a text property from a
 * {@link ClientWindow}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class ClientNameLabel extends Widget {

	private RenderAreaPropertiesManipulator propertyDelegate;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static interface ClientNameLabelView extends View {
		PaintCall<?, ?> onNameUpdate(String name, Object... args);
	}

	private ClientWindow targetWindow;
	private String namePropertyName;

	/**
	 * 
	 * @param namePropertyName
	 */
	public ClientNameLabel(final String namePropertyName) {
		super();
		setNamePropertyName(namePropertyName);
	}

	/**
	 * 
	 * @return
	 */
	public String getNamePropertyName() {
		return this.namePropertyName;
	}

	/**
	 * 
	 * @param namePropertyName
	 */
	public void setNamePropertyName(final String namePropertyName) {
		this.namePropertyName = namePropertyName;
	}

	@Override
	protected ClientNameLabelView initView(final ViewFactory<?> viewFactory) {
		return viewFactory.newClientNameLabelView();
	}

	@Override
	public ClientNameLabelView getView() {
		return (ClientNameLabelView) super.getView();
	}

	/**
	 * 
	 * @param targetWindow
	 * 
	 */
	public void setTargetWindow(final ClientWindow targetWindow) {
		if (this.targetWindow != null) {
			// TODO remove listener from previous window
		}
		this.targetWindow = targetWindow;
		this.propertyDelegate = new RenderAreaPropertiesManipulator(
				targetWindow);

		targetWindow
				.addEventHandler(
						new EventHandler<RenderAreaPropertyChangedEvent<Property<PropertyInstanceText>>>() {
							@Override
							public void handleEvent(
									final RenderAreaPropertyChangedEvent<Property<PropertyInstanceText>> event) {
								updateNameLabel();
							}
						}, RenderAreaPropertyChangedEvent
								.TYPE(this.namePropertyName));

		// TODO automatically call painter when we call a method of the view?
		updateNameLabel();
	}

	/**
	 * 
	 * 
	 */
	protected void updateNameLabel() {
		final String name = this.propertyDelegate
				.<PropertyInstanceText> getPropertyValue(this.namePropertyName)
				.getText();
		getPainter().paint(getView().onNameUpdate(name));
	}

	/**
	 * 
	 * @return
	 */
	public ClientWindow getTargetWindow() {
		return this.targetWindow;
	}
}
