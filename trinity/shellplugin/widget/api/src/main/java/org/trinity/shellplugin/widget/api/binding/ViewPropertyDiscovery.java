package org.trinity.shellplugin.widget.api.binding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.render.api.PaintableSurfaceNode;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

// TODO convert to non static singleton instance
/***************************************
 * Scanner class used to discover {@link ViewProperty}s,
 * {@link ViewPropertySlot}s and {@link ViewPropertyChanged}s.
 *************************************** 
 */
@Singleton
@Bind(to = @To(value = Type.IMPLEMENTATION))
public class ViewPropertyDiscovery {
	// yo dawg, I heard you like caches so I put a cache in your cache so you
	// can cache while you cache!
	private static final Cache<Class<?>, Method[]> allViewProperties = CacheBuilder.newBuilder().build();
	private static final Cache<Class<?>, Cache<String, Optional<Method>>> viewPropertiesByName = CacheBuilder
			.newBuilder().build();
	private static final Cache<Class<?>, Optional<Method>> viewReferenceFields = CacheBuilder.newBuilder().build();
	private static final Cache<Class<?>, Optional<Method>> viewSlots = CacheBuilder.newBuilder().build();

	private final ViewSlotInvocationHandler viewSlotInvocationHandler;

	@Inject
	ViewPropertyDiscovery(final ViewSlotInvocationHandler viewSlotInvocationHandler) {
		this.viewSlotInvocationHandler = viewSlotInvocationHandler;
	}

	/****************************************
	 * Find all methods that have been annotated with {@link ViewProperty}.
	 * 
	 * @param thisObjClass
	 *            The {@link Class} to scan for annotated methods.
	 * @return All found {@link Method}s
	 * @throws ExecutionException
	 *             Thrown if a badly placed annotation is encountered. ie on a
	 *             non public method, or duplicate properties are found.
	 *************************************** 
	 */
	public Method[] lookupAllViewProperties(final Class<?> thisObjClass) throws ExecutionException {
		return ViewPropertyDiscovery.allViewProperties.get(	thisObjClass,
															new Callable<Method[]>() {
																@Override
																public Method[] call() {
																	return getAllViewProperties(thisObjClass);
																}
															});
	}

	private Method[] getAllViewProperties(final Class<?> clazz) {
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

	/***************************************
	 * Find the method annotated with {@link ViewProperty} and the specified
	 * name.
	 * 
	 * @param thisObjClass
	 *            The {@link Class} to scan for the view property.
	 * @param viewPropertyName
	 *            The name of the view property.
	 * @return a {@link Method}.
	 * @throws ExecutionException
	 *             Thrown if a badly placed annotation is encountered. ie on a
	 *             non public method, or duplicate properties are found.
	 *************************************** 
	 */
	public Optional<Method> lookupViewProperty(	final Class<?> thisObjClass,
												final String viewPropertyName) throws ExecutionException {
		return ViewPropertyDiscovery.viewPropertiesByName.get(	thisObjClass,
																new Callable<Cache<String, Optional<Method>>>() {
																	@Override
																	public Cache<String, Optional<Method>> call() {
																		return CacheBuilder.newBuilder().build();
																	}
																}).get(	viewPropertyName,
																		new Callable<Optional<Method>>() {
																			@Override
																			public Optional<Method> call() {
																				return getViewProperty(	thisObjClass,
																										viewPropertyName);
																			}
																		});
	}

	private Optional<Method> getViewProperty(	final Class<?> clazz,
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

		return Optional.fromNullable(foundMethod);
	}

	private String findViewPropertyName(final Method method) {
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

	private boolean isDuplicateViewProperty(final List<ViewProperty> viewProperties,
											final ViewProperty viewProperty) {
		for (final ViewProperty addedViewAttribute : viewProperties) {
			if (addedViewAttribute.value().equals(viewProperty.value())) {
				return true;
			}
		}
		return false;
	}

	/***************************************
	 * Find the getter annotated with {@link ViewReference}.
	 * 
	 * @param clazz
	 *            The {@link Class} to scan.
	 * @return The found getter method.
	 * @throws ExecutionException
	 *             Thrown if a badly placed annotation is encountered. ie on a
	 *             non public method, or multiple views are found.
	 *************************************** 
	 */
	public Optional<Method> lookupViewReference(final Class<?> clazz) throws ExecutionException {

		return ViewPropertyDiscovery.viewReferenceFields.get(	clazz,
																new Callable<Optional<Method>>() {
																	@Override
																	public Optional<Method> call() {
																		return getViewReferenceObject(clazz);
																	}
																});
	}

	private Optional<Method> getViewReferenceObject(final Class<?> clazz) {
		Method foundMethod = null;
		final Method[] methods = clazz.getMethods();
		for (final Method method : methods) {
			final ViewReference viewReference = method.getAnnotation(ViewReference.class);
			if ((viewReference != null) && (foundMethod != null)) {
				throw new IllegalArgumentException(String.format(	"Found multiple %s on %s",
																	ViewReference.class.getName(),
																	clazz.getName()));
			} else if (viewReference != null) {
				foundMethod = method;
			}
		}

		return Optional.fromNullable(foundMethod);
	}

	/***************************************
	 * Find the method annotated with a {@link ViewPropertySlot} with the given
	 * name.
	 * 
	 * @param clazz
	 *            The view {@link Class} to scan.
	 * @param viewPropertyName
	 *            The name that should be present in the
	 *            {@code  ViewPropertySlot}.
	 * @return The found method.
	 * @throws ExecutionException
	 *             Thrown if a badly placed annotation is found.
	 *************************************** 
	 */
	public Optional<Method> lookupViewSlot(	final Class<?> clazz,
											final String viewPropertyName) throws ExecutionException {
		return ViewPropertyDiscovery.viewSlots.get(	clazz,
													new Callable<Optional<Method>>() {
														@Override
														public Optional<Method> call() {
															return getViewSlot(	clazz,
																				viewPropertyName);
														}
													});
	}

	private Optional<Method> getViewSlot(	final Class<?> clazz,
											final String viewAttributeName) {
		Method foundMethod = null;

		for (final Method method : clazz.getMethods()) {

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

		return Optional.fromNullable(foundMethod);
	}

	/***************************************
	 * Manually notify a view slot.
	 * 
	 * @param dataContextClass
	 *            The class of the object the encloses the annotated view
	 *            object.
	 * @param dataContext
	 *            The instance that encloses the view object.
	 * @param propertyNames
	 *            The names of the view properties who's matching view slots
	 *            should be notified.
	 * @throws ExecutionException
	 *             Thrown if a badly placed annotation is encountered.
	 *************************************** 
	 */
	public <T> void notifyViewSlot(	final Class<? extends T> dataContextClass,
									final T dataContext,
									final String... propertyNames) throws ExecutionException {

		final Object thisObj = dataContext;
		final Class<?> thisObjClass = dataContextClass;

		final String[] viewAttributeIds = propertyNames;

		final Optional<Method> viewReference = lookupViewReference(thisObjClass);
		if (!viewReference.isPresent()) {
			return;
		}

		final Method viewField = viewReference.get();
		Object view;
		try {
			view = viewField.invoke(thisObj);

			for (final String viewAttributeName : viewAttributeIds) {
				final Optional<Method> viewPropertyMethod = lookupViewProperty(	thisObjClass,
																				viewAttributeName);
				if (!viewPropertyMethod.isPresent()) {
					continue;
				}

				final Optional<Method> viewSlotMethod = lookupViewSlot(	view.getClass(),
																		viewAttributeName);
				if (!viewSlotMethod.isPresent()) {
					continue;
				}

				Object argument;

				argument = viewPropertyMethod.get().invoke(thisObj);

				this.viewSlotInvocationHandler.invokeSlot(	(PaintableSurfaceNode) thisObj,
															viewPropertyMethod.get().getAnnotation(ViewProperty.class),
															view,
															viewSlotMethod.get(),
															argument);

			}
		} catch (final IllegalAccessException e) {
			throw new ExecutionException(e);
		} catch (final IllegalArgumentException e) {
			throw new ExecutionException(e);
		} catch (final InvocationTargetException e) {
			throw new ExecutionException(e);
		}
	}
}
