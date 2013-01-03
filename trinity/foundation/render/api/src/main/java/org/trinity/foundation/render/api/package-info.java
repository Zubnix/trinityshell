/**
 * Defines a simple high-level API for a rendering scene. A scene consists of
 * {@link org.trinity.foundation.render.api.SurfaceNode}s. A scene can be
 * updated by passing a
 * {@link org.trinity.foundation.render.api.PaintInstruction} to a render
 * back-end. It is the implemention's responsibility to provide a paint API by
 * extending {@link org.trinity.foundation.render.api.PaintContext}.
 */
package org.trinity.foundation.render.api;