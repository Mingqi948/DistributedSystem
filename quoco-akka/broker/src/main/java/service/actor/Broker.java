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
    private ActorRef clientRef = null;

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                //Registry handler
                .match(String.class, msg -> {
                    if (!msg.equals("register")) return;
                    actorRefs.add(getSender());
                    System.out.println(getSender() +
                            " is successfully registered. Currently " +
                            actorRefs.size() + " services available.");
                })
                //Receive request from Client -> transfer to quoter services
                .match(ApplicationRequest.class, msg -> {
                    clientRef = getSender();
                    for (ActorRef ref : actorRefs) {
                        ref.tell(new QuotationRequest(SEED_ID, msg.getClientInfo()), getSelf());
                    }
                    cache.put(SEED_ID, new ApplicationResponse(msg.getClientInfo(), new ArrayList<>()));
                    System.out.println("Application ID = " + SEED_ID + " established");
                    getContext().system().scheduler().scheduleOnce(
                            Duration.create(2, TimeUnit.SECONDS),
                            getSelf(),
                            new RequestDeadline(SEED_ID++),
                            getContext().dispatcher(),
                            null);
                })
                //Receive responses from quoter services
                .match(QuotationResponse.class, msg -> {
                    if(cache.containsKey(msg.getId()))
                        cache.get(msg.getId()).getQuotations().add(msg.getQuotation());
                    else System.out.println("Error: " + msg.getId() + " doesn't exist.");
                })
                //RequestDeadline handler
                .match(RequestDeadline.class, msg -> {
                    clientRef.tell(cache.get(msg.getSEED_ID()), getSelf());
                    cache.remove(msg.getSEED_ID());
                    System.out.println("Application ID = " + msg.getSEED_ID() + " processed.");
                })
                .build();
    }

}
