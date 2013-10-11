package org.trinity.shellplugin.wm.view.javafx;

import javafx.scene.control.Skin;
import org.trinity.foundation.render.javafx.api.FXView;


public class ClientInfoView extends FXView {

    //reference our skin as a 'local' field so the view binder can pick it up.
    private final Skin<?> skin;


    public ClientInfoView(final Skin<?> skin) {
        getStyleClass().add("client-info-view");
        this.skin = getSkin();
    }

    @Override
    protected String getUserAgentStylesheet() {
        return getClass().getResource("/skin.css").toExternalForm();
    }
}
