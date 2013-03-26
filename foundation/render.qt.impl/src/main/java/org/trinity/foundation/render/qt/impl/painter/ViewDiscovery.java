package org.trinity.foundation.render.qt.impl.painter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.render.binding.error.BindingError;
import org.trinity.foundation.api.render.binding.model.ViewReference;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.trolltech.qt.gui.QWidget;

public class ViewDiscovery {

	private static final Cache<Class<?>, Optional<Method>> views = CacheBuilder.newBuilder().build();

	ViewDiscovery() {
	}

	public QWidget lookupView(final Object model) {

		final Class<?> modelClass = model.getClass();

		try {
			final Optional<Method> viewGetter = ViewDiscovery.views.get(model.getClass(),
																		new Callable<Optional<Method>>() {
																			@Override
																			public Optional<Method> call() {
																				return getView(modelClass);
																			}
																		});
			if (viewGetter.isPresent()) {
				final Method viewGetterMethod = viewGetter.get();
				try {
					final Object view = viewGetterMethod.invoke(model);
					if (view instanceof QWidget) {
						return (QWidget) view;
					} else {
						throw new BindingError(String.format(	"Expected view %s of model %s to be of type %s",
																view,
																model,
																QWidget.class.getName()));
					}

				} catch (final IllegalAccessException e) {
					throw new BindingError(	String.format(	"Can not invoke view getter for model %s. Did you declare it public?",
															model),
											e);
				} catch (final IllegalArgumentException e) {
					// TODO explanation.
					throw new BindingError(	"",
											e);
				} catch (final InvocationTargetException e) {
					// TODO explanation.
					throw new BindingError(	String.format(	"Invoking the view getter for model %s throws an exception.",
															model),
											e);
				}
			} else {
				throw new BindingError(String.format(	"Can not find view getter on class %s for model %s. Did you annotate a getter with %s ?",
														modelClass.getName(),
														model,
														ViewReference.class.getName()));
			}
		} catch (final ExecutionException e1) {
			Throwables.propagate(e1);
			return null;
		}

	}

	protected Optional<Method> getView(final Class<?> clazz) {
		Method foundMethod = null;
		final Method[] methods = clazz.getMethods();
		for (final Method method : methods) {
			final ViewReference viewReference = method.getAnnotation(ViewReference.class);
			if ((viewReference != null) && (foundMethod != null)) {
				throw new BindingError(String.format(	"Found multiple %s on %s",
														ViewReference.class.getName(),
														clazz.getName()));
			} else if (viewReference != null) {
				foundMethod = method;
			}
		}

		return Optional.fromNullable(foundMethod);
	}
}
