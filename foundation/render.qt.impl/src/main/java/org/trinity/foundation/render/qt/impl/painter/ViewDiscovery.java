/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

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

@Deprecated
public class ViewDiscovery {

	private static final Logger LOG = LoggerFactory.getLogger(ViewDiscovery.class);
	private static final Cache<Class<?>, Optional<Method>> views = CacheBuilder.newBuilder().build();

	ViewDiscovery() {
	}

	public QWidget lookupView(final Object model) {

		LOG.debug("Looking up view for model={}",
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

					if (foundView instanceof QWidget) {
						view = (QWidget) foundView;
					} else {
						LOG.error("Expected found view={} of model={} to be of type={}",
                                foundView,
                                model,
                                QWidget.class.getName());
					}
				} catch (final IllegalAccessException e) {
					LOG.error("Can not invoke view getter for model=" + model
                            + ". Did you declare it as a no arg public method?",
                            e);
				} catch (final IllegalArgumentException e) {
					LOG.error("Can not invoke view getter for model=" + model
                            + ". Did you declare it as a no arg public method?",
                            e);
				} catch (final InvocationTargetException e) {
					LOG.error("Invoking the view getter for model=" + model + " throws an exception.",
                            e);
				}
			} else {
				LOG.error("Can not find view getter on class={} for model={}. Did you annotate a getter with {}?",
                        modelClass.getName(),
                        model,
                        ViewReference.class.getName());
			}
		} catch (final ExecutionException e1) {
			LOG.error("Exception while searching for a view on model=" + model,
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
				LOG.error("Found multiple {} on {}",
                        ViewReference.class.getName(),
                        clazz.getName());
			} else if (viewReference != null) {
				foundMethod = method;
			}
		}

		return Optional.fromNullable(foundMethod);
	}
}
