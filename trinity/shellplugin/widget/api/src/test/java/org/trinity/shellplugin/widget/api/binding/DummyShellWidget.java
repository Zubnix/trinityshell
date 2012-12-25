package org.trinity.shellplugin.widget.api.binding;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.input.api.Input;
import org.trinity.foundation.input.api.InputModifier;
import org.trinity.foundation.input.api.KeyboardInput;
import org.trinity.foundation.input.api.Momentum;
import org.trinity.foundation.input.api.PointerInput;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.ShellNodeParent;
import org.trinity.shell.api.node.ShellNodeTransformation;
import org.trinity.shell.api.node.manager.ShellLayoutManager;
import org.trinity.shell.api.surface.ShellSurfaceExecutor;
import org.trinity.shell.api.widget.ShellWidget;

public class DummyShellWidget implements ShellWidget {

	// private final DummyView view = new DummyView();
	//
	// private Object object;
	// private final Object nameless = "namelessValue";
	// private final boolean primitiveBoolean = true;

	private final DummyShellWidget mockedDummyShellWidget;

	public DummyShellWidget(final DummyShellWidget mockedDummyShellWidget) {
		this.mockedDummyShellWidget = mockedDummyShellWidget;
	}

	@InputSlot(name = "onClick", momentum = { Momentum.STARTED, Momentum.STOPPED }, modifier = { InputModifier.MOD_1,
			InputModifier.MOD_CTRL })
	public void onClick(final PointerInput pointerInput) {
		this.mockedDummyShellWidget.onClick(pointerInput);
	}

	@InputSlot(name = "onKey", momentum = Momentum.STARTED)
	public void onKey(final KeyboardInput keyboardInput) {
		this.mockedDummyShellWidget.onKey(keyboardInput);

	}

	@InputSlot(input = KeyboardInput.class)
	public void onAnyKeyInput(final Input input) {
		this.mockedDummyShellWidget.onAnyKeyInput(input);
	}

	@InputSlot
	public void onAny() {
		this.mockedDummyShellWidget.onAny();
	}

	@ViewReference
	public DummyView getView() {
		return this.mockedDummyShellWidget.getView();
	}

	@ViewPropertyChanged("object")
	public void setObject(final Object object) {
		this.mockedDummyShellWidget.setObject(object);

	}

	@ViewProperty("object")
	public Object getObject() {
		return this.mockedDummyShellWidget.getObject();
	}

	@ViewPropertyChanged("nameless")
	public void setNameless(final Object nameless) {
		this.mockedDummyShellWidget.setNameless(nameless);
	}

	@ViewProperty
	public Object getNameless() {
		return this.mockedDummyShellWidget.getNameless();
	}

	@ViewPropertyChanged("primitiveBoolean")
	public void setPrimitiveBoolean(final boolean primitiveBoolean) {
		this.mockedDummyShellWidget.setPrimitiveBoolean(primitiveBoolean);
	}

	@ViewProperty("primitiveBoolean")
	public boolean isPrimitiveBoolean() {
		return this.mockedDummyShellWidget.isPrimitiveBoolean();
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

	@Override
	public ShellSurfaceExecutor getShellNodeExecutor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHeightIncrement() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMinHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMinWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DisplaySurface getDisplaySurface() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWidthIncrement() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isMovable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isResizable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setHeightIncrement(final int heightIncrement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaxHeight(final int maxHeight) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaxWidth(final int maxWidth) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMinHeight(final int minHeight) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMinWidth(final int minWidth) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMovable(final boolean movable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setResizable(final boolean isResizable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWidthIncrement(final int widthIncrement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void syncGeoToDisplaySurface() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInputFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancelPendingMove() {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancelPendingResize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doDestroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doLower() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doReparent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doMove() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doRaise() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doMoveResize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doResize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doShow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doHide() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDestroyed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void requestLower() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestMove() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestMoveResize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestRaise() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestReparent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestResize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestShow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestHide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWidth(final int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHeight(final int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setParent(final ShellNodeParent parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setX(final int x) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setY(final int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addShellNodeEventHandler(final Object shellNodeEventHandler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeShellNodeEventHandler(final Object shellNodeEventHandler) {
		// TODO Auto-generated method stub

	}

	@Override
	public ShellNodeTransformation toGeoTransformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ShellLayoutManager getLayoutManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void layout() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLayoutManager(final ShellLayoutManager shellLayoutManager) {
		// TODO Auto-generated method stub

	}

	@Override
	public ShellNode[] getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleChildReparentEvent(final ShellNode child) {
		// TODO Auto-generated method stub

	}

	@Override
	public ShellNodeParent getParent() {
		// TODO Auto-generated method stub
		return null;
	}

}
