package org.hypercube.view.fusionqtjambi;

import java.util.List;

import org.fusion.qt.painter.QFusionPaintCall;
import org.hydrogen.paintinterface.PaintContext;
import org.hypercube.view.fusionqtjambi.visual.KeyDrivenMenuVisual;
import org.hyperdrive.widget.KeyDrivenMenu;
import org.hyperdrive.widget.PaintInstruction;

import com.trolltech.qt.gui.QWidget;

public class KeyDrivenMenuView extends WidgetView implements KeyDrivenMenu.View {

	public static final PaintInstruction<Void> CLEAR_INSTRUCTION = new PaintInstruction<Void>(
			new QFusionPaintCall<Void, KeyDrivenMenuVisual>() {
				@Override
				public Void call(
						final PaintContext<KeyDrivenMenuVisual> paintContext) {
					paintContext.getPaintPeer().clearVisual();
					return null;
				}
			});

	public static final PaintInstruction<Void> ACTIVATE_INSTRUCTION = new PaintInstruction<Void>(
			new QFusionPaintCall<Void, KeyDrivenMenuVisual>() {
				@Override
				public Void call(
						final PaintContext<KeyDrivenMenuVisual> paintContext) {
					paintContext.getPaintPeer().activate();
					return null;
				}
			});

	public static final PaintInstruction<Void> DEACTIVATE_INSTRUCTION = new PaintInstruction<Void>(
			new QFusionPaintCall<Void, KeyDrivenMenuVisual>() {
				@Override
				public Void call(
						final PaintContext<KeyDrivenMenuVisual> paintContext) {
					paintContext.getPaintPeer().deactivate();
					return null;
				}
			});

	@Override
	protected QWidget createPaintPeer(
			final PaintContext<? extends QWidget> paintContext,
			final QWidget parentPaintPeer) {

		return new KeyDrivenMenuVisual(parentPaintPeer);
	}

	@Override
	public PaintInstruction<Void> clear() {
		return KeyDrivenMenuView.CLEAR_INSTRUCTION;
	}

	@Override
	public PaintInstruction<Void> update(final String input,
			final List<String> possibleValues, final int activeValue) {
		return new PaintInstruction<Void>(
				new QFusionPaintCall<Void, KeyDrivenMenuVisual>() {
					@Override
					public Void call(
							final PaintContext<KeyDrivenMenuVisual> paintContext) {
						paintContext.getPaintPeer().updateVisual(input,
								possibleValues, activeValue);
						return null;
					}
				});
	}

	@Override
	public PaintInstruction<Void> activate() {
		return KeyDrivenMenuView.ACTIVATE_INSTRUCTION;
	}

	@Override
	public PaintInstruction<Void> deactivate() {
		return KeyDrivenMenuView.DEACTIVATE_INSTRUCTION;
	}

}
