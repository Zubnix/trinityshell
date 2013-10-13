package org.trinity.shellplugin.wm.view.javafx;

import com.google.inject.Inject;
import javafx.scene.control.Skin;
import org.trinity.foundation.api.render.binding.view.SubView;
import org.trinity.foundation.render.javafx.api.FXView;


public class ClientInfoView extends FXView {

    //reference our skin as a 'local' field so the view binder can pick it up.
    @SubView
    private final Skin<?> skin;

    @Inject
    ClientInfoView() {
        getStyleClass().add("client-info-view");
        skin = getSkin();
    }

    @Override
    protected String getUserAgentStylesheet() {
        return getClass().getResource("/skin.css").toExternalForm();
    }
}
