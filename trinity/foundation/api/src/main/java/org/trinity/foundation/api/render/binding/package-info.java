// TODO extensive documentation on how to use the binding api.
/**
 * A basic <a href="http://en.wikipedia.org/wiki/Model_View_ViewModel">model
 * view - viewmodel</a> pattern. It offers the following bindings between a
 * viewmodel, which can be any type of object, and view, usually of type
 * {@link org.trinity.foundation.api.render.binding.view.View}.
 * <p>
 * View bindings:
 * <ul>
 * <li>{@link org.trinity.foundation.api.render.binding.view.DataContext}</li>
 * <li>{@link org.trinity.foundation.api.render.binding.view.InputSignals}</li>
 * <li>
 * {@link org.trinity.foundation.api.render.binding.view.ObservableCollection}</li>
 * <li>{@link org.trinity.foundation.api.render.binding.view.PropertySlots}</li>
 * </ul>
 * <p>
 * Model bindings:
 * <ul>
 * <li>{@link org.trinity.foundation.api.render.binding.model.InputSlot}:</li>
 * <li>{@link org.trinity.foundation.api.render.binding.model.PropertyChanged}:</li>
 * <li>{@link org.trinity.foundation.api.render.binding.model.ViewReference}:</li>
 * </ul>
 */
package org.trinity.foundation.api.render.binding;