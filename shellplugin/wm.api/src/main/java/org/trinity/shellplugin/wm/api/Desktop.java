package org.trinity.shellplugin.wm.api;

import org.trinity.shell.api.widget.ShellWidget;

import java.util.List;

public interface Desktop extends ShellWidget{

	List<Object> getNotificationsBar();

	List<Object> getClientsBar();

	List<Object> getBottomBar();
}