package org.trinity.x11.defaul;

import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_window_attributes_cookie_t;
import org.freedesktop.xcb.xcb_get_window_attributes_reply_t;
import org.freedesktop.xcb.xcb_query_tree_cookie_t;
import org.freedesktop.xcb.xcb_query_tree_reply_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.nio.ByteBuffer;

import static java.nio.ByteOrder.nativeOrder;
import static org.freedesktop.xcb.LibXcb.*;
import static org.freedesktop.xcb.xcb_map_state_t.XCB_MAP_STATE_VIEWABLE;

public class XClientExplorer {

    private static final Logger LOG = LoggerFactory.getLogger(XClientExplorer.class);

    private final XEventLoop   xEventLoop;
    private final XSurfacePool displaySurfacePool;

    @Inject
    XClientExplorer(final XEventLoop xEventLoop,
                    final XSurfacePool xSurfacePool) {
        this.xEventLoop = xEventLoop;
        this.displaySurfacePool = xSurfacePool;
    }

    public void findClientDisplaySurfaces() {
        // find client display surfaces that are already active on the X server
        final int root = this.xEventLoop.getXcbScreen()
                                        .getRoot();
        final SWIGTYPE_p_xcb_connection_t connection = this.xEventLoop.getXcbConnection();
        final xcb_query_tree_cookie_t query_tree_cookie = xcb_query_tree(connection,
                                                                         root);
        final xcb_generic_error_t e = new xcb_generic_error_t();
        // this is a one time call, no need to make it async.
        final xcb_query_tree_reply_t query_tree_reply = xcb_query_tree_reply(connection,
																			 query_tree_cookie,
																			 e);
		if(xcb_generic_error_t.getCPtr(e) != 0) {
			LOG.error("X error while doing query tree: {}.",
					  XcbErrorUtil.toString(e));
			return;
		}

		final ByteBuffer tree_children = xcb_query_tree_children(query_tree_reply).order(nativeOrder());
		int tree_children_length = xcb_query_tree_children_length(query_tree_reply);
		while(tree_children_length > 0) {
			tree_children_length--;

			final int tree_child = tree_children.getInt();

			final xcb_get_window_attributes_cookie_t get_window_attributes_cookie = xcb_get_window_attributes(connection,
																											  tree_child);
			final xcb_get_window_attributes_reply_t get_window_attributes_reply = xcb_get_window_attributes_reply(connection,
																												  get_window_attributes_cookie,
																												  e);
			if(xcb_generic_error_t.getCPtr(e) != 0) {
				LOG.error("X error while doing get window attributes: {}.",
						  XcbErrorUtil.toString(e));
			}
			else {
				final short override_redirect = get_window_attributes_reply.getOverride_redirect();
				final short map_state = get_window_attributes_reply.getMap_state();
				// Check for override redirect flag and ignore the window if it's set.
				// Ignore unmapped displaySurfaces, we'll see them as soon as
				// they reconfigure/map themselves
				if((map_state != XCB_MAP_STATE_VIEWABLE) || (override_redirect != 0)) {
					continue;
				}

				this.displaySurfacePool.get(tree_child);
			}
		}
	}
}
