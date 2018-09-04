package clientUI;

import javafx.beans.property.SimpleStringProperty;

public class Request {
    private final SimpleStringProperty clientId;
    private final SimpleStringProperty sequenceId;
    private final SimpleStringProperty protocol;
    private final SimpleStringProperty serviceName;

    public Request(String clientId) {
        this.clientId = new SimpleStringProperty(clientId);
        this.sequenceId = new SimpleStringProperty("");
        this.protocol = new SimpleStringProperty("");
        this.serviceName = new SimpleStringProperty("");
    }

    public Request(String clientId, String sequenceId, String protocol, String serviceName) {
        this.clientId = new SimpleStringProperty(clientId);
        this.sequenceId = new SimpleStringProperty(sequenceId);
        this.protocol = new SimpleStringProperty(protocol);
        this.serviceName = new SimpleStringProperty(serviceName);
    }

    public String getClientId() {
        return clientId.get();
    }

    public SimpleStringProperty clientIdProperty() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId.set(clientId);
    }

    public String getSequenceId() {
        return sequenceId.get();
    }

    public SimpleStringProperty sequenceIdProperty() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId.set(sequenceId);
    }

    public String getProtocol() {
        return protocol.get();
    }

    public SimpleStringProperty protocolProperty() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol.set(protocol);
    }

    public String getServiceName() {
        return serviceName.get();
    }

    public SimpleStringProperty serviceNameProperty() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName.set(serviceName);
    }

    @Override
    public String toString() {
        if (sequenceId.isEmpty().get()) {
            return "Client Id: " + clientId.get();
        } else {
            return "sequenceId=" + sequenceId.get() +
                    ", protocol=" + protocol.get() +
                    ", serviceName=" + serviceName.get();
        }
    }
}