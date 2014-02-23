package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.render.binding.view.PropertySlot;

/**
 *
 */
public interface PropertyBindingFactory {

    PropertyBinding create(ViewBindingMeta viewBindingMeta,
                           PropertySlot propertySlot);
}
