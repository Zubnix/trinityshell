package org.fusion.x11.ewmh;

import org.fusion.x11.core.XDisplay;
 

public class Ewmh {
	private final XDisplay display;
	private final EwmhAtoms ewmhAtoms;

	public Ewmh(XDisplay display)    {
		this.display = display;
		this.ewmhAtoms = new EwmhAtoms(display);
	}

	public XDisplay getDisplay() {
		return display;
	}

	public EwmhAtoms getEwmhAtoms() {
		return ewmhAtoms;
	}
}
