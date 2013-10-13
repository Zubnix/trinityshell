package org.trinity.shellplugin.wm.view.javafx.skin;


import com.sun.javafx.scene.control.skin.SkinBase;
import javafx.fxml.FXMLLoader;
import org.trinity.shellplugin.wm.view.javafx.ClientInfoView;

import java.io.IOException;

public class ClientInfoSkin extends SkinBase<ClientInfoView, ClientInfoBehavior> {
    public ClientInfoSkin(final ClientInfoView clientInfoView) throws IOException {
        super(clientInfoView,
              new ClientInfoBehavior(clientInfoView));

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ClientInfoSkin.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
    }
}
