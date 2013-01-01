package org.trinity.render.qt.impl;

import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.binding.refactor.view.InputSignal;
import org.trinity.foundation.api.render.binding.refactor.view.InputSignals;
import org.trinity.foundation.api.render.binding.refactor.view.PropertySlot;

import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;

public class DummyView extends QWidget {

	private final QPushButton pushButton0 = new QPushButton("button0",
															this);
	private final QPushButton pushButton1 = new QPushButton("button1",
															this);

	private QPushButton button;

	private final QLayout layout = new QVBoxLayout(this);

	public DummyView() {

		this.pushButton0.resize(100,
								100);
		this.pushButton1.resize(50,
								50);

		this.layout.addWidget(this.pushButton0);
		this.layout.addWidget(this.pushButton1);
		setLayout(this.layout);
		makebutton0Active();
		show();
		this.layout.layout();
	}

	@PropertySlot("slot0")
	public void viewSlot0(final Object arg) {

	}

	@PropertySlot({ "slot0", "slot1" })
	public void viewSlot1(	final PaintContext paintContext,
							final Object arg) {

	}

	@InputSignals(@InputSignal(name = "onClick", inputType = PointerInput.class))
	public QWidget button() {
		return this.button;
	}

	public QPushButton getPushButton0() {
		return this.pushButton0;
	}

	public QPushButton getPushButton1() {
		return this.pushButton1;
	}

	public void makebutton0Active() {
		this.button = this.pushButton0;
	}

	public void makebutton1Actiate() {
		this.button = this.pushButton1;
	}
}