package org.trinity.foundation.input.api;

public class Input {

	public Momentum momentum;

	/*****************************************
	 * 
	 ****************************************/
	public Input(final Momentum momentum) {
		this.momentum = momentum;
	}

	public Momentum getMomentum() {
		return this.momentum;
	}
}
