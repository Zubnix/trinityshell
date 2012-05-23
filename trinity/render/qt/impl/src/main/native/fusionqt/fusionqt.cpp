/*
 * fusion_qt.c
 *
 *  Created on: May 11, 2010
 *      Author: Erik De Rijcke
 */
#include "include/fusionqt.h"
#include <QtGui/qwidget.h>
#include <xcb/xcb.h>

/*
 * Class:     org_fusion_qt_nativeqt_QtNativeCalls
 * Method:    fromPlatformHandle
 * Signature: (J)Lcom/trolltech/qt/gui/QWidget;
 */
JNIEXPORT jobject JNICALL
Java_org_fusion_qt_nativeqt_QtNativeCalls_fromPlatformHandle(JNIEnv *env,
		jclass clazz, jlong handle) {

	QWidget* widget = QWidget::find((WId) handle);
	jobject qwidget = qtjambi_from_QWidget(env, widget);

	return qwidget;
}
