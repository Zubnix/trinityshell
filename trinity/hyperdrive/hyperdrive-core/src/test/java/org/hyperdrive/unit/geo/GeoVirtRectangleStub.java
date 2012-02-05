package org.hyperdrive.unit.geo;

import org.hyperdrive.geo.GeoEvent;
import org.hyperdrive.geo.GeoManager;
import org.hyperdrive.geo.GeoVirtRectangle;

/**
 * Expose all the things!
 * <p>
 * All protected methods are repeated so they can be accessed in the test
 * package.
 * 
 * @author Erik De Rijcke
 * 
 */
public class GeoVirtRectangleStub extends GeoVirtRectangle {
	@Override
	protected void doUpdateParentValue(final boolean execute) {
		super.doUpdateParentValue(execute);
	}

	@Override
	protected void execUpdateParentValue() {
		super.execUpdateParentValue();
	}

	@Override
	protected void doUpdateSizePlaceValue(final boolean execute) {
		super.doUpdateSizePlaceValue(execute);
	}

	@Override
	protected void execUpdateSizePlaceValue() {
		super.execUpdateSizePlaceValue();
	}

	@Override
	protected void doDestroy(final boolean execute) {
		super.doDestroy(execute);
	}

	@Override
	protected void doUpdateLower(final boolean execute) {
		super.doUpdateLower(execute);
	}

	@Override
	protected void doUpdatePlaceValue(final boolean execute) {
		super.doUpdatePlaceValue(execute);
	}

	@Override
	protected void doUpdateRaise(final boolean execute) {
		super.doUpdateRaise(execute);
	}

	@Override
	protected void doUpdateSizeValue(final boolean execute) {
		super.doUpdateSizeValue(execute);
	}

	@Override
	protected void doUpdateVisibility(final boolean execute) {
		super.doUpdateVisibility(execute);
	}

	@Override
	protected void execDestroy() {
		super.execDestroy();
	}

	@Override
	protected void handleVisibilityEvent(final GeoEvent event) {
		super.handleVisibilityEvent(event);
	}

	@Override
	protected void handleResizeEvent(final GeoEvent event) {
		super.handleResizeEvent(event);
	}

	@Override
	protected void execUpdateLower() {
		super.execUpdateLower();
	}

	@Override
	protected void execUpdatePlaceValue() {
		super.execUpdatePlaceValue();
	}

	@Override
	protected void execUpdateRaise() {
		super.execUpdateRaise();
	}

	@Override
	protected void execUpdateSizeValue() {
		super.execUpdateSizeValue();
	}

	@Override
	protected void execUpdateVisibility() {
		super.execUpdateVisibility();
	}

	@Override
	protected GeoManager getDominantGeoManager() {
		return super.getDominantGeoManager();
	}

	@Override
	protected void handleLowerEvent(final GeoEvent event) {
		super.handleLowerEvent(event);
	}

	@Override
	protected void handleMoveEvent(final GeoEvent event) {
		super.handleMoveEvent(event);
	}

	@Override
	protected void handleMoveResizeEvent(final GeoEvent event) {
		super.handleMoveResizeEvent(event);
	}

	@Override
	protected void handleOwnReparentEvent(final GeoEvent event) {
		super.handleOwnReparentEvent(event);
	}

	@Override
	protected void handleRaiseEvent(final GeoEvent event) {
		super.handleRaiseEvent(event);
	}
}