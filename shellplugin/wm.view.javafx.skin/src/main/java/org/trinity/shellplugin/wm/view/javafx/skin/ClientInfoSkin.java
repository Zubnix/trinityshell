package org.trinity.shellplugin.wm.view.javafx.skin;


import com.sun.javafx.scene.control.skin.SkinBase;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import org.trinity.foundation.api.render.binding.view.DataModelContext;
import org.trinity.foundation.api.render.binding.view.EventSignal;
import org.trinity.foundation.api.render.binding.view.EventSignals;
import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;
import org.trinity.foundation.api.render.binding.view.SubView;
import org.trinity.shellplugin.wm.view.javafx.ClientInfoView;

import java.io.IOException;

@EventSignals(@EventSignal(name = "onPointerInput",
                           filter = PointerInputFilter.class))
@PropertySlots(@PropertySlot(propertyName = "text",
                             methodName = "setClientName",
                             argumentTypes = String.class))
public class ClientInfoSkin extends SkinBase<ClientInfoView, ClientInfoBehavior> {


    private final StringProperty clientName = new SimpleStringProperty();

    @SubView
    @DataModelContext("closeButton")
    @EventSignals(@EventSignal(name = "onPointerInput",
                               filter = PointerInputFilter.class))
    public Icon closeButton;

    public ClientInfoSkin(final ClientInfoView clientInfoView) throws IOException {
        super(clientInfoView,
              new ClientInfoBehavior(clientInfoView));

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
}
