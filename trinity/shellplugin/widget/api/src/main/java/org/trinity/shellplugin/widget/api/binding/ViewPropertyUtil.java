package org.trinity.shellplugin.widget.api.binding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.render.api.PaintableSurfaceNode;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

// TODO convert to non static singleton instance
/***************************************
 * Utility class used by {@link ViewPropertySignalDispatcher} to scan for
 * {@link ViewProperty}s, {@link ViewPropertySlot}s and
 * {@link ViewPropertyChanged}s.
 *************************************** 
 */
public class ViewPropertyUtil {
	// yo dawg, I heard you like caches so I put a cache in your cache so you
	// can cache while you cache!
	private static final Cache<Class<?>, Method[]> allViewProperties = CacheBuilder.newBuilder().build();
	private static final Cache<Class<?>, Cache<String, Method>> viewPropertiesByName = CacheBuilder.newBuilder()
			.build();
	private static final Cache<Class<?>, Method> viewReferenceFields = CacheBuilder.newBuilder().build();
	private static final Cache<Class<?>, Method> viewSlots = CacheBuilder.newBuilder().build();

	private ViewPropertyUtil() {

	}

	public static Method[] lookupAllViewProperties(final Class<?> thisObjClass) throws ExecutionException {
		return ViewPropertyUtil.allViewProperties.get(	thisObjClass,
														new Callable<Method[]>() {
															@Override
															public Method[] call() {
																return getAllViewProperties(thisObjClass);
															}
														});
	}

	private static Method[] getAllViewProperties(final Class<?> clazz) {
		final List<Method> allViewProperties = new ArrayList<Method>();

		final List<Method> viewProperties = new ArrayList<Method>();
		final List<ViewProperty> viewPropertyAnnotations = new ArrayList<ViewProperty>();

		for (final Method method : clazz.getMethods()) {
			final ViewProperty viewPropertyAnnotation = method.getAnnotation(ViewProperty.class);
			if (viewPropertyAnnotation == null) {
				continue;
			}

			if (isDuplicateViewProperty(viewPropertyAnnotations,
										viewPropertyAnnotation)) {
				throw new IllegalArgumentException(String.format(	"Found duplicate %s with name=%s on %s",
																	ViewProperty.class.getName(),
																	viewPropertyAnnotation.value(),
																	clazz.getName()));
			}

			viewPropertyAnnotations.add(viewPropertyAnnotation);
			viewProperties.add(method);
		}
		allViewProperties.addAll(viewProperties);

		return allViewProperties.toArray(new Method[] {});
	}

	public static Method lookupViewProperty(final Class<?> thisObjClass,
											final String viewPropertyName) throws ExecutionException {
		return ViewPropertyUtil.viewPropertiesByName.get(	thisObjClass,
															new Callable<Cache<String, Method>>() {
																@Override
																public Cache<String, Method> call() {
																	return CacheBuilder.newBuilder().build();
																}
															}).get(	viewPropertyName,
																	new Callable<Method>() {
																		@Override
																		public Method call() {
																			return getViewProperty(	thisObjClass,
																									viewPropertyName);
																		}
																	});
	}

	private static Method getViewProperty(	final Class<?> clazz,
											final String viewPropertyName) {

		final Method[] viewProperties = getAllViewProperties(clazz);

		Method foundMethod = null;
		for (final Method method : viewProperties) {
			final String foundViewPropertyName = findViewPropertyName(method);
			if (foundViewPropertyName.equals(viewPropertyName)) {
				foundMethod = method;
				break;
			}
		}

		if (foundMethod == null) {
			// TODO return 'null' Method and don't throw an exception.
			throw new IllegalArgumentException(String.format(	"Found no %s with name: %s on %s",
																ViewProperty.class.getName(),
																viewPropertyName,
																clazz.getName()));
		}
		return foundMethod;
	}

	private static String findViewPropertyName(final Method method) {
		final String viewPropertyName = method.getAnnotation(ViewProperty.class).value();
		if (!viewPropertyName.equals("")) {
			return viewPropertyName;
		}

		final StringBuilder sb = new StringBuilder();
		final String methodName = method.getName();

		int methodNameFirstCharIdx = 0;
		if (methodName.startsWith("get")) {
			methodNameFirstCharIdx = 3;
		} else if (methodName.startsWith("is")) {
			methodNameFirstCharIdx = 2;
		}

		return sb.append(Character.toLowerCase(methodName.charAt(methodNameFirstCharIdx)))
				.append(methodName.substring(methodNameFirstCharIdx + 1)).toString();

	}

	private static boolean isDuplicateViewProperty(	final List<ViewProperty> viewProperties,
													final ViewProperty viewProperty) {
		for (final ViewProperty addedViewAttribute : viewProperties) {
			if (addedViewAttribute.value().equals(viewProperty.value())) {
				return true;
			}
		}
		return false;
	}

	public static Method lookupViewReferenceObject(final Class<?> clazz) throws ExecutionException {

		return ViewPropertyUtil.viewReferenceFields.get(clazz,
														new Callable<Method>() {
															@Override
															public Method call() {
																return getViewReferenceObject(clazz);
															}
														});
	}

	private static Method getViewReferenceObject(final Class<?> clazz) {
		Method foundMethod = null;
		final Method[] declaredMethods = clazz.getMethods();
		for (final Method method : declaredMethods) {
			final ViewReference viewReference = method.getAnnotation(ViewReference.class);
			if ((viewReference != null) && (foundMethod != null)) {
				throw new IllegalArgumentException(String.format(	"Found multiple %s on %s",
																	ViewReference.class.getName(),
																	clazz.getName()));
			} else if (viewReference != null) {
				foundMethod = method;
			}
		}
		if (foundMethod == null) {
			// TODO return a 'null' method instead of throwing an exception.
			throw new IllegalArgumentException(String.format(	"Found no %s on %s",
																ViewReference.class.getName(),
																clazz.getName()));
		}

		return foundMethod;
	}

	public static Method lookupViewSlot(final Class<?> clazz,
										final String viewPropertyName) throws ExecutionException {
		return ViewPropertyUtil.viewSlots.get(	clazz,
												new Callable<Method>() {
													@Override
													public Method call() {
														return getViewSlot(	clazz,
																			viewPropertyName);
													}
												});
	}

	private static Method getViewSlot(	final Class<?> clazz,
										final String viewAttributeName) {
		Method foundMethod = null;

		Class<?> searchedClass = clazz;
		while (searchedClass != null) {
			for (final Method method : searchedClass.getDeclaredMethods()) {

				final ViewPropertySlot viewSlot = method.getAnnotation(ViewPropertySlot.class);

				if (viewSlot == null) {
					continue;
				}

				final String[] values = viewSlot.value();
				if (Arrays.asList(values).contains(viewAttributeName)) {
					if ((foundMethod == null)) {
						foundMethod = method;
					} else if ((foundMethod != null)) {
						throw new IllegalArgumentException(String.format(	"Found multiple %s in the class hierarchy of %s",
																			ViewPropertySlot.class.getName(),
																			clazz.getName()));
					}
				}
			}
			searchedClass = searchedClass.getSuperclass();
		}

		return foundMethod;
	}

	public static <T> void notifyViewSlot(	final ViewSlotInvocationHandler viewSlotInvocationHandler,
											final Class<? extends T> dataContextClass,
											final T dataContext,
											final String... propertyNames) throws ExecutionException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		final Object thisObj = dataContext;
		final Class<?> thisObjClass = dataContextClass;

		final String[] viewAttributeIds = propertyNames;

		final Method viewField = ViewPropertyUtil.lookupViewReferenceObject(thisObjClass);
		final Object view = viewField.invoke(thisObj);

		for (final String viewAttributeName : viewAttributeIds) {
			final Method method = ViewPropertyUtil.lookupViewProperty(	thisObjClass,
																		viewAttributeName);
			final Method viewSlotMethod = ViewPropertyUtil.lookupViewSlot(	view.getClass(),
																			viewAttributeName);

			final Object argument = method.invoke(thisObj);

			viewSlotInvocationHandler.invokeSlot(	(PaintableSurfaceNode) thisObj,
													method.getAnnotation(ViewProperty.class),
													view,
													viewSlotMethod,
													argument);
		}
	}
}
