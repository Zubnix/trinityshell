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

import org.fusion.qt.painter.QFusionPaintCallBack;
import org.hydrogen.paintinterface.Paintable;
import org.hyperdrive.widget.View;

import com.trolltech.qt.gui.QWidget;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public abstract class QFusionViewImpl implements View {
	@Override
	public QFusionPaintCallBack<? extends QWidget, ? extends QWidget> onCreate(
			final Object... args) {
		return new QFusionPaintCallBack<QWidget, QWidget>() {
			@Override
			public QWidget call(QWidget paintPeer, Paintable paintable) {
				QWidget visual = initVisual(paintPeer, paintable, args);
				return visual;
			}
		};
	}

	protected abstract QWidget initVisual(QWidget parentVisual,
			Paintable paintable, Object... args);

	@Override
	public QFusionPaintCallBack<? extends QWidget, Void> onDestroy(
			final Object... args) {
		return new QFusionPaintCallBack<QWidget, Void>() {
			@Override
			public Void call(final QWidget paintPeer, final Paintable paintable) {
				paintPeer.close();
				return null;
			}
		};
	}
}
