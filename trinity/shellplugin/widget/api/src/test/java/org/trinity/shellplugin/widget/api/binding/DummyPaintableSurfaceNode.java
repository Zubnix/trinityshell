package org.trinity.shellplugin.widget.api.binding;

import org.trinity.foundation.input.api.Input;
import org.trinity.foundation.input.api.InputModifier;
import org.trinity.foundation.input.api.KeyboardInput;
import org.trinity.foundation.input.api.Momentum;
import org.trinity.foundation.input.api.PointerInput;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.foundation.render.api.SurfaceNode;

public class DummyPaintableSurfaceNode implements PaintableSurfaceNode {

	private final DummyView view = new DummyView();

	private Object object;
	private Object nameless = "namelessValue";
	private boolean primitiveBoolean = true;

	@InputSlot(name = "onClick", momentum = { Momentum.STARTED, Momentum.STOPPED }, modifier = { InputModifier.MOD_1,
			InputModifier.MOD_CTRL })
	public void onClick(PointerInput pointerInput) {

	}

	@InputSlot(name = "onKey", momentum = Momentum.STARTED)
	public void onKey(KeyboardInput keyboardInput) {

	}

	@InputSlot(input = KeyboardInput.class)
	public void onAnyKeyInput(Input input) {

	}

	@InputSlot
	public void onAny() {

	}

	@ViewReference
	public DummyView getView() {
		return view;
	}

	@ViewPropertyChanged("object")
	public void setObject(Object object) {
		this.object = object;
	}

	@ViewProperty("object")
	public Object getObject() {
		return this.object;
	}

	@ViewPropertyChanged("nameless")
	public void setNameless(Object nameless) {

	}

	@ViewProperty
	public Object getNameless() {
		return nameless;
	}

	@ViewPropertyChanged("primitiveBoolean")
	public void setPrimitiveBoolean(boolean primitiveBoolean) {
		this.primitiveBoolean = primitiveBoolean;
	}

	@ViewProperty("primitiveBoolean")
	public boolean isPrimitiveBoolean() {
		return primitiveBoolean;
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
