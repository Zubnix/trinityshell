/**
 * Property bindings. Property bindings can be used on a
 * {@link org.trinity.foundation.render.api.PaintableSurfaceNode}, eg. a
 * {@link org.trinity.shell.api.widget.ShellWidget}, to automatically propagate
 * changes of view properties to a view object.
 * <p>
 * To use this API, annotate a single object inside a
 * {@code PaintableSurfaceNode} with the
 * {@link org.trinity.foundation.api.render.binding.refactor.model.View} annotation.
 * Usually this object is a {@link org.trinity.shell.api.widget.ShellWidgetView}
 * . Next, annotate a property getter with the
 * {@link org.trinity.foundation.api.render.binding.refactor.model.ViewProperty} annotation to
 * mark it as a view property. You can give this view property a name. If no
 * name is given the name is derived from the getter method, that is the name of
 * the getter method without 'get' and will start with a lower case. To notify
 * the render back-end that the property has changed, mark a method with the
 * {@link org.trinity.foundation.api.render.binding.refactor.model.ViewPropertyChanged}
 * annotation. This annotation can be given multiple names of view properties to
 * indicate which view properties have changed after the method has been
 * executed. After invocation of this marked method, the object that was
 * annotated with {@code View} will be notified of the change. In order
 * for this object to be notified, a method of this object must be marked with
 * {@link org.trinity.foundation.api.render.binding.refactor.view.PropertySlot}. This
 * annotation is given the names of the properties that it will listened to. To
 * manually notify a {@code PropertySlot}, inject a
 * {@link org.trinity.foundation.api.render.binding.BindingDiscovery}
 * instance and call
 * {@link org.trinity.foundation.api.render.binding.BindingDiscovery#notifyViewPropertySlot(Class, Object, String...)}.
 */
package org.trinity.foundation.api.render.binding;