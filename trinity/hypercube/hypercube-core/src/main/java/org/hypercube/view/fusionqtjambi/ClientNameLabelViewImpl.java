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
import org.hypercube.view.fusionqtjambi.visual.LabelVisual;

import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ClientNameLabelViewImpl extends QFusionViewImpl implements
		org.hyperdrive.widget.ClientNameLabel.ClientNameLabelView {
	public static final String CSS_NAME = "ClientNameLabel";

	@Override
	public QFusionPaintCallBack<QLabel, Void> onNameUpdate(final String name,
			final Object... args) {
		return new QFusionPaintCallBack<QLabel, Void>() {
			@Override
			public Void call(final QLabel paintPeer, final Paintable paintable) {
				final QLabel clientNameLabel = paintPeer;
				clientNameLabel.setText(name);
				return null;
			}
		};
	}

	@Override
	protected LabelVisual initVisual(QWidget parentVisual, Paintable paintable,
			Object... args) {
		return new LabelVisual(parentVisual);
	}
}
