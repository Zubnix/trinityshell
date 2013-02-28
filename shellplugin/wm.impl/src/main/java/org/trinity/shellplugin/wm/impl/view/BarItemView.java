package org.trinity.shellplugin.wm.impl.view;

import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;

import com.google.common.base.Preconditions;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPixmap;

@PropertySlots({ //
		@PropertySlot(propertyName = "text", methodName = "setText", argumentTypes = { String.class }),//
		@PropertySlot(propertyName = "imageData", methodName = "setImageData", argumentTypes = { byte[].class }), //
		@PropertySlot(propertyName = "imageWidth", methodName = "setImageWidth", argumentTypes = { int.class }), //
		@PropertySlot(propertyName = "imageHeight", methodName = "setImageHeight", argumentTypes = { int.class }) //
})
public class BarItemView extends QLabel {
	// TODO how to resize/sync/bind pixmap to label image?

	private int imageWidth;
	private int imageHeight;

	public void setImageData(byte[] imageData) {
		Preconditions.checkState(imageWidth > 0, "Image width must be > 0");
		Preconditions.checkState(imageHeight > 0, "Image height must be > 0");

		QPixmap pixmapImage = new QPixmap(imageWidth, imageHeight);
		pixmapImage.loadFromData(imageData);
		setPixmap(pixmapImage);
	}

	public void setImageWidth(int width) {
		this.imageWidth = width;
	}

	public void setImageHeight(int height) {
		this.imageHeight = height;
	}
}