package org.trinity.foundation.render.qt.impl.binding.view.delegate;

import javax.annotation.concurrent.Immutable;

import org.apache.onami.autobind.annotations.Bind;
import org.trinity.foundation.api.render.binding.view.ViewElementTypes;

import com.google.inject.Singleton;
import com.trolltech.qt.gui.QWidget;

@Bind
@Singleton
@Immutable
public class ViewElementTypesImpl implements ViewElementTypes {

	private static final Class<?>[] viewElementTypes = { QWidget.class };

	@Override
	public Class<?>[] getViewElementTypes() {
		return viewElementTypes;
	}
}