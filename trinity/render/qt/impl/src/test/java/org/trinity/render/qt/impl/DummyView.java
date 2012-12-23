package org.trinity.render.qt.impl;

import org.trinity.foundation.input.api.PointerInput;
import org.trinity.render.qt.api.QJPaintContext;
import org.trinity.shellplugin.widget.api.binding.InputEmitter;
import org.trinity.shellplugin.widget.api.binding.InputSignal;
import org.trinity.shellplugin.widget.api.binding.ViewPropertySlot;

import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;

public class DummyView extends QWidget {

	private QPushButton pushButton0 = new QPushButton(	"button0",
														this);
	private QPushButton pushButton1 = new QPushButton(	"button1",
														this);

	private QPushButton button;

	private QLayout layout = new QVBoxLayout(this);

	public DummyView() {

		pushButton0.resize(	100,
							100);
		pushButton1.resize(	50,
							50);

		layout.addWidget(pushButton0);
		layout.addWidget(pushButton1);
		setLayout(layout);
		makebutton0Active();
		show();
		layout.layout();
	}

	@ViewPropertySlot("slot0")
	public void viewSlot0(Object arg) {

	}

	@ViewPropertySlot({ "slot0", "slot1" })
	public void viewSlot1(	QJPaintContext paintContext,
							Object arg) {

	}

	@InputEmitter(@InputSignal(inputSlotName = "onClick", inputType = PointerInput.class))
	public QWidget button() {
		return button;
	}

	public QPushButton getPushButton0() {
		return pushButton0;
	}

	public QPushButton getPushButton1() {
		return pushButton1;
	}

	public void makebutton0Active() {
		button = pushButton0;
	}

	public void makebutton1Actiate() {
		button = pushButton1;
	}
}