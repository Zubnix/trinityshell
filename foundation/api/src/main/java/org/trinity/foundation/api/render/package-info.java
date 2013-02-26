/**
 * Defines a simple high-level API for rendering a scene. The most
 * straightforward way of using this api is to {@link com.google.inject.Inject}
 * a {@link org.trinity.foundation.api.render.PainterFactory} in a view model
 * and create a {@link org.trinity.foundation.api.render.Painter}. The next step
 * is to let the {@code Painter} know which
 * {@link org.trinity.foundation.api.render.binding.view.View} object to use for
 * this model. A model's view is an injected instance of type {@code View} that
 * implements an existing widget from a 3rd party toolkit, enriched with
 * additional binding annotations found in the
 * org.trinity.foundation.api.render.binding package. The view can then be
 * exposed to the painter by annotating the view getter with
 * {@link org.trinity.foundation.api.render.binding.model.ViewReference} in the
 * model class. Finally, everything is glued together by calling
 * {@link org.trinity.foundation.api.render.Painter#bindView()}.
 * <p>
 * {@code Painter} is also used to change the elements visual geometry through
 * it's inherited
 * {@link org.trinity.foundation.api.display.DisplayAreaManipulator} methods.
 * <p>
 * A scene can also be directly updated by passing a
 * {@link org.trinity.foundation.api.render.PaintRoutine} to a
 * {@link org.trinity.foundation.api.render.PaintRenderer}. Optionally, the
 * {@code PaintRenderer} capabilities can be extended by extending
 * {@link org.trinity.foundation.api.render.PaintContext}.
 */
package org.trinity.foundation.api.render;