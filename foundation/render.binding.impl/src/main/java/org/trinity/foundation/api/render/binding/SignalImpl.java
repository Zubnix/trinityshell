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

package org.trinity.foundation.api.render.binding;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.render.binding.model.delegate.Signal;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

public class SignalImpl implements Signal {

	private static final Logger LOG = LoggerFactory.getLogger(SignalImpl.class);
	private static Cache<HashCode, Optional<Method>> eventSlotsByHash = CacheBuilder.newBuilder().concurrencyLevel(1)
			.build();
	private final ListeningExecutorService modelExecutor;
	private final Object view;
	private final Map<Object, Object> dataContextValueByView;
	private final String inputSlotName;

	SignalImpl(	final ListeningExecutorService modelExecutor,
				final Object view,
				final Map<Object, Object> dataContextValueByView,
				final String inputSlotName) {
		this.modelExecutor = modelExecutor;
		this.view = view;
		this.dataContextValueByView = dataContextValueByView;
		this.inputSlotName = inputSlotName;
	}

	private static Optional<Method> findSlot(	final Class<?> modelClass,
												final String methodName) throws ExecutionException {

		final HashCode hashCode = Hashing.goodFastHash(32).newHasher().putInt(modelClass.hashCode())
				.putString(methodName).hash();

		return eventSlotsByHash.get(hashCode,
									new Callable<Optional<Method>>() {
										@Override
										public Optional<Method> call() {
											return getSlot(	modelClass,
															methodName);
										}
									});
	}

	private static Optional<Method> getSlot(final Class<?> modelClass,
											final String methodName) {
		Method inputSlot = null;
		try {
			inputSlot = modelClass.getMethod(methodName);
		} catch (final SecurityException e) {
			LOG.error(	"Error while trying to find an input slot for class=" + modelClass + " with slotname="
								+ methodName,
						e);
		} catch (final NoSuchMethodException e) {
			LOG.warn(	"No input slot found for class=" + modelClass + " with slotname=" + methodName,
						e);
		}
		return Optional.fromNullable(inputSlot);
	}

	@Override
	public ListenableFuture<Void> fire() {
		return modelExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				final Object viewModel = dataContextValueByView.get(view);
				final Optional<Method> optionalInputSlot = findSlot(viewModel.getClass(),
																	inputSlotName);
				if (optionalInputSlot.isPresent()) {
					final Method method = optionalInputSlot.get();
					method.setAccessible(true);
					method.invoke(viewModel);
				}
				return null;
			}
		});
	}
}
