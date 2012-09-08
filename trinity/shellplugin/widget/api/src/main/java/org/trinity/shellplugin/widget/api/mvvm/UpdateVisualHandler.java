package org.trinity.shellplugin.widget.api.mvvm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class UpdateVisualHandler implements MethodInterceptor {

	private final Cache<Class<?>, Cache<String, Field>> classVisualFieldBindings = CacheBuilder.newBuilder().build();
	private final Cache<Class<?>, Field> classViewFieldBinding = CacheBuilder.newBuilder().build();

	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		final Class<?> objectClass = invocation.getThis().getClass();
		final ViewSignal viewSignal = invocation.getMethod().getAnnotation(ViewSignal.class);
		final List<Field> fields = getVisualFieldsFromClass(objectClass,
															viewSignal.value());
		if (fields.isEmpty()) {
			return invocation.proceed();
		}

		// TODO Auto-generated method stub

		return null;
	}

	private List<Field> getVisualFieldsFromClass(	final Class<?> clazz,
													final String[] visualIds) throws ExecutionException {

		final List<Field> fields = new ArrayList<>(visualIds.length);
		for (final String visualId : visualIds) {
			final Field field = this.classVisualFieldBindings.get(	clazz,
																	new Callable<Cache<String, Field>>() {
																		@Override
																		public Cache<String, Field> call()
																				throws Exception {
																			return CacheBuilder.newBuilder().build();
																		}
																	}).get(	visualId,
																			new Callable<Field>() {
																				@Override
																				public Field call() throws Exception {
																					return getFieldFromVisualId(objectClass
																														.getDeclaredFields(),
																												visualId);
																				}
																			});
			if (field != null) {
				fields.add(field);
			}
		}

		return fields;
	}

	private Field getFieldFromVisualId(	final Field[] fields,
										final String visualId) {
		for (final Field field : fields) {
			final Visual visual = field.getAnnotation(Visual.class);
			if ((visual != null) && visual.value().equals(visualId)) {
				return field;
			}
		}
		return null;
	}

	private Object getView(final Class<?> clazz) {
		
		return this.classViewFieldBinding.get(clazz, valueLoader)
		
		final Field[] fields = object.getClass().getDeclaredFields();

	}
}