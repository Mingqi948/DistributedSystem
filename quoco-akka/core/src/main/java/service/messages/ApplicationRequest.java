package service.messages;

import service.core.ClientInfo;

public class ApplicationRequest implements MySerializable {

    private ClientInfo clientInfo;

    public ApplicationRequest() {

    }

    public ApplicationRequest(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public void setClientInfo(ClientInfo info) {
        this.clientInfo = info;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

}
