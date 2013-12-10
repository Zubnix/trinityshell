package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.render.binding.view.PropertySlot;

/**
 *
 */
public interface PropertyBindingFactory {

    PropertyBinding createPropertyBinding(ViewBindingMeta viewBindingMeta, PropertySlot propertySlot);
}
