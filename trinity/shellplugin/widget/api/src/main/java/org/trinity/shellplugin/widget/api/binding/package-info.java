/**
 * Property bindings. Property bindings can be used on
 * {@link org.trinity.shell.api.widget.ShellWidget}s to automatically propagate
 * changes of properties to a render back-end.
 * <p>
 * To use the binding API, annotate the
 * {@link org.trinity.shell.api.widget.ShellWidgetView} with the
 * {@link org.trinity.shellplugin.widget.api.binding.ViewReference} annotation.
 * Next, annotate the attribute that stores the property with the
 * {@link org.trinity.shellplugin.widget.api.binding.ViewAttribute} annotation.
 * To notify the render back-end that the property has changed, annotate a
 * method with the
 * {@link org.trinity.shellplugin.widget.api.binding.ViewAttributeChanged}
 * annotation. After invocation of this method the render back-end will be
 * notified of the change, together with the value of attribute.
 */
package org.trinity.shellplugin.widget.api.binding;