/*
 * This file is part of Hypercube.
 * 
 * Hypercube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hypercube is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hypercube. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hypercube.view.fusionqtjambi;

import java.util.List;

import org.fusion.qt.painter.QFusionPaintCallBack;
import org.hydrogen.paintinterface.PaintCall;
import org.hydrogen.paintinterface.Paintable;
import org.hypercube.view.fusionqtjambi.visual.KeyDrivenMenuVisual;
import org.hyperdrive.widget.KeyDrivenMenu.KeyDrivenMenuView;

import com.trolltech.qt.gui.QWidget;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class KeyDrivenMenuViewImpl extends QFusionViewImpl implements
		KeyDrivenMenuView {

	@Override
	public QFusionPaintCallBack<KeyDrivenMenuVisual, Void> clear() {
		return new QFusionPaintCallBack<KeyDrivenMenuVisual, Void>() {
			@Override
			public Void call(final KeyDrivenMenuVisual paintPeer,
					final Paintable paintable) {
				paintPeer.clearVisual();
				return null;
			}
		};
	}

	@Override
	public QFusionPaintCallBack<KeyDrivenMenuVisual, Void> update(
			final String desired, final List<String> possibleValues,
			final int activeValue) {
		return new QFusionPaintCallBack<KeyDrivenMenuVisual, Void>() {
			@Override
			public Void call(final KeyDrivenMenuVisual paintPeer,
					final Paintable paintable) {
				paintPeer.updateVisual(desired, possibleValues, activeValue);
				return null;
			}
		};
	}

	@Override
	protected QWidget initVisual(final QWidget parentVisual,
			final Paintable paintable, final Object... args) {
		return new KeyDrivenMenuVisual(parentVisual);
	}

	@Override
	public PaintCall<?, ?> activate() {
		return new QFusionPaintCallBack<KeyDrivenMenuVisual, Void>() {
			@Override
			public Void call(final KeyDrivenMenuVisual paintPeer,
					final Paintable paintable) {
				paintPeer.activate();
				return null;
			}
		};
	}

	@Override
	public PaintCall<?, ?> deactivate() {
		return new QFusionPaintCallBack<KeyDrivenMenuVisual, Void>() {
			@Override
			public Void call(final KeyDrivenMenuVisual paintPeer,
					final Paintable paintable) {
				paintPeer.deactivate();
				return null;
			}
		};
	}
}
