package org.trinity.foundation.render.qt.impl.binding.view.delegate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocatorDelegate;

import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class PropertySlotInvocatorDelegateImpl implements PropertySlotInvocatorDelegate {

	private static final Logger logger = LoggerFactory.getLogger(PropertySlotInvocatorDelegateImpl.class);

	PropertySlotInvocatorDelegateImpl() {
	}

	@Override
	public void invoke(	final Object view,
						final Method viewMethod,
						final Object argument) {

		try {
			viewMethod.invoke(	view,
								argument);
		} catch (final IllegalAccessException e) {
			// TODO explanation
			logger.error(	"",
							e);
		} catch (final IllegalArgumentException e) {
			// TODO explanation
			logger.error(	"",
							e);
		} catch (final InvocationTargetException e) {
			// TODO explanation
			logger.error(	"",
							e);
		}
	}
}
