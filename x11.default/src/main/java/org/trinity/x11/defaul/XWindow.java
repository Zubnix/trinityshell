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
package org.trinity.x11.defaul;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_geometry_cookie_t;
import org.freedesktop.xcb.xcb_get_geometry_reply_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.common.Listenable;
import org.trinity.shell.scene.api.Buffer;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.media.nativewindow.util.Dimension;
import javax.media.nativewindow.util.DimensionImmutable;
import javax.media.nativewindow.util.Rectangle;
import javax.media.nativewindow.util.RectangleImmutable;
import java.nio.ByteBuffer;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.nativeOrder;
import static org.freedesktop.xcb.LibXcb.xcb_configure_window;
import static org.freedesktop.xcb.LibXcb.xcb_destroy_window;
import static org.freedesktop.xcb.LibXcb.xcb_get_geometry;
import static org.freedesktop.xcb.LibXcb.xcb_get_geometry_reply;
import static org.freedesktop.xcb.LibXcb.xcb_map_window;
import static org.freedesktop.xcb.LibXcb.xcb_unmap_window;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_HEIGHT;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_WIDTH;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_X;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_Y;

@ThreadSafe
@AutoFactory
public class XWindow extends EventBus implements Listenable, Buffer {

    private static final Logger LOG = LoggerFactory.getLogger(XWindow.class);

    private static final int        RESIZE_VALUE_MASK             = XCB_CONFIG_WINDOW_WIDTH | XCB_CONFIG_WINDOW_HEIGHT;
    private static final int        MOVE_VALUE_MASK        = XCB_CONFIG_WINDOW_X | XCB_CONFIG_WINDOW_Y;
    private static final ByteBuffer RESIZE_VALUE_LIST_BUFFER      = allocateDirect(8).order(nativeOrder());
    private static final ByteBuffer MOVE_VALUE_LIST_BUFFER = allocateDirect(8).order(nativeOrder());

    private final EventBus visitorDispatcher = new EventBus();
    {
        this.visitorDispatcher.register(this);
    }

    @Nonnull
    private final Integer    nativeHandle;
    private final XEventLoop xEventLoop;

    XWindow(@Provided final XEventLoop xEventLoop,
            @Nonnull final Integer nativeHandle) {
        checkNotNull(nativeHandle);

        this.xEventLoop = xEventLoop;
        this.nativeHandle = nativeHandle;
    }

    @Nonnull
    public Integer getNativeHandle() {
        return this.nativeHandle;
    }

    public void destroy() {
        final int winId = getNativeHandle();
        LOG.debug("[winId={}] destroy.",
                  winId);
        xcb_destroy_window(getConnectionRef(),
                           winId);
    }

    private SWIGTYPE_p_xcb_connection_t getConnectionRef() {
        return this.xEventLoop.getXcbConnection();
    }

    @Nonnull
    public XWindow move(final int x,
                        final int y) {

        MOVE_VALUE_LIST_BUFFER.clear();
        MOVE_VALUE_LIST_BUFFER.putInt(x)
                                     .putInt(y);

        final int winId = getNativeHandle();
        LOG.debug("[winId={}] move x={}, y={}.",
                  winId,
                  x,
                  y);
        xcb_configure_window(getConnectionRef(),
                             winId,
                             XWindow.MOVE_VALUE_MASK,
                             XWindow.MOVE_VALUE_LIST_BUFFER);
        return this;
    }

    @Nonnull
    public XWindow resize(@Nonnegative final int width,
                          @Nonnegative final int height) {

        RESIZE_VALUE_LIST_BUFFER.clear();
        RESIZE_VALUE_LIST_BUFFER.putInt(width)
                                .putInt(height);

        final int winId = getNativeHandle();
        LOG.debug("[winId={}] resize width={}, height={}.",
                  winId,
                  width,
                  height);
        xcb_configure_window(getConnectionRef(),
                             winId,
                             XWindow.RESIZE_VALUE_MASK,
                             XWindow.RESIZE_VALUE_LIST_BUFFER);
        return this;
    }

    public XWindow map() {
        final int winId = getNativeHandle();
        xcb_map_window(getConnectionRef(),
                       winId);
        return this;
    }

    public XWindow unmap() {
        final int winId = getNativeHandle();
        xcb_unmap_window(getConnectionRef(),
                         winId);
        return this;
    }

    @Nonnull
    public RectangleImmutable getShape() {
        //TODO keep track of the size & border through event listeners?

        final int winId = getNativeHandle();

        final xcb_get_geometry_cookie_t geometryRequest = xcb_get_geometry(getConnectionRef(),
                                                                           winId);

        LOG.debug("get geometry reply.");

        final xcb_generic_error_t e = new xcb_generic_error_t();
        final xcb_get_geometry_reply_t get_geometry_reply = xcb_get_geometry_reply(getConnectionRef(),
                                                                                   geometryRequest,
                                                                                   e);

        checkError(e);
        final int width = get_geometry_reply.getWidth() + (2 * get_geometry_reply.getBorder_width());
        final int height = get_geometry_reply.getHeight() + (2 * get_geometry_reply.getBorder_width());
        final int x = get_geometry_reply.getX();
        final int y = get_geometry_reply.getY();

        return new Rectangle(x,
                             y,
                             width,
                             height);
    }

	@Nonnull
    @Override
	public DimensionImmutable getSize() {
		final RectangleImmutable shape = getShape();
		return new Dimension(shape.getWidth(),
							 shape.getHeight());
	}

	private void checkError(final xcb_generic_error_t e) {
		if(xcb_generic_error_t.getCPtr(e) != 0) {
			LOG.error("X error: {}.",
					  XcbErrorUtil.toString(e));
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if(obj instanceof XWindow) {
			final XWindow otherWindow = (XWindow) obj;
			return otherWindow.getNativeHandle()
							  .equals(getNativeHandle());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getNativeHandle();
	}

	@Override
	public String toString() {
		return String.format("%s=%s",
							 getClass().getSimpleName(),
							 getNativeHandle());
	}

    @Override
    public void accept(@Nonnull final Object renderCommand) {
        //We (ab)use guava's eventbus as a dynamic type based dispatcher. That way we don't have to cast!
        //Any unsupported render command will simply be ignored
        this.visitorDispatcher.post(renderCommand);
    }

    @Subscribe
    public void accept(@Nonnull final XWindowRenderCommand xWindowRenderCommand){
        xWindowRenderCommand.visit(this);
    }
}