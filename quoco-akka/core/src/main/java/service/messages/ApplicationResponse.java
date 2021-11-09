package service.messages;

import service.core.ClientInfo;
import service.core.Quotation;

import java.util.List;

public class ApplicationResponse implements MySerializable {

    private ClientInfo clientInfo;
    private List<Quotation> quotations;

    public ApplicationResponse() {

    }

    public ApplicationResponse(ClientInfo info, List<Quotation> quotations) {
        this.clientInfo = info;
        this.quotations = quotations;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public List<Quotation> getQuotations() {
        return quotations;
    }

    public void setQuotations(List<Quotation> quotations) {
        this.quotations = quotations;
    }

}
