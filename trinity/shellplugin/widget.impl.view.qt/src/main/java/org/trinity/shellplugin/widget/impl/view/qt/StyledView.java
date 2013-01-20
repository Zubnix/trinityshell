package org.trinity.shellplugin.widget.impl.view.qt;

import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;
import org.trinity.foundation.api.render.binding.view.View;

import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QFrame;

@PropertySlots({ @PropertySlot(dataContext = "class", propertyName = "simpleName", methodName = "setObjectName", argumentTypes = { String.class }) })
public class StyledView extends QFrame implements View {

	StyledView() {

		setWindowFlags(WindowType.X11BypassWindowManagerHint);
		setAttribute(	WidgetAttribute.WA_DeleteOnClose,
						true);
		setAttribute(	WidgetAttribute.WA_DontCreateNativeAncestors,
						true);
	}
}