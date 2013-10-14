package org.trinity.shellplugin.wm.view.javafx.skin;

import com.sun.javafx.scene.control.skin.SkinBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.SubView;
import org.trinity.shellplugin.wm.view.javafx.ClientInfoView;
import org.trinity.shellplugin.wm.view.javafx.DesktopView;

import java.io.IOException;


public class DesktopSkin extends SkinBase<DesktopView, DesktopBehavior> {

    static {
        Font.loadFont(DesktopSkin.class.getResource("/font/fontawesome-webfont.ttf").toExternalForm(),
                      16.0);
    }

    @FXML
    @SubView
    @ObservableCollection(value = "clientsBar",
                          view = ClientInfoView.class)
    private Pane topBar;

    public DesktopSkin(final DesktopView desktopView) throws IOException {
        super(desktopView,
              new DesktopBehavior(desktopView));

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/DesktopSkin.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
    }
}
