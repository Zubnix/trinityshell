// package org.trinity.render.qt.impl;
//
// import org.trinity.render.qt.api.QJViewEventSubscription;
//
// import com.trolltech.qt.core.QObject;
// import com.trolltech.qt.gui.QWidget;
//
// public class QJViewEventSubscriptionImpl implements QJViewEventSubscription {
//
// private QWidget view;
// private QObject renderEventFilter;
//
// private boolean subscribed = true;
//
// public QJViewEventSubscriptionImpl(QWidget view, QObject renderEventFilter) {
// this.view = view;
// this.renderEventFilter = renderEventFilter;
// }
//
// @Override
// public QWidget getView() {
// return view;
// }
//
// @Override
// public void unsubscripe() {
// getView().removeEventFilter(renderEventFilter);
// subscribed = false;
// }
//
// @Override
// public boolean isSubscriped() {
// return subscribed;
// }
// }