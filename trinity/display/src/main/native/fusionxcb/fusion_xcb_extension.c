/*
 * fusion_xcb_extension.c
 *
 *  Created on: Oct 20, 2011
 *      Author: Erik De Rijcke
 */

#include "include/jni/org_fusion_x11_core_extension_XExtensionNative.h"
#include "include/fusion_xcb.h"

#include <xcb/damage.h>
#include <xcb/shape.h>
#include <xcb/render.h>
#include <xcb/composite.h>
#include <xcb/sync.h>

/*
 * Class:     org_fusion_x11_core_extension_XExtensionNative
 * Method:    nativeXCompositeRedirectSubwindow
 * Signature: (JJILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_extension_XExtensionNative_nativeXCompositeRedirectSubwindow(
		JNIEnv *env, jclass class, jlong display, jlong window, jint updateMode,
		jobject buffer) {
	xcb_void_cookie_t cookie = xcb_composite_redirect_subwindows_checked(
			(xcb_connection_t *) display, (xcb_window_t) window,
			(uint8_t) updateMode);

	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			cookie);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return 0;
}

/*
 * Class:     org_fusion_x11_core_extension_XExtensionNative
 * Method:    nativeXDamageSubtract
 * Signature: (JJJJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_extension_XExtensionNative_nativeXDamageSubtract(
		JNIEnv *env, jclass class, jlong display, jlong damage,
		jlong repairRegion, jlong partsRegion, jobject buffer) {

	xcb_void_cookie_t cookie = xcb_damage_subtract_checked(
			(xcb_connection_t *) display, (xcb_damage_damage_t) damage,
			(xcb_xfixes_region_t) repairRegion,
			(xcb_xfixes_region_t) partsRegion);

	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			cookie);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}
	return 0;
}

/*
 * Class:     org_fusion_x11_core_extension_XExtensionNative
 * Method:    nativeXDamageCreate
 * Signature: (JJILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_extension_XExtensionNative_nativeXDamageCreate(
		JNIEnv *env, jclass class, jlong display, jlong window,
		jint damageToReport, jobject buffer) {

	uint32_t damage = xcb_generate_id(display);
	xcb_void_cookie_t cookie = xcb_damage_create_checked(display, damage,
			window, damageToReport);

	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);
	xcb_generic_error_t* error = xcb_request_check((xcb_connection_t*) display,
			cookie);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	memcpy(resultBuffer, &damage, sizeof(uint32_t));

	return 0;
}

/*
 * Class:     org_fusion_x11_core_extension_XExtensionNative
 * Method:    nativeXDamageInit
 * Signature: (JIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_extension_XExtensionNative_nativeXDamageInit(
		JNIEnv *env, jclass class, jlong display, jint majorVersion,
		jint minorVersion, jobject buffer) {

	xcb_damage_query_version_cookie_t cookie = xcb_damage_query_version(
			(xcb_connection_t *) display, (uint32_t) majorVersion,
			(uint32_t) minorVersion);

	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);

	xcb_generic_error_t * error;
	xcb_damage_query_version_reply_t* reply = xcb_damage_query_version_reply(
			(xcb_connection_t *) display, cookie, &error);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	void* resultIndex = mempcpy(resultBuffer, &reply->major_version,
			sizeof(uint32_t));
	memcpy(resultIndex, &reply->minor_version, sizeof(uint32_t));

	return 0;
}

/*
 * Class:     org_fusion_x11_core_extension_XExtensionNative
 * Method:    nativeXCompositeInit
 * Signature: (JIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_extension_XExtensionNative_nativeXCompositeInit(
		JNIEnv *env, jclass class, jlong display, jint majorVersion,
		jint minorVersion, jobject buffer) {
	xcb_composite_query_version_cookie_t cookie = xcb_composite_query_version(
			(xcb_connection_t *) display, (uint32_t) majorVersion,
			(uint32_t) minorVersion);

	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);

	xcb_generic_error_t * error;
	xcb_composite_query_version_reply_t* reply =
			xcb_composite_query_version_reply((xcb_connection_t *) display,
					cookie, &error);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	void* resultIndex = mempcpy(resultBuffer, &reply->major_version,
			sizeof(uint32_t));
	memcpy(resultIndex, &reply->minor_version, sizeof(uint32_t));

	return 0;
}

/*
 * Class:     org_fusion_x11_core_extension_XExtensionNative
 * Method:    nativeXFixesInit
 * Signature: (JIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_extension_XExtensionNative_nativeXFixesInit(
		JNIEnv *env, jclass class, jlong display, jint majorVersion,
		jint minorVersion, jobject buffer) {
	xcb_xfixes_query_version_cookie_t cookie = xcb_xfixes_query_version(
			(xcb_connection_t *) display, (uint32_t) majorVersion,
			(uint32_t) minorVersion);

	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);

	xcb_generic_error_t * error;
	xcb_xfixes_query_version_reply_t* reply = xcb_xfixes_query_version_reply(
			(xcb_connection_t *) display, cookie, &error);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	void* resultIndex = mempcpy(resultBuffer, &reply->major_version,
			sizeof(uint32_t));
	memcpy(resultIndex, &reply->minor_version, sizeof(uint32_t));

	return 0;
}

/*
 * Class:     org_fusion_x11_core_extension_XExtensionNative
 * Method:    nativeXRenderInit
 * Signature: (JIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_extension_XExtensionNative_nativeXRenderInit(
		JNIEnv *env, jclass class, jlong display, jint majorVersion,
		jint minorVersion, jobject buffer) {
	xcb_render_query_version_cookie_t cookie = xcb_render_query_version(
			(xcb_connection_t *) display, (uint32_t) majorVersion,
			(uint32_t) minorVersion);

	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);

	xcb_generic_error_t * error;
	xcb_render_query_version_reply_t* reply = xcb_render_query_version_reply(
			(xcb_connection_t *) display, cookie, &error);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	void* resultIndex = mempcpy(resultBuffer, &reply->major_version,
			sizeof(uint32_t));
	memcpy(resultIndex, &reply->minor_version, sizeof(uint32_t));

	return 0;
}

/*
 * Class:     org_fusion_x11_core_extension_XExtensionNative
 * Method:    nativeXShapeInit
 * Signature: (JIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_extension_XExtensionNative_nativeXShapeInit(
		JNIEnv *env, jclass class, jlong display, jint majorVersion,
		jint minorVersion, jobject buffer) {
	xcb_shape_query_version_cookie_t cookie = xcb_shape_query_version(
			(xcb_connection_t *) display);

	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);

	xcb_generic_error_t * error;
	xcb_shape_query_version_reply_t* reply = xcb_shape_query_version_reply(
			(xcb_connection_t *) display, cookie, &error);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	void* resultIndex = mempcpy(resultBuffer, &reply->major_version,
			sizeof(uint32_t));
	memcpy(resultIndex, &reply->minor_version, sizeof(uint32_t));

	return 0;
}

/*
 * Class:     org_fusion_x11_core_extension_XExtensionNative
 * Method:    nativeXSyncInit
 * Signature: (JIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_extension_XExtensionNative_nativeXSyncInit(JNIEnv *env,
		jclass class, jlong display, jint majorVersion, jint minorVersion,
		jobject buffer) {

	xcb_sync_initialize_cookie_t cookie = xcb_sync_initialize(
			(xcb_connection_t *) display, (uint32_t) majorVersion,
			(uint32_t) minorVersion);

	void* resultBuffer = (*env)->GetDirectBufferAddress(env, buffer);

	xcb_generic_error_t * error;
	xcb_sync_initialize_reply_t * reply = xcb_sync_initialize_reply(
			(xcb_connection_t *) display, cookie, &error);

	if (error) {
		writeError(error);
		return (jboolean) 1;
	}

	void* resultIndex = mempcpy(resultBuffer, &reply->major_version,
			sizeof(uint32_t));
	memcpy(resultIndex, &reply->minor_version, sizeof(uint32_t));

	return 0;
}

/*
 * Class:     org_fusion_x11_core_extension_XExtensionNative
 * Method:    nativeXSyncAwaitCondition
 * Signature: (JLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_extension_XExtensionNative_nativeXSyncAwaitCondition(
		JNIEnv *env, jclass class, jlong display, jobject buffer) {
	return 1;
}
