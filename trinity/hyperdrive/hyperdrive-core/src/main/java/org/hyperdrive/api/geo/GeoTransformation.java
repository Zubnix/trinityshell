package org.hyperdrive.api.geo;


public interface GeoTransformation {
	int getDeltaHeight();

	int getDeltaWidth();

	int getDeltaX();

	int getDeltaY();

	int getHeight0();

	int getHeight1();

	GeoTransformableRectangle getParent0();

	GeoTransformableRectangle getParent1();

	int getWidth0();

	int getWidth1();

	int getX0();

	int getX1();

	int getY0();

	int getY1();

	boolean isVisible0();

	boolean isVisible1();
}
