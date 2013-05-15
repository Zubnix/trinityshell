package org.trinity.shellplugin.wm.api;

import java.util.List;

public interface Desktop {

	List<Object> getNotificationsBar();

	List<Object> getClientsBar();

	List<Object> getBottomBar();
}