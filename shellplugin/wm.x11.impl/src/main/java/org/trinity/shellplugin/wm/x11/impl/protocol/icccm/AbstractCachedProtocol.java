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

package org.trinity.shellplugin.wm.x11.impl.protocol.icccm;

import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import org.freedesktop.xcb.xcb_property_notify_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.Listenable;
import org.trinity.foundation.api.shared.ListenableEventBus;
import org.trinity.shellplugin.wm.x11.impl.protocol.XAtomCache;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Map;
import java.util.WeakHashMap;

@ThreadSafe
public abstract class AbstractCachedProtocol<P> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractCachedProtocol.class);
	private final Map<DisplaySurface, Optional<P>> protocolCache = new WeakHashMap<>();
	private final Map<DisplaySurface, Listenable> listenersByWindow = new WeakHashMap<>();
	private int protocolAtomId;

	AbstractCachedProtocol(
							@Nonnull final XAtomCache xAtomCache,
							@Nonnull final String protocolName) {
				AbstractCachedProtocol.this.protocolAtomId = xAtomCache.getAtom(protocolName);
	}

	public int getProtocolAtomId() {
		return this.protocolAtomId;
	}

	public void addProtocolListener(	@Nonnull final DisplaySurface xWindow,
														@Nonnull final ProtocolListener<P> listener) {

				Listenable listeners = AbstractCachedProtocol.this.listenersByWindow.get(xWindow);
				if (listeners == null) {
					listeners = new ListenableEventBus();
                    AbstractCachedProtocol.this.listenersByWindow.put(xWindow,
                                                                      listeners);
                }
                listeners.register(	listener);
	}

	public Optional<P> get(@Nonnull final DisplaySurface xWindow) {

		final Optional<P> protocol =  AbstractCachedProtocol.this.protocolCache.get(xWindow);

									if (protocol == null) {
										trackProtocol(xWindow);
										return queryProtocol(xWindow);
									}
        return protocol;
	}

	protected void trackProtocol(final DisplaySurface xWindow) {
		xWindow.register(new Object() {
			@Subscribe
			public void onXPropertyChanged(final xcb_property_notify_event_t property_notify_event) {
				if (property_notify_event.getAtom() == AbstractCachedProtocol.this.protocolAtomId) {
					updateProtocolCache(xWindow);
				}
			}
		});
	}

	protected void updateProtocolCache(final DisplaySurface xWindow) {
		final Optional<P> protocol = queryProtocol(xWindow);
							AbstractCachedProtocol.this.protocolCache.put(	xWindow,
																			protocol);
							notifyProtocolListeners(xWindow,
													protocol);
	}

	protected abstract Optional<P> queryProtocol(final DisplaySurface xWindow);

	public void removeProtocolListener(	@Nonnull final DisplaySurface xWindow,
															@Nonnull final ProtocolListener<P> listener) {

				final Listenable listeners = AbstractCachedProtocol.this.listenersByWindow.get(xWindow);
				listeners.unregister(listener);
	}

	protected void notifyProtocolListeners(	final DisplaySurface xWindow,
											final Optional<P> protocol) {
		final Listenable listeners = AbstractCachedProtocol.this.listenersByWindow.get(xWindow);
		listeners.post(protocol);
	}
}
