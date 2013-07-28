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

package org.trinity.foundation.api.shared;

import java.util.concurrent.ExecutorService;

import javax.annotation.Nonnull;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * Extends guava's {@link EventBus} idea with asynchronious event delivery. This
 * interface does not (can not) extend from guava's Eventbus as it's developers
 * think it's a good idea to *not* separate implementation (a class) from api
 * (an interface).
 */
public interface AsyncListenable {
	/**
	 * Register a listener who's {@link Subscribe}d methods will be invoked by
	 * the {@link MoreExecutors#sameThreadExecutor()}, ie the thread that calls
	 * {@link #post(Object)} on this object.
	 *
	 * @see {@link EventBus#register(Object)}
	 * @param listener
	 */
	void register(@Nonnull Object listener);

	/**
	 * Register a listener who's {@link Subscribe}d methods will be invoked by
	 * the given {@link ExecutorService}.
	 *
	 * @see {@link EventBus#register(Object)}
	 * @param listener
	 * @param executor
	 */
	void register(	@Nonnull Object listener,
					@Nonnull ExecutorService executor);

	/**
	 * @see {@link EventBus#unregister(Object)}
	 * @param listener
	 */
	void unregister(@Nonnull Object listener);

	/**
	 * Asynchronously post an event.
	 *
	 * @see {@link EventBus#post(Object)}
	 * @param event
	 */
	void post(@Nonnull Object event);
}
