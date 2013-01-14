/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.api.render.binding.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.display.input.InputModifier;
import org.trinity.foundation.api.display.input.Momentum;

/***************************************
 * Mark a method as a handler of {@link Input}. Methods that are marked can
 * optionally accept a single {@link Input} parameter. Based on the origin of
 * the input, this parameter can be cast to any of the subclasses of
 * {@code Input}.
 * 
 *************************************** 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InputSlot {

	/***************************************
	 * The valid momenta which will trigger this input slot.
	 * 
	 * @return {@link Momentum}
	 *************************************** 
	 */
	Momentum[] momentum() default { Momentum.STARTED, Momentum.STOPPED };

	/***************************************
	 * The valid modifiers which will trigger this input slot.
	 * 
	 * @return A valid modifier name. For a valid names check any of the
	 *         statically defined names in {@link InputModifier}.
	 *************************************** 
	 */
	String[] modifier() default {};
}
