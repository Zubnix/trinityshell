/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.api.widget;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hydrogen.api.paint.Paintable;
import org.hyperdrive.api.widget.Widget.View;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public final class ViewBinder {

	private static final Logger LOGGER = Logger.getLogger(ViewBinder.class);
	private static final String NO_VIEW_FOUND_WARNING = "Paintable type %s does not have a view bound. Paintable will not be displayed.";

	private static final Map<Class<?>, Class<?>> VIEW_BINDINGS = new HashMap<Class<?>, Class<?>>();

	private ViewBinder() {
	}

	public static <T extends View> void bindView(
			final Class<? extends T> viewItfClass,
			final Class<? extends T> viewImplClass) {
		if (!viewItfClass.isInterface()) {
			// TODO exception
		}

		if (viewImplClass.isInterface()) {
			// TODO exception
		}

		ViewBinder.VIEW_BINDINGS.put(viewItfClass, viewImplClass);
	}

	public static <T extends Widget.View> T createView(
			final Paintable paintable, final Class<T> viewInterface) {

		if (!viewInterface.isInterface()) {
			throw new IllegalArgumentException(
					"Class argument must be an interface! Got: "
							+ viewInterface);
		}

		// get view instantiating method from map and call it.
		final View view = ViewBinder.getViewInstance(paintable, viewInterface);

		// TODO instead of creating a reflection proxy, use an asm library and
		// create our own pure java proxy.
		@SuppressWarnings("unchecked")
		final T viewProxy = (T) Proxy.newProxyInstance(ViewBinder.class
				.getClassLoader(), new Class[] { viewInterface },
				new PaintInvocationHandler(paintable, view));

		return viewProxy;
	}

	private static View getViewInstance(final Paintable paintable,
			final Class<? extends Widget.View> viewInterface) {
		final Class<?> viewImplementationClass = ViewBinder.VIEW_BINDINGS
				.get(viewInterface);

		if (viewImplementationClass == null) {
			ViewBinder.LOGGER.warn(String.format(
					ViewBinder.NO_VIEW_FOUND_WARNING, paintable.getClass()));
			return null;
		}

		try {
			final View viewInstance = (View) viewImplementationClass
					.newInstance();
			return viewInstance;
		} catch (final InstantiationException e) {
			// TODO fatal exception
			e.printStackTrace();

		} catch (final IllegalAccessException e) {
			// TODO fatal exception
			e.printStackTrace();
		}
		return null;
	}
}