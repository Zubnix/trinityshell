package org.fusion.x11.ewmh.refactor;

import org.fusion.x11.core.XDisplay;
import org.fusion.x11.ewmh.EwmhAtoms;
import org.fusion.x11.icccm.Icccm;

public final class Ewmh {

	private final XDisplay display;

	private final Icccm icccm;

	private final EwmhAtoms ewmhAtoms;

	public Ewmh(final Icccm icccm) {
		this.icccm = icccm;
		this.display = getIcccm().getDisplay();
		this.ewmhAtoms = new EwmhAtoms(this.display);
	}

	public Icccm getIcccm() {
		return this.icccm;
	}

	public XDisplay getDisplay() {
		return this.display;
	}

	public EwmhAtoms getEwmhAtoms() {
		return this.ewmhAtoms;
	}
}
