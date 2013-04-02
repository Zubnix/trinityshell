package org.trinity.foundation.render.qt.impl.painter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.render.binding.model.ViewReference;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.trolltech.qt.gui.QWidget;

public class ViewDiscovery {

	private static final Logger logger = LoggerFactory.getLogger(ViewDiscovery.class);

	private static final Cache<Class<?>, Optional<Method>> views = CacheBuilder.newBuilder().build();

	ViewDiscovery() {
	}

	public QWidget lookupView(final Object model) {

		logger.debug(	"Looking up view for model={}",
						model);

		final Class<?> modelClass = model.getClass();
		QWidget view = null;

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
					final Object foundView = viewGetterMethod.invoke(model);
					if (view instanceof QWidget) {
						view = (QWidget) foundView;
					} else {
						logger.error(	"Expected view={} of model={} to be of type={}",
										view,
										model,
										QWidget.class.getName());
					}
				} catch (final IllegalAccessException e) {
					logger.error(	"Can not invoke view getter for model=" + model
											+ ". Did you declare it as a no arg public method?",
									e);
				} catch (final IllegalArgumentException e) {
					logger.error(	"Can not invoke view getter for model=" + model
											+ ". Did you declare it as a no arg public method?",
									e);
				} catch (final InvocationTargetException e) {
					logger.error(	"Invoking the view getter for model=" + model + " throws an exception.",
									e);
				}
			} else {
				logger.error(	"Can not find view getter on class={} for model={}. Did you annotate a getter with {}?",
								modelClass.getName(),
								model,
								ViewReference.class.getName());
			}
		} catch (final ExecutionException e1) {
			logger.error(	"Exception while searching for a view on model=" + model,
							e1);
		}
		return view;
	}

	protected Optional<Method> getView(final Class<?> clazz) {
		Method foundMethod = null;
		final Method[] methods = clazz.getMethods();
		for (final Method method : methods) {
			final ViewReference viewReference = method.getAnnotation(ViewReference.class);
			if ((viewReference != null) && (foundMethod != null)) {
				logger.error(	"Found multiple {} on {}",
								ViewReference.class.getName(),
								clazz.getName());
			} else if (viewReference != null) {
				foundMethod = method;
			}
		}

		return Optional.fromNullable(foundMethod);
	}
}
