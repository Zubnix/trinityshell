package org.trinity.shellplugin.widget.impl.view.qt;

import org.trinity.foundation.api.render.PaintRenderer;
import org.trinity.foundation.api.render.binding.view.View;

import com.google.inject.Inject;

public class StyledViewProvider extends AbstractViewProvider {

	@Inject
	StyledViewProvider(PaintRenderer paintRenderer) {
		super(paintRenderer);
	}

	@Override
	View createView() {
		return new StyledView();
	}

}
