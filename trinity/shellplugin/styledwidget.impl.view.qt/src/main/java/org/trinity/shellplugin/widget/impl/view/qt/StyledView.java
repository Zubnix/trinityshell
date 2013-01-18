package org.trinity.shellplugin.widget.impl.view.qt;

import javax.inject.Named;

import org.trinity.foundation.api.render.binding.view.View;

import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QFrame;

public class StyledView extends QFrame implements View{

	StyledView() {
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