package org.hyperdrive.widget.api;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.hydrogen.paint.api.Paintable;
import org.hydrogen.paint.api.Painter;

public class ViewBinder {

	private static final ViewBinder instance = new ViewBinder();

	public static ViewBinder get() {
		return instance;
	}

	private final Map<Class<?>, Class<?>> viewBindings = new HashMap<Class<?>, Class<?>>();

	public void bind(Class<?> viewImplClass) {
		ViewImplementation viewImplementation = viewImplClass
				.getAnnotation(ViewImplementation.class);
		if (viewImplementation == null) {
			throw new IllegalArgumentException(
					String.format(
							"View implementation class %s is not annotated with a ViewImplementation.",
							viewImplClass.getName()));
		}

		Class<?>[] viewApiClasses = viewImplementation.value();
		for (Class<?> viewApiClass : viewApiClasses) {
			if (viewApiClass.isAssignableFrom(viewImplClass)) {
				viewBindings.put(viewApiClass, viewImplClass);
			} else {
				throw new IllegalArgumentException("View implementation "
						+ viewImplClass.getName()
						+ " does not implement or extend "
						+ viewApiClass.getName());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends T> getViewImplementation(Class<T> viewApiClass) {
		Class<?> viewImplClass = viewBindings.get(viewApiClass);
		if (viewImplClass == null) {
			throw new IllegalArgumentException(String.format(
					"No view implementation was found for view interface %s",
					viewApiClass.getName()));
		}
		return (Class<? extends T>) viewImplClass;
	}

	public <T> void injectView(Paintable paintable) {
		Class<?> viewExposerClass = paintable.getClass();

		Field[] fields = viewExposerClass.getDeclaredFields();
		for (Field field : fields) {
			ViewReference viewReference = field
					.getAnnotation(ViewReference.class);
			if (viewReference == null) {
				continue;
			}
			@SuppressWarnings("unchecked")
			Class<T> viewApiClass = (Class<T>) field.getType();
			Class<? extends T> viewImplementationClass = getViewImplementation(viewApiClass);

			try {
				T viewImplemenationInstance = viewImplementationClass
						.newInstance();
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}

				field.set(paintable, viewImplemenationInstance);

			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
}