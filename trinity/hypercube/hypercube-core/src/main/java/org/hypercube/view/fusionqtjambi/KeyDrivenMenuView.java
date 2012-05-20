package org.hypercube.view.fusionqtjambi;

import java.util.List;

import org.fusion.qt.painter.QFusionPaintCall;
import org.hydrogen.paint.api.PaintContext;
import org.hypercube.view.fusionqtjambi.visual.KeyDrivenMenuVisual;
import org.hyperdrive.widget.api.KeyDrivenMenu;
import org.hyperdrive.widget.api.ViewImplementation;

import com.trolltech.qt.gui.QWidget;

@ViewImplementation(KeyDrivenMenu.View.class)
public class KeyDrivenMenuView extends WidgetView implements KeyDrivenMenu.View {

	public static final QFusionPaintCall<Void, KeyDrivenMenuVisual> CLEAR_INSTRUCTION = new QFusionPaintCall<Void, KeyDrivenMenuVisual>() {
		@Override
		public Void call(final PaintContext<KeyDrivenMenuVisual> paintContext) {
			paintContext.getPaintPeer().clearVisual();
			return null;
		}
	};

	public static final QFusionPaintCall<Void, KeyDrivenMenuVisual> ACTIVATE_INSTRUCTION = new QFusionPaintCall<Void, KeyDrivenMenuVisual>() {
		@Override
		public Void call(final PaintContext<KeyDrivenMenuVisual> paintContext) {
			paintContext.getPaintPeer().activate();
			return null;
		}
	};

	public static final QFusionPaintCall<Void, KeyDrivenMenuVisual> DEACTIVATE_INSTRUCTION = new QFusionPaintCall<Void, KeyDrivenMenuVisual>() {
		@Override
		public Void call(final PaintContext<KeyDrivenMenuVisual> paintContext) {
			paintContext.getPaintPeer().deactivate();
			return null;
		}
	};

	@Override
	protected QWidget createPaintPeer(
			final PaintContext<? extends QWidget> paintContext,
			final QWidget parentPaintPeer) {

		return new KeyDrivenMenuVisual(parentPaintPeer);
	}

	@Override
	public QFusionPaintCall<Void, KeyDrivenMenuVisual> clear() {
		return KeyDrivenMenuView.CLEAR_INSTRUCTION;
	}

	@Override
	public QFusionPaintCall<Void, KeyDrivenMenuVisual> update(
			final String input, final List<String> possibleValues,
			final int activeValue) {
		return new QFusionPaintCall<Void, KeyDrivenMenuVisual>() {
			@Override
			public Void call(
					final PaintContext<KeyDrivenMenuVisual> paintContext) {
				paintContext.getPaintPeer().updateVisual(input, possibleValues,
						activeValue);
				return null;
			}
		};
	}

	@Override
	public QFusionPaintCall<Void, KeyDrivenMenuVisual> startedKeyListening() {
		return KeyDrivenMenuView.ACTIVATE_INSTRUCTION;
	}

	@Override
	public QFusionPaintCall<Void, KeyDrivenMenuVisual> stoppedKeyListening() {
		return KeyDrivenMenuView.DEACTIVATE_INSTRUCTION;
	}

}
