package org.hyperdrive.acceptance.geo;

import junit.framework.Assert;

import org.hyperdrive.geo.GeoManager;
import org.hyperdrive.geo.GeoTransformableRectangle;
import org.hyperdrive.geo.HasGeoManager;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.embedder.Embedder;
import org.mockito.Mockito;

//TODO we want to refactor this class to a general template for 
//geomanager acceptance tests.
//TODO we want all subclasses of GeoTransformableRectangles to use and pass these tests.
/**
 * GeoManager.story
 * 
 * @author Erik De Rijcke
 * 
 */
public class GeoManagerStories extends Embedder {

	private GeoTransformableRectangle parent;
	private GeoTransformableRectangle child;

	// TODO write parameterconverter
	@Given("a $geovirtrectangle parent with a $direct geomanager")
	public void aParentWithADirectGeoManager(
			final GeoTransformableRectangle parent, final GeoManager geoManager) {
		if (parent instanceof HasGeoManager) {
			final HasGeoManager geoManagerAware = (HasGeoManager) parent;
			// we use mockito for something it wasn't intended for but does very
			// well...
			// TODO find a way without using mockito?
			Mockito.when(Mockito.spy(geoManagerAware).getGeoManager())
					.thenReturn(geoManager);
			this.parent = parent;
		} else {
			throw new RuntimeException(
					"Parent in story is not a 'HasGeoManager' instead got: "
							+ parent);
		}
	}

	@Given("a geovirtrectangle child")
	public void a(final GeoTransformableRectangle child) {
		child.setParent(this.parent);
		this.child = child;
	}

	@When("the child sets a new horizontal $position")
	public void theChildSetsANewHorizontal(final int position) {
		this.child.setRelativeX(position);
	}

	@When("the child sets a new vertical $position")
	public void theChildSetsANewVertical(final int position) {
		this.child.setRelativeY(position);
	}

	@When("the child sets a new $width")
	public void theChildSetsANewW(final int width) {
		this.child.setWidth(width);
	}

	@When("the child sets a new $height")
	public void theChildSetsANewH(final int height) {
		this.child.setHeight(height);
	}

	@When("the child requests to update it's geometry")
	public void theChildRequestsToUpdateItsGeometry() {
		this.child.requestMoveResize();
	}

	@When("the child requests to update it's size")
	public void theChildRequetsToUpdateItsSize() {
		this.child.requestResize();
	}

	@When("the child requests to update it's position")
	public void theChildRequetsToUpdateItsPosition() {
		this.child.requestMove();
	}

	@Then("the child should be updated to the new horizontal $position")
	public void theChildShouldBeUpdatedToTheNewHorizontal(final int position) {
		Assert.assertEquals(position, this.child.getRelativeX());
	}

	@Then("the child should be updated to the new vertical $position")
	public void theChildShouldBeUpdatedToTheNewVertical(final int position) {
		Assert.assertEquals(position, this.child.getRelativeY());
	}

	@Then("the child should be updated to the new $width")
	public void theChildShouldBeUpdatedToTheNewW(final int width) {
		Assert.assertEquals(width, this.child.getWidth());
	}

	@Then("the child should be updated to the new $height")
	public void theChildShouldBeUpdatedToTheNewH(final int height) {
		Assert.assertEquals(height, this.child.getHeight());
	}
}