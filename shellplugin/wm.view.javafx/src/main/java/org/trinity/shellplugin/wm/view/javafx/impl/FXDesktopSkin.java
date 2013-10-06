package org.trinity.shellplugin.wm.view.javafx.impl;

import com.sun.javafx.scene.control.skin.SkinBase;
import javafx.fxml.FXMLLoader;

import java.io.IOException;


public class FXDesktopSkin extends SkinBase<FXDesktopView, FXDesktopBehavior> {

    public FXDesktopSkin(final FXDesktopView fxDesktopView) throws IOException {
        super(fxDesktopView,
              new FXDesktopBehavior(fxDesktopView));

        final FXMLLoader fxmlLoader = new FXMLLoader(FXDesktopView.class.getResource("FXDesktopSkin.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(getSkinnable());
        fxmlLoader.load();
    }
}
