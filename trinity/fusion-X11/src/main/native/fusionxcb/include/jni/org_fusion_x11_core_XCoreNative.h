#include <jni.h>
/* Header for class org_fusion_x11_core_XCoreNative */

#ifndef _Included_org_fusion_x11_core_XCoreNative
#define _Included_org_fusion_x11_core_XCoreNative
#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetInputFocus
 * Signature: (J[BLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetInputFocus(JNIEnv *, jclass,
		jlong, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeUngrabKeyboard
 * Signature: (J[BLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeUngrabKeyboard(JNIEnv *, jclass,
		jlong, jint, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeUngrabMouse
 * Signature: (J[BLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeUngrabMouse(JNIEnv *, jclass, jlong,
		jint, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGrabMouse
 * Signature: (JJ[BLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGrabMouse(JNIEnv *, jclass, jlong,
		jlong, jint, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGrabKeyboard
 * Signature: (JJ[BLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGrabKeyboard(JNIEnv *, jclass, jlong,
		jlong, jint, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeSendClientMessage
 * Signature: (JJJI[BLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeSendClientMessage(JNIEnv *, jclass,
		jlong, jlong, jlong, jint, jbyteArray, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeRegisterAtom
 * Signature: (JLjava/lang/String;Ljava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNativee_nativeRegisterAtom(JNIEnv *, jclass,
		jlong, jstring, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeChangeProperty
 * Signature: (JJJJILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean
JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeChangeProperty(JNIEnv *, jclass,
		jlong, jlong, jlong, jlong, jint, jbyteArray, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetProperty
 * Signature: (JJJJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean
JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetProperty(JNIEnv *, jclass, jlong,
		jlong, jlong, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeDestroy
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_fusion_x11_core_XCoreNative_nativeDestroy(
		JNIEnv *, jclass, jlong, jlong, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeFlush
 * Signature: (JLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_fusion_x11_core_XCoreNative_nativeFlush(
		JNIEnv *, jclass, jlong, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetChildren
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetChildren(JNIEnv *, jclass, jlong,
		jlong, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetCurrentWindowGeometry
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetCurrentWindowGeometry(JNIEnv *,
		jclass, jlong, jlong, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetNextEvent
 * Signature: (JLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetNextEvent(JNIEnv *, jclass, jlong,
		jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetPointerInfo
 * Signature: (JLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetPointerInfo(JNIEnv *, jclass,
		jlong, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetRootWindow
 * Signature: (JLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetRootWindow(JNIEnv *, jclass,
		jlong, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetWindowAttributes
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetWindowAttributes(JNIEnv *, jclass,
		jlong, jlong, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGiveFocus
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGiveFocus(JNIEnv *, jclass, jlong,
		jlong, jint, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeLower
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_fusion_x11_core_XCoreNative_nativeLower(
		JNIEnv *, jclass, jlong, jlong, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeMap
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_fusion_x11_core_XCoreNative_nativeMap(
		JNIEnv *, jclass, jlong, jlong, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeMove
 * Signature: (JJIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_fusion_x11_core_XCoreNative_nativeMove(
		JNIEnv *, jclass, jlong, jlong, jint, jint, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeMoveResize
 * Signature: (JJIIIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeMoveResize(JNIEnv *, jclass, jlong,
		jlong, jint, jint, jint, jint, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeOpenDisplay
 * Signature: (Ljava/lang/String;Ljava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeOpenDisplay(JNIEnv *, jclass,
		jstring, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeOverrideRedirect
 * Signature: (JJZLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeOverrideRedirect(JNIEnv *, jclass,
		jlong, jlong, jboolean, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativePlatformIntBytesCount
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_fusion_x11_core_XCoreNative_nativePlatformIntBytesCount(JNIEnv *,
		jclass);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativePlatformLongBytesCount
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_fusion_x11_core_XCoreNative_nativePlatformLongBytesCount(JNIEnv *,
		jclass);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativePlatformPointerBytesCount
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_fusion_x11_core_XCoreNative_nativePlatformPointerBytesCount(JNIEnv *,
		jclass);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativePropagateEvent
 * Signature: (JJJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativePropagateEvent(JNIEnv *, jclass,
		jlong, jlong, jlong, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeRaise
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_fusion_x11_core_XCoreNative_nativeRaise(
		JNIEnv *, jclass, jlong, jlong, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeReparent
 * Signature: (JJJIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_fusion_x11_core_XCoreNative_nativeReparent(
		JNIEnv *, jclass, jlong, jlong, jlong, jint, jint, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeRequestDestroy
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeRequestDestroy(JNIEnv *, jclass,
		jlong, jlong, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeResize
 * Signature: (JJIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_fusion_x11_core_XCoreNative_nativeResize(
		JNIEnv *, jclass, jlong, jlong, jint, jint, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeSaveYourself
 * Signature: (JJZLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeSaveYourself(JNIEnv *, jclass, jlong,
		jlong, jboolean, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeShutDown
 * Signature: (JLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeShutDown(JNIEnv *, jclass, jlong,
		jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeUnmap
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeUnmap(JNIEnv *, jclass, jlong, jlong,
		jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetKeySymbol
 * Signature: (JIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetKeySymbol(JNIEnv *, jclass, jlong,
		jint, jint, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetKeyCode
 * Signature: (JLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_fusion_x11_core_XCoreNative_nativeGetKeyCodes(JNIEnv *, jclass, jlong,
		jlong, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGrabKey
 * Signature: (JJIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_fusion_x11_core_XCoreNative_nativeGrabKey(
		JNIEnv *, jclass, jlong, jlong, jint, jint, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGrabButton
 * Signature: (JJIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_fusion_x11_core_XCoreNative_nativeGrabButton(
		JNIEnv *, jclass, jlong, jlong, jint, jint, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeUngrabKey
 * Signature: (JJIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_fusion_x11_core_XCoreNative_nativeUngrabKey(
		JNIEnv *, jclass, jlong, jlong, jint, jint, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeUngrabButton
 * Signature: (JJIILjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_fusion_x11_core_XCoreNative_nativeUngrabButton(
		JNIEnv *, jclass, jlong, jlong, jint, jint, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeSetSelectionOwner
 * Signature: (JJJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_fusion_x11_core_XCoreNative_nativeSetSelectionOwner(
		JNIEnv *, jclass, jlong, jlong, jlong, jint, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeGetSelectionOwner
 * Signature: (JJLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_fusion_x11_core_XCoreNative_nativeGetSelectionOwner(
		JNIEnv *, jclass, jlong, jlong, jobject);

/*
 * Class:     org_fusion_x11_core_XCoreNative
 * Method:    nativeCreateNewWindow
 * Signature: (JLjava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_fusion_x11_core_XCoreNative_nativeCreateNewWindow(
		JNIEnv *, jclass, jlong, jobject);

#ifdef __cplusplus
}
#endif
#endif
