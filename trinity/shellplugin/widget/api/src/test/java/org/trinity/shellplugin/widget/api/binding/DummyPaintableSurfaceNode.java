package org.trinity.shellplugin.widget.api.binding;

import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.foundation.render.api.SurfaceNode;

public class DummyPaintableSurfaceNode implements PaintableSurfaceNode {

	@ViewProperty(value = "dummyAttribute")
	private String attributeOne = "valOne";
	@ViewReference
	private final DummyView view;

	public DummyPaintableSurfaceNode(final DummyView view) {
		this.view = view;
	}

	@ViewPropertyChanged("dummyAttribute")
	public void setBothAttributes() {
		this.attributeOne = "val1";
	}

	@Override
	public int getAbsoluteX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAbsoluteY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SurfaceNode getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Painter getPainter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaintableSurfaceNode getParentPaintableSurface() {
		// TODO Auto-generated method stub
		return null;
	}
}
