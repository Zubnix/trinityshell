/**
 *
 */

#define _GNU_SOURCE

#include "include/fusion_xcb_core.h"

#include <X11/keysym.h>
#include <xcb/xcb_keysyms.h>
#include <xcb/xcb_util.h>

xcb_screen_t *screen_of_display(xcb_connection_t *c, int screen) {
	xcb_screen_iterator_t iter;
	const xcb_setup_t *setup = xcb_get_setup(c);
	if (!setup)
		return NULL;
	iter = xcb_setup_roots_iterator(setup);
	for (; iter.rem; --screen, xcb_screen_next(&iter))
		if (screen == 0)
			return iter.data;
	return NULL;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetInputFocus
 * Signature: (J[BLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetInputFocus(JNIEnv *env,
		jclass class, jlong display, jobject buffer) {
	xcb_get_input_focus_cookie_t input_focus_cookie = xcb_get_input_focus(
			(xcb_connection_t*) display);

	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error;

	xcb_get_input_focus_reply_t* reply = xcb_get_input_focus_reply(
			(xcb_connection_t*) display, input_focus_cookie, &error);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	memcpy(resultBuffer, &reply->focus, sizeof(xcb_window_t));

	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeUngrabKeyboard
 * Signature: (J[BLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeUngrabKeyboard(JNIEnv *env,
		jclass class, jlong display, jobject buffer) {
	xcb_void_cookie_t void_cookie = xcb_ungrab_keyboard(
			(xcb_connection_t*) display, XCB_CURRENT_TIME);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeUngrabMouse
 * Signature: (J[BLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeUngrabMouse(JNIEnv *env,
		jclass class, jlong display, jobject buffer) {
	xcb_void_cookie_t void_cookie = xcb_ungrab_pointer_checked(
			(xcb_connection_t*) display, XCB_CURRENT_TIME);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGrabKeyMouse
 * Signature: (JJ[BLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGrabMouse(JNIEnv *env, jclass class,
		jlong display, jlong windowId, jobject buffer) {

	//, PointerMotionMask, PointerMotionHintMask, Button1MotionMask, Button2MotionMask, Button3MotionMask, Button4MotionMask, Button5MotionMask, ButtonMotionMask

	xcb_grab_pointer_cookie_t grab_pointer_cookie = xcb_grab_pointer(
			(xcb_connection_t*) display,
			0,
			(xcb_window_t) windowId,
			XCB_EVENT_MASK_BUTTON_PRESS | XCB_EVENT_MASK_BUTTON_RELEASE
					| XCB_EVENT_MASK_ENTER_WINDOW | XCB_EVENT_MASK_LEAVE_WINDOW,
			XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC, XCB_WINDOW_NONE,
			XCB_CURSOR_NONE, XCB_CURRENT_TIME);

	char* resultBuffer = (char*) (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error;

	xcb_grab_pointer_reply((xcb_connection_t*) display, grab_pointer_cookie,
			&error);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGrabKeyboard
 * Signature: (JJ[BLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGrabKeyboard(JNIEnv *env,
		jclass class, jlong display, jlong windowId, jobject buffer) {
	xcb_grab_keyboard_cookie_t grab_keyboard_cookie = xcb_grab_keyboard(
			(xcb_connection_t*) display, 0, (xcb_window_t) windowId,
			XCB_CURRENT_TIME, XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC);

	char* resultBuffer = (char*) (*env)->GetDirectBufferAddress(env, buffer);

	xcb_generic_error_t* error;
	//xcb_grab_keyboard_reply_t* grab_keyboard_reply =
	xcb_grab_keyboard_reply((xcb_connection_t*) display, grab_keyboard_cookie,
			&error);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeSendClientMessage
 * Signature: (JJJI[BLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeSendClientMessage(JNIEnv *env,
		jclass class, jlong display, jlong windowId, jlong atomId, jint format,
		jbyteArray data, jobject buffer) {

	jboolean isCopy;
	jbyte* dataBytes = (*env)->GetByteArrayElements(env, data, &isCopy);
	int dataBytesS = sizeof(dataBytes) / sizeof(dataBytes[0]);

	xcb_client_message_event_t ev;
	ev.response_type = XCB_CLIENT_MESSAGE;
	ev.window = (xcb_window_t) windowId;
	ev.format = (uint8_t) format;
	ev.type = (xcb_atom_t) atomId;
	memcpy(&ev.data, dataBytes, dataBytesS);

	xcb_void_cookie_t void_cookie = xcb_send_event((xcb_connection_t*) display,
			0, (xcb_window_t) windowId, XCB_EVENT_MASK_NO_EVENT, (char *) &ev);
	if (isCopy) {
		free(dataBytes);
	}
	char* resultBuffer = (char*) (*env)->GetDirectBufferAddress(env, buffer);

	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeRegisterAtom
 * Signature: (JLjava/lang/String;Ljava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeRegisterAtom(JNIEnv *env,
		jclass class, jlong display, jstring atomName, jobject buffer) {
	const char *atom = (*env)->GetStringUTFChars(env, atomName, 0);
	xcb_intern_atom_cookie_t cookie = xcb_intern_atom(
			(xcb_connection_t*) display, 0, strlen(atom), atom);
	xcb_generic_error_t* error;
	char* resultBuffer = (char*) (*env)->GetDirectBufferAddress(env, buffer);
	xcb_intern_atom_reply_t *reply = xcb_intern_atom_reply(
			(xcb_connection_t*) display, cookie, &error);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	xcb_atom_t atomReply = reply->atom;
	memcpy(resultBuffer, &atomReply, sizeof(xcb_atom_t));
	free(reply);
	(*env)->ReleaseStringUTFChars(env, atomName, atom);

	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeChangeProperty
 * Signature: (JJJJILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean
JNICALL Java_org_fusion_x11_core_XCoreNative_nativeChangeProperty(JNIEnv *env,
		jclass class, jlong display, jlong windowId, jlong atomId,
		jlong typeAtomId, jint format, jbyteArray data, jobject buffer) {
	jboolean isCopy;
	jbyte* dataBytes = (*env)->GetByteArrayElements(env, data, &isCopy);
	int dataBytesS = sizeof(dataBytes) / sizeof(dataBytes[0]);

	xcb_void_cookie_t void_cookie = xcb_change_property(
			(xcb_connection_t*) display, XCB_PROP_MODE_REPLACE,
			(xcb_window_t) windowId, (xcb_atom_t) atomId,
			(xcb_atom_t) typeAtomId, format, (uint32_t) dataBytesS, dataBytes);

	if (isCopy) {
		free(dataBytes);
	}

	char* resultBuffer = (char*) (*env)->GetDirectBufferAddress(env, buffer);

	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetProperty
 * Signature: (JJJJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetProperty(JNIEnv *env,
		jclass class, jlong display, jlong window, jlong propertyAtom,
		jobject buffer) {
	xcb_get_property_cookie_t cookie = xcb_get_property(
			(xcb_connection_t*) display, 0, (xcb_window_t) window,
			(xcb_atom_t) propertyAtom, XCB_GET_PROPERTY_TYPE_ANY, 0, 0xffff);

	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error;

	const xcb_get_property_reply_t *property = xcb_get_property_reply(
			(xcb_connection_t*) display, cookie, &error);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	resultBuffer = mempcpy(resultBuffer, property,
			sizeof(xcb_get_property_reply_t));

	void* value = xcb_get_property_value(property);
	size_t size = xcb_get_property_value_length(property);

	memcpy(resultBuffer, value, size);

	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeKeyCodeToKeySymCode
 * Signature: (JJILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeKeyCodeToKeySymCode(JNIEnv *env,
		jclass class, jlong display, jlong keyCode, jint modifiers,
		jobject buffer) {
	xcb_key_symbols_t* key_symbols = xcb_key_symbols_alloc(
			(xcb_connection_t*) display);

	xcb_key_symbols_free(key_symbols);
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativePlatformIntBytesCount
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_fusion_x11_core_XCoreNative_nativePlatformIntBytesCount(JNIEnv *env,
		jclass class) {
	return (jint) sizeof(int);
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativePlatformLongBytesCount
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_fusion_x11_core_XCoreNative_nativePlatformLongBytesCount(JNIEnv *env,
		jclass class) {
	return (jint) sizeof(long);
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativePlatformPointerBytesCount
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_fusion_x11_core_XCoreNative_nativePlatformPointerBytesCount(
		JNIEnv *env, jclass class) {
	return (jint) sizeof(char*);
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeDestroy
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeDestroy(JNIEnv *env, jclass class,
		jlong display, jlong window, jobject buffer) {
	xcb_void_cookie_t void_cookie = xcb_kill_client_checked(
			(xcb_connection_t*) display, (xcb_window_t) window);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_xcb_XcbCoreNative
 * Method:    nativeAllocateKeysyms
 * Signature: (J)J
 */
JNIEXPORT
jlong JNICALL Java_org_fusion_x11_core_xcb_XcbCoreNative_nativeAllocateKeysyms(
		JNIEnv *env, jclass class, jlong display) {
	return (jlong) xcb_key_symbols_alloc((xcb_connection_t*) display);
}

/*
 * Class:     org_fusion_x11_core_xcb_XcbCoreNative
 * Method:    nativeFreeKeysyms
 * Signature: (J)Z
 */
JNIEXPORT
jboolean JNICALL Java_org_fusion_x11_core_xcb_XcbCoreNative_nativeFreeKeysyms(
		JNIEnv *env, jclass class, jlong keySyms) {
	xcb_key_symbols_free((xcb_key_symbols_t*) keySyms);
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeFlush
 * Signature: (JLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeFlush(JNIEnv *env, jclass class,
		jlong display, jobject buffer) {
	int flush_stat = xcb_flush((xcb_connection_t*) display);
	if (flush_stat <= 0) {
		//TODO write error to buffer
		return (jboolean) 1;
	}

	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetChildren
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetChildren(JNIEnv *env,
		jclass class, jlong display, jlong window, jobject buffer) {
	xcb_query_tree_cookie_t cookie = xcb_query_tree((xcb_connection_t*) display,
			(xcb_window_t) window);

	xcb_generic_error_t* error;
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_query_tree_reply_t *tree = xcb_query_tree_reply(
			(xcb_connection_t*) display, cookie, &error);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	int len = xcb_query_tree_children_length(tree);
	xcb_window_t* children = xcb_query_tree_children(tree);

	void* resultIndex = mempcpy(resultBuffer, &len, sizeof(int));
	mempcpy(resultIndex, children, sizeof(xcb_window_t) * len);

	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetCurrentWindowGeometry
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetCurrentWindowGeometry(JNIEnv *env,
		jclass class, jlong display, jlong window, jobject buffer) {
	xcb_get_geometry_cookie_t geomCookie = xcb_get_geometry(
			(xcb_connection_t*) display, (xcb_window_t) window);

	xcb_generic_error_t* error;
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_get_geometry_reply_t *geom = xcb_get_geometry_reply(
			(xcb_connection_t*) display, geomCookie, &error);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	memcpy(resultBuffer, geom, sizeof(xcb_get_geometry_reply_t));
	free(geom);
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetNextEvent
 * Signature: (JLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetNextEvent(JNIEnv *env,
		jclass class, jlong display, jobject buffer) {

	xcb_generic_event_t *ev = xcb_wait_for_event((xcb_connection_t*) display);

	//TODO check for error event.
	if (ev == NULL) {
		//TODO write error to buffer
		//		char* resultBuffer = initResultHandling(env, &buffer);
		return (jboolean) 1;
	}

	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	memcpy(resultBuffer, ev, sizeof(*ev));
	free(ev);
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetPointerInfo
 * Signature: (JLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetPointerInfo(JNIEnv *env,
		jclass class, jlong display, jobject buffer) {
	xcb_screen_t *screen = screen_of_display((xcb_connection_t*) display, 0);
	if (screen == NULL) {
		return (jboolean) 1;
	}
	xcb_window_t window = screen->root;
	xcb_query_pointer_cookie_t cooky = xcb_query_pointer(
			(xcb_connection_t*) display, window);
	xcb_generic_error_t* error;
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	const xcb_query_pointer_reply_t *reply = xcb_query_pointer_reply(
			(xcb_connection_t*) display, cooky, &error);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	memcpy(resultBuffer, reply, sizeof(*reply));

	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetRootWindow
 * Signature: (JLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetRootWindow(JNIEnv *env,
		jclass class, jlong display, jobject buffer) {
	//TODO pass screen nr as well
	xcb_screen_t *screen = screen_of_display((xcb_connection_t*) display, 0);
	if (screen == NULL) {
		return (jboolean) 1;
	}

	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	memcpy(resultBuffer, &screen->root, sizeof(xcb_window_t));

	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetWindowAttributes
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetWindowAttributes(JNIEnv *env,
		jclass class, jlong display, jlong window, jobject buffer) {
	xcb_get_window_attributes_cookie_t cookie = xcb_get_window_attributes(
			(xcb_connection_t*) display, (xcb_window_t) window);

	xcb_generic_error_t* error;
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_get_window_attributes_reply_t *attributes =
			xcb_get_window_attributes_reply((xcb_connection_t*) display, cookie,
					&error);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	memcpy(resultBuffer, attributes, sizeof(xcb_get_window_attributes_reply_t));

	free(attributes);

	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGiveFocus
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGiveFocus(JNIEnv *env, jclass class,
		jlong display, jlong window, jobject buffer) {
	xcb_void_cookie_t void_cookie = xcb_set_input_focus_checked(
			(xcb_connection_t*) display, XCB_INPUT_FOCUS_POINTER_ROOT,
			(xcb_window_t) window, XCB_CURRENT_TIME);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeLower
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeLower(JNIEnv *env, jclass class,
		jlong display, jlong window, jobject buffer) {
	const static uint32_t values[] = { XCB_STACK_MODE_BELOW };
	xcb_void_cookie_t void_cookie = xcb_configure_window_checked(
			(xcb_connection_t*) display, (xcb_window_t) window,
			XCB_CONFIG_WINDOW_STACK_MODE, values);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeMap
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeMap(JNIEnv *env, jclass class,
		jlong display, jlong window, jobject buffer) {
	xcb_void_cookie_t void_cookie = xcb_map_window_checked(
			(xcb_connection_t*) display, (xcb_window_t) window);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeMove
 * Signature: (JJIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeMove(JNIEnv *env, jclass class,
		jlong display, jlong window, jint x, jint y, jobject buffer) {
	uint32_t values[] = { (uint32_t) x, (uint32_t) y };
	xcb_void_cookie_t void_cookie = xcb_configure_window_checked(
			(xcb_connection_t*) display, (xcb_window_t) window,
			XCB_CONFIG_WINDOW_X | XCB_CONFIG_WINDOW_Y, values);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeMoveResize
 * Signature: (JJIIIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeMoveResize(JNIEnv *env, jclass class,
		jlong display, jlong window, jint x, jint y, jint width, jint height,
		jobject buffer) {

	xcb_get_geometry_cookie_t geomCookie = xcb_get_geometry(
			(xcb_connection_t*) display, (xcb_window_t) window);

	xcb_generic_error_t* error;
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_get_geometry_reply_t *geom = xcb_get_geometry_reply(
			(xcb_connection_t*) display, geomCookie, &error);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	//compensate for the border in X. Compensating when retrieving a window's size is done in the java code.
	uint32_t values[] = { (uint32_t) x, (uint32_t) y, (uint32_t) width
			- (2 * geom->border_width), (uint32_t) height
			- (2 * geom->border_width) };
	xcb_void_cookie_t void_cookie = xcb_configure_window_checked(
			(xcb_connection_t*) display,
			(xcb_window_t) window,
			XCB_CONFIG_WINDOW_X | XCB_CONFIG_WINDOW_Y | XCB_CONFIG_WINDOW_WIDTH
					| XCB_CONFIG_WINDOW_HEIGHT, values);
	error = xcb_request_check((xcb_connection_t*) display, void_cookie);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeOpenDisplay
 * Signature: (Ljava/lang/String;Ljava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeOpenDisplay(JNIEnv *env,
		jclass class, jstring displayName, jobject buffer) {
	const char* nativeDisplayName = (*env)->GetStringUTFChars(env, displayName,
			0);
	xcb_connection_t *connection = xcb_connect(nativeDisplayName, 0);

	if (connection == NULL) {
		//TODO write error to buffer
		return (jboolean) 1;
	}
	if (xcb_connection_has_error(connection)) {
		//TODO write error to buffer
		return (jboolean) 1;
	}

	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	memcpy(resultBuffer, &connection, sizeof(xcb_connection_t*));

	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeOverrideRedirect
 * Signature: (JJZLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeOverrideRedirect(JNIEnv *env,
		jclass class, jlong display, jlong window, jboolean override,
		jobject buffer) {
	const uint32_t values[] = { override };
	xcb_void_cookie_t void_cookie = xcb_change_window_attributes_checked(
			(xcb_connection_t*) display, (xcb_window_t) window,
			XCB_CW_OVERRIDE_REDIRECT, values);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativePropagateEvent
 * Signature: (JJJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativePropagateEvent(JNIEnv *env,
		jclass class, jlong display, jlong window, jlong eventMask,
		jobject buffer) {
	const uint32_t values[] = { eventMask };
	xcb_void_cookie_t void_cookie = xcb_change_window_attributes_checked(
			(xcb_connection_t*) display, (xcb_window_t) window,
			XCB_CW_EVENT_MASK, values);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeRaise
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeRaise(JNIEnv *env, jclass class,
		jlong display, jlong window, jobject buffer) {
	uint32_t values[] = { XCB_STACK_MODE_ABOVE };
	xcb_void_cookie_t void_cookie = xcb_configure_window_checked(
			(xcb_connection_t*) display, (xcb_window_t) window,
			XCB_CONFIG_WINDOW_STACK_MODE, values);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeReparent
 * Signature: (JJJIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeReparent(JNIEnv *env, jclass class,
		jlong display, jlong childWindow, jlong parentWindow, jint x, jint y,
		jobject buffer) {
	xcb_void_cookie_t void_cookie = xcb_reparent_window_checked(
			(xcb_connection_t*) display, (xcb_window_t) childWindow,
			(xcb_window_t) parentWindow, (int16_t) x, (int16_t) y);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeResize
 * Signature: (JJIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeResize(JNIEnv *env, jclass class,
		jlong display, jlong window, jint width, jint height, jobject buffer) {
	xcb_get_geometry_cookie_t geomCookie = xcb_get_geometry(
			(xcb_connection_t*) display, (xcb_window_t) window);

	xcb_generic_error_t* error;
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_get_geometry_reply_t *geom = xcb_get_geometry_reply(
			(xcb_connection_t*) display, geomCookie, &error);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	//compensate for the border in X. Compensating when retrieving a window's size is done in the java code.
	uint32_t values[] = { (uint32_t) width - (2 * geom->border_width),
			(uint32_t) height - (2 * geom->border_width) };
	xcb_void_cookie_t void_cookie = xcb_configure_window_checked(
			(xcb_connection_t*) display, (xcb_window_t) window,
			XCB_CONFIG_WINDOW_WIDTH | XCB_CONFIG_WINDOW_HEIGHT, values);

	error = xcb_request_check((xcb_connection_t*) display, void_cookie);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeSaveYourself
 * Signature: (JJZLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeSaveYourself(JNIEnv *env,
		jclass class, jlong display, jlong window, jboolean jBool,
		jobject buffer) {
	xcb_void_cookie_t void_cookie = xcb_change_save_set_checked(
			(xcb_connection_t*) display, (uint8_t) jBool,
			(xcb_window_t) window);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeShutDown
 * Signature: (JLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeShutDown(JNIEnv *env, jclass class,
		jlong display, jobject buffer) {
	xcb_disconnect((xcb_connection_t*) display);
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeUnmap
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeUnmap(JNIEnv *env, jclass class,
		jlong display, jlong window, jobject buffer) {
	xcb_void_cookie_t void_cookie = xcb_unmap_window_checked(
			(xcb_connection_t*) display, (xcb_window_t) window);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetKeySymbol
 * Signature: (JIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetKeySymbol(JNIEnv *env,
		jclass class, jlong keySyms, jint keyCode, jint keyColumn,
		jobject buffer) {
	xcb_keysym_t keySymbol = xcb_key_symbols_get_keysym(
			(xcb_key_symbols_t*) keySyms, (xcb_keycode_t) keyCode,
			(int) keyColumn);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	memcpy(resultBuffer, &keySymbol, sizeof(xcb_keysym_t));
	return 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetKeyCode
 * Signature: (JLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetKeyCodes(JNIEnv *env,
		jclass class, jlong keySyms, jlong keySym, jobject buffer) {
	xcb_keycode_t* keyCodes = xcb_key_symbols_get_keycode(
			(xcb_key_symbols_t*) keySyms, (xcb_keysym_t) keySym);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);

	if (keyCodes != NULL) {
		int i = 0;
		while (keyCodes[i + 1] != XCB_NO_SYMBOL) {
			i++;
		}

		void* resultIndex = mempcpy(resultBuffer, &i, sizeof(int));
		if (i) {
			memcpy(resultIndex, keyCodes, sizeof(xcb_keycode_t) * i);
		}
	} else {
		return (jboolean) 1;
	}

	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGrabKey
 * Signature: (JIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGrabKey(JNIEnv *env, jclass class,
		jlong display, jlong windowId, jint keyCode, jint modifiersMask,
		jobject buffer) {
	xcb_void_cookie_t void_cookie = xcb_grab_key_checked(
			(xcb_connection_t*) display, 0, (xcb_window_t) windowId,
			(uint16_t) modifiersMask, (xcb_keycode_t) keyCode,
			XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGrabButton
 * Signature: (JIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGrabButton(JNIEnv *env, jclass class,
		jlong display, jlong windowId, jint buttonCode, jint modifiersMask,
		jobject buffer) {

	xcb_void_cookie_t void_cookie = xcb_grab_button_checked(
			(xcb_connection_t*) display, 0, (xcb_window_t) windowId,
			XCB_EVENT_MASK_BUTTON_PRESS | XCB_EVENT_MASK_BUTTON_RELEASE,
			XCB_GRAB_MODE_ASYNC, XCB_GRAB_MODE_ASYNC, XCB_NONE, XCB_NONE,
			buttonCode, modifiersMask);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeUngrabKey
 * Signature: (JJIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeUngrabKey(JNIEnv *env, jclass class,
		jlong display, jlong windowId, jint keyCode, jint modifiersMask,
		jobject buffer) {
	xcb_void_cookie_t void_cookie = xcb_ungrab_key_checked(
			(xcb_connection_t*) display, keyCode, (xcb_window_t) windowId,
			modifiersMask);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeUngrabButton
 * Signature: (JJIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeUngrabButton(JNIEnv *env,
		jclass class, jlong display, jlong windowId, jint buttonCode,
		jint modifiersMask, jobject buffer) {
	xcb_void_cookie_t void_cookie = xcb_ungrab_button_checked(
			(xcb_connection_t*) display, buttonCode, (xcb_window_t) windowId,
			modifiersMask);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);
	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeSetSelectionOwner
 * Signature: (JJJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_fusion_x11_core_XCoreNative_nativeSetSelectionOwner(
		JNIEnv *env, jclass class, jlong display, jlong selectionAtom,
		jlong owner, jobject buffer) {
	xcb_void_cookie_t void_cookie = xcb_set_selection_owner_checked(
			(xcb_connection_t*) display, (xcb_window_t) owner,
			(xcb_atom_t) selectionAtom, XCB_TIME_CURRENT_TIME);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);

	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetSelectionOwner
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_fusion_x11_core_XCoreNative_nativeGetSelectionOwner(
		JNIEnv *env, jclass class, jlong display, jlong selectionAtom,
		jobject buffer) {

	xcb_get_selection_owner_cookie_t cookie = xcb_get_selection_owner(
			(xcb_connection_t*) display, (xcb_atom_t) selectionAtom);
	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);

	xcb_generic_error_t* error;
	xcb_get_selection_owner_reply_t* reply = xcb_get_selection_owner_reply(
			display, cookie, &error);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	memcpy(resultBuffer, reply->owner, sizeof(xcb_window_t));

	return (jboolean) 0;
}

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeCreateNewWindow
 * Signature: (JLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_fusion_x11_core_XCoreNative_nativeCreateNewWindow(
		JNIEnv *env, jclass class, jlong display, jobject buffer) {

	uint32_t id = xcb_generate_id((xcb_connection_t*) display);
	int16_t x = 0;
	int16_t y = 0;
	uint16_t width = 1;
	uint16_t height = 1;
	uint16_t borderWidth = 0;

	/* Get the first screen */
	const xcb_setup_t *setup = xcb_get_setup(display);
	xcb_screen_iterator_t iter = xcb_setup_roots_iterator(setup);
	xcb_screen_t *screen = iter.data;

	xcb_void_cookie_t void_cookie = xcb_create_window_checked(
			(xcb_connection_t*) display, XCB_COPY_FROM_PARENT, id, screen->root,
			x, y, width, height, borderWidth, XCB_WINDOW_CLASS_INPUT_OUTPUT,
			screen->root_visual, 0, NULL);

	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);

	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			void_cookie);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	memcpy(resultBuffer, &id, sizeof(uint32_t));

	return (jboolean) 0;
}
