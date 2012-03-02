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
package org.hypercube.startup;

import java.net.URISyntaxException;

import org.fusion.qt.x11.QFusionDisplayConfiguration;
import org.hypercube.Hypercube;
import org.hypercube.hyperlogic.GroovyLogicLoaderFactory;

import com.martiansoftware.jsap.JSAPException;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke, Ryan Schmitt
 * @since 1.0
 */
public class Go {
	/**
	 * 
	 * @param args
	 */
	public static void main(final String[] args) throws JSAPException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException, URISyntaxException {

		// TODO fix uncaught exceptions

		new Hypercube(new GroovyLogicLoaderFactory(),
				new QFusionDisplayConfiguration(), args);
	}
}
