package org.trinity.shellplugin.wm.impl.view;

import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shellplugin.wm.api.ImageDefinition;

import com.google.common.base.Preconditions;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPixmap;

@PropertySlots({ //
		@PropertySlot(propertyName = "text", methodName = "setText", argumentTypes = { String.class }),// HasText
		@PropertySlot(propertyName = "imageDefinition", methodName = "setImageDefinition", argumentTypes = { ImageDefinition.class }) // HasImageDefinition
})
class BarItemView extends QLabel {

	{
		// workaround for jambi css bug
		setObjectName(getClass().getSimpleName());
	}

	public void setImageDefinition(final ImageDefinition imageDefinition) {
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

}