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

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;

/**
 * An <code>ArgumentParser</code> is responsible for parsing all arguments that
 * are given when Hypercube is launched through <code>Hypercube</code>.
 * <p>
 * An <code>ArgumentParser</code> defines a number of possible arguments. These
 * arguments follow the traditional Unix style of argument passing. Either
 * through a short form, for example "-d :1", or through a long form, for
 * example "--display :1".
 * <p>
 * The following arguments are defined:
 * <p>
 * <code> -d &lt;display&gt;, --display
 * &lt;display&gt;</code>
 * 
 * <pre>
 * 	The display address of X display server.
 * 	When omitted the DISPLAY environment variable will be read.
 * </pre>
 * 
 * 
 * -l &lt;logic directory&gt;, --logicDir &lt;logic directory&gt;
 * 
 * <pre>
 * 	The directory where the script(s) that define the behaviour of Hypercube can
 * 	be found. When omitted, first the user's ".config/hypbercube/" directory is
 * 	read. If this directory does not exist, the "/etc/xdg/hypercube" directory is
 * 	read. If this directory does not exist, the build in logic directory of
 * 	Hypercube is read.
 * </pre>
 * <p>
 * 
 * 
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ArgumentParser {

	private final String displayNameDefault = System.getenv("DISPLAY");

	private static final String DISPLAY_FLAG_LONG = "display";
	private static final char DISPLAY_FLAG_SHORT = 'd';
	private final static String LOGIC_DIR_DEFAULT = String.format(
			"%s/.config/hypercube/", System.getProperty("user.home"));
	private static final String LOGIC_DIR_FLAG_LONG = "logicDir";
	private static final char LOGIC_DIR_FLAG_SHORT = 'l';

	private final JSAP jSapArgumentParser;
	private final JSAPResult jSapParsedArguments;

	/**
	 * Create a new <code>ArgumentParser</code>. The given <code>String</code>
	 * array will be checked for known arguments.
	 * 
	 * @param arguments
	 *            A <code>String</code> array.
	 * @throws JSAPException
	 */
	public ArgumentParser(final String[] arguments) throws JSAPException {
		// checkLogicDir();
		this.jSapArgumentParser = new JSAP();

		this.initArgumentParser();

		this.jSapParsedArguments = this.jSapArgumentParser.parse(arguments);
	}

	/**
	 * The parsed display address.
	 * <p>
	 * When no display address was given for parsing, the environment DISPLAY
	 * variable value is returned.
	 * 
	 * @return A <code>String</code>.
	 */
	public String getDisplayName() {
		return this.jSapParsedArguments
				.getString(ArgumentParser.DISPLAY_FLAG_LONG);
	}

	/**
	 * The parsed logic directory.
	 * <p>
	 * When no logic directory was given for parsing, the user's
	 * ".config/hypbercube/" directory is returned. If this directory does not
	 * exist, the "/etc/xdg/hypercube" directory is returned. If this directory
	 * does not exist, the build in logic directory of Hypercube is returned.
	 * 
	 * @return A <code>String</code>.
	 */
	public String getLogicDir() {
		return this.jSapParsedArguments
				.getString(ArgumentParser.LOGIC_DIR_FLAG_LONG);
	}

	/**
	 * Initialize the short and long arguments and their default values.
	 * 
	 * @throws JSAPException
	 *             Thrown when the initialization fails.
	 */
	private void initArgumentParser() throws JSAPException {
		{// display name.
			final FlaggedOption displayName = new FlaggedOption(
					ArgumentParser.DISPLAY_FLAG_LONG);
			displayName.setStringParser(JSAP.STRING_PARSER);
			displayName.setShortFlag(ArgumentParser.DISPLAY_FLAG_SHORT)
					.setLongFlag(ArgumentParser.DISPLAY_FLAG_LONG);
			displayName.setDefault(this.displayNameDefault);
			this.jSapArgumentParser.registerParameter(displayName);
		}
		{// read logic
			final FlaggedOption logicDir = new FlaggedOption(
					ArgumentParser.LOGIC_DIR_FLAG_LONG);
			logicDir.setStringParser(JSAP.STRING_PARSER);
			logicDir.setShortFlag(ArgumentParser.LOGIC_DIR_FLAG_SHORT)
					.setLongFlag(ArgumentParser.LOGIC_DIR_FLAG_LONG);
			logicDir.setDefault(ArgumentParser.LOGIC_DIR_DEFAULT);
			this.jSapArgumentParser.registerParameter(logicDir);
		}// TODO more arguments.
	}

}
