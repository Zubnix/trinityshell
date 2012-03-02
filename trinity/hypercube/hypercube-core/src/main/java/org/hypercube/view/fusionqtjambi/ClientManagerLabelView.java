package org.hypercube.view.fusionqtjambi;

import org.fusion.qt.painter.QFusionPaintCall;
import org.hydrogen.paintinterface.PaintContext;
import org.hyperdrive.widget.ClientManager.ClientManagerLabel;
import org.hyperdrive.widget.PaintInstruction;

import com.trolltech.qt.gui.QWidget;

public class ClientManagerLabelView extends LabelView implements
		ClientManagerLabel.View {

	public static final PaintInstruction<Void> CLIENT_GAIN_FOCUS_INSTRUCTION = new PaintInstruction<Void>(
			new QFusionPaintCall<Void, QWidget>() {
				@Override
				public Void call(final PaintContext<QWidget> paintContext) {
					final QWidget paintPeer = paintContext.getPaintPeer();
					paintPeer.setProperty("active", true);
					paintPeer.style().unpolish(paintPeer);
					paintPeer.style().polish(paintPeer);
					return null;
				}
			});

	public static final PaintInstruction<Void> CLIENT_LOST_FOCUS_INSTRUCTION = new PaintInstruction<Void>(
			new QFusionPaintCall<Void, QWidget>() {
				@Override
				public Void call(final PaintContext<QWidget> paintContext) {
					final QWidget paintPeer = paintContext.getPaintPeer();
					paintPeer.setProperty("active", false);
					paintPeer.style().unpolish(paintPeer);
					paintPeer.style().polish(paintPeer);
					return null;
				}
			});

	@Override
	public PaintInstruction<Void> onClientGainFocus() {
		return ClientManagerLabelView.CLIENT_GAIN_FOCUS_INSTRUCTION;
	}

	@Override
	public PaintInstruction<Void> onClientLostFocus() {
		return ClientManagerLabelView.CLIENT_LOST_FOCUS_INSTRUCTION;
	}

}
