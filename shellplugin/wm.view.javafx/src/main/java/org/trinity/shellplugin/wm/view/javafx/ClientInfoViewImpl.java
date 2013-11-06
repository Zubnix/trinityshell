package org.trinity.shellplugin.wm.view.javafx;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.trinity.foundation.api.render.binding.ViewBinder;
import org.trinity.foundation.render.javafx.api.FXView;
import org.trinity.shell.api.bindingkey.ShellExecutor;

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;

@Bind(to = @To(IMPLEMENTATION))
public class ClientInfoViewImpl extends FXView {

    @Inject
    ClientInfoViewImpl(final @ShellExecutor ListeningExecutorService shellExecutor,
                       final ViewBinder viewBinder) {
        super(shellExecutor,
              viewBinder);
        getStyleClass().add("client-info-view");
    }

    @Override
    protected String getUserAgentStylesheet() {
        return getClass().getResource("/skin.css").toExternalForm();
    }
}
