package org.trinity.foundation.render.qt.impl.binding.view.delegate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.render.binding.model.delegate.InputSlotCallerDelegate;
import org.trinity.foundation.api.render.binding.view.delegate.InputListenerInstallerDelegate;
import org.trinity.foundation.api.shared.AsyncListenable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.trolltech.qt.core.QObject;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class InputListenerInstallerDelegateImpl implements InputListenerInstallerDelegate {

	private static final Logger logger = LoggerFactory.getLogger(InputListenerInstallerDelegateImpl.class);

	private final HashFunction hashFunction = Hashing.goodFastHash(16);
	private final Cache<Integer, QObject> inputListeners = CacheBuilder.newBuilder().softValues().build();
	private final InputSlotCallerDelegate inputSlotCallerDelegate;

	@Inject
	InputListenerInstallerDelegateImpl(final InputSlotCallerDelegate inputSlotCallerDelegate) {
		this.inputSlotCallerDelegate = inputSlotCallerDelegate;
	}

	@Override
	public void installViewInputListener(	final Class<? extends Input> inputType,
											final Object view,
											final AsyncListenable inputEventTarget,
											final String inputSlotName) {
		checkArgument(	view instanceof QObject,
						format(	"Expected view should be of type %s",
								QObject.class.getName()));

		final int inputListenerHash = this.hashFunction.newHasher().putInt(inputType.hashCode()).putInt(view.hashCode()).putInt(inputEventTarget.hashCode())
				.putString(inputSlotName).hashCode();

		final Callable<QObject> inputListenerCreator = new Callable<QObject>() {
			@Override
			public QObject call() throws Exception {
				return new BoundInputListener(	inputType,
												InputListenerInstallerDelegateImpl.this.inputSlotCallerDelegate,
												inputEventTarget,
												inputSlotName);
			}
		};

		final QObject viewInstance = (QObject) view;
		try {
			final QObject inputListener = InputListenerInstallerDelegateImpl.this.inputListeners.get(	Integer.valueOf(inputListenerHash),
																										inputListenerCreator);
			viewInstance.installEventFilter(inputListener);
		} catch (final ExecutionException e) {
			// TODO explenation
			logger.error(	"",
							e);
		}
	}

	@Override
	public void removeViewInputListener(final Class<? extends Input> inputType,
										final Object view,
										final AsyncListenable inputEventTarget,
										final String inputSlotName) {
		checkArgument(	view instanceof QObject,
						format(	"Expected view should be of type %s",
								QObject.class.getName()));

		final int inputListenerHash = this.hashFunction.newHasher().putInt(inputType.hashCode()).putInt(view.hashCode()).putInt(inputEventTarget.hashCode())
				.putString(inputSlotName).hashCode();

		final Callable<QObject> inputListenerCreator = new Callable<QObject>() {
			@Override
			public QObject call() throws Exception {
				return new BoundInputListener(	inputType,
												InputListenerInstallerDelegateImpl.this.inputSlotCallerDelegate,
												inputEventTarget,
												inputSlotName);
			}
		};

		final QObject viewInstance = (QObject) view;
		try {
			final QObject inputListener = InputListenerInstallerDelegateImpl.this.inputListeners.get(	Integer.valueOf(inputListenerHash),
																										inputListenerCreator);
			viewInstance.removeEventFilter(inputListener);
		} catch (final ExecutionException e) {
			// TODO explenation
			logger.error(	"",
							e);
		}
	}
}