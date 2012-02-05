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
package org.hypercube;

/**
 * A <code>LogicLoaderFactory</code> creates new <code>LogicLoader</code>
 * implementations.
 * <p>
 * The implementations of the created <code>LogicLoader</code>s defines what
 * kind of behavioral scripts it can load.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface LogicLoaderFactory {
	/**
	 * Create a new <code>LogicLoader</code>.
	 * <p>
	 * The implementations of the created <code>LogicLoader</code>s defines what
	 * kind of behavioral scripts it can load.
	 * 
	 * @return A {@link LogicLoader}.
	 */
	LogicLoader newLogicLoader();
}
