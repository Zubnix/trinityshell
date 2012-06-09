package org.fusion.x11.ewmh;

import org.trinity.display.x11.impl.XServerImpl;
 

public class Ewmh {
	private final XServerImpl display;
	private final EwmhAtoms ewmhAtoms;

	public Ewmh(XServerImpl display)    {
		this.display = display;
		this.ewmhAtoms = new EwmhAtoms(display);
	}

	public XServerImpl getDisplay() {
		return display;
	}

	public EwmhAtoms getEwmhAtoms() {
		return ewmhAtoms;
	}
}
