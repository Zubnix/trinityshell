package org.trinity.shellplugin.wm.x11.impl.view;

import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;

import com.trolltech.qt.gui.QLabel;

@PropertySlots({ //
@PropertySlot(propertyName = "text", methodName = "setText", argumentTypes = { String.class }) // HasText
})
class NotificationsBarElementView extends QLabel {
	{
		// workaround for jambi css bug
		setObjectName("NotificationsBarElement");
	}
}