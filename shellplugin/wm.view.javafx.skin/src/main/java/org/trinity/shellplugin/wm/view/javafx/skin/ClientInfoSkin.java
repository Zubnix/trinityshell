package org.trinity.shellplugin.wm.view.javafx.skin;


import com.sun.javafx.scene.control.skin.SkinBase;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.trinity.foundation.api.render.binding.view.DataContext;
import org.trinity.foundation.api.render.binding.view.EventSignal;
import org.trinity.foundation.api.render.binding.view.EventSignals;
import org.trinity.foundation.api.render.binding.view.SubView;
import org.trinity.shellplugin.wm.view.javafx.ClientInfoView;

import java.io.IOException;

@EventSignals(@EventSignal(name = "onPointerInput",
                           filter = PointerInputFilter.class))
public class ClientInfoSkin extends SkinBase<ClientInfoView, ClientInfoBehavior> {

    private final StringProperty clientName = new SimpleStringProperty("Client name goes here :)");

    @FXML
    @SubView
    @DataContext("closeButton")
    @EventSignals(@EventSignal(name = "onPointerInput",
                               filter = PointerInputFilter.class))
    private Node closeButton;

    public ClientInfoSkin(final ClientInfoView clientInfoView) throws IOException {
        super(clientInfoView,
              new ClientInfoBehavior(clientInfoView));

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ClientInfoSkin.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
    }

    public String getClientName() {
        return clientName.get();
    }

    public void setClientName(String clientName) {
        this.clientName.setValue(clientName);
    }

    public StringProperty clientNameProperty() {
        return clientName;
    }
}
