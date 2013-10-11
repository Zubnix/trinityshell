package org.trinity.shellplugin.wm.view.javafx.skin;

import com.sun.javafx.scene.control.skin.SkinBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.SubView;
import org.trinity.shellplugin.wm.view.javafx.ClientInfoView;
import org.trinity.shellplugin.wm.view.javafx.DesktopView;

import java.io.IOException;


@SubView
public class DesktopSkin extends SkinBase<DesktopView, DesktopBehavior> {

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
