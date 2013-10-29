package org.trinity.shellplugin.wm.view.javafx;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.trinity.foundation.api.render.binding.Binder;
import org.trinity.foundation.api.render.binding.view.EventSignal;
import org.trinity.foundation.api.render.binding.view.EventSignals;
import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;
import org.trinity.foundation.render.javafx.api.FXView;
import org.trinity.shell.api.bindingkey.ShellExecutor;

@EventSignals(@EventSignal(name = "onPointerInput",
                           filter = PointerInputFilter.class))
@PropertySlots(@PropertySlot(propertyName = "text",
                             methodName = "setClientName",
                             argumentTypes = String.class))
public class ClientInfoView extends FXView {

    private final StringProperty clientName = new SimpleStringProperty();

    @Inject
    ClientInfoView(final @ShellExecutor ListeningExecutorService shellExecutor,
                   final Binder binder) {
        super(shellExecutor,binder);
        getStyleClass().add("client-info-view");
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
    protected String getUserAgentStylesheet() {
        return getClass().getResource("/skin.css").toExternalForm();
    }
}
