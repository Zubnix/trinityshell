package org.trinity.shellplugin.wm.view.javafx.skin;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.SubView;
import org.trinity.shellplugin.wm.view.javafx.ClientInfoView;
import org.trinity.shellplugin.wm.view.javafx.DesktopView;

import java.io.IOException;


public class DesktopSkin extends SkinBase<DesktopView> {

    static {
        Font.loadFont(DesktopSkin.class.getResource("/font/fontawesome-webfont.ttf").toExternalForm(),
                      16.0);
    }

    @SubView
    @ObservableCollection(value = "clientsBar",
                          view = ClientInfoView.class)
    public Pane topBar;

    public DesktopSkin(final DesktopView desktopView) throws IOException {
        super(desktopView);

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/DesktopSkin.fxml"));
        fxmlLoader.setRoot(desktopView);
        fxmlLoader.setController(desktopView);
        fxmlLoader.load();
    }
}
