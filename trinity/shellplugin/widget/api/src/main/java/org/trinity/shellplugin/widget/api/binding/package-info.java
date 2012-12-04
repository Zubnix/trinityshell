/**
 * Property bindings. Property bindings can be used on
 * {@link org.trinity.shell.api.widget.ShellWidget}s to automatically propagate
 * changes of properties to a render back-end.
 * <p>
 * To use this binding API, annotate the
 * {@link org.trinity.shell.api.widget.ShellWidgetView} with the
 * {@link org.trinity.shellplugin.widget.api.binding.ViewReference} annotation.
 * Next, annotate the attribute or getter that returns the property value with
 * the {@link org.trinity.shellplugin.widget.api.binding.ViewProperty}
 * annotation. To notify the render back-end that the property has changed,
 * annotate a method with the
 * {@link org.trinity.shellplugin.widget.api.binding.ViewPropertyChanged}
 * annotation. After invocation of this annotated method the render back-end
 * will be notified of the change. In order for the view to be notified, a
 * method of the annotated {@code ViewReference} object must be marked with
 * {@link org.trinity.shellplugin.widget.api.binding.ViewPropertySlot}.
 * <p>
 * A {@code ViewProperty} has a
 * {@link org.trinity.shellplugin.widget.api.binding.ViewProperty#value()}. A
 * name uniquely identifies a {@code ViewProperty} within his datacontext i.e.
 * the object that encapsulates it. The value of the name is used to find the
 * {@code ViewProperty} and the corresponding {@code ViewPropertySlot} of the
 * view.
 */
package org.trinity.shellplugin.widget.api.binding;