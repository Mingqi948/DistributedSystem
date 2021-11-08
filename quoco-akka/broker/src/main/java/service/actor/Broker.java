package service.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import scala.concurrent.duration.Duration;
import service.message.RequestDeadline;
import service.messages.ApplicationRequest;
import service.messages.ApplicationResponse;
import service.messages.QuotationRequest;
import service.messages.QuotationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class Broker extends AbstractActor {

    private List<ActorRef> actorRefs = new ArrayList<>();
    private int SEED_ID = 0;
    private Map<Integer, ApplicationResponse> cache = new HashMap<>();

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                //Registry handler
                .match(String.class, msg -> {
                            if (!msg.equals("register")) return;
                            System.out.println(getSender() +
                                    " is successfully registered. Currently " +
                                    actorRefs.size() + " services available.");
                            actorRefs.add(getSender());
                        })
                //Receive request from Client -> transfer to quoter services
                .match(ApplicationRequest.class, msg -> {
                    for (ActorRef ref : actorRefs) {
                        ref.tell(new QuotationRequest(SEED_ID, msg.getClientInfo()), getSelf());
                    }
                    cache.put(SEED_ID, new ApplicationResponse(msg.getClientInfo(), new ArrayList<>()));
                    getContext().system().scheduler().scheduleOnce(
                            Duration.create(2, TimeUnit.SECONDS),
                            getSelf(),
                            new RequestDeadline(SEED_ID++),
                            getContext().dispatcher(), null);
                })
                //Receive responses from quoter services
                .match(QuotationResponse.class, msg -> {

                })
                .build();
    }

}
