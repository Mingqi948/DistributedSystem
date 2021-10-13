package service.message;

import service.core.ClientInfo;
import service.core.Quotation;

import java.io.Serializable;
import java.util.List;

public class ClientApplicationMessage implements Serializable {

    public ClientInfo info;
    public List<Quotation> quotations;

    public ClientApplicationMessage(ClientInfo info, List<Quotation> quotations) {
        this.info = info;
        this.quotations = quotations;
    }

}
