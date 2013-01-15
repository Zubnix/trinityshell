package org.trinity.foundation.render.qt.impl.binding.view.delegate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.PaintRoutine;
import org.trinity.foundation.api.render.Renderer;
import org.trinity.foundation.api.render.binding.view.delegate.InputListenerInstallerDelegate;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.trolltech.qt.core.QObject;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class InputListenerInstallerDelegateImpl implements InputListenerInstallerDelegate {

	private final Renderer renderer;

	@Inject
	InputListenerInstallerDelegateImpl(final Renderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public void installInputListener(	final Class<? extends Input> inputType,
										final Object view,
										final Object inputEventTarget,
										final String inputSlotName) {
		checkArgument(	view instanceof QObject,
						format(	"Expected view should be of type %s",
								QObject.class.getName()));

		this.renderer.invoke(	this,
								new PaintRoutine<Void, PaintContext>() {
									@Override
									public Void call(final PaintContext paintContext) {
										final QObject viewInstance = (QObject) view;
										viewInstance.installEventFilter(arg__1);
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

		this.renderer.invoke(	this,
								new PaintRoutine<Void, PaintContext>() {
									@Override
									public Void call(final PaintContext paintContext) throws ExecutionException {
										final QObject viewInstance = (QObject) view;
										viewInstance.removeEventFilter(arg__1);
										return null;
									}
								});
	}
}