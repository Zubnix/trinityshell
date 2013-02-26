package org.trinity.shellplugin.wm.impl.view;

import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;

import com.trolltech.qt.gui.QLabel;

@PropertySlots({ @PropertySlot(propertyName = "text", methodName = "setText", argumentTypes = { String.class }) })
public class BarItemView extends QLabel {

}