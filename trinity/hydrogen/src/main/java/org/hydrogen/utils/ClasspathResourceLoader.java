/*
 * This file is part of Hydrogen.
 * 
 * Hydrogen is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hydrogen is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * A convenience class for referencing
 * resources (files) that live on the classpath, be it in jar/war/... files or a
 * plain directory, as a Java <code>File</code>s.
 * 
 * @author Erik De Rijcke, Ryan Schmitt
 * @since 1.0
 */
public class ClasspathResourceLoader {
	private final String tempFileNamePrefix;

	/**
	 * Construct a default <code>ClasspathResourceLoader</code>. Every loaded
	 * resource will be placed in the operating system's temporary directory and
	 * prefixed with "hydro_tmp_resource" when no destination <code>File</code>
	 * is given.
	 */
	public ClasspathResourceLoader() {
		this("hydro_tmp_resource");
	}

	/**
	 * Construct a <code>ClasspathResourceLoader</code> with the given
	 * tempFileNamePrefix. Every loaded resource will be placed in the operating
	 * system's temporary directory and prefixed with the given
	 * tempFileNamePrefix when no destination <code>File</code> is given.
	 * 
	 * @param tempFileNamePrefix
	 *            The file name prefix this <code>ClasspathResourceLoader</code>
	 *            will use when a resource is extracted.
	 */
	public ClasspathResourceLoader(final String tempFileNamePrefix) {
		this.tempFileNamePrefix = tempFileNamePrefix;
	}

	/**
	 * Copy all data from a given <code>InputStrea</code> to a given
	 * <code>OutputStream</code>.
	 * 
	 * @param in
	 *            The source {@link InputStream} where all data will be read.
	 * @param out
	 *            The destination {@link OutputStream} where all data will be
	 *            written.
	 * @throws IOException
	 *             Thrown when an error occurs in one of the given streams.
	 */
	private void copyStreamData(final InputStream in, final OutputStream out)
			throws IOException {
		final byte[] buffer = new byte[8192];
		int len = in.read(buffer);
		while (len > 0) {
			out.write(buffer, 0, len);
			len = in.read(buffer);
		}
	}

	/**
	 * Load a <code>File</code> from this <code>ClasspathResourceLoader</code>'s
	 * classpath.
	 * 
	 * @param resource
	 *            The name of the resource <code>File</code>, relative to this
	 *            <code>ClasspathResourceLoader</code>'s classpath.
	 * @return A {@link File}.
	 * @throws IOException
	 *             Thrown when an error occurs when reading the resource.
	 */
	public File fromClassPath(final String resource) throws IOException {
		final File tmpFile = fromClassPath(resource, getClass()
				.getClassLoader(), File.createTempFile(this.tempFileNamePrefix,
				null));
		tmpFile.deleteOnExit();
		return tmpFile;
	}

	/**
	 * Load a <code>File</code> from the given <code>ClassLoader</code>'s
	 * classpath.
	 * 
	 * @param resource
	 *            The name of the resource <code>File</code>, relative to the
	 *            given <code>ClassLoader</code>'s classpath.
	 * @param classLoader
	 *            A {@link ClassLoader}.
	 * @return A {@link File}.
	 * @throws IOException
	 *             Thrown when an error occurs when reading the resource.
	 */
	public File fromClassPath(final String resource,
			final ClassLoader classLoader) throws IOException {
		final File tmpFile = fromClassPath(resource, classLoader,
				File.createTempFile(this.tempFileNamePrefix, null));
		tmpFile.deleteOnExit();
		return tmpFile;
	}

	/**
	 * Load a <code>File</code> from the given <code>ClassLoader</code>'s
	 * classpath into the given destination <code>File</code>.
	 * 
	 * @param resource
	 *            The name of the resource <code>File</code>, relative to the
	 *            given <code>ClassLoader</code>'s classpath.
	 * @param classLoader
	 *            A {@link ClassLoader}.
	 * @param destResourceFile
	 *            A destination resource <code>File</code> that will reference
	 *            the loaded classpath resource.
	 * @return The destination resource <code>File</code> referencing the
	 *         classpath resource.
	 * @throws IOException
	 *             Thrown when an error occurs when reading the resource.
	 */
	public File fromClassPath(final String resource,
			final ClassLoader classLoader, final File destResourceFile)
			throws IOException {
		final URL resourceURL = classLoader.getResource(resource);
		final InputStream in = resourceURL.openStream();
		final FileOutputStream out = new FileOutputStream(destResourceFile);
		copyStreamData(in, out);
		out.flush();
		in.close();
		out.close();
		return destResourceFile;
	}

	/**
	 * 
	 * @param resource
	 * @param resourceFile
	 * @return The destination resource <code>File</code> referencing the
	 *         classpath resource.
	 * @throws IOException
	 *             Thrown when an error occurs when reading the resource.
	 */
	public File fromClassPath(final String resource, final File resourceFile)
			throws IOException {
		return fromClassPath(resource, getClass().getClassLoader(),
				resourceFile);
	}
}