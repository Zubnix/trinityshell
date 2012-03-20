package org.hyperdrive.widget;

import org.hyperdrive.api.widget.HasView;
import org.hyperdrive.api.widget.Label;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
@HasView(Label.View.class)
public class BaseLabel extends BaseWidget implements Label {

	@Override
	public Label.View getView() {
		return (Label.View) super.getView();
	}

	@Override
	public void updateLabel(final String name) {
		getView().labelUpdated(name);
	}

}
