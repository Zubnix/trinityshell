package org.trinity.shellplugin.wm.view.javafx.skin;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.layout.StackPane;
import org.trinity.foundation.api.render.binding.view.DataModelContext;
import org.trinity.foundation.api.render.binding.view.EventSignal;
import org.trinity.foundation.api.render.binding.view.EventSignals;
import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;
import org.trinity.foundation.api.render.binding.view.SubView;
import org.trinity.shellplugin.wm.view.javafx.ClientInfoViewImpl;
import org.trinity.shellplugin.wm.view.javafx.PointerInputFilter;

import java.io.IOException;

@EventSignals(@EventSignal(name = "onPointerInput",
                           filter = PointerInputFilter.class))
@PropertySlots(@PropertySlot(propertyName = "text",
                             methodName = "setClientName",
                             argumentTypes = String.class))
public class ClientInfoSkin extends StackPane implements Skin<ClientInfoViewImpl> {

    private final StringProperty clientName = new SimpleStringProperty();

    @SubView
    @DataModelContext("closeButton")
    @EventSignals(@EventSignal(name = "onPointerInput",
                               filter = PointerInputFilter.class))
    public Icon closeButton;
    private ClientInfoViewImpl clientInfoViewImpl;

    public ClientInfoSkin(final ClientInfoViewImpl clientInfoViewImpl) throws IOException {
        this.clientInfoViewImpl = clientInfoViewImpl;

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ClientInfoSkin.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
    }

    public void setClientName(String clientName) {
        this.clientName.setValue(clientName);
    }

    public StringProperty clientNameProperty() {
        return clientName;
    }

    public String getClientName() {
        return this.clientName.get();
    }

    @Override
    public ClientInfoViewImpl getSkinnable() {
        return this.clientInfoViewImpl;
    }

    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public void dispose() {

    }
}