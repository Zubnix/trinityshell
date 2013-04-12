package org.trinity.shell.widget.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.render.binding.model.delegate.InputSlotCallerDelegate;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class InputSlotCallerDelegateImpl implements InputSlotCallerDelegate {

	private static Logger logger = LoggerFactory.getLogger(InputSlotCallerDelegateImpl.class);
	private static Cache<HashCode, Optional<Method>> inputSlotsByHash = CacheBuilder.newBuilder().concurrencyLevel(1).build();

	private final ListeningExecutorService shellExecutor;

	@Inject
	InputSlotCallerDelegateImpl(@Named("Shell") final ListeningExecutorService shellExecutor) {
		this.shellExecutor = shellExecutor;
	}

	@Override
	public ListenableFuture<Void> callInputSlot(final Object model,
												final String methodName,
												final Input input) {

		return this.shellExecutor.submit(	new Runnable() {

												@Override
												public void run() {
													try {
														callInputSlotImpl(	model,
																			methodName,
																			input);
													} catch (final ExecutionException e) {
														logger.error(	"Error while trying to find an input slot for model=" + model + " with slotname="
																				+ methodName + " and argument " + input,
																		e);
													}
												}
											},
											null);
	}

	private void callInputSlotImpl(	final Object model,
									final String methodName,
									final Input input) throws ExecutionException {
		final Optional<Method> optionalInputSlot = findSlot(model.getClass(),
															methodName,
															input.getClass());
		if (optionalInputSlot.isPresent()) {
			try {
				optionalInputSlot.get().invoke(	model,
												input);
			} catch (final IllegalArgumentException e) {
				logger.error(	"Error while trying to invoke input slot for model=" + model + " with slotname=" + methodName + " and argument " + input,
								e);
			} catch (final IllegalAccessException e) {
				logger.error(	"Error while trying to invoke input slot for model=" + model + " with slotname=" + methodName + " and argument " + input,
								e);
			} catch (final InvocationTargetException e) {
				logger.error(	"Error while trying to invoke input slot for model=" + model + " with slotname=" + methodName + " and argument " + input,
								e);
			}
		}
	}

	private static Optional<Method> findSlot(	final Class<?> modelClass,
												final String methodName,
												final Class<? extends Input> inputType) throws ExecutionException {

		final Hasher hasher = Hashing.goodFastHash(32).newHasher();
		hasher.putInt(modelClass.hashCode()).putString(methodName).putInt(inputType.hashCode());
		final HashCode hashCode = hasher.hash();

		return inputSlotsByHash.get(hashCode,
									new Callable<Optional<Method>>() {
										@Override
										public Optional<Method> call() throws Exception {
											return getSlot(	modelClass,
															methodName,
															inputType);
										}
									});
	}

	private static Optional<Method> getSlot(final Class<?> modelClass,
											final String methodName,
											final Class<? extends Input> inputType) {
		Method inputSlot = null;
		try {
			inputSlot = modelClass.getMethod(	methodName,
												inputType);
		} catch (final SecurityException e) {
			logger.error(	"Error while trying to find an input slot for class=" + modelClass + " with slotname=" + methodName + " and argument type "
									+ inputType,
							e);
		} catch (final NoSuchMethodException e) {
			logger.warn("No input slot found for class=" + modelClass + " with slotname=" + methodName + " and argument type " + inputType,
						e);
		}
		return Optional.fromNullable(inputSlot);
	}
}
