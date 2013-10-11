package org.trinity.shellplugin.wm.view.javafx.skin;


import com.sun.javafx.scene.control.skin.SkinBase;
import org.trinity.shellplugin.wm.view.javafx.ClientInfoView;

public class ClientInfoSkin extends SkinBase<ClientInfoView, ClientInfoBehavior> {
    public ClientInfoSkin(final ClientInfoView clientInfoView) {
        super(clientInfoView,
              new ClientInfoBehavior(clientInfoView));
    }
}
