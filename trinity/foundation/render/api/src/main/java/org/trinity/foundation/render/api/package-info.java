/**
 * Defines a simple high-level API for a scene of
 * {@link org.trinity.foundation.render.api.SurfaceNode}s. A node is rendered by
 * passing a {@link org.trinity.foundation.render.api.PaintInstruction} to an
 * actual render back-end so it can be processed asynchronously. It is the
 * implemention's responsibility to provide a paint API by extending
 * {@link org.trinity.foundation.render.api.PaintContext}.
 */
package org.trinity.foundation.render.api;