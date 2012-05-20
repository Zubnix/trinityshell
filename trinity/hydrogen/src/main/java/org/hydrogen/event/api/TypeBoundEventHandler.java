package org.hydrogen.event.api;

/**
 * Same as an {@link EventHandler} but only suitable for a single {@link Type}.
 * 
 * @see EventHandler
 * @author Erik De Rijcke
 * @since 1.0
 * 
 * @param <T>
 * @param <E>
 */
public interface TypeBoundEventHandler<T extends Type, E extends Event<T>> extends
		EventHandler<E> {

	T getType();
}
