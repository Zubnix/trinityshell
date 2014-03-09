package org.trinity.foundation.api.binding.binding;

import com.google.common.base.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ConstantDataModelPropertyTest {

	@Mock
	private Object                    rootDataModel;
	@InjectMocks
	private ConstantDataModelProperty constantDataModelProperty;

	@Test
	public void test() {
		//given
		//a root data model property

		//when
		//the property value is requested
		final Optional<Object> propertyValue = this.constantDataModelProperty.getPropertyValue();

		//then
		//the same present root data model is returned
		assertTrue(propertyValue.isPresent());
		assertEquals(this.rootDataModel,
					 propertyValue.get());
	}
}