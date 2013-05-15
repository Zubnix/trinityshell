package org.trinity.shellplugin.wm.x11.impl.view;

import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.binding.view.DataContext;
import org.trinity.foundation.api.render.binding.view.InputSignal;
import org.trinity.foundation.api.render.binding.view.InputSignals;
import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shellplugin.wm.api.ImageDefinition;

import com.google.common.base.Preconditions;
import com.trolltech.qt.core.QMargins;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPixmap;

class ClientBarElementView extends QLabel {

	@PropertySlots({ //
	@PropertySlot(propertyName = "imageDefinition", methodName = "setImageDefinition", argumentTypes = { ImageDefinition.class }) // HasImageDefinition
	})
	@InputSignals({ //
	@InputSignal(name = "onPointerInput", inputType = PointerInput.class) // ReceivesPointerInput
	})
	QLabel clientIconView = new QLabel() {
		{
			// workaround for jambi css bug
			setObjectName("clientIconView");
		}

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
	@InputSignal(name = "onPointerInput", inputType = PointerInput.class) // ReceivesPointerInput
	})
	QLabel clientNameView = new QLabel() {
		{
			// workaround for jambi css bug
			setObjectName("clientNameView");
		}
	};

	@DataContext("closeButton")
	@InputSignals({ //
	@InputSignal(name = "onPointerInput", inputType = PointerInput.class) // ReceivesPointerInput
	})
	QFrame closeButtonView = new QFrame() {
		// workaround for jambi css bug
		{
			setObjectName("CloseButtonView");
		}
	};

	QHBoxLayout barItemlayout = new QHBoxLayout(this);

	{
		// workaround for jambi css bug
		setObjectName(getClass().getSimpleName());

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