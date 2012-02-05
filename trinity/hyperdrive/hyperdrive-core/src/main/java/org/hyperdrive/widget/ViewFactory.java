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

import org.hydrogen.paintinterface.PaintCall;
import org.hyperdrive.widget.Button.ButtonView;
import org.hyperdrive.widget.ClientManager.ClientManagerLabel.ClientManagerLabelView;
import org.hyperdrive.widget.ClientManager.ClientManagerView;
import org.hyperdrive.widget.ClientNameLabel.ClientNameLabelView;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public interface ViewFactory<P extends PaintCall<?, ?>> {
	/**
	 * 
	 * @param key
	 * @return
	 * @throws HyperdriveException
	 */
	<T extends View> T newCustomView(Widget widget);

	/**
	 * 
	 * @param widget
	 * @param viewClass
	 */
	void registerCustomView(Widget widget, Class<? extends View> viewClass);

	/**
	 * 
	 * @return
	 */
	ButtonView newMaximizeButtonView();

	/**
	 * 
	 * @return
	 */
	ClientManagerView newClientManagerView();

	/**
	 * 
	 * @return
	 */
	View newBaseView();

	/**
	 * 
	 * @return
	 */
	ButtonView newCloseButtonView();

	/**
	 * 
	 * @return
	 */
	ButtonView newDragButtonView();

	/**
	 * 
	 * @return
	 */
	View newRealRootView();

	/**
	 * 
	 * @return
	 */
	ButtonView newResizeButtonView();

	/**
	 * 
	 * @return
	 */
	View newVirtualRootView();

	/**
	 * 
	 * @return
	 */
	View newKeyDrivenMenuView();

	/**
	 * 
	 * @return
	 */
	ClientNameLabelView newClientNameLabelView();

	/**
	 * 
	 * @return
	 */
	ClientManagerLabelView newClientManagerLabelView();

	/**
	 * 
	 * @return
	 */
	ButtonView newHideButtonView();
}
