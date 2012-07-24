package org.trinity.display.x11.core.impl;

import org.trinity.display.x11.core.api.XAtom;
import org.trinity.display.x11.core.api.XDisplayProtocol;
import org.trinity.display.x11.core.api.XWindow;
import org.trinity.foundation.display.api.DisplayRenderArea;

import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class XDisplayProtocolImpl implements XDisplayProtocol {

	private final XAtom wmDelete;

	public XDisplayProtocolImpl(@Named("WM_DELETE") final XAtom wmDelete) {
		this.wmDelete = wmDelete;
	}

	@Override
	public void requestClose(final DisplayRenderArea displayRenderArea) {
		final XWindow client = (XWindow) displayRenderArea;
		// TODO send client message
		throw new RuntimeException("Not yet implemented.");
	}
}
