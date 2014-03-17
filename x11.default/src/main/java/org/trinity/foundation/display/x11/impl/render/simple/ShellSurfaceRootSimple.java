package org.trinity.foundation.display.x11.impl.render.simple;

import com.google.common.eventbus.EventBus;
import org.trinity.foundation.display.x11.impl.XWindow;
import org.trinity.shell.scene.api.BufferSpace;
import org.trinity.shell.scene.api.HasSize;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.ShellSurfaceConfiguration;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.media.nativewindow.util.DimensionImmutable;
import javax.media.nativewindow.util.Point;
import javax.media.nativewindow.util.PointImmutable;

@Singleton
public class ShellSurfaceRootSimple extends EventBus implements ShellSurface {

	private final PointImmutable position = new Point(0,
													  0);

	private final XWindow rootXWindow;

	@Inject
	ShellSurfaceRootSimple(final XWindow rootXWindow) {
		this.rootXWindow = rootXWindow;
	}

	@Override
	public void accept(final ShellSurfaceConfiguration shellSurfaceConfiguration) {
		//TODO implement with xrandr thingy?
		//shellSurfaceConfiguration.setShape(...);
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	public PointImmutable getPosition() {
		return this.position;
	}

	@Override
	public HasSize<BufferSpace> getBuffer() {

		return this.rootXWindow;
	}

	@Override
	public Boolean isVisible() {
		return true;
	}

	@Override
	public Boolean isDestroyed() {
		return false;
	}

	@Override
	public void requestLower() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void requestMove(final int x,
							final int y) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void requestRaise() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void requestReparent(final ShellSurface parent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void requestResize(final int width,
							  final int height) {
		//TODO delegate to xrandr thingy?
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	public void requestShow() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void requestHide() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ShellSurface getParent() {
		return this;
	}

	@Override
	public DimensionImmutable getSize() {
		return this.rootXWindow.getSize();
	}
}
