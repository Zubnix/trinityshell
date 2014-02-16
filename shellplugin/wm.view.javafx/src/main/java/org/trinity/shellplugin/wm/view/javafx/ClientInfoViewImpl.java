package org.trinity.shellplugin.wm.view.javafx;

import com.google.inject.Inject;
import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.trinity.foundation.api.render.binding.ViewBinder;
import org.trinity.foundation.render.javafx.api.FXView;

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;

@Bind(to = @To(IMPLEMENTATION))
public class ClientInfoViewImpl extends FXView {

    @Inject
    ClientInfoViewImpl(
                       final ViewBinder viewBinder) {
        super(
              viewBinder);
        getStyleClass().add("client-info-view");
    }

    @Override
    protected String getUserAgentStylesheet() {
        return getClass().getResource("/skin.css").toExternalForm();
    }
}
