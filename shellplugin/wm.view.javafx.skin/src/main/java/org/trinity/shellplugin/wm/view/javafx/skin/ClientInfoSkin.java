package org.trinity.shellplugin.wm.view.javafx.skin;


import javafx.fxml.FXMLLoader;
import javafx.scene.control.SkinBase;
import org.trinity.foundation.api.render.binding.view.DataModelContext;
import org.trinity.foundation.api.render.binding.view.EventSignal;
import org.trinity.foundation.api.render.binding.view.EventSignals;
import org.trinity.foundation.api.render.binding.view.SubView;
import org.trinity.shellplugin.wm.view.javafx.ClientInfoView;
import org.trinity.shellplugin.wm.view.javafx.PointerInputFilter;

import java.io.IOException;


public class ClientInfoSkin extends SkinBase<ClientInfoView> {

    @SubView
    @DataModelContext("closeButton")
    @EventSignals(@EventSignal(name = "onPointerInput",
                               filter = PointerInputFilter.class))
    public Icon closeButton;

    public ClientInfoSkin(final ClientInfoView clientInfoView) throws IOException {
        super(clientInfoView);

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ClientInfoSkin.fxml"));
        fxmlLoader.setRoot(clientInfoView);
        fxmlLoader.setController(clientInfoView);
        fxmlLoader.load();
    }
}