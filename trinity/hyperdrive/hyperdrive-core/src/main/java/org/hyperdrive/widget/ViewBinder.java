package org.hyperdrive.widget;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hydrogen.paintinterface.Paintable;
import org.hyperdrive.widget.Widget.View;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public final class ViewBinder {

	static final Logger LOGGER = Logger.getLogger(ViewBinder.class);
	static final String NULL_PAINT_INSTR_ERROR = "Received null PaintInstruction from %s. Ignoring paint call.";
	private static final String NO_VIEW_FOUND_WARNING = "Paintable type %s does not have a view bound. Paintable will not be displayed.";
	static final String NO_VIEW_INSTANCE_SET_WARNING = "Received view update request but no view instance is set for paintable %s. Ignoring.";

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