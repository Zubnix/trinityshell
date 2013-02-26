package org.trinity.foundation.render.qt.impl.binding.view.delegate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.inject.Named;

import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.PaintRenderer;
import org.trinity.foundation.api.render.PaintRoutine;
import org.trinity.foundation.api.render.binding.view.delegate.InputListenerInstallerDelegate;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.eventbus.EventBus;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.trolltech.qt.core.QObject;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class InputListenerInstallerDelegateImpl implements InputListenerInstallerDelegate {

	private final HashFunction hashFunction = Hashing.goodFastHash(16);
	private final Cache<Integer, QObject> inputListeners = CacheBuilder.newBuilder().softValues().build();

	private final EventBus displayEventBus;
	private final PaintRenderer paintRenderer;

	@Inject
	InputListenerInstallerDelegateImpl(	@Named("DisplayEventBus") final EventBus displayEventBus,
										final PaintRenderer paintRenderer) {
		this.displayEventBus = displayEventBus;
		this.paintRenderer = paintRenderer;
	}

	@Override
	public void installInputListener(	final Class<? extends Input> inputType,
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
				return new BoundInputListener(	InputListenerInstallerDelegateImpl.this.displayEventBus,
												inputType,
												inputEventTarget,
												inputSlotName);
			}
		};

		this.paintRenderer.invoke(	this,
									new PaintRoutine<Void, PaintContext>() {
										@Override
										public Void call(final PaintContext paintContext) throws ExecutionException {
											final QObject viewInstance = (QObject) view;

											final QObject inputListener = InputListenerInstallerDelegateImpl.this.inputListeners
													.get(	Integer.valueOf(inputListenerHash),
															inputListenerCreator);

											viewInstance.installEventFilter(inputListener);
											return null;
										};
									});
	}

	@Override
	public void removeInputListener(final Class<? extends Input> inputType,
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
				return new BoundInputListener(	InputListenerInstallerDelegateImpl.this.displayEventBus,
												inputType,
												inputEventTarget,
												inputSlotName);
			}
		};

		this.paintRenderer.invoke(	this,
									new PaintRoutine<Void, PaintContext>() {
										@Override
										public Void call(final PaintContext paintContext) throws ExecutionException {
											final QObject viewInstance = (QObject) view;

											final QObject inputListener = InputListenerInstallerDelegateImpl.this.inputListeners
													.get(	Integer.valueOf(inputListenerHash),
															inputListenerCreator);
											viewInstance.removeEventFilter(inputListener);

											return null;
										}
									});
	}
}