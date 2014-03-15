package org.trinity.foundation.display.x11.impl.render;

import com.google.common.eventbus.EventBus;
import org.trinity.shell.scene.api.HasSize;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.ShellSurfaceConfiguration;
import org.trinity.shell.scene.api.SpaceBuffer;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.media.nativewindow.util.RectangleImmutable;

@Singleton
public class ShellSurfaceRootSimple extends EventBus implements ShellSurface{

	@Inject
    ShellSurfaceRootSimple() {
	}

	@Override
	public void accept(final ShellSurfaceConfiguration shellSurfaceConfiguration) {
		//TODO implement with xrandr thingy?
		//shellSurfaceConfiguration.setShape(...);
	}

	@Override
	public HasSize<SpaceBuffer> getBuffer() {
		//TODO return rootwindow?
		return null;
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
		//ignored
	}

	@Override
	public void requestMove(final int x,
							final int y) {
		//ingored
	}

	@Override
	public void requestRaise() {
		//ignored
	}

	@Override
	public void requestReparent(final ShellSurface parent) {
		//ignored
	}

	@Override
	public void requestResize(final int width,
							  final int height) {
		//TODO delegate to xrandr thingy
	}

	@Override
	public void requestShow() {
		//ignored
	}

	@Override
	public void requestHide() {
		//ignored
	}

	@Override
	public ShellSurface getParent() {
		return this;
	}

	@Override
	public RectangleImmutable getShape() {
		return null;
	}
}
