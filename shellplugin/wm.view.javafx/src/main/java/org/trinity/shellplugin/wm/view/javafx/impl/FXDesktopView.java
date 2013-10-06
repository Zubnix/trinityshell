package org.trinity.shellplugin.wm.view.javafx.impl;


import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.trinity.foundation.render.javafx.api.FXView;

import javax.inject.Inject;

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;


@Bind(to = @To(IMPLEMENTATION))
public class FXDesktopView extends FXView {

    @Inject
    FXDesktopView() {
        getStyleClass().add("view");
    }

    @Override
    protected String getUserAgentStylesheet() {
        return FXDesktopView.class.getResource("FXDesktopView.css").toExternalForm();
    }
}
