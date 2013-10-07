package org.trinity.shellplugin.wm.view.javafx;


import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.trinity.foundation.render.javafx.api.FXView;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;


@Bind(to = @To(IMPLEMENTATION))
@Singleton
public class DesktopView extends FXView {

    @Inject
    DesktopView() {
        getStyleClass().add("desktop-view");
    }

    @Override
    protected String getUserAgentStylesheet() {
        return getClass().getResource("/org/trinity/shellplugin/wm/view/javafx/skin/skin.css").toExternalForm();
    }
}
