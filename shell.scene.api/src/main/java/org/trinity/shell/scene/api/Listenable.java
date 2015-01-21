/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

package org.trinity.shell.scene.api;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.MoreExecutors;

import javax.annotation.Nonnull;

/**
 * Expose guava's {@link EventBus} mechanism as an interface.
 */
public interface Listenable {
    /**
     * Immediately register a listener who's {@link Subscribe}d methods will be
     * invoked by the {@link MoreExecutors#sameThreadExecutor()}, ie the thread
     * that calls {@link #post(Object)} on this object.
     * <p/>
     * This method should be used if the calling executor service is the same as
     * than the one that owns this object.
     *
     * @param listener An object with a public, single argument, {@link Subscribe}
     *                 method.
     * @see EventBus#register(Object)
     */
    void register(@Nonnull Object listener);

    /**
     * @param listener An previously registered listener.
     * @see EventBus#unregister(Object)
     */
    void unregister(@Nonnull Object listener);

    /**
     * Asynchronously post an event.
     *
     * @param event The even to post.
     * @see EventBus#post(Object)
     */
    void post(@Nonnull Object event);
}
