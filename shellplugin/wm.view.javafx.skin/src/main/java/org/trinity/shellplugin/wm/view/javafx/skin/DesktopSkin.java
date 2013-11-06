package org.trinity.shellplugin.wm.view.javafx.skin;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.SubView;
import org.trinity.shellplugin.wm.view.javafx.ClientInfoViewImpl;
import org.trinity.shellplugin.wm.view.javafx.DesktopViewImpl;

import java.io.IOException;

public class DesktopSkin extends StackPane implements Skin<DesktopViewImpl> {

    static {
        Font.loadFont(DesktopSkin.class.getResource("/font/fontawesome-webfont.ttf").toExternalForm(),
                      16.0);
    }

    @SubView
    @ObservableCollection(value = "clientsBar",
                          view = ClientInfoViewImpl.class)
    public Pane topBar;
    private DesktopViewImpl desktopViewImpl;

    public DesktopSkin(final DesktopViewImpl desktopViewImpl) throws IOException {
        this.desktopViewImpl = desktopViewImpl;

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/DesktopSkin.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
    }

    @Override
    public DesktopViewImpl getSkinnable() {
        return this.desktopViewImpl;
    }

    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public void dispose() {

    }
}