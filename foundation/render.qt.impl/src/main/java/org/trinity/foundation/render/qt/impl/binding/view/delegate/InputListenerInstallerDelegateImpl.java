package org.trinity.foundation.render.qt.impl.binding.view.delegate;

import java.util.concurrent.Callable;

import org.apache.onami.autobind.annotations.Bind;
import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.render.binding.model.delegate.InputSlotCallerDelegate;
import org.trinity.foundation.api.render.binding.view.delegate.InputListenerInstallerDelegate;
import org.trinity.foundation.api.shared.AsyncListenable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QApplication;

import static java.lang.String.format;

import static com.google.common.base.Preconditions.checkArgument;

@Bind
@Singleton
public class InputListenerInstallerDelegateImpl implements InputListenerInstallerDelegate {

	private final HashFunction hashFunction = Hashing.goodFastHash(16);
	private final Cache<Integer, QObject> inputListeners = CacheBuilder.newBuilder().softValues().build();
	private final InputSlotCallerDelegate inputSlotCallerDelegate;

	@Inject
	InputListenerInstallerDelegateImpl(final InputSlotCallerDelegate inputSlotCallerDelegate) {
		this.inputSlotCallerDelegate = inputSlotCallerDelegate;
	}

	@Override
	public ListenableFuture<Void> installViewInputListener(	final Class<? extends Input> inputType,
															final Object view,
															final Object inputEventTarget,
															final String inputSlotName) {
		checkArgument(	view instanceof QObject,
						format(	"Expected view should be of type %s",
								QObject.class.getName()));

		final int inputListenerHash = this.hashFunction.newHasher().putInt(inputType.hashCode())
				.putInt(view.hashCode()).putInt(inputEventTarget.hashCode()).putString(inputSlotName).hashCode();
		final Callable<QObject> inputListenerCreator = new Callable<QObject>() {
			@Override
			public QObject call() throws Exception {
				return new BoundInputListener(	inputType,
												InputListenerInstallerDelegateImpl.this.inputSlotCallerDelegate,
												inputEventTarget,
												inputSlotName);
			}
		};

		final ListenableFutureTask<Void> installViewInputListenerTask = ListenableFutureTask
				.create(new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						final QObject viewInstance = (QObject) view;
						final QObject inputListener = InputListenerInstallerDelegateImpl.this.inputListeners
								.get(	Integer.valueOf(inputListenerHash),
										inputListenerCreator);
						viewInstance.installEventFilter(inputListener);
						return null;
					}
				});

		QApplication.invokeLater(installViewInputListenerTask);
		return installViewInputListenerTask;
	}

	@Override
	public ListenableFuture<Void> removeViewInputListener(	final Class<? extends Input> inputType,
															final Object view,
															final AsyncListenable inputEventTarget,
															final String inputSlotName) {
		checkArgument(	view instanceof QObject,
						format(	"Expected view should be of type %s",
								QObject.class.getName()));

		final int inputListenerHash = this.hashFunction.newHasher().putInt(inputType.hashCode())
				.putInt(view.hashCode()).putInt(inputEventTarget.hashCode()).putString(inputSlotName).hashCode();

		final Callable<QObject> inputListenerCreator = new Callable<QObject>() {
			@Override
			public QObject call() throws Exception {
				return new BoundInputListener(	inputType,
												InputListenerInstallerDelegateImpl.this.inputSlotCallerDelegate,
												inputEventTarget,
												inputSlotName);
			}
		};

		final ListenableFutureTask<Void> removeViewInputListenerTask = ListenableFutureTask
				.create(new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						final QObject viewInstance = (QObject) view;
						final QObject inputListener = InputListenerInstallerDelegateImpl.this.inputListeners
								.get(	Integer.valueOf(inputListenerHash),
										inputListenerCreator);
						viewInstance.removeEventFilter(inputListener);
						return null;
					}
				});

		QApplication.invokeLater(removeViewInputListenerTask);
		return removeViewInputListenerTask;
	}
}