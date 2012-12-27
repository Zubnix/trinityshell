package org.trinity.render.qt.impl;

import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.binding.InputEmitter;
import org.trinity.foundation.api.render.binding.InputSignal;
import org.trinity.foundation.api.render.binding.ViewPropertySlot;

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

	@ViewPropertySlot("slot0")
	public void viewSlot0(final Object arg) {

	}

	@ViewPropertySlot({ "slot0", "slot1" })
	public void viewSlot1(	final PaintContext paintContext,
							final Object arg) {

	}

	@InputEmitter(@InputSignal(inputSlotName = "onClick", inputType = PointerInput.class))
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