package org.trinity.shellplugin.styledwidget.view.qt.impl;

import javax.inject.Named;

import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QFrame;

public abstract class StyledView extends QFrame {

	protected StyledView() {
		setWindowFlags(WindowType.X11BypassWindowManagerHint);
		setAttribute(	WidgetAttribute.WA_DeleteOnClose,
						true);
		setAttribute(	WidgetAttribute.WA_DontCreateNativeAncestors,
						true);
		final Named named = getClass().getAnnotation(Named.class);
		if (named != null) {
			final String name = named.value();
			setAccessibleName(name);
			ensurePolished();
		}
	}
}