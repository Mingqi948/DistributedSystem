package service.messages;

import service.core.ClientInfo;

public class ApplicationRequest implements MySerializable {

    private ClientInfo clientInfo;

    public ApplicationRequest(ClientInfo info) {
        this.clientInfo = info;
    }

    public void setClientInfo(ClientInfo info) {
        this.clientInfo = info;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

}
