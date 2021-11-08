package service.actor;

import akka.actor.AbstractActor;
import service.core.Quotation;
import service.core.QuotationService;
import service.message.Init;
import service.messages.QuotationRequest;
import service.messages.QuotationResponse;

public class Quoter extends AbstractActor {

    private QuotationService service;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(QuotationRequest.class,
                msg -> {
                    Quotation quotation = service.generateQuotation(msg.getClientInfo());
                    getSender().tell(new QuotationResponse(msg.getId(), quotation), getSelf());
                })
                .match(Init.class, msg -> {
                    service = msg.getQuotationService();
                })
                .build();
    }

}
