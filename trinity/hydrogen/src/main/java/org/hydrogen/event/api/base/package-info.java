// TODO documentation
/**
 * Provides a basic publish–subscribe pattern. A <code>EventHandler</code> 
 * can express its interest in certain <code>Event</code> by registering 
 * itself at an <code>EventHandlerManager</code> together with the 
 * <code>Type</code> of the <code>Event</code>. Emitted <code>Event</code>s
 * are delivered directly. The <code>Event</code> will be discarded after
 * all interested <code>EventHandlers</code> are notified.
 */
package org.hydrogen.event.api.base;