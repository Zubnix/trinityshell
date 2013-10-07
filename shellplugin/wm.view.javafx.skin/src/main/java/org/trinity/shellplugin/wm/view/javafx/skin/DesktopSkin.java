package org.trinity.shellplugin.wm.view.javafx.skin;

import com.sun.javafx.scene.control.skin.SkinBase;
import javafx.fxml.FXMLLoader;
import org.trinity.shellplugin.wm.view.javafx.DesktopView;

import java.io.IOException;


public class DesktopSkin extends SkinBase<DesktopView, DesktopBehavior> {

    public DesktopSkin(final DesktopView desktopView) throws IOException {
        super(desktopView,
                new DesktopBehavior(desktopView));

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/trinity/shellplugin/wm/view/javafx/skin/DesktopSkin.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(getSkinnable());
        fxmlLoader.load();
    }
}
