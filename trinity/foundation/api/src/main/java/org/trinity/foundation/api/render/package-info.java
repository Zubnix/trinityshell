/**
 * Defines a simple high-level API for a rendering scene. A scene consists of
 * {@link org.trinity.foundation.api.render.SurfaceNode}s. A scene can be
 * updated by passing a {@link org.trinity.foundation.api.render.PaintRoutine}
 * to a render back-end. It is the implemention's responsibility to provide a
 * paint API by extending {@link org.trinity.foundation.api.render.PaintContext}
 * .
 */
package org.trinity.foundation.api.render;