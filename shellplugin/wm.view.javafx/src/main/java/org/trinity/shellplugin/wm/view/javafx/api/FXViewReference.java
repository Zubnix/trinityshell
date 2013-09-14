package org.trinity.shellplugin.wm.view.javafx.api;


import javafx.scene.Node;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.render.ViewReference;

public class FXViewReference implements ViewReference{


	private final Node node;

	public FXViewReference(Node node) {
		this.node = node;
	}

	@Override
	public Object getView() {
		return null;
	}

	@Override
	public DisplaySurface getViewDisplaySurface() {
		return null;
	}
}
