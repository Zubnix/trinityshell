/*
 * This file is part of Hypercube.
 * 
 * Hypercube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hypercube is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hypercube. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hypercube.hyperwidget;

import org.hypercube.view.ClientContainerViewImpl;
import org.hyperdrive.widget.View;
import org.hyperdrive.widget.ViewFactory;
import org.hyperdrive.widget.Widget;

// TODO documentation
// TODO find a cleaner and easier way to bind a view to a widget
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ClientContainer extends Widget {

	public ClientContainer() {
		super();
	}

	private final boolean registered = false;

	@Override
	protected View initView(final ViewFactory<?> viewFactory) {
		if (!this.registered) {
			getManagedDisplay().getWidgetViewFactory().registerCustomView(this,
					ClientContainerViewImpl.class);
		}

		return viewFactory.newCustomView(this);
	}
}
