package org.trinity.shellplugin.wm.view.javafx;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import org.trinity.foundation.api.render.binding.Binder;
import org.trinity.foundation.render.javafx.api.FXView;
import org.trinity.shell.api.bindingkey.ShellExecutor;


public class ClientInfoView extends FXView {

    @Inject
    ClientInfoView(final @ShellExecutor ListeningExecutorService shellExecutor,
                   final Binder binder) {
        super(shellExecutor,binder);
        getStyleClass().add("client-info-view");
    }

    @Override
    protected String getUserAgentStylesheet() {
        return getClass().getResource("/skin.css").toExternalForm();
    }
}
