/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

package org.trinity.shellplugin.wm.x11.impl.view;

import org.trinity.foundation.api.render.binding.view.DataContext;
import org.trinity.foundation.api.render.binding.view.EventSignal;
import org.trinity.foundation.api.render.binding.view.InputSignals;
import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shellplugin.widget.impl.view.qt.LeftMouseButtonFilter;
import org.trinity.shellplugin.wm.api.ImageDefinition;

import com.google.common.base.Preconditions;
import com.trolltech.qt.core.QMargins;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPixmap;

class ClientBarElementView extends QFrame {

	@PropertySlots({ //
	@PropertySlot(propertyName = "imageDefinition", methodName = "setImageDefinition", argumentTypes = { ImageDefinition.class }) // HasImageDefinition
	})
	@InputSignals({ //
	@EventSignal(name = "onPointerInput", filter = LeftMouseButtonFilter.class) // ReceivesPointerInput
	})
	QLabel clientIconView = new QLabel() {
		{
			// workaround for jambi css bug
			setObjectName("ClientIcon");
		}

		@SuppressWarnings("unused")
		// used by property annotation.
		void setImageDefinition(final ImageDefinition imageDefinition) {
			final Size imageSize = imageDefinition.getImageSize();
			final int width = imageSize.getWidth();
			final int height = imageSize.getHeight();
			Preconditions.checkState(	width > 0,
										"Image width must be > 0");
			Preconditions.checkState(	height > 0,
										"Image height must be > 0");

			final byte[] imageData = imageDefinition.getImageData();

			final QPixmap pixmapImage = new QPixmap(width,
													height);
			pixmapImage.loadFromData(imageData);
			setPixmap(pixmapImage);
		}
	};
	@PropertySlots({ //
	@PropertySlot(propertyName = "text", methodName = "setText", argumentTypes = { String.class }) // HasText
	})
	@InputSignals({ //
	@EventSignal(name = "onPointerInput", filter = LeftMouseButtonFilter.class) // ReceivesPointerInput
	})
	QLabel clientNameView = new QLabel() {
		{
			// workaround for jambi css bug
			setObjectName("ClientName");
		}
	};
	@DataContext("closeButton")
	@InputSignals({ //
	@EventSignal(name = "onPointerInput", filter = LeftMouseButtonFilter.class) // ReceivesPointerInput
	})
	QFrame closeButtonView = new QFrame() {
		// workaround for jambi css bug
		{
			setObjectName("ClientCloseButton");
		}
	};
	QHBoxLayout barItemlayout = new QHBoxLayout(this);
	{
		// workaround for jambi css bug
		setObjectName("ClientBarElement");

		this.barItemlayout.setContentsMargins(new QMargins(	0,
															0,
															0,
															0));
		setLayout(this.barItemlayout);

		this.barItemlayout.addWidget(this.clientIconView);
		this.barItemlayout.addWidget(this.clientNameView);
		this.barItemlayout.addWidget(this.closeButtonView);
	}
}
